package com.ebts.generator.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.ebts.generator.service.GenTableColumnService;
import com.ebts.generator.utils.GenReturnConstants;
import com.ebts.generator.utils.exception.GenCustomException;
import com.ebts.generator.utils.text.Convert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ebts.generator.entity.GenTableColumn;
import com.ebts.generator.dao.GenTableColumnDao;

/**
 * 业务字段 服务层实现
 *
 * @author binlin
 */
@Service
public class GenTableColumnServiceImpl implements GenTableColumnService {
    private Logger logger = LoggerFactory.getLogger(GenTableColumnServiceImpl.class);

    @Autowired
    private GenTableColumnDao genTableColumnDao;

    /**
     * 查询业务字段列表
     *
     * @param tableId 业务字段编号
     * @return 业务字段集合
     */
    @Override
    public List<GenTableColumn> selectGenTableColumnListByTableId(Long tableId) {
        try {
            return genTableColumnDao.selectGenTableColumnListByTableId(tableId);
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new GenCustomException(GenReturnConstants.OP_ERROR);
        }
    }

    /**
     * 新增业务字段
     *
     * @param genTableColumn 业务字段信息
     * @return 结果
     */
    @Override
    public int insertGenTableColumn(GenTableColumn genTableColumn) {
        try {
            return genTableColumnDao.insertGenTableColumn(genTableColumn);
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new GenCustomException(GenReturnConstants.OP_ERROR);
        }
    }

    /**
     * 修改业务字段
     *
     * @param genTableColumn 业务字段信息
     * @return 结果
     */
    @Override
    public int updateGenTableColumn(GenTableColumn genTableColumn) {
        try {
            List<GenTableColumn> genTableColumns = new ArrayList<GenTableColumn>();
            genTableColumns.add(genTableColumn);
            return genTableColumnDao.updateGenTableColumn(genTableColumns);
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new GenCustomException(GenReturnConstants.OP_ERROR);
        }
    }

    /**
     * 删除业务字段对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteGenTableColumnByIds(String ids) {
        try {
            return genTableColumnDao.deleteGenTableColumnByIds(Convert.toLongArray(ids));
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new GenCustomException(GenReturnConstants.OP_ERROR);
        }
    }
}
