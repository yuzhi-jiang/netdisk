package com.yefeng.netdisk.front.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qcloud.cos.utils.Md5Utils;
import com.yefeng.hdfs.feign.client.HdfsClient;
import com.yefeng.netdisk.common.constans.FileTypeEnum;
import com.yefeng.netdisk.common.result.ApiResult;
import com.yefeng.netdisk.common.result.HttpCodeEnum;
import com.yefeng.netdisk.common.result.ResultUtil;
import com.yefeng.netdisk.front.bo.FileBo;
import com.yefeng.netdisk.front.entity.DiskFile;
import com.yefeng.netdisk.front.entity.File;
import com.yefeng.netdisk.front.service.IDiskFileService;
import com.yefeng.netdisk.front.service.IFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

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
public class DiskFileController {

    @Resource
    IDiskFileService diskFileService;

    @Resource
    IFileService fileService;

    @Resource
    HdfsClient hdfsClient;

    @Value("${mycloud.hdfsBasePath}")
    String hdfsBasePath;



    //todo 根据路径获取文件列表
    @ApiOperation("获取云盘列表")
    @GetMapping("/list")
    public ApiResult getDiskFile(@RequestParam("disk_id") String diskId, @RequestParam(name = "parent_file_id", defaultValue = "root") String parentFileId) {
        List<FileBo> fileBoList = diskFileService.getFileList(diskId, parentFileId);

        log.info("Disk file list:{}", fileBoList);

        return ResultUtil.success(fileBoList.toArray());
    }

    //上传文件到当前文件夹  使用feign调用hdfs
    @ApiOperation("上传文件")
    @PostMapping(value = "file")
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
        byte[] bytes;
        try {
            bytes = file.getBytes();
            String hash = Md5Utils.md5Hex(bytes);
            QueryWrapper<File> hashQuery = new QueryWrapper<File>().eq("hash", hash);
//            long count = fileService.count(hashQuery);
            File fileInBase = fileService.getOne(hashQuery);

            if (fileInBase != null) {
                log.info("hdfs:文件{} 已经存在，直接妙传", fileInBase.getName());
                DiskFile diskFile = new DiskFile();
                diskFile.setDiskId(Long.valueOf(diskId));
                diskFile.setFileId(fileInBase.getId());
                diskFile.setFileName(fileInBase.getName());
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
                        diskFile.setFileId(fileEntity.getId());
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
            return ResultUtil.success(fileEntity);
        }
        return ResultUtil.fail();
    }


    //上传文件夹


    //创建文件夹

    //删除文件列表


    //修改文件名称


    //预览文件


    //下载文件


}
