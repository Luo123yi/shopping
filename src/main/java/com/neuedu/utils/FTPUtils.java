package com.neuedu.utils;

import org.apache.commons.net.ftp.FTPClient;

import java.io.*;
import java.util.List;

/**
 * ftp工具类
 */
public class FTPUtils {


    /**
     * 定义连接ftp的参数
     */
    private static final String FTPID=PropertiesUtils.readByKey("ftp.server.id");
    private static final String FTPUSERNAME=PropertiesUtils.readByKey("ftp.server.name");
    private static final String FTPPASSWORD=PropertiesUtils.readByKey("ftp.server.password");

    //定义成员变量
    private String ftpId;
    private String ftpName;
    private String ftpPassword;
    //端口号
    private Integer port;

    public FTPUtils(String ftpId, String ftpName, String ftpPassword, Integer port) {
        this.ftpId = ftpId;
        this.ftpName = ftpName;
        this.ftpPassword = ftpPassword;
        this.port = port;
    }

    /**
     * 图片上传到ftp
     * @param files--->文件集合
     * @return
     */
    public static boolean uploadFile(List<File> files) throws IOException {

        FTPUtils ftpUtils = new FTPUtils(FTPID,FTPUSERNAME,FTPPASSWORD,21);
        System.out.println("开始连接ftp服务器....");
        //连接ftp
        ftpUtils.uploadFile("img",files,ftpUtils);
        return false;
    }

    /**
     * 连接ftp
     * @param remotePath:连接ftp的远程地址
     * @param fileList：要传送的文件集合
     * @param ftpUtils：
     * @return
     */
    public boolean uploadFile(String remotePath,List<File> fileList,FTPUtils ftpUtils) throws IOException {
        InputStream inputStream =null;
        /**
         * 连接ftp服务器
         */
        if(connectFTPServer(ftpUtils)){
            try {
                ftpClient.changeWorkingDirectory(remotePath);
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("utf-8");
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();//打开被动传输模式
                for (File file:fileList) {
                    inputStream = new FileInputStream(file);
                    ftpClient.storeFile(file.getName(),inputStream);
                }
                System.out.println("文件上传成功");
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("文件上传有误.....");
            }finally {
                if(null != inputStream){
                    inputStream.close();
                }
                if(null != ftpClient){
                    ftpClient.disconnect();
                }
            }
        }
        return false;
    }

    /**
     * 连接服务器
     * @param ftpUtils
     * @return
     */
    FTPClient ftpClient = null;
     public boolean connectFTPServer(FTPUtils ftpUtils){
        ftpClient = new FTPClient();
         try {
             ftpClient.connect(ftpUtils.getFtpId());
             return ftpClient.login(ftpUtils.getFtpName(),ftpUtils.getFtpPassword());
         } catch (IOException e) {
             e.printStackTrace();
             System.out.println("连接ftp服务器失败....");
         }
         return false;
     }

    public String getFtpId() {
        return ftpId;
    }

    public void setFtpId(String ftpId) {
        this.ftpId = ftpId;
    }

    public String getFtpName() {
        return ftpName;
    }

    public void setFtpName(String ftpName) {
        this.ftpName = ftpName;
    }

    public String getFtpPassword() {
        return ftpPassword;
    }

    public void setFtpPassword(String ftpPassword) {
        this.ftpPassword = ftpPassword;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
