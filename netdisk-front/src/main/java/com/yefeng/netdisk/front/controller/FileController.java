package com.yefeng.netdisk.front.controller;


import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.yefeng.hdfs.feign.client.HdfsClient;
import com.yefeng.netdisk.common.constans.FileTypeEnum;
import com.yefeng.netdisk.common.request.RequestParams;
import com.yefeng.netdisk.common.result.ApiResult;
import com.yefeng.netdisk.common.result.HttpCodeEnum;
import com.yefeng.netdisk.common.result.ResultUtil;
import com.yefeng.netdisk.common.util.JWTUtil;
import com.yefeng.netdisk.common.validator.Assert;
import com.yefeng.netdisk.front.bo.FileBo;
import com.yefeng.netdisk.front.dto.CreateFileDto;
import com.yefeng.netdisk.front.entity.DiskFile;
import com.yefeng.netdisk.front.entity.File;
import com.yefeng.netdisk.front.mapStruct.mapper.DiskFileMapperStruct;
import com.yefeng.netdisk.front.mapper.DiskMapper;
import com.yefeng.netdisk.front.service.IDiskFileService;
import com.yefeng.netdisk.front.service.IFileService;
import com.yefeng.netdisk.front.service.IShareService;
import com.yefeng.netdisk.front.task.DiskCapacityTask;
import com.yefeng.netdisk.front.util.*;
import com.yefeng.netdisk.front.vo.DiskFileVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

/**
 *
 * 云盘文件
 *
 *
 * @author yefeng
 * @since 2023-01-15
 */
@Api(tags = "文件模块")
@RestController
@RequestMapping("/file")
@Slf4j
@CrossOrigin
public class FileController {


    @Value("${mycloud.hdfsBasePath}")
    String hdfsBasePath;

    @Value("${mycloud.upload.idSize}")
    Integer uploadIdSize;
    @Value("${mycloud.fileIdSize}")
    Integer fileIdSize;

    @Value("${mycloud.upload.tokenExpire}")
    Long uploadTokenExpire;
    @Autowired
    private RedisUtil redisUtil;

    /*

{
    "drive_id": "358565",
    "query": "parent_file_id = \"621c5ea65a1b6c367517412cbf90645fa3d41c22\" and (name = \"笔试.mp4\")",
    "order_by": "name ASC",
    "limit": 100
}

{
    "drive_id": "358565",
    "limit": 20,
    "query": "name match \"xs\"",
    "order_by": "updated_at DESC"
}
     */

    @Resource
    IDiskFileService diskFileService;

    @ApiOperation(value = "获取文件hash,方式为sha1，仅做测试使用", notes = "仅做测试使用")
    @PostMapping("/hash")
    public ApiResult<String> getFileHash(@ApiParam(name = "file", required = true) @RequestPart("file")
                                 MultipartFile file) {
        try {
            String contentHash = DigestUtil.sha1Hex(file.getBytes());
            return ResultUtil.success(contentHash);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    /**
     * 查看云盘中是否有该文件
     *
     * @param diskId  云盘id
     * @param limit    条数
     * @param query    json格式的查询条件
     * @param orderBy json格式的排序条件
     * @return
     */
    @ApiOperation("查找文件")
    @PostMapping("/search")
    public ApiResult<List<DiskFileVo>> search(@RequestParam("diskId") String diskId,
                            @RequestParam("query") String query,
                            @RequestParam(value = "orderBy", required = false) String orderBy,
                            @RequestParam(value = "limit", defaultValue = "20") Integer limit
    ) {

        QueryWrapper<DiskFile> wrapper = QueryWrapperUtil.getWrapper(query);
        wrapper.eq("disk_id", diskId);
        String[] s = orderBy.split(" ");
        if (s.length > 1) {
            wrapper.orderBy(true, Objects.equals(s[1], "ASC"), s[0]);
        }

        PageHelper.startPage(0, limit);
        List<DiskFile> list = diskFileService.list(wrapper);
        List<DiskFileVo> collect = list.stream().map(DiskFileMapperStruct.INSTANCE::toDto).collect(Collectors.toList());

        return ResultUtil.success(collect);
    }





    /*
    * content_hash
    *
    *
    * {
  "drive_id": "358565",
  "parent_file_id": "621c5ea65a1b6c367517412cbf90645fa3d41c22",
  "name": "bench.sh",
  "type": "file",
  "size": 293927,
  "content_hash": "E31139B2A92437CB04B798F4AB0633CA80B2368C",
//  "content_hash_name": "sha1",

}
* 返回值
*
* {
  "parent_file_id": "621c5ea65a1b6c367517412cbf90645fa3d41c22",
  "part_info_list": [
    {
      "part_number": 1,
      "upload_url": "https://cn-beijing-data.aliyundrive.net/jpqp6pMM%2F358565%2F63e39641799412ad87a74501b2974a2b81431575%2F63e396411dbaa90b4deb4ca0b5da05a585cd22d8?partNumber=1&security-token=CAIS%2BgF1q6Ft5B2yfSjIr5WGOo7ko6xJ1ICSdBHks0QFbdkY2a6fpTz2IHFPeHJrBeAYt%2FoxmW1X5vwSlq5rR4QAXlDfNSuBEDzcqVHPWZHInuDox55m4cTXNAr%2BIhr%2F29CoEIedZdjBe%2FCrRknZnytou9XTfimjWFrXWv%2Fgy%2BQQDLItUxK%2FcCBNCfpPOwJms7V6D3bKMuu3OROY6Qi5TmgQ41Uh1jgjtPzkkpfFtkGF1GeXkLFF%2B97DRbG%2FdNRpMZtFVNO44fd7bKKp0lQLukMWr%2Fwq3PIdp2ma447NWQlLnzyCMvvJ9OVDFyN0aKEnH7J%2Bq%2FzxhTPrMnpkSlacGoABbAU5ObHhzORtdX6RNe5i24Zan%2BsYgNZVwXIrQ%2Bs1pLkWnkYIqLae15wlYTTLlvVqOU%2FpO2bTgiJ36jcwS7rl7QN81H5SlaagoB1NU%2Fdrl2ERRD1akJpwG%2B35EEd3hmzHv9TWn1sV2mKSznJy5No35mYc5iVxAMB5KiEqBEb3i%2Fc%3D&uploadId=A5669276D0C3480E807A4D3EDA8044D7&x-oss-access-key-id=STS.NV3q4PNshcByv7UWDPaV46k4G&x-oss-expires=1675863121&x-oss-signature=5eQYB1llsKCmsEMNELWII3tyyK66TImkqsGZe1bjhFw%3D&x-oss-signature-version=OSS2",
      "internal_upload_url": "http://ccp-bj29-bj-1592982087.oss-cn-beijing-internal.aliyuncs.com/jpqp6pMM%2F358565%2F63e39641799412ad87a74501b2974a2b81431575%2F63e396411dbaa90b4deb4ca0b5da05a585cd22d8?partNumber=1&security-token=CAIS%2BgF1q6Ft5B2yfSjIr5fPf%2F%2FR3IlQz4iOZWPovFY0dMtnvK7Mhjz2IHFPeHJrBeAYt%2FoxmW1X5vwSlq5rR4QAXlDfNVeHEDzcqVHPWZHInuDox55m4cTXNAr%2BIhr%2F29CoEIedZdjBe%2FCrRknZnytou9XTfimjWFrXWv%2Fgy%2BQQDLItUxK%2FcCBNCfpPOwJms7V6D3bKMuu3OROY6Qi5TmgQ41Uh1jgjtPzkkpfFtkGF1GeXkLFF%2B97DRbG%2FdNRpMZtFVNO44fd7bKKp0lQLukMWr%2Fwq3PIdp2ma447NWQlLnzyCMvvJ9OVDFyN0aKEnH7J%2Bq%2FzxhTPrMnpkSlacGoABlFXdrKkgUru5IwiyCtLwT1nNWxzxUJF2YoP52jqMGt2UKzGCdNS8Snv5mj1ai16nULyUZqjTvnEJhxIiReznJi9yoXUGIrfTrpZpnhWMkQW7EpfeF6ggYSM2ir%2Fdye9vJmwEgccJEHsD0RhBsPMDsVjjhPXiz2rE5EIibeguxVY%3D&uploadId=A5669276D0C3480E807A4D3EDA8044D7&x-oss-access-key-id=STS.NTz4Ee1VqxJegEYXVaxDKSkgd&x-oss-expires=1675863121&x-oss-signature=FAzuqfThQv2OpCPDOF1TvURNUjSTSUPQLORG1iRhczM%3D&x-oss-signature-version=OSS2",
      "content_type": ""
    }
  ],
  "upload_id": "A5669276D0C3480E807A4D3EDA8044D7",
  "rapid_upload": false,
  "type": "file",
  "file_id": "63e39641799412ad87a74501b2974a2b81431575",
  "drive_id": "358565",
  "file_name": "bench.sh",

}
*
* 创建文件夹
* {
  "drive_id": "358565",
  "parent_file_id": "621c5ea65a1b6c367517412cbf90645fa3d41c22",
  "name": "aa",
  "check_name_mode": "refuse",
  "type": "folder"
}
{
    "parent_file_id": "621c5ea65a1b6c367517412cbf90645fa3d41c22",
    "type": "folder",
    "file_id": "63e3ac2bf79c3c8b39af477fb431e9046ae37da9",
    "domain_id": "bj29",
    "drive_id": "358565",
    "file_name": "cc",
    "encrypt_mode": "none"
}
    * */
    //createWithFolders()


    @Resource
    IFileService fileService;
    @Resource
    HdfsClient hdfsClient;


    public ApiResult<Object> createWithFolders(@RequestBody RequestParams params) {
        String type = params.getStringValue("type");
        Assert.isBlank(type, "创建类型不能为空");
        String name = params.getStringValue("name");
        String diskId = params.getStringValue("diskId");
        String parentFileId = params.getStringValue("parentFileId");
        String checkNameMode = params.getStringValue("checkNameMode");
        String file_id = RandomUtil.randomString(fileIdSize);
        //创建文件夹
        if (type.equals(FileTypeContents.FOLDER.getName())) {
            List<DiskFile> one = diskFileService.list(new QueryWrapper<DiskFile>().allEq(new HashMap<>() {
                {
                    put("disk_id", diskId);
                    put("file_name", name);
                    put("parent_file_id", parentFileId);
                    put("type", FileTypeContents.FOLDER.getCode());
                }
            }));
            //有相同文件名，并且是新建文件夹

            //不处理
            if (Objects.equals(checkNameMode, CheckNameModeEnum.refuse.getName())) {

                JSONObject json = JSONUtil.parseObj(one.get(0));

                json.putOnce("exist", true);
                return ResultUtil.success(json);
            }

            DiskFile diskFile = new DiskFile();

            diskFile.setFileName(name);
            diskFile.setDiskId(Long.valueOf(diskId));
            diskFile.setParentFileId(parentFileId);
            diskFile.setType(FileTypeContents.FOLDER.getCode());
            diskFile.setDiskFileId(diskId);

            boolean flag = diskFileService.save(diskFile);
            if (flag) {
                DiskFile file = diskFileService.getById(diskFile.getId());
                return ResultUtil.success(file);
            }

            return ResultUtil.fail();
        }
        //创建(上传)文件
        else if (type.equals(FileTypeContents.FILE.getName())) {
               /* {
                        "check_name_mode": "auto_rename"//保留两者？
                        "drive_id": "358565",
                        "parent_file_id": "621c5ea65a1b6c367517412cbf90645fa3d41c22",
                        "name": "bench.sh",
                        "type": "file",
                        "size": 293927,
                        "content_hash": "E31139B2A92437CB04B798F4AB0633CA80B2368C",
                         "content_hash_name": "sha1",

                    {
                      "drive_id": "358565",
                      "part_info_list": [
                        {
                          "part_number": 1
                        }
                      ],
                      "parent_file_id": "621c5ea65a1b6c367517412cbf90645fa3d41c22",
                      "name": "a.txt",
                      "type": "file",
                          "check_name_mode": "overwrite",//覆盖
                      "size": 23,
                      "create_scene": "file_upload",
                      "device_name": "",
                      "content_hash": "E0DD257C42F689AE1E10B4F8C16BD6FF2F796126",
                      "content_hash_name": "sha1",
                      "proof_code": "aGgKZHNm",
                      "proof_version": "v1"
                    }
                */


//            diskFileService.createDiskFile(diskId,parentFileId,type,name,checkNameMode);


            Long size = (Long) params.get("size");
            String contentHash = params.getStringValue("contentHash");
            //妙传
//            log.info("hdfs:文件{} 已经存在，直接妙传", hasFile.getName());
            DiskFile diskFile = new DiskFile();
            diskFile.setDiskFileId(file_id);
            diskFile.setDiskId(Long.valueOf(diskId));
            diskFile.setParentFileId(parentFileId);
            diskFile.setType(FileTypeContents.FILE.getCode());
            diskFile.setStatus(FileStatusEnum.create.getCode());
            diskFile.setFileName(name);

            JSONObject json = JSONUtil.parseObj(diskFile);


            File hasFile = fileService.getOne(new QueryWrapper<File>().eq("hash", contentHash));


            //需要自动重命名
            if (checkNameMode.equals(CheckNameModeEnum.auto_rename.getName())) {

//                List<DiskFile> list = diskFileService.list(new QueryWrapper<DiskFile>()
//                        .eq("disk_id", diskId)
//                        .eq("parent_file_id", parentFileId)
//                        .eq("type", FileTypeContents.FILE.getCode())
//                        .likeRight("file_name", name));
//
//                int i=0;
//                for (DiskFile file : list) {
//                    if (!file.getFileName().equals(name+"("+(++i)+")")) {
//                        break;
//                    }
//                }
//
//                if(i>0){
//                    diskFile.setFileName(name+"("+(i)+")");
//                }
            }

            //覆盖文件，删除原该有的name文件
            else if (checkNameMode.equals(CheckNameModeEnum.overwrite.getName())) {
//                boolean removed = diskFileService.remove(new QueryWrapper<DiskFile>()
//                        .eq("disk_id", diskId)
//                        .eq("parent_file_id", parentFileId)
//                        .eq("type", FileTypeContents.FILE.getCode())
//                        .eq("file_name", name));

//                diskFileService.overwriteDiskFile(diskId,parentFileId,type,name);


            }


            //hdfs没有，需要真实上传
            json.putOnce("rapidUpload", "false");


                /*
                {
                  "parent_file_id": "621c5ea65a1b6c367517412cbf90645fa3d41c22",
//                  "upload_id": "rapid-6ebe0bdc-af21-49fd-a8a9-ade986b75c7b",
                  "rapid_upload": true,
                  "type": "file",
                  "file_id": "63e2002390eaffadfc5f4321af10058c4df225c1",
//                  "revision_id": "63e3b070958f2b5b141c4e3fbce2b77ddbc7362e",
//                  "domain_id": "bj29",
                  "drive_id": "358565",
                  "file_name": "a.txt",
//                  "encrypt_mode": "none",
//                  "location": "cn-beijing"
                }
                 */


        }


        return ResultUtil.fail();


    }


    /**
     * 上传文件逻辑
     * 1.文件名不同
     * 数据库中有，则直接拿到放入，快速上传
     * 数据库没有，则创建diskfile，并让上传
     * 2.文件名相同
     * 数据库有，不覆盖名，需要拿到fileId，重命名，快速上传
     * 数据库有，需要覆盖，拿到fileid,并删除原file, 快速上传
     * 数控没有，不覆盖，创建file，并上传
     * 没有，覆盖，删除原file，并上传
     *
     * @param fileBo
     * @return
     */
    @ApiOperation("上传文件")
    @PostMapping("/createWithFolders")
    public ApiResult<CreateFileDto> createWithFolders1(@RequestBody FileBo fileBo) {
        String type = fileBo.getType();
        Assert.isNull(type, "创建类型不能为空");
        String name = fileBo.getFileName();
        String diskId = fileBo.getDiskId();
        String parentFileId = fileBo.getParentFileId();
        String checkNameMode = fileBo.getCheckNameMode();

        String diskFileId = RandomUtil.randomString(fileIdSize);



        DiskFile diskFile = new DiskFile();
        diskFile.setFileName(name);
        diskFile.setDiskId(Long.valueOf(diskId));
        diskFile.setParentFileId(parentFileId);
        diskFile.setDiskFileId(diskFileId);


        //创建文件夹
        if (type.equals(FileTypeContents.FOLDER.getName())) {

            diskFile.setType(FileTypeContents.FOLDER.getCode());
            diskFile.setStatus(FileStatusEnum.valid.getCode());

            CreateFileDto creatFolder = diskFileService.creatFolder(diskFile, checkNameMode);
            return ResultUtil.success(creatFolder);
        }
        //创建(上传)文件
        else if (type.equals(FileTypeContents.FILE.getName())) {
            diskFile.setType(FileTypeContents.FILE.getCode());
            diskFile.setStatus(FileStatusEnum.create.getCode());//创建等待上传


            String contentHash = fileBo.getHash();
            //如果数据库有文件则获取文件信息，并标注快速上传
            File hasFile = fileService.getOne(new QueryWrapper<File>().eq("hash", contentHash));
            if (hasFile != null) {
                diskFileId = hasFile.getFileId();
                diskFile.setStatus(FileStatusEnum.invalid.getCode());//直接可用
                diskFile.setFileId(hasFile.getId());
            }

            //创建文件
            DiskFile file = diskFileService.createFile(diskFile, checkNameMode);

            CreateFileDto createFile = DiskFileMapperStruct.INSTANCE.toCreateFile(file);


            if (hasFile != null) {
                createFile.setRapidUpload(true);
            } else {
                createFile.setRapidUpload(false);

                String uploadId = RandomUtil.randomString(uploadIdSize);
//                String finalFileId = RandomUtil.randomString(40);
                String finalFileId = diskFileId;
                HashMap<String, Object> hashMap = new HashMap<>() {
                    {
                        put("diskId", diskId);
                        put("uploadId", uploadId);
                        put("contentHash", contentHash);
                        put("fileId", finalFileId);
                    }
                };
                //生产token 和 uploadId
                String token = JWTUtil.createToken(hashMap, uploadTokenExpire);
                createFile.setToken(token);
                createFile.setUploadId(uploadId);

            }

            return ResultUtil.success(createFile);
        }
        return ResultUtil.fail();
    }

    /**
     * 上传文件块
     *
     * @param uploadId
     * @param token
     * @param file
     * @return
     */
    @PutMapping("/uploadPart")
    public ApiResult uploadPart(@RequestParam("uploadId") String uploadId, @RequestParam("token") String token,
                                @ApiParam(name = "file", required = true) @RequestPart("file")
                                MultipartFile file) {
        try {
            System.out.println("you have uploaded");
            JWTUtil.validateToken(token);
            String contentHash = DigestUtil.sha1Hex(file.getBytes());
            System.out.println(contentHash);
            Object[] payload = JWTUtil.getPayloadFromToken(token, "uploadId", "contentHash", "diskId");
            if (payload == null || payload.length != 3) {
                return ResultUtil.failMsg("token 参数校验不通过");
            }
            if (contentHash != null && !contentHash.equals(payload[1])) {
                return ResultUtil.failMsg("hash mismatch");
            }
            if (uploadId != null && !uploadId.equals(payload[0])) {
                return ResultUtil.failMsg("uploadId mismatch");
            }
            String uploadKey = "";
            if (openRedis) {
                uploadKey = RedisKeys.getUploadKey(uploadId);
                Object flag = redisUtil.get(uploadKey);
                if (flag != null) {
                    //上传过了
                    return ResultUtil.success("upload is successfully");
                }
            } else {
                File file1 = fileService.getOne(new QueryWrapper<File>().eq("uploadId", uploadId));
                if (file1.getStatus() == FileStatusEnum.upload.getCode()) {
                    return ResultUtil.success("upload is successfully");
                }
            }

            ApiResult apiResult= null;
            try {

                apiResult  = hdfsClient.uploadFile(hdfsBasePath, file);
            }catch (Exception e){
                //上传失败，删除,重新上传
                String diskFileId = (String) payload[2];
                diskFileService.remove(new QueryWrapper<DiskFile>().eq("disk_file_id",diskFileId));
            }

            if (apiResult.getCode() != HttpCodeEnum.OK.getCode()) {
                return ResultUtil.failMsg("upload failed");
            }


            File file1 = new File();
            file1.setPath(hdfsBasePath);
            file1.setOriginalName(file.getOriginalFilename());
            file1.setName(file1.getOriginalName());
            file1.setHash((String) payload[1]);
            file1.setLength(String.valueOf(file.getSize()));
//            file1.setFileId(payload[2]);
            file1.setFileId(RandomUtil.randomString(fileIdSize));

            file1.setType((byte) FileTypeEnum.FILE.getCode());
            file1.setStatus(FileStatusEnum.upload.getCode());//文件上传了


            file1.setUploadId(uploadId);

            boolean saved = fileService.save(file1);


            if (openRedis && saved) {
                redisUtil.setObject(uploadKey, file1.getId());
            }

            JSONObject jsonObject = new JSONObject(apiResult.getData());

            jsonObject.putOnce("file_id", file1.getId());
            apiResult.setData(jsonObject);

            return apiResult;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Value("${mycloud.redis.open}")
    private boolean openRedis;
    @Resource(name = "commonQueueThreadPool")
    ExecutorService commonQueueThreadPool;

    @Resource
    DiskMapper diskMapper;
    /**
     * 上传完成，请求合并
     *
     * @param diskId
     * @param diskFileId
     * @param uploadId
     * @return
     */
    @PostMapping("/complete")
    public ApiResult complete(@RequestParam("diskId") String diskId, @RequestParam("fileId") String diskFileId, @RequestParam("uploadId") String uploadId) {
        Boolean flag = false;
        Long fileId = null;
        Long fileSize=0L;
        if (openRedis) {
            try {
                String uploadKey = RedisKeys.getUploadKey(uploadId);
                Object afileId = redisUtil.get(uploadKey);

                fileId = Long.valueOf(afileId.toString());
            } catch (Exception e) {
                log.error("Error getting upload");
            }
        }
        if (fileId == null) {
            File file = fileService.getOne(new QueryWrapper<File>().eq("upload_id", uploadId));
            if (file.getStatus() == FileStatusEnum.upload.getCode()) {
                fileId = file.getId();
                fileSize= Long.valueOf(file.getLength());
            }
        }

        System.out.println("fileId is " + fileId);

        if (fileId != null) {
            boolean saveFlag = diskFileService.saveWithFileId(diskId, diskFileId, fileId);
            commonQueueThreadPool.execute(new DiskCapacityTask(Long.valueOf(diskId), CapacityContents.ADD_USE_CAPACITY, fileSize, this.diskMapper));

            return ResultUtil.success();
        }
        return ResultUtil.failMsg("Could not upload");
    }
/*
    {
  "drive_id": "358565",
  "domain_id": "bj29",
  "file_id": "63e45bfc229210c1d7594f7895530ec8feabbf89",
  "name": "大道为丹.txt",
  "type": "file",
  "content_type": "application/oct-stream",
  "created_at": "2023-02-09T02:35:40.601Z",
  "updated_at": "2023-02-09T02:35:44.986Z",
  "file_extension": "txt",
  "hidden": false,
  "size": 3315315,
  "starred": false,
  "status": "available",
  "user_meta": "{\"channel\":\"file_upload\",\"client\":\"web\"}",
  "upload_id": "FC1B320AC8704525B5D3BF82AFEF8631",
  "parent_file_id": "63e3a71cd2a424dc55bb44bb9baefbbf00fb8b3c",
  "crc64_hash": "14789964617497916950",
  "content_hash": "48109BF55C85AC7CE88E381967658401A3AA8678",
  "content_hash_name": "sha1",
  "category": "doc",
  "encrypt_mode": "none",
  "creator_type": "User",
  "creator_id": "17d0f46b8fe8448c89af8bd03ca04380",
  "last_modifier_type": "User",
  "last_modifier_id": "17d0f46b8fe8448c89af8bd03ca04380",
  "user_tags": {
    "channel": "file_upload",
    "client": "web",
    "device_id": "Tz68G62zDGwCAduA5qv+y7r0",
    "device_name": "chrome"
  },
  "revision_id": "63e45bfc8218616dd7ff44998f8483a9c13d0288",
  "revision_version": 1,
  "location": "cn-beijing"
}
     */


    /**
     * {
     * "share_id": "SoUvRnD5HLm",
     * "parent_file_id": "627fb51e4ee16e1110bd44fa9497bb5aa1a71c62",
     * "limit": 20,
     * "image_thumbnail_process": "image/resize,w_256/format,jpeg",
     * "image_url_process": "image/resize,w_1920/format,jpeg/interlace,1",
     * "video_thumbnail_process": "video/snapshot,t_1000,f_jpg,ar_auto,w_256",
     * "order_by": "name",
     * "order_direction": "DESC"
     * }
     * <p>
     * <p>
     * <p>
     * [
     * {
     * "drive_id": "358565",
     * "domain_id": "bj29",
     * "file_id": "62f90b6a2173e426e8ad40a081de4c54b179c838",
     * "share_id": "xSX2h1D5RHL",
     * "name": "余胜军java",
     * "type": "folder",
     * "created_at": "2022-08-14T14:49:14.589Z",
     * "updated_at": "2022-11-20T14:12:47.466Z",
     * "parent_file_id": "root"
     * }
     * ]
     * <p>
     * [
     * {
     * "drive_id": "358565",
     * "domain_id": "bj29",
     * "file_id": "62f9ac458f827b6828d947fb985f0d0783924f63",
     * "share_id": "xSX2h1D5RHL",
     * "name": "每特教育&蚂蚁课堂JavaWeb开发基础2022版本",
     * "type": "folder",
     * "created_at": "2022-08-15T02:15:33.549Z",
     * "updated_at": "2022-08-15T02:15:33.549Z",
     * "parent_file_id": "62f90b6a2173e426e8ad40a081de4c54b179c838"
     * },
     * {
     * "drive_id": "358565",
     * <p>
     * "file_id": "62f9ac1deb190306e1414e4c9fe043823fbeb6b4",
     * "share_id": "xSX2h1D5RHL",
     * "name": "java基础",
     * "type": "folder",
     * "created_at": "2022-08-15T02:14:53.893Z",
     * "updated_at": "2022-08-15T02:14:53.893Z",
     * "parent_file_id": "62f90b6a2173e426e8ad40a081de4c54b179c838"
     * }
     * ]
     **/


    @Resource
    IShareService shareService;

    /**
     * 根据分享id查看文件
     *
     * @param shareId
     * @param parentFileId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/list_by_share")
    public ApiResult<List<DiskFileVo>> listByShare(@RequestParam("shareId") String shareId,
                                 @RequestParam(name = "parentFileId", defaultValue = "root") String parentFileId,
                                 @RequestParam(name = "pageNum", defaultValue = "0") Integer pageNum, @RequestParam(name = "pageSize", defaultValue = "0") Integer pageSize) {

        List<DiskFile> diskFiles = shareService.getFilesByShareId(shareId, parentFileId, pageNum, pageSize);
        List<DiskFileVo> collect = diskFiles.stream().map(DiskFileMapperStruct.INSTANCE::toDto).collect(Collectors.toList());

        return ResultUtil.success(collect);

    }

}
