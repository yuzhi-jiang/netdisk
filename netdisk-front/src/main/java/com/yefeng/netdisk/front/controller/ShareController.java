package com.yefeng.netdisk.front.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yefeng.netdisk.common.result.ApiResult;
import com.yefeng.netdisk.common.result.ResultUtil;
import com.yefeng.netdisk.front.bo.BatchBo;
import com.yefeng.netdisk.front.bo.BatchBodyBo;
import com.yefeng.netdisk.front.bo.ShareBo;
import com.yefeng.netdisk.front.entity.DiskFile;
import com.yefeng.netdisk.front.entity.Share;
import com.yefeng.netdisk.front.mapStruct.mapper.DiskFileMapperStruct;
import com.yefeng.netdisk.front.mapStruct.mapper.ShareMapperStruct;
import com.yefeng.netdisk.front.service.IDiskFileService;
import com.yefeng.netdisk.front.service.IShareService;
import com.yefeng.netdisk.front.vo.DiskFileVo;
import com.yefeng.netdisk.front.vo.ListDataVo;
import com.yefeng.netdisk.front.vo.ShareVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.Min;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 *
 * 分享文件
 *
 *
 * @author yefeng
 * @since 2023-01-15
 */
@Api(tags = "分享文件")
@RestController
@CrossOrigin
@RequestMapping("/share")
@RefreshScope
public class ShareController {


    @Value("${webclient.url}")
    String webClientUrl;

    @Resource
    IShareService shareService;

    /**
     * 查看我的分享文件
     *
     * @param diskId
     * @param page
     * @param pageSize
     * @return List
     */
    @ApiOperation("查看我的分享文件")
    @GetMapping("/list")
    public ApiResult<ListDataVo<ShareVo>> listShare(@RequestParam("diskId") String diskId, @RequestParam(defaultValue = "0",name = "page")@Min(value = 1,message = "分页最小从1开始") Integer page,
                                                    @RequestParam(defaultValue = "20",name = "pageSize") Integer pageSize) {
        PageHelper.startPage(page,pageSize);
        List<Share> shareList = shareService.list(new QueryWrapper<Share>()
                .eq("disk_id", diskId).eq("is_valid","1"));
        PageInfo<Share> pageInfo = new PageInfo<>(shareList);
        List<ShareVo> collect = shareList.stream().map(share -> {
            ShareVo shareVo = ShareMapperStruct.INSTANCE.toVo(share);
            shareVo.setShareUrl(webClientUrl + "/share/" + share.getId());
            return shareVo;
        }).collect(Collectors.toList());


        return ResultUtil.success(new ListDataVo<>(collect,pageInfo.getTotal()));
    }


    /**
     * 更改失效时间
     * @param shareBo
     * @return
     */
    @ApiOperation("更改失效时间")
    @PostMapping("/updateExpire")
    public ApiResult updateExpired(@RequestBody ShareBo shareBo) {
        String diskId = shareBo.getDiskId();

        boolean update = shareService.update(new UpdateWrapper<Share>()
                .eq("disk_id", diskId)
                .eq("id", shareBo.getShareId())
                .set("expired_time", shareBo.getExpiredTime()));
        if (update) {
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }

    /**
     * 分享文件
     *
     * @param shareBo
     * @return
     */
    @ApiOperation("创建分享")
    @PostMapping("/create")
    public ApiResult<ShareVo> create(@RequestBody ShareBo shareBo) {

        shareBo.setExpiredTime(shareBo.getExpiredTime());
        shareBo.setShareUrl(webClientUrl+"/share");
        ShareVo shareVo = shareService.create(shareBo);
        return ResultUtil.success(shareVo);
    }


    /**
     * 取消分享文件
     * @param diskId
     * @param shareIds
     * @return
     */
    @ApiOperation("取消分享")
    @DeleteMapping("/cancel")
    public ApiResult cancel(@RequestParam("diskId") String diskId,
                            @RequestBody String[] shareIds) {
        UpdateWrapper<Share> wrapper = new UpdateWrapper<Share>().eq("disk_id", diskId)
                .in("id", (Object[]) shareIds)
                .set("is_valid", "0");

        boolean update = shareService.update(wrapper);
        if (update) {
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }
    @ApiOperation("清空分享")
    @DeleteMapping("/clear")
    public ApiResult cancel(@RequestParam("diskId") String diskId) {
        UpdateWrapper<Share> wrapper = new UpdateWrapper<Share>().eq("disk_id", diskId)
                .set("is_valid", "0");

        boolean update = shareService.update(wrapper);
        if (update) {
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }

    @Resource
    IDiskFileService diskFileService;

    @Value("${mycloud.fileIdSize}")
    Integer fileIdSize;


    @ApiOperation("文件转存")
    @PutMapping("/save")
    public ApiResult<List<DiskFileVo>> move(@RequestBody BatchBo batchBo){

        //查到文件信息list 并将其存入到新的磁盘中
        List<DiskFile> diskFileList = Arrays.stream(batchBo.getRequests()).map(request->{
            BatchBodyBo body = request.getBody();
            DiskFile diskFile = new DiskFile();

            diskFile.setDiskFileId(body.getFileId());

            diskFile.setDiskId(Long.valueOf(body.getDiskId()));

            return diskFile;
        }).collect(Collectors.toList());

        List<DiskFile> list = diskFileService.list(new QueryWrapper<DiskFile>().eq("disk_id", "").in("disk_file_id", diskFileList.stream().map(DiskFile::getDiskFileId).collect(Collectors.toList())));

        //将信息放到新的磁盘中
        List<DiskFile> bodyBos = Arrays.stream(batchBo.getRequests()).map(request->{
            BatchBodyBo body = request.getBody();
            DiskFile diskFile = new DiskFile();

            diskFile.setDiskFileId(body.getFileId());
            diskFile.setParentFileId(body.getToParentFileId());
            diskFile.setDiskId(Long.valueOf(body.getToDiskId()));

            return diskFile;
        }).collect(Collectors.toList());

        //拿到diskFileId 通过diskFileId查询文件信息 并将其存入到新的磁盘中
        boolean flag = diskFileService.saveBatch(bodyBos);
        if(flag){
            List<DiskFileVo> collect = bodyBos.stream().map(DiskFileMapperStruct.INSTANCE::toDto).collect(Collectors.toList());
            return ResultUtil.success(collect);
        }
        return ResultUtil.fail();
    }
}
