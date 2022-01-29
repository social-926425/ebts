package com.ebts.system.service.impl;

import java.util.List;

import com.ebts.common.constant.ReturnConstants;
import com.ebts.common.exception.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ebts.common.core.entity.entity.DictData;
import com.ebts.common.utils.DictUtils;
import com.ebts.system.dao.DictDataDao;
import com.ebts.system.service.DictDataService;

/**
 * 字典 业务层处理
 *
 * @author binlin
 */
@Service
public class DictDataServiceImpl implements DictDataService {
    private Logger logger = LoggerFactory.getLogger(DictDataServiceImpl.class);
    @Autowired
    private DictDataDao dictDataDao;

    /**
     * 根据条件分页查询字典数据
     *
     * @param dictData 字典数据信息
     * @return 字典数据集合信息
     */
    @Override
    public List<DictData> selectDictDataList(DictData dictData) {
        try {
            return dictDataDao.selectDictDataList(dictData);
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw  new CustomException(ReturnConstants.DB_EX);
        }
    }

    /**
     * 根据字典类型和字典键值查询字典数据信息
     *
     * @param dictType  字典类型
     * @param dictValue 字典键值
     * @return 字典标签
     */
    @Override
    public String selectDictLabel(String dictType, String dictValue) {
        return dictDataDao.selectDictLabel(dictType, dictValue);
    }

    /**
     * 根据字典数据ID查询信息
     *
     * @param dictCode 字典数据ID
     * @return 字典数据
     */
    @Override
    public DictData selectDictDataById(Long dictCode) {
        return dictDataDao.selectDictDataById(dictCode);
    }

    /**
     * 批量删除字典数据信息
     *
     * @param dictCodes 需要删除的字典数据ID
     * @return 结果
     */
    @Override
    public int deleteDictDataByIds(Long[] dictCodes) {
        int row = dictDataDao.deleteDictDataByIds(dictCodes);
        if (row > 0) {
            DictUtils.clearDictCache();
        }
        return row;
    }

    /**
     * 新增保存字典数据信息
     *
     * @param dictData 字典数据信息
     * @return 结果
     */
    @Override
    public int insertDictData(DictData dictData) {
        int row = dictDataDao.insertDictData(dictData);
        if (row > 0) {
            DictUtils.clearDictCache();
        }
        return row;
    }

    /**
     * 修改保存字典数据信息
     *
     * @param dictData 字典数据信息
     * @return 结果
     */
    @Override
    public int updateDictData(DictData dictData) {
        int row = dictDataDao.updateDictData(dictData);
        if (row > 0) {
            DictUtils.clearDictCache();
        }
        return row;
    }
}
