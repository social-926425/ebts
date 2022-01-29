package com.ebts.system.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.ebts.common.constant.ReturnConstants;
import com.ebts.common.core.entity.entity.Role;
import com.ebts.common.core.entity.model.LoginUser;
import com.ebts.common.utils.SecurityUtils;
import com.ebts.common.utils.ServerResult;
import com.ebts.system.entity.SysFile;
import com.ebts.system.utils.FtpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ebts.system.dao.FileDao;
import com.ebts.system.service.FileService;

/**
 * 文件信息Service业务层处理
 *
 * @author binlin
 * @date 2021-02-17
 */
@Service
public class FileServiceImpl implements FileService {
    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Autowired
    private FileDao fileDao;


    /**
     * 查询文件信息
     *
     * @param fileId 文件信息ID
     * @return 文件信息
     */
    @Override
    public ServerResult<SysFile> selectFileById(Long fileId) {
        try {
            SysFile sysFile = fileDao.selectFileById(fileId);
            if (sysFile != null) {
                return new ServerResult<>(true, sysFile);
            } else {
                return new ServerResult<>(false, ReturnConstants.RESULT_EMPTY);
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return new ServerResult<>(false, ReturnConstants.DB_EX);
        }
    }

    /**
     * 查询文件信息列表
     *
     * @param sysFile 文件信息
     * @return 文件信息
     */
    @Override
    public ServerResult<List<SysFile>> selectFileList(SysFile sysFile) {
        try {
            List<SysFile> sysFileList = fileDao.selectFileList(sysFile);
            if (sysFileList.size() > 0) {
                return new ServerResult<List<SysFile>>(true, sysFileList);
            } else {
                return new ServerResult<List<SysFile>>(false, ReturnConstants.RESULT_EMPTY);
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return new ServerResult<List<SysFile>>(false, ReturnConstants.DB_EX);
        }
    }

    @Override
    public ServerResult<List<SysFile>> selectFileFolder() {
        try {
            List<SysFile> sysFiles = fileDao.selectFileFolder();
            return new ServerResult<>(true, sysFiles);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return new ServerResult<List<SysFile>>(false, ReturnConstants.DB_EX);
        }
    }

    /**
     * 新增文件信息
     *
     * @param sysFile 文件信息
     * @return 结果
     */
    @Override
    public ServerResult<SysFile> insertFile(SysFile sysFile) {
        try {
            if (sysFile == null) {
                return new ServerResult<>(false, "文件上传失败!");
            }
            if (sysFile.getpId() != null) {
                if (sysFile.getpId() > 0) {
                    SysFile folder = fileDao.selectFileById(sysFile.getpId());
                    sysFile.setInherit(folder.getRoleIds());
                }
            }
            sysFile.setCreateBy(SecurityUtils.getUserId());
            try {
                sysFile = FtpUtils.uploadFtp(sysFile);
            } catch (RuntimeException e) {
                logger.error(e.getMessage());
                return new ServerResult<>(false, "文件处理出错,请重新上传文件!");
            }
            Integer renewal = fileDao.insertFile(sysFile);
            if (renewal > 0) {
                return new ServerResult<>(true, sysFile);
            } else {
                return new ServerResult<>(false, ReturnConstants.SYS_FAILL);
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return new ServerResult<>(false, ReturnConstants.DB_EX);
        }
    }

    /**
     * 修改文件信息
     *
     * @param sysFile 文件信息
     * @return 结果
     */
    @Override
    public ServerResult<Integer> updateFile(SysFile sysFile) {
        try {
            if (sysFile.getIsPublic().equals("1")){
                return new ServerResult<>(false,"此文件不可修改!");
            }
            if (sysFile.getpId() != null || sysFile.getpId() > 0) {
                SysFile folder = fileDao.selectFileById(sysFile.getpId());
                sysFile.setInherit(folder.getRoleIds());
            }
            sysFile.setUpdateBy(SecurityUtils.getUserId());
            Integer renewal = fileDao.updateFile(sysFile);
            if (renewal > 0) {
                return new ServerResult<>(true, renewal);
            } else {
                return new ServerResult<>(false, ReturnConstants.SYS_FAILL);
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return new ServerResult<>(false, ReturnConstants.DB_EX);
        }
    }

    /**
     * 批量删除文件信息
     *
     * @param fileIds 需要删除的文件信息ID
     * @return 结果
     */
    @Override
    public ServerResult<Integer> deleteFileByIds(Long[] fileIds) {
        try {
            List<SysFile> sysFiles = new ArrayList<>();
            SysFile sysFile = null;
            for (Long fileId : fileIds) {
                sysFile = fileDao.selectFileById(fileId);
                sysFiles.add(sysFile);
            }
            if (!FtpUtils.deleteFile(sysFiles)) {
                return new ServerResult<>(false, ReturnConstants.OP_ERROR);
            }
            Integer renewal = fileDao.deleteFileByIds(fileIds);
            if (renewal > 0) {
                return new ServerResult<>(true, renewal);
            } else {
                return new ServerResult<>(false, ReturnConstants.SYS_FAILL);
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return new ServerResult<Integer>(false, ReturnConstants.DB_EX);
        }
    }

    /**
     * 下载文件
     * @param fileId
     * @param loginUser
     * @return
     */
    @Override
    public ServerResult<SysFile> downloadFile(Long fileId, LoginUser loginUser) {
        try {
            SysFile sysFile = fileDao.selectFileById(fileId);
            if (sysFile.getIsPublic().equals("2")) {
                if (loginUser == null) {
                    return new ServerResult<>(false, "请登录后再试!");
                } else {
                    if (!loginUser.getUser().isAdmin()) {
                        String roleid;
                        if (sysFile.getpId() != 0) {
                            roleid = sysFile.getInherit() + "," + sysFile.getRoleIds();
                        } else {
                            roleid = sysFile.getRoleIds();
                        }
                        String[] roleIds = roleid.split(",");
                        boolean start = true;
                        for (Role role : loginUser.getUser().getRoles()) {
                            for (String roleId : roleIds) {
                                if (String.valueOf(role.getRoleId()).equals(roleId)) {
                                    start = false;
                                    break;
                                }
                            }
                            if (!start) {
                                break;
                            }
                        }
                        if (start) {
                            return new ServerResult<>(false, "您没有下载该文件的权限!");
                        }
                    }
                }
            }
            String fileName = FtpUtils.downloadFile(sysFile);
            if (fileName == null) {
                return new ServerResult<>(false, "文件下载失败!");
            } else {
                return new ServerResult<>(true, sysFile);
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return new ServerResult<>(false, ReturnConstants.DB_EX);
        }
    }

    /**
     * 删除文件信息信息
     *
     * @param fileId 文件信息ID
     * @return 结果
     */
    @Override
    public ServerResult<Integer> deleteFileById(Long fileId) {
        try {
            Integer renewal = fileDao.deleteFileById(fileId);
            if (renewal > 0) {
                return new ServerResult<>(true, renewal);
            } else {
                return new ServerResult<>(false, ReturnConstants.SYS_FAILL);
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return new ServerResult<>(false, ReturnConstants.DB_EX);
        }
    }

}
