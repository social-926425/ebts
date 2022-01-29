package com.ebts.generator.service.impl;

import java.util.List;

import com.ebts.generator.dao.RegularDao;
import com.ebts.generator.entity.Regular;
import com.ebts.generator.service.RegularService;
import com.ebts.generator.utils.GenDateUtils;
import com.ebts.generator.utils.GenReturnConstants;
import com.ebts.generator.utils.GenSecurityUtils;
import com.ebts.generator.utils.GenServerResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 校验规则Service业务层处理
 *
 * @author binlin
 * @date 2021-01-18
 */
@Service
public class RegularServiceImpl implements RegularService {
    private Logger logger = LoggerFactory.getLogger(RegularServiceImpl.class);

    @Autowired
    private RegularDao regularDao;

    /**
     * 查询校验规则
     *
     * @param id 校验规则ID
     * @return 校验规则
     */
    @Override
    public GenServerResult<Regular> selectRegularById(Long id) {
        try {
            Regular regular = regularDao.selectRegularById(id);
            if (regular != null) {
                return new GenServerResult<>(true, regular);
            } else {
                return new GenServerResult<>(false, GenReturnConstants.RESULT_EMPTY);
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return new GenServerResult<>(false, GenReturnConstants.DB_EX);
        }
    }

    /**
     * 查询校验规则列表
     *
     * @param regular 校验规则
     * @return 校验规则
     */
    @Override
    public GenServerResult<List<Regular>> selectRegularList(Regular regular) {
        try {
            List<Regular> regulars = regularDao.selectRegularList(regular);
            if (regulars.size() > 0) {
                return new GenServerResult<>(true, regulars);
            } else {
                return new GenServerResult<>(false, GenReturnConstants.RESULT_EMPTY);
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return new GenServerResult<>(false, GenReturnConstants.DB_EX);
        }
    }

    /**
     * 新增校验规则
     *
     * @param regular 校验规则
     * @return 结果
     */
    @Override
    public GenServerResult<Integer> insertRegular(Regular regular) {
        try {
            regular.setCreateTime(GenDateUtils.getNowDate());
            regular.setCreateBy(GenSecurityUtils.getUserId());
            int renewal = regularDao.insertRegular(regular);
            return new GenServerResult<>(true, renewal);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return new GenServerResult<>(false, GenReturnConstants.DB_EX);
        }
    }

    /**
     * 修改校验规则
     *
     * @param regular 校验规则
     * @return 结果
     */
    @Override
    public GenServerResult<Integer> updateRegular(Regular regular) {
        try {
            regular.setUpdateTime(GenDateUtils.getNowDate());
            regular.setUpdateBy(GenSecurityUtils.getUserId());
            int renewal = regularDao.updateRegular(regular);
            return new GenServerResult<>(true, renewal);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return new GenServerResult<>(false, GenReturnConstants.DB_EX);
        }
    }

    /**
     * 批量删除校验规则
     *
     * @param ids 需要删除的校验规则ID
     * @return 结果
     */
    @Override
    public GenServerResult<Integer> deleteRegularByIds(Long[] ids) {
        try {
            Integer renewals = regularDao.deleteRegularByIds(ids);
            return new GenServerResult<>(true, renewals);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return new GenServerResult<>(false, GenReturnConstants.DB_EX);
        }
    }

    /**
     * 删除校验规则信息
     *
     * @param id 校验规则ID
     * @return 结果
     */
    @Override
    public GenServerResult<Integer> deleteRegularById(Long id) {
        try {
            Integer renewal = regularDao.deleteRegularById(id);
            return new GenServerResult<>(true, renewal);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return new GenServerResult<>(false, GenReturnConstants.DB_EX);
        }
    }
}
