package com.yefeng.netdisk.front.controller;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.qcloud.cos.utils.Md5Utils;
import com.yefeng.hdfs.feign.client.HdfsClient;
import com.yefeng.netdisk.common.constans.FileTypeEnum;
import com.yefeng.netdisk.common.request.RequestParams;
import com.yefeng.netdisk.common.result.ApiResult;
import com.yefeng.netdisk.common.result.HttpCodeEnum;
import com.yefeng.netdisk.common.result.ResultUtil;
import com.yefeng.netdisk.common.util.JWTUtil;
import com.yefeng.netdisk.common.validator.Assert;
import com.yefeng.netdisk.front.bo.FileBo;
import com.yefeng.netdisk.front.entity.DiskFile;
import com.yefeng.netdisk.front.entity.File;
import com.yefeng.netdisk.front.mapper.DiskMapper;
import com.yefeng.netdisk.front.service.IDiskFileService;
import com.yefeng.netdisk.front.service.IFileService;
import com.yefeng.netdisk.front.task.DiskCapacityTask;
import com.yefeng.netdisk.front.util.CapacityContents;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * <p>
 * 云盘文件控制器
 * </p>
 *
 * @author yefeng
 * @since 2023-01-15
 */
@Slf4j
@Api(tags = "用户网盘")
@RestController
@RequestMapping("/diskFile")
@RefreshScope
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
    @ApiOperation("获取云盘列表")
    @GetMapping("/list")
    public ApiResult getDiskFile(@RequestParam("disk_id") String diskId,
                                 @RequestParam(name = "parent_file_id", defaultValue = "root")
                                 String parentFileId,
                                 @RequestParam(name = "limit", defaultValue = "20")
                                 Integer limit

    ) {
        List<FileBo> fileBoList = diskFileService.getFileList(diskId, parentFileId);

        log.info("Disk file list:{}", fileBoList);

        return ResultUtil.success(fileBoList.toArray());

    }


    @Resource
    DiskMapper diskMapper;

    //上传文件到当前文件夹  使用feign调用hdfs
    @ApiOperation("上传文件")
    @PostMapping(value = "/file")
    public ApiResult uploadFile(
            @RequestParam("disk_id")
            String diskId,

            @ApiParam(name = "parent_file_id", required = true)
            @RequestParam("parent_file_id")
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


    //上传文件夹




    /**
     * 我的删除文件  列表
     */

    @PostMapping("delete")
    public ApiResult deleteFile(
            @RequestBody
            RequestParams[] requestParams
    ) {
        List<String> idLists = new ArrayList<String>(requestParams.length);
        for (int i = 0; i < requestParams.length; i++) {
            System.out.println(requestParams[i]);
            idLists.add(requestParams[i].getStringValue("file_id"));
        }
//        diskFileService.remove(new QueryWrapper<DiskFile>().eq("disk_id",))
        //todo 删除文件，/回收站
        return ResultUtil.success(requestParams);
    }


    //修改文件名称
    @PutMapping("/update")
    public ApiResult updateFileName(
            @RequestParam("disk_id")
            String diskId,
            @ApiParam(name = "file_id", required = true)
            @RequestParam("file_id")
            String fileId,
            @ApiParam(name = "name", required = true)
            @RequestParam("name")
            String name
    ) {
        //todo 校验

        //todo 修改文件（夹）名称
        boolean flag = fileService.update(new UpdateWrapper<File>().eq("disk", diskId).eq("file_id", fileId).set("file_name", name));
        return ResultUtil.success(flag);
    }

    //预览文件


    @Value("${mycloud.download.urlExpireTime:600}")
    Long downloadUrlExpireTime;

    //下载文件
    @GetMapping("/get_download_url")
    ApiResult getDownloadUrl(
            @RequestParam("disk_id")
            String diskId,
            @ApiParam(name = "file_id", required = true)
            @RequestParam("file_id")
            String fileId) {
        //todo 获取文件下载地址  ?拼接token,过期时间等验证参数
        File file = fileService.getFileWithDiskIdAndFileId(diskId, fileId);
        Assert.isNull(file,"没有该文件");

        String downloadToken = JWTUtil.createToken(new HashMap<>() {
            {
                put("diskId", diskId);
                put("type", "download");
                put("file_name", file.getOriginalName());
                put("expire_time", downloadUrlExpireTime);
            }
        }, downloadUrlExpireTime);


        String downloadUrl = file.getPath() + "/" + file.getOriginalName();
        JSONObject jsonObject = new JSONObject();
        jsonObject.putOnce("downloadUrl", downloadUrl);
        jsonObject.putOnce("downloadToken", downloadToken);


        System.out.println("jsonObject= " + jsonObject.toString());
        return ResultUtil.success(jsonObject);
    }





}
