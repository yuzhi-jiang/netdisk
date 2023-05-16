package com.yefeng.netdisk.front.controller;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qcloud.cos.utils.Md5Utils;
import com.yefeng.hdfs.feign.client.HdfsClient;
import com.yefeng.netdisk.common.constans.FileTypeEnum;
import com.yefeng.netdisk.common.result.ApiResult;
import com.yefeng.netdisk.common.result.HttpCodeEnum;
import com.yefeng.netdisk.common.result.ResultUtil;
import com.yefeng.netdisk.common.util.JWTUtil;
import com.yefeng.netdisk.common.validator.Assert;
import com.yefeng.netdisk.front.bo.BatchBo;
import com.yefeng.netdisk.front.bo.BatchBodyBo;
import com.yefeng.netdisk.front.bo.FileParamBo;
import com.yefeng.netdisk.front.entity.DiskFile;
import com.yefeng.netdisk.front.entity.File;
import com.yefeng.netdisk.front.mapStruct.mapper.DiskFileMapperStruct;
import com.yefeng.netdisk.front.mapper.DiskMapper;
import com.yefeng.netdisk.front.service.IDiskFileService;
import com.yefeng.netdisk.front.service.IFileService;
import com.yefeng.netdisk.front.task.DiskCapacityTask;
import com.yefeng.netdisk.front.util.CapacityContents;
import com.yefeng.netdisk.front.util.FileStatusEnum;
import com.yefeng.netdisk.front.vo.DiskFileVo;
import com.yefeng.netdisk.front.vo.ListDataVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

/**
 * 云盘文件
 *
 * @author yefeng
 * @since 2023-01-15
 */
@Slf4j
@Api(tags = "文件模块2")
@RestController
@RequestMapping("/file")
@RefreshScope
@Validated
@CrossOrigin
public class DiskFileController {


    @Resource
    IDiskFileService diskFileService;

    @Resource
    IFileService fileService;

    @Resource
    HdfsClient hdfsClient;

    @Value("${mycloud.hdfsBasePath}")
    String hdfsBasePath;


    @Resource(name = "commonQueueThreadPool")
    ExecutorService commonQueueThreadPool;

    //todo 根据路径获取文件列表

    //    public ApiResult<List<DiskFileVo>> list(@RequestParam("diskId") String diskId,
//                          @RequestParam(name = "parentFileId", defaultValue = "root")
//                          String parentFileId,
//                          @RequestParam(name = "page",defaultValue = "1")
//                              @Min(value = 1,message = "分页最小从1开始") Integer page,
//                          @RequestParam(name = "pageSize", defaultValue = "20") Integer pageSize
//
//    )
    @ApiOperation("获取云盘文件列表")
    @GetMapping("/list")
    public ApiResult<ListDataVo<DiskFileVo>> list(FileParamBo fileParamBo) {
        PageHelper.startPage(fileParamBo.getPageNum(), fileParamBo.getPageSize());
        List<DiskFileVo> fileVoList = diskFileService.getFileList(fileParamBo.getDiskId(),
                fileParamBo.getParentFileId(), FileStatusEnum.valid.getCode(),fileParamBo.getSearch());

        PageInfo<DiskFileVo> page = new PageInfo<DiskFileVo>(fileVoList);
        return ResultUtil.success(new ListDataVo<DiskFileVo>(page.getList(), page.getTotal()));
    }



    /**
     * 获取文件夹路径
     *
     * @param deskId
     * @param fileId
     * @return
     */
    @ApiOperation("获取文件夹路径")
    @GetMapping("/getPath")
    public List<DiskFileVo> getPath(@RequestParam("diskId") String deskId, @RequestParam("fileId") String fileId) {
        List<DiskFileVo> paths = diskFileService.getPath(deskId, fileId);
        return paths;
    }

    @Resource
    DiskMapper diskMapper;

    //上传文件到当前文件夹  使用feign调用hdfs
//    @ApiOperation("上传文件")
//    @PostMapping(value = "/file")
    public ApiResult uploadFile(
            @RequestParam("diskId")
            String diskId,

            @ApiParam(name = "parentFileId", required = true)
            @RequestParam("parentFileId")
            String parentFileId,

            @ApiParam(name = "file", required = true)
            @RequestPart("file")
            MultipartFile file
    ) {
        boolean flag = false;
        File fileEntity = null;
        byte[] fileBytes;
        try {
            fileBytes = file.getBytes();
            String hash = Md5Utils.md5Hex(fileBytes);

            QueryWrapper<File> hashQuery = new QueryWrapper<File>().eq("hash", hash);
//            long count = fileService.count(hashQuery);


            // 查看当前目录下我是否已经有了该文件名
            DiskFile diskFileExist = diskFileService.getOne(new QueryWrapper<DiskFile>().allEq(new HashMap<String, Object>(3) {
                {
                    put("disk_id", diskId);
                    put("parent_file_id", parentFileId);
                    put("file_name", file.getOriginalFilename());
                }
            }));


            File fileInBase = fileService.getOne(hashQuery);
            if (fileInBase != null) {
                log.info("hdfs:文件{} 已经存在，直接妙传", fileInBase.getName());
                DiskFile diskFile = new DiskFile();
                diskFile.setDiskId(Long.valueOf(diskId));
                diskFile.setDiskFileId(fileInBase.getFileId());
                diskFile.setFileName(fileEntity.getName());
                diskFile.setParentFileId(parentFileId);
                flag = diskFileService.save(diskFile);
            } else {
                //执行实际上传hdfs
                fileEntity = new File();
                fileEntity.setHash(hash);
                fileEntity.setName(file.getOriginalFilename());
                fileEntity.setOriginalName(file.getOriginalFilename());
                fileEntity.setLength(String.valueOf(file.getSize()));
                fileEntity.setPath("/" + diskId);
                fileEntity.setType((byte) FileTypeEnum.FILE.getCode());
                ApiResult apiResult = hdfsClient.uploadFile(hdfsBasePath, file);
                log.info("Uploaded file {} result: {}", file.getOriginalFilename(), apiResult);

                if (apiResult.getCode() != HttpCodeEnum.OK.getCode()) {
                    return apiResult;
                } else {
                    flag = fileService.save(fileEntity);
                    if (flag) {
                        DiskFile diskFile = new DiskFile();
                        diskFile.setDiskId(Long.valueOf(diskId));
                        diskFile.setDiskFileId(fileEntity.getFileId());
                        diskFile.setFileName(fileEntity.getName());
                        diskFile.setParentFileId(parentFileId);
                        flag = diskFileService.save(diskFile);

                    }
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (flag) {
            commonQueueThreadPool.execute(new DiskCapacityTask(Long.valueOf(diskId), CapacityContents.ADD_USE_CAPACITY, file.getSize(), this.diskMapper));
            return ResultUtil.success(fileEntity);
        }
        return ResultUtil.fail();
    }


    @ApiOperation("文件移动")
    @PutMapping("/move")
    public ApiResult<List<DiskFileVo>> move(@RequestBody BatchBo batchBo) {
        List<DiskFile> bodyBos = Arrays.stream(batchBo.getRequests()).map(request -> {
            BatchBodyBo body = request.getBody();
            DiskFile diskFile = new DiskFile();
            diskFile.setDiskFileId(body.getFileId());
            diskFile.setParentFileId(body.getToParentFileId());
            diskFile.setDiskId(Long.valueOf(body.getDiskId()));
            diskFile.setModifyTime(LocalDateTime.now());
            return diskFile;
        }).collect(Collectors.toList());

        boolean flag = diskFileService.moveFile(bodyBos);
        if (flag) {
            List<DiskFileVo> collect = bodyBos.stream().map(DiskFileMapperStruct.INSTANCE::toDto).collect(Collectors.toList());
            return ResultUtil.success(collect);
        }
        return ResultUtil.fail();
    }
    @ApiOperation("文件复制")
    @PutMapping("/copy")
    public Boolean copy(@RequestBody BatchBo batchBo) {
//        List<DiskFile> bodyBos = Arrays.stream(batchBo.getRequests()).map(request -> {
//            BatchBodyBo body = request.getBody();
//            DiskFile diskFile = new DiskFile();
//            diskFile.setDiskFileId(body.getFileId());
//            diskFile.setParentFileId(body.getToParentFileId());
//            diskFile.setDiskId(Long.valueOf(body.getDiskId()));
//            diskFile.setModifyTime(LocalDateTime.now());
//            return diskFile;
//        }).collect(Collectors.toList());
        String toParentFileId = batchBo.getRequests()[0].getBody().getToParentFileId();
        String diskId = batchBo.getRequests()[0].getBody().getDiskId();
        List<String> fileIds = Arrays.stream(batchBo.getRequests()).map(request -> {
            String diskFileId = request.getBody().getFileId();
            return diskFileId;
        }).collect(Collectors.toList());
        boolean flag = diskFileService.copyDiskFileBatch(diskId,diskId,toParentFileId,fileIds);

        return flag;
    }

    //修改文件名称
    @ApiOperation("修改文件名")
    @PostMapping("/filename")
    public ApiResult updateFileName(
            @RequestParam("diskId")
            String diskId,
            @ApiParam(name = "fileId", required = true)
            @RequestParam("fileId")
            String fileId,
            @ApiParam(name = "name", required = true)
            @RequestParam("name")
            String name
    ) {
        //todo 校验

        //todo 修改文件（夹）名称
        boolean flag = diskFileService.update(new UpdateWrapper<DiskFile>().eq("disk_id", diskId).eq("disk_file_id", fileId).set("file_name", name));
        return ResultUtil.success(flag);
    }

    //预览文件


    @Value("${mycloud.download.urlExpireTime:600}")
    Long downloadUrlExpireTime;

    //下载文件
    @GetMapping("/getDownloadUrl")
    ApiResult getDownloadUrl(
            @RequestParam("diskId")
            String diskId,
            @ApiParam(name = "fileId", required = true)
            @RequestParam("fileId")
            String fileId) {
        //todo 获取文件下载地址  ?拼接token,过期时间等验证参数
        File file = fileService.getFileWithDiskIdAndFileId(diskId, fileId);
        Assert.isNull(file, "没有该文件");


        String downloadToken = JWTUtil.createToken(new HashMap<>() {
            {
                put("diskId", diskId);
                put("type", "download");
                put("file_name", file.getOriginalName());
                put("expire_time", downloadUrlExpireTime);
            }
        }, downloadUrlExpireTime);


        String downloadUrl = file.getPath();
        JSONObject jsonObject = new JSONObject();
        jsonObject.putOnce("previewUrl", downloadUrl);
        jsonObject.putOnce("fileId", file.getFileId());
        jsonObject.putOnce("fileName", file.getOriginalName());
        jsonObject.putOnce("downloadToken", downloadToken);


        System.out.println("jsonObject= " + jsonObject.toString());
        return ResultUtil.success(jsonObject);
    }


}
