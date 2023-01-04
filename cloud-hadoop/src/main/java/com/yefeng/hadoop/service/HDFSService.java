package com.yefeng.hadoop.service;

import com.yefeng.hadoop.config.HDFSConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 夜枫
 */
public class HDFSService {

    private static FileSystem fileSystem;

    private static final String hostName = "hadoop102";
    private static final String port = "9000";
    private static final String username = "root";


     static {
//        HDFSConfig config = new HDFSConfig();
//        config.setHostname(hostName);
//        config.setPort(port);
//        config.setUsername(username);
//        Configuration configuration = new Configuration();
//        configuration.set("dfs.client.use.datanode.hostname", "true");
//        try {
//            System.out.println("getHdfsUrl(config): " + getHdfsUrl(config));
//            // 获得FileSystem对象，指定使用root用户上传
//            fileSystem = FileSystem.get(new URI(getHdfsUrl(config)),  configuration,
//                    config.getUsername());
//
//            System.out.println(fileSystem);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }



         Configuration configuration = null;
         String HADOOP_HOME = "D:\\oneDrive\\毕设\\网盘设计\\hadoop";
        try {
            configuration = new Configuration();
            configuration.set("fs.defaultFS", "hdfs://hadoop102:9000/");
            configuration.set("dfs.client.use.datanode.hostname", "true");

            /*System.setProperty("HADOOP_USER_NAME", "hdfs");*/
            System.setProperty("HADOOP_USER_NAME", "root");
            System.setProperty("hadoop.home.dir", HADOOP_HOME);
            configuration.set("dfs.permissions", "false");
            fileSystem = FileSystem.get(configuration);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件上传
     *
     * @param source
     * @param destination
     * @return void
     *
     * @author big uncle
     * @date 2021/5/10 20:00
     **/
    public static void upload(String source, String destination) {

        try {
            // 创建输入流，参数指定文件输出地址
            InputStream in = new FileInputStream(source);
            // 调用create方法指定文件上传，参数HDFS上传路径
            OutputStream out = fileSystem.create(new Path(destination));
            // 使用Hadoop提供的IOUtils，将in的内容copy到out，设置buffSize大小，是否关闭流设置true
            IOUtils.copyBytes(in, out, 4096, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Map<String, Object>> readPathInfo(String path) throws Exception {
        if (StringUtils.isEmpty(path)) {
            return null;
        }
        if (!existFile(path)) {
            return null;
        }
        FileSystem fs = fileSystem;
        // 目标路径
        Path newPath = new Path(path);
        FileStatus[] statusList = fs.listStatus(newPath);
        List<Map<String, Object>> list = new ArrayList<>();
        if (null != statusList && statusList.length > 0) {
            for (FileStatus fileStatus : statusList) {
                Map<String, Object> map = new HashMap<>();
                map.put("filePath", fileStatus.getPath());
                map.put("fileStatus", fileStatus.toString());
                list.add(map);
            }
            return list;
        } else {
            return null;
        }
    }

    public static boolean existFile(String path) throws Exception {
        if (StringUtils.isEmpty(path)) {
            return false;
        }
        FileSystem fs = fileSystem;
        Path srcPath = new Path(path);
        boolean isExists = fs.exists(srcPath);
        return isExists;
    }

    /**
     * 文件下载
     *
     * @param source
     * @param destination
     * @return void
     *
     * @author big uncle
     * @date 2021/5/10 20:00
     **/
    public static void download(String source, String destination) {

        try {
            // 调用open方法进行下载，参数HDFS路径
            InputStream in = fileSystem.open(new Path(source));


            // 创建输出流，参数指定文件输出地址
            OutputStream out = new FileOutputStream(destination);
            IOUtils.copyBytes(in, out, 4096, true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除文件
     *
     * @param target
     * @return boolean
     *
     * @author big uncle
     * @date 2021/5/10 20:00
     **/
    public static boolean delete(String target) {
        boolean flag = false;
        try {
            // 调用delete方法，删除指定的文件。参数:false:表示是否递归删除
            flag = fileSystem.delete(new Path(target), false);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return flag;
    }

    /**
     * 创建文件夹
     *
     * @param directory
     * @return boolean
     *
     * @author big uncle
     * @date 2021/5/10 19:59
     **/
    public static boolean mkdir(String directory) {
        boolean flag = false;
        try {
            // 调用mkdirs方法，在HDFS文件服务器上创建文件夹。
            flag = fileSystem.mkdirs(new Path(directory));

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        ;
        return flag;
    }

    /**
     * 拼接连接
     *
     * @param config
     * @return java.lang.String
     *
     * @author big uncle
     * @date 2021/5/10 20:00
     **/
    private static String getHdfsUrl(HDFSConfig config) {
        StringBuilder builder = new StringBuilder();
        builder.append("hdfs://").append(config.getHostname()).append(":").append(config.getPort());
        return builder.toString();
    }
    public static void listFile1() throws Exception {
        try {
            //获取所有的文件或者文件夹; 指定遍历的路径，指定是否要递归遍历
            RemoteIterator<LocatedFileStatus> locatedFileStatusRemoteIterator = fileSystem.listFiles(new Path("/"), true);
            while (locatedFileStatusRemoteIterator.hasNext()) {
                // 获取得到每一个文件详细信息
                LocatedFileStatus fileStatus = locatedFileStatusRemoteIterator.next();
                // 获取每一个文件存储路径 名称
                System.out.println(fileStatus.getPath());
                System.out.println(fileStatus.getPath().getName());
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(HDFSService.class);
        try {
            logger.info("info");
            logger.warn("warn");
            logger.debug("debug");
            logger.error("error");
//            List<Map<String, Object>> list = readPathInfo("/");
//            list.forEach(System.out::println);
//            listFile1();
//            upload("F:\\Projects\\IDEAproject\\netdisk\\cloud-hadoop\\src\\main\\resources\\logback-test.xml","wcinput/logback1.xml");
//            delete("yefeng/logback-test.xml");
//            download("wcinput/logback1.xml","./logback1.xml");
        } catch (Exception e) {
//            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}