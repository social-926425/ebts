package com.ebts.generator.dao;

import com.ebts.generator.entity.RelColumn;
import com.ebts.generator.entity.RelTable;

import java.util.List;
import java.util.Map;

/**
 * @Author 18209
 * @Date 2021/2/24 16:21
 * @Version 1.0
 */
public interface RelDao {
    /**
     * 获取表信息
     *
     * @return
     */
    List<Map<String,Object>> tableInfos();

    /**
     * 获取表字段信息
     *
     * @param tableName
     * @return
     */
    List<RelColumn> selectTableColumn(String tableName);

    /**
     * 批量增加字段信息
     *
     * @param relColumns
     * @return
     */
    Integer insertRelColumns(List<RelColumn> relColumns);

    /**
     * 批量增加子表字段
     * @param relTables
     * @return
     */
    Integer insertRelTables(List<RelTable> relTables);

    /**
     * 通过tableName获取到column信息
     *
     * @param tableName
     * @return
     */
    List<Map<String,Object>> tableColumns(String tableName);

    /**
     * 通过主表id查询子表信息
     * @param tableId
     * @return
     */
    List<RelTable> relTableByTableId(Long tableId);

    /**
     * 通过relId删除RelColumn信息
     * @param relId
     * @return
     */
    Integer deleteRelColumnByRelId(Long relId);

    /**
     * 通过tableid删除RelTable信息
     * @param tableId
     * @return
     */
    Integer deleteRelTableByTableId(Long tableId);

}
