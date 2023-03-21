package com.yefeng.netdisk.front.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yefeng.netdisk.common.result.ApiResult;
import com.yefeng.netdisk.common.result.ResultUtil;
import com.yefeng.netdisk.front.bo.ShareBo;
import com.yefeng.netdisk.front.entity.Share;
import com.yefeng.netdisk.front.mapStruct.mapper.ShareMapperStruct;
import com.yefeng.netdisk.front.service.IShareService;
import com.yefeng.netdisk.front.util.DateUtil;
import com.yefeng.netdisk.front.vo.ListDataVo;
import com.yefeng.netdisk.front.vo.ShareVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.Min;
import java.text.ParseException;
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
public class ShareController {

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
                .eq("disk_id", diskId));
        PageInfo<Share> pageInfo = new PageInfo<>(shareList);
        List<ShareVo> collect = shareList.stream().map(ShareMapperStruct.INSTANCE::toDto).collect(Collectors.toList());


        return ResultUtil.success(new ListDataVo<>(collect,pageInfo.getTotal()));
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
    @ApiOperation("更改失效时间")
    @PostMapping("/updateExpire")
    public ApiResult updateExpired(@RequestParam("diskId") String diskId,@RequestParam("shareId") String shareId
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
    @ApiOperation("创建分享")
    @PostMapping("/create")
    public ApiResult<ShareVo> create(@RequestBody ShareBo shareBo) {

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
    @ApiOperation("取消分享")
    @DeleteMapping("/cancel")
    public ApiResult cancel(@RequestParam("diskId") String diskId,
                            @RequestParam("shareId") String shareId) {
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
