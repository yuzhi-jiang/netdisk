package com.yefeng.netdisk.front.bo;

/**
 * This class is for
 *
 * @author 夜枫
 * @version 2023-03-05 21:33
 */

public class BatchBo {
    private BatchRequestBo[] requests;
    private String resource;

    private String diskId;

    public BatchRequestBo[] getRequests() {
        return requests;
    }

    public void setRequests(BatchRequestBo[] requests) {
        this.requests = requests;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getDiskId() {
        return diskId;
    }

    public void setDiskId(String diskId) {
        this.diskId = diskId;
    }
}

