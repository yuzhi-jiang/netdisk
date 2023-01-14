package com.yefeng.netdisk.hadoop.service;

import com.yefeng.netdisk.hadoop.entity.FileEntity;
import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.fs.FSDataInputStream;
import org.springframework.web.multipart.MultipartFile;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public interface HDFSService {

		// 创建文件夹
    boolean makeFolder(String path);
		// 是否存在文件
    boolean existFile(String path);
		
    List<Map<String, Object>> readCatalog(String path);

    boolean createFile(String path, MultipartFile file);

    String readFileContent(String path);

    List<Map<String, Object>> listFile(String path);

    boolean renameFile(String oldName, String newName);

    /**
     * 系统退出时删除文件
     * @param path
     * @return
     */
    boolean deleteFileOnExit(String path);


    /**
     * 立即删除文件
     * @param path
     * @return
     */
    boolean deleteFile(String path);


    /**
     * 删除文件夹
      * @param path
     * @return
     */
    boolean deleteFolder(String path);

    boolean uploadFile(String path, String uploadPath);

    boolean downloadFile(String path, String downloadPath);
    boolean downloadFile(String path, OutputStream out);
    FSDataInputStream downloadFile(String path);

    boolean copyFile(String sourcePath, String targetPath);

    byte[] openFileToBytes(String path);

    BlockLocation[] getFileBlockLocations(String path);

    FileEntity getFileEntity(String path);


}
