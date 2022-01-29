package com.ebts.generator.service.impl;

import com.ebts.generator.dao.RelDao;
import com.ebts.generator.entity.RelColumn;
import com.ebts.generator.entity.RelTable;
import com.ebts.generator.service.RelService;
import com.ebts.generator.utils.GenServerResult;
import com.ebts.generator.utils.RelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author 18209
 * @Date 2021/2/24 16:21
 * @Version 1.0
 */
@Service
public class RelServiceImpl implements RelService {
    private Logger logger = LoggerFactory.getLogger(RelServiceImpl.class);

    @Autowired
    private RelDao relDao;

    /**
     * 获取到数据库表信息
     *
     * @return
     */
    @Override
    public GenServerResult<List<Map<String, Object>>> tableInfos() {
        try {
            List<Map<String, Object>> dataMap = relDao.tableInfos();
            return new GenServerResult<>(dataMap);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return new GenServerResult<>(false, "获取数据库表信息错误");
        }
    }

    /**
     * 获取到关联边字段信息
     *
     * @param tableName 关联表表名
     * @param relId     关联关系id
     * @return
     */
    @Override
    public GenServerResult<List<RelColumn>> relColumns(String tableName, Long relId) {
        try {
            List<RelColumn> relColumns = relDao.selectTableColumn(tableName);
            for (RelColumn relColumn : relColumns) {
                RelUtil.initRelColumn(relColumn, relId);
            }
            return new GenServerResult<>(true,relColumns);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return new GenServerResult<>(false, "获取关联边字段信息错误");
        }
    }

    @Override
    public GenServerResult<List<Map<String, Object>>> tableColumns(String tableName) {
        try {
            List<Map<String,Object>> columns = relDao.tableColumns(tableName);
            return new GenServerResult<>(columns);
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            return new GenServerResult<>(false,"获取字段信息错误!");
        }
    }

    @Override
    public GenServerResult<List<RelTable>> relTableByTableId(Long tableId) {
        try {
            List<RelTable> relTables = relDao.relTableByTableId(tableId);
            return new GenServerResult<>(relTables);
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            return new GenServerResult<>(false,"获取关联数据错误!");
        }
    }
}