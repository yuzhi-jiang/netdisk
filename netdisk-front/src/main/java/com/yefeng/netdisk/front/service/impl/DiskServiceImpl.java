package com.yefeng.netdisk.front.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yefeng.netdisk.common.validator.Assert;
import com.yefeng.netdisk.front.entity.Disk;
import com.yefeng.netdisk.front.entity.DiskItem;
import com.yefeng.netdisk.front.mapStruct.mapper.DiskItemMapperStruct;
import com.yefeng.netdisk.front.mapStruct.mapper.DiskMapperStruct;
import com.yefeng.netdisk.front.mapper.DiskItemMapper;
import com.yefeng.netdisk.front.mapper.DiskMapper;
import com.yefeng.netdisk.front.service.IDiskService;
import com.yefeng.netdisk.front.vo.DiskVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author yefeng
 * @since 2023-01-15
 */
@Service
public class DiskServiceImpl extends ServiceImpl<DiskMapper, Disk> implements IDiskService {


    @Resource
    DiskMapper diskMapper;

    @Resource
    DiskItemMapper diskItemMapper;

    @Value("${mycloud.register.baseCapacity}")
    private BigInteger baseCapacity;

    @Override
    public Disk initDisk(Long userId) {
        return createBaseCapacity(userId);
    }

    @Transactional(rollbackForClassName = "Exception")
    @Override
    public DiskVo getDiskInfoById(Long diskId) {
        Disk disk = baseMapper.selectById(diskId);
        List<DiskItem> diskItems = getDiskItems(diskId);
        DiskVo diskVo = new DiskVo();

        BeanUtils.copyProperties(disk,diskVo);

        diskVo.setDiskItems(diskItems.stream().map(DiskItemMapperStruct.INSTANCE::toVo).collect(Collectors.toList()));
        return diskVo;
    }

//    @Transactional(rollbackForClassName = "Exception")
    @Override
    public DiskVo getDiskInfoByUerId(Long userId) {


        Disk disk = baseMapper.selectOne(new QueryWrapper<Disk>().eq("user_id", userId));

        Assert.isNull(disk,"用户没有云盘");

        List<DiskItem> diskItems = getDiskItems(disk.getId());



        DiskVo diskVo=DiskMapperStruct.INSTANCE.toDto(disk);
        diskVo.setDiskItems(diskItems.stream().map(DiskItemMapperStruct.INSTANCE::toVo).collect(Collectors.toList()));
        return diskVo;
    }


    @Transactional(rollbackForClassName = {"RuntimeException", "Exception"})
    Disk createBaseCapacity(Long userId) {
        Disk disk = new Disk();
        disk.setUseCapacity(BigDecimal.valueOf(0L));
        disk.setTotalCapacity(new BigDecimal(baseCapacity));
        disk.setUserId(userId);
        diskMapper.insert(disk);

        if (createDiskItem(disk.getId())) {
            return disk;
        }
        return null;
    }




    boolean createDiskItem(Long diskId) {
        LocalDateTime eternalTime = LocalDateTime.of(1970, Month.JANUARY, 1, 0, 0);

        DiskItem diskItem = new DiskItem(diskId, "基础容量", new BigDecimal(baseCapacity), eternalTime);

        return diskItemMapper.insert(diskItem) > 0;
    }
    boolean createDiskItem(Long diskId,LocalDateTime endTime) {

        DiskItem diskItem = new DiskItem(diskId, "基础容量", new BigDecimal(baseCapacity), endTime);

        return diskItemMapper.insert(diskItem) > 0;
    }

    /**
     * 获取网盘容量细节
     * @param diskId
     * @return
     */
    private List<DiskItem> getDiskItems(Long diskId){
        List<DiskItem> diskItem = diskItemMapper.selectList(new QueryWrapper<DiskItem>().eq("disk_id", diskId));
           return diskItem;
    }
}
