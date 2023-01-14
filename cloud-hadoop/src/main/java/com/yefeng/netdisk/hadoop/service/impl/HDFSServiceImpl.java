package com.yefeng.netdisk.hadoop.service.impl;

import cn.hutool.json.JSONUtil;
import com.yefeng.netdisk.common.exception.BizException;
import com.yefeng.netdisk.common.util.FileNameUtil;
import com.yefeng.netdisk.hadoop.entity.FileEntity;
import com.yefeng.netdisk.hadoop.entity.FileStatusEntity;
import com.yefeng.netdisk.hadoop.service.HDFSService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

@Slf4j
@Service
public class HDFSServiceImpl implements HDFSService {

    private static final int bufferSize = 1024 * 1024 * 64;

    @Resource
    private FileSystem fileSystem;

    @Override
    public boolean makeFolder(String path) {
        boolean target = false;
        if (StringUtils.isEmpty(path)) {
            return false;
        }
        if (existFile(path)) {
            return true;
        }
        Path src = new Path(path);
        try {
            target = fileSystem.mkdirs(src);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return target;
    }

    @Override
    public boolean existFile(String path) {
        if (StringUtils.isEmpty(path)) {
            return false;
        }
        Path src = new Path(path);
        try {
            return fileSystem.exists(src);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return false;
    }

    @Override
    public List<Map<String, Object>> readCatalog(String path) {
        if (StringUtils.isEmpty(path)) {
            return Collections.emptyList();
        }
        if (!existFile(path)) {
            log.error("catalog is not exist!!");
            return Collections.emptyList();
        }

        Path src = new Path(path);
        FileStatus[] fileStatuses = null;
        try {
            fileStatuses = fileSystem.listStatus(src);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        List<Map<String, Object>> result = new ArrayList<>(fileStatuses.length);


        if (null != fileStatuses && 0 < fileStatuses.length) {
            for (FileStatus fileStatus : fileStatuses) {
                Map<String, Object> cataLogMap = new HashMap<>();

                FileStatusEntity statusEntity = new FileStatusEntity();

                try {
                    statusEntity.setPath(fileStatus.getPath().toString());
                    statusEntity.setIsdir(fileStatus.isDirectory());
                    statusEntity.setModification_time(fileStatus.getModificationTime());
                    statusEntity.setAccess_time(fileStatus.getAccessTime());
                    statusEntity.setOwner(fileStatus.getOwner());
                    statusEntity.setGroup(fileStatus.getGroup());
                    statusEntity.setPermission(fileStatus.getPermission().toString());
                    statusEntity.setLength(fileStatus.getLen());
                    statusEntity.setBlock_replication(fileStatus.getReplication());
                    statusEntity.setBlocksize(fileStatus.getBlockSize());
                }catch (Exception e){
                    log.warn("filestatus to filestatusVo has error: " + e.getMessage());
                }
//                cataLogMap.put("filePath", fileStatus.getPath());
                cataLogMap.put("fileStatus", JSONUtil.parseObj(statusEntity, true));

                result.add(cataLogMap);
            }
        }
        return result;
    }

    @Override
    public boolean createFile(String path, MultipartFile file) {
        boolean target = false;
        if (StringUtils.isEmpty(path)) {
            return false;
        }
        String fileName = file.getOriginalFilename();
//        String fileName1 = file.getResource().getFilename();


        String name = FileNameUtil.getFileNameByPath(fileName);



        Path newPath = new Path(path + "/" + name);
        log.info("filePath:{}", newPath.toString());
        OutputStream outputStream = null;

        try {
            outputStream = fileSystem.create(newPath);
            InputStream in=file.getInputStream();
            IOUtils.copyBytes(in, outputStream, 4096, true);

//            outputStream.write(file.getBytes());
            target = true;
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            if (null != outputStream) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
        }
        return target;
    }

    @Override
    public String readFileContent(String path) {
        if (StringUtils.isEmpty(path)) {
            return null;
        }

        if (!existFile(path)) {
            return null;
        }

        Path src = new Path(path);

        FSDataInputStream inputStream = null;
        StringBuilder sb = new StringBuilder();
        try {
            inputStream = fileSystem.open(src);
            String lineText = "";
            while ((lineText = inputStream.readLine()) != null) {
                sb.append(lineText);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
        }
        return sb.toString();
    }

    @Override
    public List<Map<String, Object>> listFile(String path) {
        if (StringUtils.isEmpty(path)) {
            return Collections.emptyList();
        }
        if (!existFile(path)) {
            return Collections.emptyList();
        }
        List<Map<String, Object>> resultList = new ArrayList<>();

        Path src = new Path(path);
        try {
            RemoteIterator<LocatedFileStatus> fileIterator = fileSystem.listFiles(src, true);
            while (fileIterator.hasNext()) {
                LocatedFileStatus next = fileIterator.next();
                Path filePath = next.getPath();
                String fileName = filePath.getName();
                Map<String, Object> map = new HashMap<>();
                map.put("fileName", fileName);
                map.put("filePath", filePath.toString());
                resultList.add(map);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return resultList;
    }

    @Override
    public boolean renameFile(String oldName, String newName) {
        boolean target = false;
        if (StringUtils.isEmpty(oldName) || StringUtils.isEmpty(newName)) {
            return false;
        }
        Path oldPath = new Path(oldName);
        Path newPath = new Path(newName);
        try {
            target = fileSystem.rename(oldPath, newPath);
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return target;
    }

    @Override
    public boolean deleteFileOnExit(String path) {
        boolean target = false;
        if (StringUtils.isEmpty(path)) {
            return false;
        }
        if (!existFile(path)) {
            return false;
        }
        Path src = new Path(path);
        try {
            target = fileSystem.deleteOnExit(src);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return target;
    }

    @Override
    public boolean deleteFile(String path) {
        boolean target = false;
        if (StringUtils.isEmpty(path)) {
            return false;
        }
        if (!existFile(path)) {
            return false;
        }
        Path src = new Path(path);
        try {
            //false 为不递归删除
            target = fileSystem.delete(src,false);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return target;
    }
    @Override
    public boolean deleteFolder(String path) {
        boolean target = false;
        if (StringUtils.isEmpty(path)) {
            return false;
        }
        if (!existFile(path)) {
            return false;
        }
        Path src = new Path(path);
        try {
            //false 为不递归删除
            target = fileSystem.delete(src,true);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return target;
    }

    @Override
    public boolean uploadFile(String path, String uploadPath) {
        if (StringUtils.isEmpty(path) || StringUtils.isEmpty(uploadPath)) {
            return false;
        }

        Path clientPath = new Path(path);

        Path serverPath = new Path(uploadPath);

        try {
            fileSystem.copyFromLocalFile(false, clientPath, serverPath);
            return true;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean downloadFile(String path, String downloadPath) {
        if (StringUtils.isEmpty(path) || StringUtils.isEmpty(downloadPath)) {
            return false;
        }

        Path clienPath = new Path(path);

        Path targetPath = new Path(downloadPath);

        try {
            fileSystem.copyToLocalFile(false, clienPath, targetPath);
            return true;
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean downloadFile(String path, OutputStream out) {
        if (StringUtils.isEmpty(path)) {
            return false;
        }
        try {
            Path clienPath = new Path(path);
            InputStream in = fileSystem.open(clienPath);
            IOUtils.copyBytes(in, out, 4096, true);
            return true;
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return false;
    }

    @Override
    public FSDataInputStream downloadFile(String path) {
        if (StringUtils.isEmpty(path)) {
            return null;
        }
        try {
            Path clienPath = new Path(path);
            FSDataInputStream inputStream = fileSystem.open(clienPath);
//            InputStream in = inputStream;
            return inputStream;
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public boolean copyFile(String sourcePath, String targetPath) {
        if (StringUtils.isEmpty(sourcePath) || StringUtils.isEmpty(targetPath)) {
            return false;
        }

        Path oldPath = new Path(sourcePath);

        Path newPath = new Path(targetPath);

        FSDataInputStream inputStream = null;
        FSDataOutputStream outputStream = null;

        try {
            inputStream = fileSystem.open(oldPath);
            outputStream = fileSystem.create(newPath);

            IOUtils.copyBytes(inputStream, outputStream, bufferSize, false);
            return true;
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
            if (null != outputStream) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
        }
        return false;
    }

    @Override
    public byte[] openFileToBytes(String path) {

        if (StringUtils.isEmpty(path)) {
            return null;
        }

        if (!existFile(path)) {
            return null;
        }

        Path src = new Path(path);
        byte[] result = null;
        FSDataInputStream inputStream = null;
        try {
            inputStream = fileSystem.open(src);
            result = IOUtils.readFullyToByteArray(inputStream);
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
        }

        return result;
    }

    @Override
    public BlockLocation[] getFileBlockLocations(String path) {
        if (StringUtils.isEmpty(path)) {
            return null;
        }
        if (!existFile(path)) {
            return null;
        }
        BlockLocation[] blocks = null;
        Path src = new Path(path);
        try {
            FileStatus fileStatus = fileSystem.getFileStatus(src);
            blocks = fileSystem.getFileBlockLocations(fileStatus, 0, fileStatus.getLen());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return blocks;
    }

    @Override
    public FileEntity getFileEntity(String path) {
        if (StringUtils.isEmpty(path)) {
            return null;
        }
        try {
            FileStatus fileStatus = fileSystem.getFileStatus(new Path(path));
            FileEntity fileEntity = new FileEntity();

            setFileEntityByFileStatus(fileEntity, fileStatus);


            return fileEntity;
        } catch (IOException e) {
            log.warn("Error getting file，tht file {} dose not exist", path);
            throw new BizException(String.format("file %s does not exist", path));
        }
    }

    private FileEntity getFileEntityByFileStatus(FileStatus fileStatus){
        FileEntity fileEntity = new FileEntity();
        fileEntity.setFileName(FileNameUtil.getFileNameByPath(fileStatus.getPath().toString()));


        fileEntity.setPath(fileStatus.getPath());
        fileEntity.setLength(fileStatus.getLen());
        fileEntity.setIsdir(fileStatus.isDirectory());
        fileEntity.setBlock_replication(fileStatus.getReplication());

        fileEntity.setBlocksize(fileStatus.getBlockSize());
        fileEntity.setModification_time(fileStatus.getModificationTime());
        fileEntity.setAccess_time(fileStatus.getAccessTime());
        fileEntity.setPermission(fileStatus.getPermission());
        return fileEntity;
    }
    private void setFileEntityByFileStatus(FileEntity fileEntity,FileStatus fileStatus){

        fileEntity.setPath(fileStatus.getPath());
        fileEntity.setFileName(FileNameUtil.getFileNameByPath(fileStatus.getPath().toString()));
        fileEntity.setLength(fileStatus.getLen());
        fileEntity.setIsdir(fileStatus.isDirectory());
        fileEntity.setBlock_replication(fileStatus.getReplication());

        fileEntity.setBlocksize(fileStatus.getBlockSize());
        fileEntity.setModification_time(fileStatus.getModificationTime());
        fileEntity.setAccess_time(fileStatus.getAccessTime());
        fileEntity.setPermission(fileStatus.getPermission());

    }

}
