package com.ebts.system.service.impl;

import java.util.List;
import javax.annotation.PostConstruct;

import com.ebts.common.constant.ReturnConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ebts.common.constant.UserConstants;
import com.ebts.common.core.entity.entity.DictData;
import com.ebts.common.core.entity.entity.DictType;
import com.ebts.common.exception.CustomException;
import com.ebts.common.utils.DictUtils;
import com.ebts.common.utils.StringUtils;
import com.ebts.system.dao.DictDataDao;
import com.ebts.system.dao.DictTypeDao;
import com.ebts.system.service.DictTypeService;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 * 字典 业务层处理
 *
 * @author binlin
 */
@Service
public class DictTypeServiceImpl implements DictTypeService {
    private Logger logger = LoggerFactory.getLogger(DictTypeServiceImpl.class);

    @Autowired
    private DictTypeDao dictTypeMapper;

    @Autowired
    private DictDataDao dictDataMapper;

    /**
     * 项目启动时，初始化字典到缓存
     */
    @PostConstruct
    public void init() {
        try {
            List<DictType> dictTypeList = dictTypeMapper.selectDictTypeAll();
            for (DictType dictType : dictTypeList) {
                List<DictData> dictDatas = dictDataMapper.selectDictDataByType(dictType.getDictType());
                DictUtils.setDictCache(dictType.getDictType(), dictDatas);
            }
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 根据条件分页查询字典类型
     *
     * @param dictType 字典类型信息
     * @return 字典类型集合信息
     */
    @Override
    public List<DictType> selectDictTypeList(DictType dictType) {
        try {
            return dictTypeMapper.selectDictTypeList(dictType);
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 根据所有字典类型
     *
     * @return 字典类型集合信息
     */
    @Override
    public List<DictType> selectDictTypeAll() {
        try {
            return dictTypeMapper.selectDictTypeAll();
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据集合信息
     */
    @Override
    public List<DictData> selectDictDataByType(String dictType) {
        try {
            List<DictData> dictDatas = DictUtils.getDictCache(dictType);
            if (StringUtils.isNotEmpty(dictDatas)) {
                return dictDatas;
            }
            dictDatas = dictDataMapper.selectDictDataByType(dictType);
            if (StringUtils.isNotEmpty(dictDatas)) {
                DictUtils.setDictCache(dictType, dictDatas);
                return dictDatas;
            }
            return null;
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 根据字典类型ID查询信息
     *
     * @param dictId 字典类型ID
     * @return 字典类型
     */
    @Override
    public DictType selectDictTypeById(Long dictId) {
        try {
            return dictTypeMapper.selectDictTypeById(dictId);
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 根据字典类型查询信息
     *
     * @param dictType 字典类型
     * @return 字典类型
     */
    @Override
    public DictType selectDictTypeByType(String dictType) {
        try {
            return dictTypeMapper.selectDictTypeByType(dictType);
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 批量删除字典类型信息
     *
     * @param dictIds 需要删除的字典ID
     * @return 结果
     */
    @Override
    public int deleteDictTypeByIds(Long[] dictIds) {
        try {
            for (Long dictId : dictIds) {
                DictType dictType = selectDictTypeById(dictId);
                if (dictDataMapper.countDictDataByType(dictType.getDictType()) > 0) {
                    throw new CustomException(String.format("%1$s已分配,不能删除", dictType.getDictName()));
                }
            }
            int count = dictTypeMapper.deleteDictTypeByIds(dictIds);
            if (count > 0) {
                DictUtils.clearDictCache();
            }
            return count;
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 清空缓存数据
     */
    @Override
    public void clearCache() {
        try {
            DictUtils.clearDictCache();
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 新增保存字典类型信息
     *
     * @param dictType 字典类型信息
     * @return 结果
     */
    @Override
    public int insertDictType(DictType dictType) {
        try {
            int row = dictTypeMapper.insertDictType(dictType);
            if (row > 0) {
                DictUtils.clearDictCache();
            }
            return row;
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 修改保存字典类型信息
     *
     * @param dictType 字典类型信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateDictType(DictType dictType) {
        try {
            DictType oldDict = dictTypeMapper.selectDictTypeById(dictType.getDictId());
            dictDataMapper.updateDictDataType(oldDict.getDictType(), dictType.getDictType());
            int row = dictTypeMapper.updateDictType(dictType);
            if (row > 0) {
                DictUtils.clearDictCache();
            }
            return row;
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 校验字典类型称是否唯一
     *
     * @param dict 字典类型
     * @return 结果
     */
    @Override
    public String checkDictTypeUnique(DictType dict) {
        try {
            Long dictId = StringUtils.isNull(dict.getDictId()) ? -1L : dict.getDictId();
            DictType dictType = dictTypeMapper.checkDictTypeUnique(dict.getDictType());
            if (StringUtils.isNotNull(dictType) && dictType.getDictId().longValue() != dictId.longValue()) {
                return UserConstants.NOT_UNIQUE;
            }
            return UserConstants.UNIQUE;
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }
}
