package com.ybau.transaction.util;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

@Component
@Slf4j
public class FtpUpload {

    @Value("${ftp.ftpIp}")
    String hostName;

    @Value("${ftp.ftpUser}")
    String userName;

    @Value("${ftp.ftpPass}")
    String passWord;

    @Value("${ftp.port}")
    String port;

    @Value("${ftp.bastPath}")
    String dir;
    //app/yb_network

    /**
     * 将文件直接上传到ftp服务器到方法，不会经过tomcat本地缓冲
     *
     * @param remotePath  ftp服务器中的文件夹
     * @param fileName    文件的新名称
     * @param inputStream
     * @return
     */


    public boolean uploadFile(String remotePath, String fileName, InputStream inputStream) throws IOException {
        boolean isSuccess = false;
        boolean result = false;
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(hostName);
            ftpClient.login(userName, passWord);
            createDir(remotePath, ftpClient);
            ftpClient.setConnectTimeout(0);
            ftpClient.setBufferSize(1024);
            ftpClient.setControlEncoding("UTF-8");
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            fileName = new String(fileName.getBytes(), "iso-8859-1");
            result = ftpClient.storeFile(fileName, inputStream);
            return result;
        } catch (IOException e) {
            log.info("文件上传失败 {}", e);
        } finally {
            try {

                inputStream.close();
                ftpClient.logout();
            } catch (IOException e) {
                log.info("输入流关闭失败 {}", e);
            }
        }
        return result;
    }

    /**
     * 删除文件方法
     * @param remotePath
     * @param fileName
     * @return
     */
    public boolean deleteFile(String remotePath, String fileName) {
        boolean isSuccess = false;
        boolean result = false;
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(hostName);
            isSuccess = ftpClient.login(userName, passWord);
            if (isSuccess) {
                ftpClient.changeWorkingDirectory(remotePath);
                result = ftpClient.deleteFile(fileName);
                return result;
            }
        } catch (IOException e) {
            log.info("文件上传失败 {}", e);
        } finally {
            try {
                ftpClient.logout();
            } catch (IOException e) {
                log.info("输入流关闭失败 {}", e);
            }
        }
        return result;
    }

    /**
     * 创建目录方法
     * @param remote
     * @param ftpClient
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public void createDir(String remote, FTPClient ftpClient) throws UnsupportedEncodingException, IOException {
        String directory = remote.substring(0, remote.lastIndexOf("/") + 1);
        if (!directory.equalsIgnoreCase("/")
                && !ftpClient.changeWorkingDirectory(new String(directory.getBytes("GBK"), "iso-8859-1"))) {
            int start = 0;
            int end = 0;
            if (directory.startsWith("/")) {
                start = 1;
            } else {
                start = 0;
            }
            end = directory.indexOf("/", start);
            while (true) {
                String subDirectory = new String(remote.substring(start, end).getBytes("GBK"), "iso-8859-1");
                if (!ftpClient.changeWorkingDirectory(subDirectory)) {
                    if (ftpClient.makeDirectory(subDirectory)) {
                        ftpClient.changeWorkingDirectory(subDirectory);
                    } else {
                        log.error("创建目录失败");
                    }

                }
                start = end + 1;
                end = directory.indexOf("/", start);
                if (end <= start) {
                    break;
                }
            }
        }
    }


}
