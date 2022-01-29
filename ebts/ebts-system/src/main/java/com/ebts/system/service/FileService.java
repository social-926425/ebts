package com.ebts.system.service;

import java.util.List;

import com.ebts.common.core.entity.model.LoginUser;
import com.ebts.common.utils.ServerResult;
import com.ebts.system.entity.SysFile;

/**
 * 文件信息Service接口
 *
 * @author binlin
 * @date 2021-02-17
 */
public interface FileService {
    /**
     * 查询文件信息
     *
     * @param fileId 文件信息ID
     * @return 文件信息
     */
    ServerResult<SysFile> selectFileById(Long fileId);

    /**
     * 查询文件信息列表
     *
     * @param sysFile 文件信息
     * @return 文件信息集合
     */
    ServerResult<List<SysFile>> selectFileList(SysFile sysFile);

    /**
     * 查询文件夹基本信息
     * @return
     */
    ServerResult<List<SysFile>> selectFileFolder();

    /**
     * 新增文件信息
     *
     * @param sysFile 文件信息
     * @return 结果
     */
    ServerResult<SysFile> insertFile(SysFile sysFile);

    /**
     * 修改文件信息
     *
     * @param sysFile 文件信息
     * @return 结果
     */
    ServerResult<Integer> updateFile(SysFile sysFile);

    /**
     * 批量删除文件信息
     *
     * @param fileIds 需要删除的文件信息ID
     * @return 结果
     */
    ServerResult<Integer> deleteFileByIds(Long[] fileIds);

    /**
     * 删除文件信息信息
     *
     * @param fileId 文件信息ID
     * @return 结果
     */
    ServerResult<Integer> deleteFileById(Long fileId);

    /**
     * 下载指定文件
     *
     * @param fileId
     * @param loginUser
     * @return
     */
    ServerResult<SysFile> downloadFile(Long fileId, LoginUser loginUser);

}
