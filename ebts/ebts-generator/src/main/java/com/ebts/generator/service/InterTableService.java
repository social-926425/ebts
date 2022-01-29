package com.ebts.generator.service;

import java.util.Map;

import com.ebts.generator.dto.InterTableDto;
import com.ebts.generator.entity.InterTable;
import com.ebts.generator.utils.GenServerResult;

/**
 * 接口信息Service接口
 *
 * @author binlin
 * @date 2021-01-25
 */
public interface InterTableService {
    /**
     * 查询接口信息
     *
     * @param id 接口信息ID
     * @return 接口信息
     */
    GenServerResult<InterTable> selectInterTableById(Long id);

    /**
     * 查询接口信息列表
     *
     * @param interTable 接口信息
     * @return 接口信息集合
     */
    GenServerResult<Map<String, Object>> selectInterTableList(InterTable interTable);

    /**
     * 新增接口信息
     *
     * @param interTable 接口信息
     * @return 结果
     */
    GenServerResult<Integer> insertInterTable(InterTable interTable);

    /**
     * 修改接口信息
     *
     * @param interTableDto 接口信息
     * @return 结果
     */
    GenServerResult<Integer> updateInterTable(InterTableDto interTableDto);

    /**
     * 批量删除接口信息
     *
     * @param ids 需要删除的接口信息ID
     * @return 结果
     */
    GenServerResult<Integer> deleteInterTableByIds(Long[] ids);

    /**
     * 删除接口信息信息
     *
     * @param id 接口信息ID
     * @return 结果
     */
    GenServerResult<Integer> deleteInterTableById(Long id);

    /**
     * 预览代码(工作流)
     *
     * @param cid
     * @return
     */
    GenServerResult<Map<String, String>> previewCodeCalss(Long cid);

    /**
     * 预览代码()工作台
     *
     * @param mid
     * @return
     */
    GenServerResult<Map<String, Object>> previewCodeMoudle(Long mid);

    /**
     * 生成代码(工作流)
     *
     * @param cid
     * @return
     */
    GenServerResult<byte[]> generatorCodeClass(Long cid);

    /**
     * 生成代码(工作台)
     *
     * @param mid
     * @return
     */
    GenServerResult<byte[]> generatorCodeMoudle(Long mid);

}