package com.yefeng.netdisk.front.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yefeng.netdisk.common.exception.BizException;
import com.yefeng.netdisk.common.util.FileNameUtil;
import com.yefeng.netdisk.front.dto.CreateFileDto;
import com.yefeng.netdisk.front.dto.DiskFileDto;
import com.yefeng.netdisk.front.entity.DiskFile;
import com.yefeng.netdisk.front.entity.File;
import com.yefeng.netdisk.front.mapStruct.mapper.DiskFileMapperStruct;
import com.yefeng.netdisk.front.mapper.DiskFileMapper;
import com.yefeng.netdisk.front.mapper.FileMapper;
import com.yefeng.netdisk.front.service.IDiskFileService;
import com.yefeng.netdisk.front.util.CheckNameModeEnum;
import com.yefeng.netdisk.front.util.FileStatusEnum;
import com.yefeng.netdisk.front.util.FileTypeContents;
import com.yefeng.netdisk.front.vo.DiskFileVo;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
                put("status",FileStatusEnum.valid.getCode());
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

    /**
     * 创建文件，类型是文件
     * @param diskFile
     * @param checkNameMode
     * @return
     */
    @Override
    public DiskFile createFile(DiskFile diskFile, String checkNameMode) {


        checkAndWriteFileName(diskFile, checkNameMode, FileTypeContents.FILE);
        baseMapper.insert(diskFile);

        return diskFile;

    }

    private void checkAndWriteFileName(DiskFile diskFile, String checkNameMode, FileTypeContents fileType) {
        if (checkNameMode.equals(CheckNameModeEnum.overwrite.getName())) {
            //覆盖，删除相同的文件
            baseMapper.delete(new QueryWrapper<DiskFile>().allEq(new HashMap<>(4) {
                {
                    put("disk_id", diskFile.getDiskId());
                    put("file_name", diskFile.getFileName());
                    put("parent_file_id", diskFile.getParentFileId());
                    put("type", fileType.getCode());
                }
            }));
        } else if (checkNameMode.equals(CheckNameModeEnum.auto_rename.getName())) {
            String fileName = diskFile.getFileName();

            String FileNameRemoveSuffix = FileNameUtil.removeSuffix(fileName);
            String suffix = FileNameUtil.getSuffix(fileName);
            //可能需要重命名
            List<DiskFile> diskFiles = baseMapper.selectList(new QueryWrapper<DiskFile>().allEq(new HashMap<>(4) {
                {
                    put("disk_id", diskFile.getDiskId());
                    put("parent_file_id", diskFile.getParentFileId());
                    put("type", fileType.getCode());
                }
            }).likeRight("file_name", FileNameRemoveSuffix));

            String pureName = FileNameUtil.getPureFileNameByPath(fileName);
            int i = 0;
            for (DiskFile file : diskFiles) {
                //如果是文件夹
                if (fileType == FileTypeContents.FOLDER) {
                    i++;
                    //如果文件夹名字相同，就需要重命名
                    String fileNameTmp = file.getFileName();
                    if(!fileNameTmp.equals(fileName + "(" + i + ")")){
                        //如果文件夹名字相同，就需要重命名
                        diskFile.setFileName(fileName + "(" + i + ")");
                    }
                } else {
                    String suffixTmp = FileNameUtil.getSuffix(fileName);
                    if(!suffixTmp.equals(suffix)){
                        //后缀不同，不需要重命名
                        continue;
                    }
                    i++;
                    String ansPureName = FileNameUtil.getPureFileNameByPath(file.getFileName());
                    if(!ansPureName.equals(pureName + "(" + i + ")")) {
                        diskFile.setFileName(pureName + "(" + i + ")." + suffix);
                    }
                }
            }
        }
    }

    @Override
    public List<DiskFileVo> getPath(String diskId, String fileId) {
        List<DiskFile> diskFiles = baseMapper.selectFilePathByDiskIDAndFileId(Long.valueOf(diskId), fileId);
        List<DiskFileVo> fileVos = diskFiles.stream().map(DiskFileMapperStruct.INSTANCE::toDto).collect(Collectors.toList());

        return fileVos;
    }

    @Override
    public boolean deleteFile(String diskId, List<String> fileIds) {
        int count = baseMapper.deleteFile(diskId, fileIds);
        return count > 0;
    }

    @Override
    public boolean updateStatus(String diskId, List<String> fileIds, FileStatusEnum status) {
        int count = baseMapper.updateStatus(diskId, fileIds, status.getCode());
        return count>0;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean restoreFile(String diskId, List<String> fileIds, FileStatusEnum status) {
        List<DiskFile> diskFiles = baseMapper.selectList(new QueryWrapper<DiskFile>().allEq(new HashMap<>(2) {
            {
                put("disk_id", diskId);
            }
        }).in("disk_file_id", fileIds).select("id","disk_file_id", "file_name", "parent_file_id", "disk_id","type"));
//        int count = baseMapper.updateStatus(diskId, fileIds, status.getCode());
        diskFiles.forEach(file->{
            FileTypeContents type = getFileType(file);
            checkAndWriteFileName(file, CheckNameModeEnum.auto_rename.getName(),type);
            file.setStatus(status.getCode());
        });
        int count = baseMapper.updateBatchByIds(diskFiles);

        return count > 0;
//        return false;

    }

    @NotNull
    private FileTypeContents getFileType(DiskFile file) {
        FileTypeContents type=null;
        if(file.getType().equals(FileTypeContents.FOLDER.getCode())) {
            type = FileTypeContents.FOLDER;
        }
        else if(file.getType().equals(FileTypeContents.FILE.getCode())){
            type = FileTypeContents.FILE;
        }
        if(type==null){
            log.error("文件类型错误");
            throw new BizException("文件类型错误,无法还原，请联系管理员");
        }
        return type;
    }

    @Override
    public boolean moveFile(List<DiskFile> diskFiles) {

        int count = baseMapper.moveFileBatch(diskFiles);
        return count > 0;
    }
    @Value("${mycloud.fileIdSize}")
    Integer fileIdSize;
    @Transactional(rollbackFor = Exception.class)
    public Boolean copyDiskFileBatch(String sourceDiskId, String toDiskId, String toParentFileId, List<String> fileIdList) {
        //查询出所有的文件
        List<DiskFile> diskFiles = diskFileMapper.selectAllSubDiskFile(Long.valueOf(sourceDiskId),fileIdList);
        Map<String, List<DiskFile>> listMap = diskFiles.stream().collect(Collectors.groupingBy(DiskFile::getParentFileId));

//        List<DiskFile> sourceDiskFiles = diskFileMapper.selectList(new QueryWrapper<DiskFile>().in("disk_file_id", fileIdList));

        List<DiskFile> toDiskFiles = baseMapper.selectList(new QueryWrapper<DiskFile>().allEq(new HashMap<>() {
            {
                put("disk_id", toDiskId);
//                put("file_name", diskFile.getFileName());
                put("parent_file_id", toParentFileId);
//                put("type", FileTypeContents.FOLDER.getCode());
            }
        }));


        diskFiles.forEach(diskFile -> {
            String file_id = RandomUtil.randomString(fileIdSize);
            diskFile.setId(null);

//            //子文件的父文件id不用改
            if(fileIdList.contains(diskFile.getDiskFileId())){
                diskFile.setParentFileId(toParentFileId);
            }

            //这个文件夹下的所有文件都要改parentFileId
            List<DiskFile> diskFiles1 = listMap.get(diskFile.getDiskFileId());
            if(diskFiles1!=null){
                diskFiles1.forEach(diskFile2 -> {
                    diskFile2.setParentFileId(file_id);
                });
            }

            diskFile.setDiskId(Long.valueOf(toDiskId));
            String fileName = diskFile.getFileName();
            if(fileIdList.contains(diskFile.getDiskFileId())){
                boolean match = toDiskFiles.stream().anyMatch(obj -> obj.getFileName().equals(fileName));
                if(match){
                    //有重名的
                    String suffix = FileNameUtil.getSuffix(fileName);
                    String pureName = FileNameUtil.getPureFileNameByPath(fileName);
                    int i = 0;
                    List<DiskFile> someNameDiskFile = toDiskFiles.stream().filter(obj -> obj.getFileName().equals(fileName)).collect(Collectors.toList());
                    FileTypeContents type = getFileType(diskFile);
                    checkAndWriteFileName(diskFile, CheckNameModeEnum.auto_rename.getName(),type);
                }
            }
            diskFile.setDiskFileId(file_id);

        });

        boolean flag = saveBatch(diskFiles);

        return flag;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveFile(File file, DiskFile diskFile) {
        //保存文件并更新diskFile的fileId
        int count = fileMapper.insert(file);
        if (count > 0) {
            diskFile.setFileId(file.getId());
            diskFile.setStatus(FileStatusEnum.valid.getCode());
            count = baseMapper.update(diskFile, new QueryWrapper<DiskFile>().eq("disk_file_id", diskFile.getDiskFileId()).eq("disk_id", diskFile.getDiskId()));
            return count > 0;
        }
        return false;
    }

    @Resource
    FileMapper fileMapper;

    @Override
    public List<DiskFileVo> getFileList(String diskId, String parentFileId, Byte status,String search) {
        List<DiskFileDto> fileList = baseMapper.getFileList(diskId, parentFileId, status,search);
        List<DiskFileVo> collect = fileList.stream().map(DiskFileMapperStruct.INSTANCE::dtoToVo).collect(Collectors.toList());
        return collect;
    }


    @Override
    public boolean saveWithFileId(String diskId, String diskFileId, Long fileId) {

        DiskFile diskFile = new DiskFile();
        diskFile.setStatus(FileStatusEnum.valid.getCode());
        diskFile.setFileId(fileId);

        int update = baseMapper.update(diskFile, new QueryWrapper<DiskFile>().eq("disk_id", diskId).eq("disk_file_id", diskFileId));


        return update > 0;


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

            int i = 0;
            for (DiskFile file : diskFiles) {
                i++;
                if (!file.getFileName().equals(fileName + "(" + i + ")")) {
                    diskFile.setFileName(fileName + "(" + i + ")");
                }
            }
        }
        int count = baseMapper.insert(diskFile);

        return diskFile;

    }


}
