package com.yefeng.netdisk.front.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.yefeng.netdisk.common.result.ApiResult;
import com.yefeng.netdisk.common.result.ResultUtil;
import com.yefeng.netdisk.front.bo.BatchBo;
import com.yefeng.netdisk.front.bo.BatchRequestBo;
import com.yefeng.netdisk.front.entity.DiskFile;
import com.yefeng.netdisk.front.mapStruct.mapper.DiskFileMapperStruct;
import com.yefeng.netdisk.front.service.IDiskFileService;
import com.yefeng.netdisk.front.util.FileStatusEnum;
import com.yefeng.netdisk.front.vo.DiskFileVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is for
 *  回收站
 * @author 夜枫
 * @version 2023-03-12 22:31
 */
@Api(tags = "回收站")
@RestController
@RequestMapping("/file/recycle")
public class RecycleController {
    @Resource
    IDiskFileService diskFileService;

    /**
     * 根据disk_file_id查看文件信息
     * @param diskFileId
     * @return
     */
    @ApiOperation("根据disk_file_id查看文件信息")
    @GetMapping("/info/{disk_file_id}")
    public ApiResult<DiskFileVo> info(@PathVariable("disk_file_id") String diskFileId) {
        DiskFile diskFile = diskFileService.getOne(
                new QueryWrapper<DiskFile>()
                        .eq("disk_file_id", diskFileId)
                        .eq("status", FileStatusEnum.invalid));
        DiskFileVo diskFileVo = DiskFileMapperStruct.INSTANCE.toDto(diskFile);
        return ResultUtil.success(diskFileVo);
    }

    /**
     * 获取回收站文件列表
     *
     * @param diskId
     * @param PageSize
     * @param PageNum
     * @return
     */
    @ApiOperation("获取回收站文件列表")
    @PostMapping("/list")
    public ApiResult<List<DiskFileVo>> recycleList(@RequestParam("disk_id") String diskId, @RequestParam("page_num") Integer PageNum, @RequestParam("page_size") Integer PageSize) {
        QueryWrapper<DiskFile> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("disk_id", diskId);
        queryWrapper.eq("status", FileStatusEnum.invalid);

        PageHelper.startPage(PageNum, PageSize);

        List<DiskFile> list = diskFileService.list(queryWrapper);
        List<DiskFileVo> diskFileVos = list.stream().map(DiskFileMapperStruct.INSTANCE::toDto).collect(Collectors.toList());
        return ResultUtil.success(diskFileVos);
    }

    /**
     * 清空回收站
     *
     * @param diskId
     * @return
     */
    @ApiOperation("清空回收站")
    @PostMapping("/clear")
    public ApiResult clear(String diskId) {
        boolean flag = diskFileService.remove(new QueryWrapper<DiskFile>().eq("disk_id", diskId).eq("status", FileStatusEnum.invalid));
        if (flag) {
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }

    /**
     * 放到回收站
     *
     * @param batchBo
     * @return
     */
    @ApiOperation("放到回收站")
    @PostMapping("/add")
    public ApiResult recycle(BatchBo batchBo) {
        List<String> fileIds = Arrays.stream(batchBo.getRequests()).map(BatchRequestBo::getId).collect(Collectors.toList());
        String diskId = batchBo.getDiskId();
        boolean flag = diskFileService.updateStatus(diskId, fileIds, FileStatusEnum.invalid);
        if (flag) {
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }

    /**
     * 还原文件
     *
     * @param batchBo
     * @return
     */
    @ApiOperation("还原文件")
    @PostMapping("/restore")
    public ApiResult restore(BatchBo batchBo) {
        List<String> fileIds = Arrays.stream(batchBo.getRequests()).map(BatchRequestBo::getId).collect(Collectors.toList());
        String diskId = batchBo.getDiskId();
        boolean flag = diskFileService.updateStatus(diskId, fileIds, FileStatusEnum.valid);
        if (flag) {
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }

    /**
     * 删除文件  列表
     * {
     *   "requests": [
     *     {
     *       "body": {
     *         "drive_id": "358565",
     *         "file_id": "637a1dcd08f610bb4bb64fbab5eb5f789c77e2e7"
     *       },
     *       "headers": {
     *         "Content-Type": "application/json"
     *       },
     *       "id": "637a1dcd08f610bb4bb64fbab5eb5f789c77e2e7",
     *       "method": "POST",
     *       "url": "/recyclebin/trash"
     *     },
     *     {
     *       "body": {
     *         "drive_id": "358565",
     *         "file_id": "637a1dcd7940c7a1bfa745bbb7fabb2c64ba4f35"
     *       },
     *       "headers": {
     *         "Content-Type": "application/json"
     *       },
     *       "id": "637a1dcd7940c7a1bfa745bbb7fabb2c64ba4f35",
     *       "method": "POST",
     *       "url": "/recyclebin/trash"
     *     },
     *     {
     *       "body": {
     *         "drive_id": "358565",
     *         "file_id": "637a1dcd0f24ea18c7f54a64a6c55964fec6c36e"
     *       },
     *       "headers": {
     *         "Content-Type": "application/json"
     *       },
     *       "id": "637a1dcd0f24ea18c7f54a64a6c55964fec6c36e",
     *       "method": "POST",
     *       "url": "/recyclebin/trash"
     *     }
     *   ],
     *   "resource": "file"
     * }
     */
    /**
     * 从云盘删除文件，应该是在回收站调用
     *
     * @param batchBo
     * @return
     */
    @ApiOperation("从云盘删除文件，应该是在回收站调用")
    @PostMapping("/delete")
    public ApiResult deleteFile(
            BatchBo batchBo
    ) {

        List<String> fileIds = Arrays.stream(batchBo.getRequests()).map(BatchRequestBo::getId).collect(Collectors.toList());

        //todo
        String diskId = batchBo.getDiskId();
        boolean flag = diskFileService.deleteFile(diskId, fileIds);
        if (flag) {
            return ResultUtil.success();
        }
        return ResultUtil.fail().setMsg("删除失败,请查看文件是否在回收站");
    }

}
