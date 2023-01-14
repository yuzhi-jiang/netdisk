package com.yefeng.netdisk.common.util;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * This class is for
 *
 * @author 夜枫
 * @version 2023-01-07 17:11
 */
@Slf4j
public class DownFileUtil {

   static void downFileOnRange(HttpServletRequest request, HttpServletResponse response,
                    InputStream ins, long fileLength, String fileName) {
        BufferedInputStream bis=null;
        OutputStream out = null;
        try {
            bis =new BufferedInputStream(ins);
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

            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);

            int n = 0;
            long readLength = 0;
            int bsize = 4096;
            byte[] bytes = new byte[bsize];
            if (rangeSwitch == 2) {
                // 针对 bytes=27000-39000 的请求，从27000开始写数据
                while (readLength <= contentLength - bsize) {
                    n = bis.read(bytes);
                    readLength += n;
                    out.write(bytes, 0, n);
                }
                if (readLength <= contentLength) {
                    n = bis.read(bytes, 0, (int) (contentLength - readLength));
                    out.write(bytes, 0, n);
                }
            } else {
                while ((n = bis.read(bytes)) != -1) {
                    out.write(bytes, 0, n);
                }
            }
            out.flush();
            out.close();
            bis.close();
            ins.close();
        } catch (IOException ie) {
            // 忽略 ClientAbortException 之类的异常
            log.warn("ip {}-{} 主动关闭了连接", IPUtils.getHostIp(), IPUtils.getIpAddr(request));
            if(out!=null){
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
}
