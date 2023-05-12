package com.yefeng.netdisk.front.task;

import com.yefeng.netdisk.front.service.IDiskFileService;
import com.yefeng.netdisk.front.util.CapacityContents;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class DiskFileCopyTask implements Callable {


    String ShareId;
    String toDiskId;
    String toParentFileId;
    List<String> fileIdList;

    IDiskFileService diskFileService;

    public DiskFileCopyTask(String shareId, String toDiskId, String toParentFileId, List<String> fileIdList, IDiskFileService diskFileService) {
        ShareId = shareId;
        this.toDiskId = toDiskId;
        this.toParentFileId = toParentFileId;
        this.fileIdList = fileIdList;
        this.diskFileService = diskFileService;
    }



    @Override
    public Object call() throws Exception {

        Boolean batch = diskFileService.copyDiskFileBatch(ShareId, toDiskId, toParentFileId, fileIdList);

        return batch;
    }
}
