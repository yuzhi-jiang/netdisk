package com.yefeng.netdisk.hadoop.controller;


import cn.hutool.core.io.FileUtil;
import com.yefeng.netdisk.common.result.ApiResult;
import com.yefeng.netdisk.common.result.ResultUtil;
import com.yefeng.netdisk.common.util.IPUtils;
import com.yefeng.netdisk.common.validator.Assert;
import com.yefeng.netdisk.hadoop.entity.FileEntity;
import com.yefeng.netdisk.hadoop.service.HDFSService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.fs.FSDataInputStream;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * This class is for
 *
 * @author 夜枫
 * @version 2022-12-28 20:05
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/hdfs")
public class HdfsController {
    @Resource
    private HDFSService hdfsService;


    void downFileOnRange(HttpServletRequest request, HttpServletResponse response,
                         InputStream ins, long fileLength, String fileName, Boolean enablePreview) {
        BufferedInputStream bis = null;
        OutputStream out = null;
        try {
            bis = new BufferedInputStream(ins);
            out = response.getOutputStream();
//            out=new BufferedOutputStream(out);

            long p = 0L;
            long toLength = 0L;
            long contentLength = 0L;
            // 0,从头开始的全文下载；1,从某字节开始的下载（bytes=27000-）；2,从某字节开始到某字节结束的下载（bytes=27000-39000）
            int rangeSwitch = 0;
            String rangBytes = "";


//            long fileLength;
//            fileLength = fileEntity.getLength();
//            String fileName = fileEntity.getFileName();


            // get file content
//            bis = new BufferedInputStream(ins);
            // tell the client to allow accept-ranges
            response.reset();
            response.setHeader("Accept-Ranges", "bytes");
            // client requests a file block download start byte
            String range = request.getHeader("Range");
            if (range != null && range.trim().length() > 0 && !"null".equals(range)) {
                response.setStatus(javax.servlet.http.HttpServletResponse.SC_PARTIAL_CONTENT);
                rangBytes = range.replaceAll("bytes=", "");
                if (rangBytes.endsWith("-")) {
                    // bytes=270000-
                    rangeSwitch = 1;
                    p = Long.parseLong(rangBytes.substring(0, rangBytes.indexOf("-")));
                    // 客户端请求的是270000之后的字节（包括bytes下标索引为270000的字节）
                    contentLength = fileLength - p;
                } else {
                    // bytes=270000-320000
                    rangeSwitch = 2;
                    String temp1 = rangBytes.substring(0, rangBytes.indexOf("-"));
                    String temp2 = rangBytes.substring(rangBytes.indexOf("-") + 1, rangBytes.length());
                    p = Long.parseLong(temp1);
                    toLength = Long.parseLong(temp2);
                    // 客户端请求的是 270000-320000 之间的字节
                    contentLength = toLength - p + 1;
                }
            } else {
                contentLength = fileLength;
            }
            // 如果设设置了Content-Length，则客户端会自动进行多线程下载。如果不希望支持多线程，则不要设置这个参数。
            // Content-Length: [文件的总大小] - [客户端请求的下载的文件块的开始字节]
            response.setHeader("Content-Length", String.valueOf(contentLength));

            // 断点开始
            // 响应的格式是:
            // Content-Range: bytes [文件块的开始字节]-[文件的总大小 - 1]/[文件的总大小]
            if (rangeSwitch == 1) {
                String contentRange = new StringBuffer("bytes ")
                        .append(p)
                        .append("-")
                        .append(fileLength - 1)
                        .append("/")
                        .append(fileLength)
                        .toString();

                response.setHeader("Content-Range", contentRange);
                bis.skip(p);
            } else if (rangeSwitch == 2) {
                //Range: bytes=100-200  --> bytes 100-200/length
                String contentRange = range.replace("=", " ") + "/" + fileLength;
                response.setHeader("Content-Range", contentRange);
                bis.skip(p);
            } else {
                String contentRange = new StringBuffer("bytes ").append("0-")
                        .append(fileLength - 1).append("/")
                        .append(fileLength).toString();
                response.setHeader("Content-Range", contentRange);
            }


            if (enablePreview) {
                response.setContentType(FileUtil.getMimeType(fileName));
                response.addHeader("Content-Disposition", "inline;filename=" + fileName);
            } else {
                response.setContentType("application/octet-stream");

//                response.setContentType("application/octet-stream");
                response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            }
            int n = 0;
            long readLength = 0;
            int bsize = 1024 * 512;//51k
            byte[] bytes = new byte[bsize];
            if (rangeSwitch == 2) {
                // 针对 bytes=27000-39000 的请求，从27000开始写数据
                while (readLength <= contentLength - bsize) {
                    n = bis.read(bytes);
                    readLength += n;
                    out.write(bytes, 0, n);
                    out.flush();
                }
                if (readLength <= contentLength) {
                    n = bis.read(bytes, 0, (int) (contentLength - readLength));
                    out.write(bytes, 0, n);

                }
            } else {
                while ((n = bis.read(bytes)) != -1) {
                    out.write(bytes, 0, n);
                    out.flush();
                }
            }
            out.flush();
            out.close();
            bis.close();
            ins.close();
        } catch (IOException ie) {
            // 忽略 ClientAbortException 之类的异常
            log.warn("ip {}-{} 主动关闭了连接", IPUtils.getHostIp(), IPUtils.getIpAddr(request));
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ignored) {

                }
            }
            try {
                bis.close();
            } catch (IOException ignored) {

            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }


    void downFileOnRange(HttpServletRequest request, HttpServletResponse response,
                         InputStream ins, long fileLength, String fileName) {
        downFileOnRange(request, response, ins, fileLength, fileName, false);
    }


    @ApiOperation("下载文件支持断点续传,可根据参数跳转是否预览")
    @GetMapping("/file")
    private void downFile(HttpServletResponse response, HttpServletRequest request, @RequestParam(name = "path") String path, @RequestParam(name = "preview", required = false) Boolean preview) {
        FileEntity fileEntity = hdfsService.getFileEntity(path);
        Assert.isNull(fileEntity, "文件路径错误");
        FSDataInputStream ins = hdfsService.downloadFile(path);

        log.info("调用下载");
        if (preview == null || !preview) {
            downFileOnRange(request, response, ins, fileEntity.getLength(), fileEntity.getFileName());
        } else {
            downFileOnRange(request, response, ins, fileEntity.getLength(), fileEntity.getFileName(), true);
        }

    }


    @ApiOperation("create folder")
    @PostMapping("/folder")
    public ApiResult createFolder(
            @ApiParam(name = "path", required = true)
            @RequestParam(name = "path", required = true)
            String path
    ) {
        return ResultUtil.success(hdfsService.makeFolder(path));
    }


    @ApiOperation("delete folder")
    @DeleteMapping("/folder")
    public ApiResult deleteFolder(
            @ApiParam(name = "path", required = true)
            @RequestParam(name = "path", required = true)
            String path
    ) {
        return ResultUtil.success(hdfsService.deleteFolder(path));
    }


    @ApiOperation("check file is exist")
    @PostMapping("/exist")
    public ApiResult existFile(
            @ApiParam(name = "path", required = true)
            @RequestParam(name = "path", required = true)
            String path
    ) {
        return ResultUtil.success(hdfsService.existFile(path));
    }

//    @ApiOperation("read catalog")
//    @GetMapping("/catalog")
//    public ApiResult readCatalog(
//            @ApiParam(name = "path", required = true)
//            @RequestParam(name = "path", required = true)
//            String path
//    ) {
//        List<Map<String, Object>> readCatalog = hdfsService.readCatalog(path);
//        log.info("the readCatalog is {}",readCatalog);
//        return ResultUtil.success(readCatalog);
//    }

    @ApiOperation("read catalog")
    @GetMapping("/catalog")
    public ApiResult readCatalog(
            @ApiParam(name = "path", required = true)
            @RequestParam(name = "path", required = true)
            String path
    ) {
        List<Map<String, Object>> readCatalog = hdfsService.readCatalog(path);
        log.info("the readCatalog is {}", readCatalog);
        return ResultUtil.success(readCatalog);
    }


    @ApiOperation("create file")
    @PostMapping(value = "/file")
    public ApiResult createFile(
            @ApiParam(name = "path", required = true)
            @RequestParam(name = "path")
            String path,

            @ApiParam(name = "file", required = true)
            @RequestPart("file")
            MultipartFile file
    ) {
        boolean flag = hdfsService.createFile(path, file);
        log.info("createFile flag is {}", flag);
        return ResultUtil.success(flag);
    }

    @ApiOperation("read file content")
    @PostMapping("/file/content")
    public ApiResult readFileContent(
            @ApiParam(name = "path", required = true)
            @RequestParam(name = "path", required = true)
            String path
    ) {
        return ResultUtil.success(hdfsService.readFileContent(path));
    }

    @ApiOperation("list file")
    @GetMapping("/file/list")
    public ApiResult<List> listFile(
            @ApiParam(name = "path", required = true)
            @RequestParam(name = "path", required = true)
            String path
    ) {
        return new ApiResult<List>(hdfsService.listFile(path));
    }

    @ApiOperation("rename file")
    @PutMapping("/file")
    public ApiResult renameFile(
            @ApiParam(name = "oldName", required = true)
            @RequestParam(name = "oldName", required = true)
            String oldName,
            @ApiParam(name = "newName", required = true)
            @RequestParam(name = "newName", required = true)
            String newName
    ) {

        return ResultUtil.success(hdfsService.renameFile(oldName, newName));
    }

    @ApiOperation("delete file")
    @DeleteMapping("/file")
    public ApiResult deleteFile(
            @ApiParam(name = "path", required = true)
            @RequestParam(name = "path", required = true)
            String path,
            @ApiParam(name = "deleteTime", required = false)
            @RequestParam(name = "deleteTime", required = false)
            Boolean isNow
    ) {
        if (isNow == null || !isNow) {
            return ResultUtil.success(hdfsService.deleteFileOnExit(path));
        }
        return ResultUtil.success(hdfsService.deleteFile(path));
    }

    @ApiOperation("upload file")
    @PostMapping("/file/upload")
    public ApiResult uploadFile(
            @ApiParam(name = "path", required = true)
            @RequestParam(name = "path", required = true)
            String path,
            @ApiParam(name = "uploadPath", required = true)
            @RequestParam(name = "uploadPath", required = true)
            String uploadPath
    ) {
        return ResultUtil.success(hdfsService.uploadFile(path, uploadPath));
    }

//    @ApiOperation("download file")
//    @PostMapping("/file/download")
//    public ApiResult downloadFile(
//            @ApiParam(name = "path", required = true)
//            @RequestParam(name = "path", required = true)
//            String path,
//            @ApiParam(name = "downloadPath", required = true)
//            @RequestParam(name = "downloadPath", required = true)
//            String downloadPath
//    ) {
//        return ResultUtil.success(hdfsService.downloadFile(path, downloadPath));
//    }

//    @GetMapping("/file/download")
//    public ApiResult downloadFile(HttpServletResponse response,
//                                  @ApiParam(name = "path", required = true)
//                                  @RequestParam(name = "path", required = true)
//                                  String path
//    ) throws IOException {
//        FileEntity fileEntity = hdfsService.getFileEntity(path);
//        if (fileEntity == null) {
//            return ResultUtil.error(new CodeMsg(ResultCode.DATA_NOT_FOUND, "path is empty"));
//        }
//        fileEntity.setFileName(FileNameUtil.getFileNameByPath(path));
//
//
////        response.setContentType("multipart/form-data");
//        response.setContentType(FileUtil.getMimeType(fileEntity.getFileName()));
//
////        res.setHeader("Content-Disposition", "attachment;filename=bbb.txt"); // 下载
////        res.setHeader("Content-Disposition", "inline;filename=bbb.txt"); // 浏览器打开
//        response.setHeader("Content-Disposition", "inline;fileName=" + new String(fileEntity.getFileName().getBytes("GB2312"), "ISO-8859-1"));
//        response.setContentLength((int) fileEntity.getLength());
//        InputStream fis = hdfsService.downloadFile(path);
//        fis = new BufferedInputStream(fis);
//
//        OutputStream sos = response.getOutputStream();
//
//
//        IOUtils.copyBytes(fis, sos, 4096, false);
////        byte[] buff = new byte[4096];
////        fis.read(content);
////        fis.close();
////        sos.write(content);
//        sos.flush();
//        sos.close();
//        fis.close();
//        return ResultUtil.success();
//    }

    @ApiOperation("copy file")
    @PostMapping("/file/copy")
    public ApiResult copyFile(
            @ApiParam(name = "sourcePath", required = true)
            @RequestParam(name = "sourcePath", required = true)
            String sourcePath,
            @ApiParam(name = "targetPath", required = true)
            @RequestParam(name = "targetPath", required = true)
            String targetPath
    ) {
        return ResultUtil.success(hdfsService.copyFile(sourcePath, targetPath));
    }

    @ApiOperation("open file to types")
    @PostMapping("file/to-bytes")
    public ApiResult openFileToBytes(
            @ApiParam(name = "path", required = true)
            @RequestParam(name = "path", required = true)
            String path
    ) {
        byte[] bytes = hdfsService.openFileToBytes(path);
        Byte[] Bytes = new Byte[bytes.length];

        int i = 0;
        for (byte b : bytes) {
            Bytes[i++] = b;
        }
        return ResultUtil.success(Bytes);
    }

    @ApiOperation("get file block locations")
    @GetMapping("/file/block-localtions")
    public ApiResult getFileBlockLocations(
            @ApiParam(name = "path", required = true)
            @RequestParam(name = "path", required = true)
            String path
    ) {
        return ResultUtil.success(hdfsService.getFileBlockLocations(path));
    }

}
