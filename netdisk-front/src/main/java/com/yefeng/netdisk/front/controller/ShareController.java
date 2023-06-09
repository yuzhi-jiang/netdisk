package com.yefeng.netdisk.front.controller;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yefeng.netdisk.common.result.ApiResult;
import com.yefeng.netdisk.common.result.HttpCodeEnum;
import com.yefeng.netdisk.common.result.ResultUtil;
import com.yefeng.netdisk.common.util.JWTUtil;
import com.yefeng.netdisk.common.validator.Assert;
import com.yefeng.netdisk.front.bo.BatchBo;
import com.yefeng.netdisk.front.bo.BatchRequestBo;
import com.yefeng.netdisk.front.bo.ShareBo;
import com.yefeng.netdisk.front.entity.Disk;
import com.yefeng.netdisk.front.entity.Share;
import com.yefeng.netdisk.front.mapStruct.mapper.ShareMapperStruct;
import com.yefeng.netdisk.front.service.IDiskFileService;
import com.yefeng.netdisk.front.service.IDiskService;
import com.yefeng.netdisk.front.service.IShareService;
import com.yefeng.netdisk.front.task.DiskCapacityTask;
import com.yefeng.netdisk.front.task.DiskFileCopyTask;
import com.yefeng.netdisk.front.util.CapacityContents;
import com.yefeng.netdisk.front.util.ShareStatusEnum;
import com.yefeng.netdisk.front.vo.ListDataVo;
import com.yefeng.netdisk.front.vo.ShareVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;


/**
 * 分享文件
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
    public ApiResult<ListDataVo<ShareVo>> listShare(@RequestParam("diskId") String diskId, @RequestParam(defaultValue = "0", name = "page") @Min(value = 1, message = "分页最小从1开始") Integer page,
                                                    @RequestParam(defaultValue = "20", name = "pageSize") Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        List<Share> shareList = shareService.list(new QueryWrapper<Share>().eq("disk_id", diskId).eq("is_valid", "1"));
        PageInfo<Share> pageInfo = new PageInfo<>(shareList);
        List<ShareVo> collect = shareList.stream().map(share -> {
            ShareVo shareVo = ShareMapperStruct.INSTANCE.toVo(share);
            String shareUrl=webClientUrl.endsWith("/")?webClientUrl:(webClientUrl+"/");
            shareUrl=shareUrl+"shareList/"+ share.getId();
            shareVo.setShareUrl(shareUrl);
            return shareVo;
        }).collect(Collectors.toList());
        return ResultUtil.success(new ListDataVo<>(collect, pageInfo.getTotal()));
    }


    /**
     * 更改失效时间
     *
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
        String shareUrl=webClientUrl.endsWith("/")?webClientUrl:(webClientUrl+"/");
        shareUrl=shareUrl+"shareList/";
        shareBo.setShareUrl(shareUrl);
        ShareVo shareVo = shareService.create(shareBo);
        return ResultUtil.success(shareVo);
    }


    /**
     * 取消分享文件
     *
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
                .set("is_valid", ShareStatusEnum.invalid.getCode());

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

    @Value("${mycloud.share.tokenExpire:3600}")
    Long shareTokenExpire;

    @Resource
    IDiskService diskService;


    @ApiOperation("获取分享文件信息")
    @GetMapping("/info")
    public ApiResult getShareInfo(@RequestParam("shareId") String shareId){
        //根据shareId
        Share share = shareService.getOne(new QueryWrapper<Share>().eq("id", shareId));
        if (share == null) {
            return ResultUtil.failMsg("分享不存在");
        }
        if (share.getIsValid().equals("0")) {
            return ResultUtil.failMsg("分享已失效");
        }
        if (share.getExpiredTime()!=null&&share.getExpiredTime().isBefore(LocalDateTime.now())) {
            return ResultUtil.failMsg("分享已过期");
        }
        ShareVo shareVo = ShareMapperStruct.INSTANCE.toVo(share);
        return ResultUtil.success(shareVo);

    }

    //get_share_token
    @ApiOperation("获取分享token")
    @GetMapping("/getShareToken")
    public ApiResult getShareToken(@RequestParam("shareId") String shareId, @RequestParam(value = "sharePwd",defaultValue = "") String sharePwd) {
        Share share = shareService.getOne(new QueryWrapper<Share>().eq("id", shareId));
        if (StringUtils.isBlank(share.getSharePwd())) {
            share.setSharePwd("");
        }
        if (share == null) {
            return ResultUtil.failMsg("分享不存在");
        }
        if (share.getIsValid().equals("0")) {
            return ResultUtil.failMsg("分享已失效");
        }
        if (share.getExpiredTime()!=null&&share.getExpiredTime().isBefore(LocalDateTime.now())) {
            return ResultUtil.failMsg("分享已过期");
        }
        if(sharePwd.isBlank()&&StringUtils.isNotBlank(share.getSharePwd())){
            return ResultUtil.failMsg("分享需要密码");
        }
        if (!share.getSharePwd().equals(sharePwd)) {
            return ResultUtil.failMsg("密码错误");
        }
        Disk disk = diskService.getOne(new QueryWrapper<Disk>().eq("id", share.getDiskId()));
        String token = JWTUtil.createToken(new HashMap<>(5) {{
            put("shareId", shareId);
            put("sharePwd", sharePwd);
            //当前时间+过期时间 token过期时间 并转秒
            put("exp", System.currentTimeMillis() / 1000 + shareTokenExpire);
            put("diskId", String.valueOf(share.getDiskId()));
            put("createUser", disk.getUserId());
        }}, shareTokenExpire);
        JSONObject jsonObject = new JSONObject();
        jsonObject.putOnce("shareToken", token);

        return ResultUtil.success(jsonObject);
    }
    @Resource(name = "commonQueueThreadPool")
    ExecutorService commonQueueThreadPool;

    @ApiOperation("文件转存")
    @PutMapping("/save")
    public ApiResult copy(@RequestHeader("shareToken")String token,@RequestBody BatchBo batchBo) {

        Assert.isBlank(token, "token不能为空");
        JWTUtil.validateToken(token);
        HashMap<String, Object> hashMap = JWTUtil.getPayloadMapFromToken(token, "shareId", "sharePwd", "exp", "diskId");
        String shareId = (String) hashMap.get("shareId");
        String sharePwd = (String) hashMap.get("sharePwd");
        String sourceDiskId = (String) hashMap.get("diskId");
        if (shareId == null || sharePwd == null || sourceDiskId == null) {
            return ResultUtil.failMsg("token错误");
        }
        //判断diskId shareId sharePwd是否正确
//        if(shareId.equals(batchBo.getRequests()[0].getBody().getShareId())
//        if(!batchBo.getDiskId().equals(diskId)){
//            return ResultUtil.failMsg("转存目标磁盘错误");
//        }
        BatchRequestBo[] requests = batchBo.getRequests();
        List<String> fileIds = Arrays.stream(requests).map(obj -> obj.getBody().getFileId()).collect(Collectors.toList());
        Future submit = commonQueueThreadPool.submit(new DiskFileCopyTask( sourceDiskId,requests[0].getBody().getDiskId(), requests[0].getBody().getToParentFileId(), fileIds, diskFileService));

        return ResultUtil.custom(HttpCodeEnum.OK.getCode(),"后台转存中");
    }
}
