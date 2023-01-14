package com.yefeng.netdisk.hadoop.entity;

import lombok.Data;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsPermission;

/**
 * This class is for
 *
 * @author 夜枫
 * @version 2023-01-06 23:21
 */
@Data
public class FileEntity {
    private String fileName;


    private Path path;
    private long length;
    private Boolean isdir;
    private short block_replication;

    private long blocksize;
    private long modification_time;
    private long access_time;
    private FsPermission permission;
}
