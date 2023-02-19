package com.yefeng.netdisk.front.task;


import com.yefeng.netdisk.front.entity.Disk;
import com.yefeng.netdisk.front.mapper.DiskMapper;
import com.yefeng.netdisk.front.util.CapacityContents;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * This class is for
 * 用户网盘修改容量 任务
 *
 * @author 夜枫
 * @version 2023-02-08 16:19
 */
@Slf4j
public class DiskCapacityTask implements Runnable {
    Long diskId;
    Integer type;
    Long capacity;
    DiskMapper diskMapper;
    Disk disk;


    public DiskCapacityTask(Long diskId, Integer type, Long capacity, DiskMapper diskMapper) {
        this.diskId = diskId;
        this.type = type;
        this.capacity = capacity;
        this.diskMapper = diskMapper;
    }

    @Override
    public void run() {

       try {
            disk = new Disk();
            disk.setId(diskId);

           //添加
           if (type.equals(CapacityContents.ADD_TOTAL_CAPACITY)) {
               diskMapper.updateCapacity(diskId,type,capacity);
           }
           else if(type.equals(CapacityContents.ADD_USE_CAPACITY)){
               disk.setUseCapacity(disk.getUseCapacity().add(BigDecimal.valueOf(capacity)));
           }
           //删除
           else if (type.equals(CapacityContents.DELETE_TOTAL_CAPACITY)) {
               disk.setTotalCapacity(disk.getUseCapacity().subtract(BigDecimal.valueOf(capacity)));
           }
           else if(type.equals(CapacityContents.DELETE_USE_CAPACITY)){
               disk.setUseCapacity(disk.getUseCapacity().subtract(BigDecimal.valueOf(capacity)));
           }
           diskMapper.updateById(disk);
       }catch (Exception e){
           log.error("云盘容量修改失败,diskId:{} updateType:{},capacity:{}",disk.getId(),type,capacity);
       }
    }

}


