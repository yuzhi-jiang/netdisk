package com.yefeng.netdisk.front.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yefeng.netdisk.front.dto.CreateFileDto;
import com.yefeng.netdisk.front.dto.DiskFileDto;
import com.yefeng.netdisk.front.entity.DiskFile;
import com.yefeng.netdisk.front.mapStruct.mapper.DiskFileMapperStruct;
import com.yefeng.netdisk.front.mapper.DiskFileMapper;
import com.yefeng.netdisk.front.mapper.FileMapper;
import com.yefeng.netdisk.front.service.IDiskFileService;
import com.yefeng.netdisk.front.util.CheckNameModeEnum;
import com.yefeng.netdisk.front.util.FileStatusEnum;
import com.yefeng.netdisk.front.util.FileTypeContents;
import com.yefeng.netdisk.front.vo.DiskFileVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
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
public class DiskFileServiceImpl extends ServiceImpl<DiskFileMapper, DiskFile> implements IDiskFileService {


    @Resource
    DiskFileMapper diskFileMapper;

    @Override
    public CreateFileDto creatFolder(DiskFile diskFile, String checkNameMode) {
        DiskFile one = baseMapper.selectOne(new QueryWrapper<DiskFile>().allEq(new HashMap<>() {
            {
                put("disk_id", diskFile.getDiskId());
                put("file_name", diskFile.getFileName());
                put("parent_file_id", diskFile.getParentFileId());
                put("type", FileTypeContents.FOLDER.getCode());
            }
        }));
        //有相同文件名，并且是新建文件夹

        //有相同的，提示换名字
        if (one != null) {

            CreateFileDto createFile = DiskFileMapperStruct.INSTANCE.toCreateFile(one);
            createFile.setExist(true);

            return createFile;
        }



        int count = baseMapper.insert(diskFile);

        CreateFileDto createFile = DiskFileMapperStruct.INSTANCE.toCreateFile(diskFile);

        return createFile;
    }

    @Override
    public DiskFile createFile(DiskFile diskFile, String checkNameMode) {


        if (checkNameMode.equals(CheckNameModeEnum.overwrite.getName())) {
            //覆盖，删除相同的文件
            baseMapper.delete(new QueryWrapper<DiskFile>().allEq(new HashMap<>(4) {
                {
                    put("disk_id", diskFile.getDiskId());
                    put("file_name", diskFile.getFileName());
                    put("parent_file_id", diskFile.getParentFileId());
                    put("type", FileTypeContents.FILE.getCode());
                }
            }));


        } else if (checkNameMode.equals(CheckNameModeEnum.auto_rename.getName())) {
            //可能需要重命名
            List<DiskFile> diskFiles = baseMapper.selectList(new QueryWrapper<DiskFile>().allEq(new HashMap<>(4) {
                {
                    put("disk_id", diskFile.getDiskId());
                    put("parent_file_id", diskFile.getParentFileId());
                    put("type", FileTypeContents.FILE.getCode());
                }
            }).likeRight("file_name", diskFile.getFileName()));

            String fileName=diskFile.getFileName();
            int i=0;
            for (DiskFile file : diskFiles) {
                i++;
                if(!file.getFileName().equals(fileName+"("+i+")")){
                    diskFile.setFileName(fileName+"("+i+")");
                }
            }
        }
        int count = baseMapper.insert(diskFile);

        return diskFile;

    }

    @Override
    public List<DiskFileVo> getPath(String diskId, String fileId) {
        List<DiskFile> diskFiles = baseMapper.selectFilePathByDiskIDAndFileId(Long.valueOf(diskId), fileId);
        List<DiskFileVo> fileVos = diskFiles.stream().map(DiskFileMapperStruct.INSTANCE::toDto).collect(Collectors.toList());

        return fileVos;
    }

    @Override
    public boolean deleteFile(String diskId, List<String> fileIds) {
       int count= baseMapper.deleteFile(diskId, fileIds);
       return count>0;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateStatus(String diskId, List<String> fileIds, FileStatusEnum status) {

       int count= baseMapper.updateStatus(diskId,fileIds,status.getCode());


       return count>0;

    }

    @Override
    public boolean moveFile(List<DiskFile> diskFiles) {

        int count=baseMapper.moveFileBatch(diskFiles);
        return count>0;
    }

    @Transactional(rollbackFor = Exception.class)
    public Long copyDiskFileBatch(String shareId, String toDiskId, String toParentFileId, List<String> fileIdList) {
        return null;
    }
    @Resource
    FileMapper fileMapper;

    @Override
    public List<DiskFileVo> getFileList(String diskId, String parentFileId,Byte status) {
        List<DiskFileDto> fileList = baseMapper.getFileList(diskId, parentFileId,status);
        List<DiskFileVo> collect = fileList.stream().map(DiskFileMapperStruct.INSTANCE::dtoToVo).collect(Collectors.toList());
        return collect;
    }


    @Override
    public boolean saveWithFileId(String diskId, String diskFileId, Long fileId) {

        DiskFile diskFile = new DiskFile();
        diskFile.setStatus(FileStatusEnum.valid.getCode());
        diskFile.setFileId(fileId);

        int update = baseMapper.update(diskFile, new QueryWrapper<DiskFile>().eq("disk_id", diskId).eq("disk_file_id", diskFileId));


        return update>0;


    }

    /**
     * 创建文件夹，需要处理有没有重复名
     *
     * @param diskId
     * @param parentFileId
     * @param fileName
     * @param checkNameMode
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object creatFolder(String diskId, String parentFileId, String fileName, String checkNameMode) {
        DiskFile one = baseMapper.selectOne(new QueryWrapper<DiskFile>().allEq(new HashMap<>() {
            {
                put("disk_id", diskId);
                put("file_name", fileName);
                put("parent_file_id", parentFileId);
                put("type", FileTypeContents.FOLDER.getCode());
            }
        }));
        //有相同文件名，并且是新建文件夹

        //有相同的，提示换名字
        if (one != null) {
            JSONObject jsonObject = new JSONObject(one);
            jsonObject.putOnce("exist", true);
            return jsonObject;
        }


//      返回值
//        {
//            "parent_file_id": "621c5ea65a1b6c367517412cbf90645fa3d41c22",
//                "type": "folder",
//                "file_id": "63e75166917a98661a87447b89dc9dea48121f2f",
//                "domain_id": "bj29",
//                "drive_id": "358565",
//                "file_name": "dd",
//                "encrypt_mode": "none"
//        }

        DiskFile diskFile = new DiskFile();
        diskFile.setFileName(fileName);
        diskFile.setDiskId(Long.valueOf(diskId));
        diskFile.setParentFileId(parentFileId);
        diskFile.setType(FileTypeContents.FOLDER.getCode());
        diskFile.setDiskFileId(RandomUtil.randomString(40));
        diskFile.setStatus(FileStatusEnum.invalid.getCode());


        int count = baseMapper.insert(diskFile);

        JSONObject jsonObject = new JSONObject(diskFile);

        return jsonObject;
    }

    /**
     * 创建文件，需要处理同名文件
     *
     * @param diskId
     * @param parentFileId
     * @param fileName
     * @param checkNameMode
     * @return
     */
    @Override
    public DiskFile createFile(String diskId, String parentFileId, String fileName, String fileId, String checkNameMode) {


        //创建文件
        DiskFile diskFile = new DiskFile();
        diskFile.setFileName(fileName);
        diskFile.setDiskId(Long.valueOf(diskId));
        diskFile.setParentFileId(parentFileId);
        diskFile.setType(FileTypeContents.FOLDER.getCode());
        diskFile.setDiskFileId(fileId);

        if (checkNameMode.equals(CheckNameModeEnum.overwrite.getName())) {
            //覆盖，删除相同的文件
            baseMapper.delete(new QueryWrapper<DiskFile>().allEq(new HashMap<>(4) {
                {
                    put("disk_id", diskId);
                    put("file_name", fileName);
                    put("parent_file_id", parentFileId);
                    put("type", FileTypeContents.FOLDER.getCode());
                }
            }));


        } else if (checkNameMode.equals(CheckNameModeEnum.auto_rename.getName())) {
            //可能需要重命名
            List<DiskFile> diskFiles = baseMapper.selectList(new QueryWrapper<DiskFile>().allEq(new HashMap<>(4) {
                {
                    put("disk_id", diskId);
                    put("parent_file_id", parentFileId);
                    put("type", FileTypeContents.FOLDER.getCode());
                }
            }).likeRight("file_name", fileName));

            int i=0;
            for (DiskFile file : diskFiles) {
                i++;
                if(!file.getFileName().equals(fileName+"("+i+")")){
                    diskFile.setFileName(fileName+"("+i+")");
                }
            }
        }
        int count = baseMapper.insert(diskFile);

        return diskFile;

    }


}
