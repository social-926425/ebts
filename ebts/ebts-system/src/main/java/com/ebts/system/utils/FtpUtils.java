package com.ebts.system.utils;


import com.ebts.common.config.EBTSConfig;
import com.ebts.common.utils.DateUtils;
import com.ebts.common.utils.file.FileUtils;
import com.ebts.common.utils.uuid.IdUtils;
import com.ebts.system.entity.SysFile;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;

/**
 * @Author 18209
 * @Date 2021/2/18 15:34
 * @Version 1.0
 */
@Component
@ConfigurationProperties(prefix = "ftp")
public class FtpUtils {

    private static Logger logger = LoggerFactory.getLogger(FtpUtils.class);

    private static String baseDir = "/profile";
    /**
     * 公开目录
     */
    private static String pubfiles;
    /**
     * 保护目录
     */
    private static String prifiles;

    public static String getPubfiles() {
        return pubfiles;
    }

    //ftp服务器ip地址
    private static String ftpAddress;
    //端口号
    private static int ftpPort;
    //用户名
    private static String ftpUsername;
    //密码
    private static String ftpPassword;
    //字符集编码
    private static String encoding;
    //静态资源域名
    private static String resources;
    //后端部署域名
    private static String apiurl;


    public static String getApiurl() {
        return apiurl;
    }

    public void setApiurl(String apiurl) {
        FtpUtils.apiurl = apiurl;
    }

    public static String getAvatarDir() {
        return pubfiles + "avatar";
    }

    public void setPubfiles(String pubfiles) {
        FtpUtils.pubfiles = pubfiles + "/";
    }

    public void setPrifiles(String prifiles) {
        FtpUtils.prifiles = prifiles + "/";
    }

    public static String getResources() {
        return resources;
    }

    public void setResources(String resources) {
        FtpUtils.resources = resources;
    }

    public void setFtpAddress(String ftpAddress) {
        FtpUtils.ftpAddress = ftpAddress;
    }

    public void setFtpPort(int ftpPort) {
        FtpUtils.ftpPort = ftpPort;
    }

    public void setFtpUsername(String ftpUsername) {
        FtpUtils.ftpUsername = ftpUsername;
    }

    public void setFtpPassword(String ftpPassword) {
        FtpUtils.ftpPassword = ftpPassword;
    }

    public void setEncoding(String encoding) {
        FtpUtils.encoding = encoding;
    }

    /**
     * 头像上传
     * @param file
     * @return
     */
    public static String uploadFtp(MultipartFile file) {
        String avatarDir = getAvatarDir();
        String avatarName = IdUtils.fastUUID();
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FTPClient ftpClient = linkFtp();
        if (ftpClient == null) {
            return null;
        }
        try {
            String[] basePathList = avatarDir.split("/");
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            for (String path : basePathList) {
                if (!ftpClient.changeWorkingDirectory(path)) {
                    ftpClient.makeDirectory(path);
                    ftpClient.changeWorkingDirectory(path);
                }
            }
            ftpClient.enterLocalPassiveMode();
            ftpClient.storeFile(avatarName, inputStream);
            inputStream.close();
            ftpClient.logout();
        } catch (IOException e) {
            logger.error(e.getMessage());
            return null;
        }
        return "/avatar/" + avatarName;
    }


    /**
     * 上传文件
     *
     * @param sysFile
     * @return
     */
    public static SysFile uploadFtp(SysFile sysFile) {
        String dir = EBTSConfig.getProfile() + sysFile.getFileAddr().substring(baseDir.length());
        String ftpdir = "/";
        if (sysFile.getIsPublic().equals("1")) {
            ftpdir += pubfiles;
        } else {
            ftpdir += prifiles;
        }
        ftpdir += DateUtils.datePath();
        File file = new File(dir);
        sysFile.setFileName(file.getName());
        sysFile.setFileSize(file.length() / 1024 / 1024);
        sysFile.setMapping(IdUtils.fastUUID() + "." + getExtension(file.getName()));
        sysFile.setFileAddr(ftpdir);
        sysFile.setFileType(getExtension(file.getName()));
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        FTPClient ftpClient = linkFtp();
        if (ftpClient == null) {
            return null;
        }
        try {
            String[] basePathList = ftpdir.split("/");
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);//设置文件类型
            for (int i = 0; i < basePathList.length; i++) {
                if (!ftpClient.changeWorkingDirectory(basePathList[i])) {
                    ftpClient.makeDirectory(basePathList[i]);//创建文件夹
                    ftpClient.changeWorkingDirectory(basePathList[i]);//改变目录
                }
            }
            ftpClient.enterLocalPassiveMode();
            ftpClient.storeFile(sysFile.getMapping(), inputStream);
            inputStream.close();
            ftpClient.logout();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        if (!FileUtils.deleteFile(dir)) {
            return null;
        }
        return sysFile;
    }


    /**
     * ftp下载文件
     *
     * @param sysFile
     * @return
     */
    public static String downloadFile(SysFile sysFile) {
        String[] basePathList = sysFile.getFileAddr().split("/");
        FTPClient ftpClient = linkFtp();
        try {
            if (ftpClient == null) {
                return null;
            }
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);//设置文件类型
            //设置linux ftp服务器
            FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_UNIX);
            ftpClient.configure(conf);
            //设置访问被动模式
            ftpClient.setRemoteVerificationEnabled(false);
            ftpClient.enterLocalPassiveMode();
            for (int i = 0; i < basePathList.length; i++) {
                ftpClient.changeWorkingDirectory(basePathList[i]);//改变目录
            }
//            FTPFile[] ftpFiles = ftpClient.listFiles(articleFile.getFileName());
//            if (ftpFiles == null || ftpFiles.length == 0) {
//                logger.error("远程文件不存在");
//                return null;
//            } else if (ftpFiles.length > 1) {
//                logger.error("远程文件是文件夹");
//                return null;
//            }
            String fileDir = EBTSConfig.getDownloadPath() + sysFile.getFileName();
            OutputStream os = new FileOutputStream(fileDir);
            ftpClient.retrieveFile(sysFile.getMapping(), os);
            return fileDir;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    /**
     * ftp删除文件
     *
     * @param sysFileList
     * @return
     */
    public static boolean deleteFile(List<SysFile> sysFileList) {
        FTPClient ftpClient = linkFtp();
        boolean start = false;
        try {
            if (ftpClient == null) {
                return false;
            }
            for (SysFile sysFile : sysFileList) {
                String[] basePathList = sysFile.getFileAddr().split("/");
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);//设置文件类型
                ftpClient.enterLocalPassiveMode();
                for (int i = 0; i < basePathList.length; i++) {
                    ftpClient.changeWorkingDirectory(basePathList[i]);//改变目录
                }
                start = ftpClient.deleteFile(sysFile.getMapping());
                if (!start) {
                    return false;
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("ftp删除文件出错:" + e.toString());
            return false;
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * ftp服务器连接
     *
     * @return
     */
    public static FTPClient linkFtp() {
        FTPClient ftpClient = new FTPClient();//实例FTP客户端
        ftpClient.enterLocalPassiveMode();
        ftpClient.setAutodetectUTF8(true);
        // 保存FTP控制连接使用的字符集，必须在连接前设置
        ftpClient.setControlEncoding(encoding);
        try {
            ftpClient.connect(ftpAddress, ftpPort);//连接FTP服务器
            ftpClient.login(ftpUsername, ftpPassword);//用户名密码登录
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                logger.error("未连接到FTP，用户名或密码错误。");
                ftpClient.disconnect();
                return null;
            } else {
                logger.info("FTP连接成功");
                return ftpClient;
            }
        } catch (IOException e) {
            logger.error("FTP登录失败" + e.getMessage());
            e.printStackTrace();
            try {
                ftpClient.disconnect();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            return null;
        }
    }

    public static final String getExtension(String fileName) {
        return FilenameUtils.getExtension(fileName);
    }

}
