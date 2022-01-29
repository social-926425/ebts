package com.ebts.generator.service.impl;

import java.util.List;

import com.ebts.generator.utils.GenReturnConstants;
import com.ebts.generator.utils.GenSecurityUtils;
import com.ebts.generator.utils.GenServerResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ebts.generator.dao.UniQueryDao;
import com.ebts.generator.entity.UniQuery;
import com.ebts.generator.service.UniQueryService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 * 万能查询Service业务层处理
 *
 * @author binlin
 * @date 2021-01-30
 */
@Service
public class UniQueryServiceImpl implements UniQueryService {
    private Logger logger = LoggerFactory.getLogger(UniQueryServiceImpl.class);

    @Autowired
    private UniQueryDao uniQueryDao;

    /**
     * 查询万能查询
     *
     * @param id 万能查询ID
     * @return 万能查询
     */
    @Override
    public GenServerResult<UniQuery> selectUniQueryById(Long id) {
        try {
            UniQuery uniQuery = uniQueryDao.selectUniQueryById(id);
            if (uniQuery != null) {
                return new GenServerResult<UniQuery>(true, uniQuery);
            } else {
                return new GenServerResult<UniQuery>(false, GenReturnConstants.RESULT_EMPTY);
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return new GenServerResult<UniQuery>(false, GenReturnConstants.DB_EX);
        }
    }

    /**
     * 查询万能查询列表
     *
     * @param uniQuery 万能查询
     * @return 万能查询
     */
    @Override
    public GenServerResult<List<UniQuery>> selectUniQueryList(UniQuery uniQuery) {
        try {
            List<UniQuery> uniQueryList = uniQueryDao.selectUniQueryList(uniQuery);
            if (uniQueryList.size() > 0) {
                return new GenServerResult<List<UniQuery>>(true, uniQueryList);
            } else {
                return new GenServerResult<List<UniQuery>>(false, GenReturnConstants.RESULT_EMPTY);
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return new GenServerResult<List<UniQuery>>(false, GenReturnConstants.DB_EX);
        }
    }

    /**
     * 新增万能查询
     *
     * @param uniQuery 万能查询
     * @return 结果
     */
    @Override
    public GenServerResult<Integer> insertUniQuery(UniQuery uniQuery) {
        try {
            uniQuery.setCreateBy(GenSecurityUtils.getUserId());
            Integer renewal = uniQueryDao.insertUniQuery(uniQuery);
            if (renewal > 0) {
                return new GenServerResult<Integer>(true, renewal);
            } else {
                return new GenServerResult<Integer>(false, GenReturnConstants.SYS_FAILL);
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return new GenServerResult<Integer>(false, GenReturnConstants.DB_EX);
        }
    }

    /**
     * 修改万能查询
     *
     * @param uniQuery 万能查询
     * @return 结果
     */
    @Override
    public GenServerResult<Integer> updateUniQuery(UniQuery uniQuery) {
        try {
            uniQuery.setUpdateBy(GenSecurityUtils.getUserId());
            Integer renewal = uniQueryDao.updateUniQuery(uniQuery);
            if (renewal > 0) {
                return new GenServerResult<Integer>(true, renewal);
            } else {
                return new GenServerResult<Integer>(false, GenReturnConstants.SYS_FAILL);
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return new GenServerResult<Integer>(false, GenReturnConstants.DB_EX);
        }
    }

    /**
     * 批量删除万能查询
     *
     * @param ids 需要删除的万能查询ID
     * @return 结果
     */
    @Override
    @Transactional
    public GenServerResult<Integer> deleteUniQueryByIds(Long[] ids) {
        try {
            Integer renewal = uniQueryDao.deleteUniQueryByIds(ids);
            for (Long id : ids) {
                uniQueryDao.deleteUniQueryByUqId(id);
            }
            if (renewal > 0) {
                return new GenServerResult<>(true, renewal);
            } else {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return new GenServerResult<>(false, GenReturnConstants.SYS_FAILL);
            }
        } catch (RuntimeException e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.error(e.getMessage());
            return new GenServerResult<>(false, GenReturnConstants.DB_EX);
        }
    }

    /**
     * 删除万能查询信息
     *
     * @param id 万能查询ID
     * @return 结果
     */
    @Override
    @Transactional
    public GenServerResult<Integer> deleteUniQueryById(Long id) {
        try {
            Integer renewal = uniQueryDao.deleteUniQueryById(id);
            uniQueryDao.deleteUniQueryByUqId(id);
            if (renewal > 0) {
                return new GenServerResult<>(true, renewal);
            } else {
                return new GenServerResult<>(false, GenReturnConstants.SYS_FAILL);
            }
        } catch (RuntimeException e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.error(e.getMessage());
            return new GenServerResult<>(false, GenReturnConstants.DB_EX);
        }
    }
}