package com.yefeng.netdisk.front.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.yefeng.netdisk.common.result.ApiResult;
import com.yefeng.netdisk.common.result.ResultUtil;
import com.yefeng.netdisk.front.bo.ShareBo;
import com.yefeng.netdisk.front.entity.Share;
import com.yefeng.netdisk.front.service.IShareService;
import com.yefeng.netdisk.front.util.DateUtil;
import com.yefeng.netdisk.front.vo.ShareVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.List;

/**
 *
 * 分享文件
 *
 *
 * @author yefeng
 * @since 2023-01-15
 */
@RestController
@RequestMapping("/share")
public class ShareController {

    @Resource
    IShareService shareService;

    /**
     * 查看我的分享文件
     *
     * @param diskId
     * @param pageNum
     * @param pageSize
     * @return List
     */
    @GetMapping("/list")
    public ApiResult listShare(@RequestParam("disk_id") String diskId,@RequestParam(defaultValue = "0") Integer pageNum,
                               @RequestParam(defaultValue = "20",name = "pageSize") Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Share> shareList = shareService.list(new QueryWrapper<Share>()
                .eq("disk_id", diskId));
        return ResultUtil.success(shareList);
    }


    /**
     * 更改失效时间
     * @param diskId
     * @param shareId
     * @param expiration
     * @return
     *
     * @throws ParseException
     */
    @PatchMapping("/updateExpire")
    public ApiResult updateExpired(@RequestParam("disk_id")String diskId,@RequestParam("share_id") String shareId
            ,@RequestParam("expiration") String expiration) {
        if (StringUtils.isBlank(expiration)) {
            expiration="9999-12-31T23:59:59Z";
            //永久
        }
        String expirationTime = DateUtil.getDateFormat(expiration);
        boolean update = shareService.update(new UpdateWrapper<Share>()
                .eq("disk_id", diskId)
                .eq("id", shareId)
                .set("expired_time", expirationTime));
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
    @PostMapping("/create")
    public ApiResult create(@RequestBody ShareBo shareBo) {

        shareBo.setExpiration(DateUtil.getDateFormat(shareBo.getExpiration()));

        ShareVo shareVo = shareService.create(shareBo);
        return ResultUtil.success(shareVo);
    }



    /**
     * 取消分享文件
     * @param diskId
     * @param shareId
     * @return
     */
    @PatchMapping("/cancel")
    public ApiResult cancel(@RequestParam("disk_id") String diskId,
                            @RequestParam("share_id") String shareId) {
        UpdateWrapper<Share> wrapper = new UpdateWrapper<Share>().eq("disk_id", diskId)
                .eq("id", shareId)
                .set("is_valid", "0");

        boolean update = shareService.update(wrapper);
        if (update) {
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }
}
