package com.yefeng.netdisk.hadoop.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * This class is for
 *
 * @author 夜枫
 * @version 2023-01-07 13:18
 */
@Data
public class FileStatusEntity implements Serializable{
    private String path;
    private long length;
    private Boolean isdir;
    private short block_replication;
    private long blocksize;
    private long modification_time;
    private long access_time;
    private String permission;
    private String owner;
    private String group;
    private String symlink;
//    private Set<org.apache.hadoop.fs.FileStatus.AttrFlags> attr;
//    public static final Set<org.apache.hadoop.fs.FileStatus.AttrFlags> NONE = Collections.emptySet();






}
