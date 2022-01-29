package com.ebts.generator.service;

import com.ebts.generator.entity.RelColumn;
import com.ebts.generator.entity.RelTable;
import com.ebts.generator.utils.GenServerResult;

import java.util.List;
import java.util.Map;

/**
 * @Author 18209
 * @Date 2021/2/24 16:21
 * @Version 1.0
 */
public interface RelService {

    /**
     * 获取到数据库表信息
     *
     * @return
     */
    GenServerResult<List<Map<String, Object>>> tableInfos();

    /**
     * 获取到关联边字段信息
     *
     * @param tableName 关联表表名
     * @param relId   关联关系id
     * @return
     */
    GenServerResult<List<RelColumn>> relColumns(String tableName, Long relId);

    /**
     * 通过tableName获取到字段信息
     *
     * @param tableName
     * @return
     */
    GenServerResult<List<Map<String,Object>>> tableColumns(String tableName);

    /**
     * 通过主表id查询子表信息
     *
     * @param tableId
     * @return
     */
    GenServerResult<List<RelTable>> relTableByTableId(Long tableId);

}
