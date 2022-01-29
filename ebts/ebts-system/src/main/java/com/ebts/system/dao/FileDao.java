package com.ebts.system.dao;

import java.util.List;

import com.ebts.system.entity.SysFile;

/**
 * 文件信息Mapper接口
 *
 * @author binlin
 * @date 2021-02-17
 */
public interface FileDao {
    /**
     * 查询文件信息
     *
     * @param fileId 文件信息ID
     * @return 文件信息
     */
    SysFile selectFileById(Long fileId);

    /**
     * 查询文件信息列表
     *
     * @param sysFile 文件信息
     * @return 文件信息集合
     */
    List<SysFile> selectFileList(SysFile sysFile);

    /**
     * 查询文件夹列表
     * @return
     */
    List<SysFile> selectFileFolder();


    /**
     * 新增文件信息
     *
     * @param sysFile 文件信息
     * @return 结果
     */
    int insertFile(SysFile sysFile);

    /**
     * 修改文件信息
     *
     * @param sysFile 文件信息
     * @return 结果
     */
    int updateFile(SysFile sysFile);

    /**
     * 删除文件信息
     *
     * @param fileId 文件信息ID
     * @return 结果
     */
    int deleteFileById(Long fileId);

    /**
     * 批量删除文件信息
     *
     * @param fileIds 需要删除的数据ID
     * @return 结果
     */
    int deleteFileByIds(Long[] fileIds);
}
