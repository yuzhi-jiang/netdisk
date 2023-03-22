package com.yefeng.netdisk.front.controller;

import com.yefeng.netdisk.front.entity.SysNotice;
import com.yefeng.netdisk.front.service.ISysNoticeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 通知公告表 前端控制器
 * </p>
 *
 * @author yefeng
 * @since 2023-01-15
 */
@RestController
@RequestMapping("/notice")
public class SysNoticeController {

    @Resource
    ISysNoticeService noticeService;

    //获取所有通知
    @GetMapping("list")
    public List<SysNotice> getAllNotices() {
        return noticeService.list();
    }


}
