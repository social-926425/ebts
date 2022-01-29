package com.ebts.generator.service.impl;

import java.util.List;

import com.ebts.generator.dao.ApiClassDao;
import com.ebts.generator.entity.ApiClass;
import com.ebts.generator.service.ApiClassService;
import com.ebts.generator.utils.GenReturnConstants;
import com.ebts.generator.utils.GenSecurityUtils;
import com.ebts.generator.utils.GenServerResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 接口类名Service业务层处理
 *
 * @author binlin
 * @date 2021-01-24
 */
@Service
public class ApiClassServiceImpl implements ApiClassService {
    private Logger logger = LoggerFactory.getLogger(ApiClassServiceImpl.class);

    @Autowired
    private ApiClassDao apiclassDao;

    /**
     * 查询接口类名
     *
     * @param id 接口类名ID
     * @return 接口类名
     */
    @Override
    public GenServerResult<ApiClass> selectApiclassById(Long id) {
        try {
            ApiClass apiclass = apiclassDao.selectApiclassById(id);
            if (apiclass != null) {
                return new GenServerResult<ApiClass>(true, apiclass);
            } else {
                return new GenServerResult<ApiClass>(false, GenReturnConstants.RESULT_EMPTY);
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return new GenServerResult<ApiClass>(false, GenReturnConstants.DB_EX);
        }
    }

    /**
     * 查询接口类名列表
     *
     * @param apiclass 接口类名
     * @return 接口类名
     */
    @Override
    public GenServerResult<List<ApiClass>> selectApiclassList(ApiClass apiclass) {
        try {
            List<ApiClass> apiClassList = apiclassDao.selectApiclassList(apiclass);
            if (apiClassList.size() > 0) {
                return new GenServerResult<List<ApiClass>>(true, apiClassList);
            } else {
                return new GenServerResult<List<ApiClass>>(false, GenReturnConstants.RESULT_EMPTY);
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return new GenServerResult<List<ApiClass>>(false, GenReturnConstants.DB_EX);
        }
    }

    /**
     * 新增接口类名
     *
     * @param apiclass 接口类名
     * @return 结果
     */
    @Override
    public GenServerResult<Integer> insertApiclass(ApiClass apiclass) {
        try {
            apiclass.setCreateBy(GenSecurityUtils.getUserId());
            Integer renewal = apiclassDao.insertApiclass(apiclass);
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
     * 修改接口类名
     *
     * @param apiclass 接口类名
     * @return 结果
     */
    @Override
    public GenServerResult<Integer> updateApiclass(ApiClass apiclass) {
        try {
            apiclass.setUpdateBy(GenSecurityUtils.getUserId());
            Integer renewal = apiclassDao.updateApiclass(apiclass);
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
     * 批量删除接口类名
     *
     * @param ids 需要删除的接口类名ID
     * @return 结果
     */
    @Override
    public GenServerResult<Integer> deleteApiclassByIds(Long[] ids) {
        try {
            Integer renewal = apiclassDao.deleteApiclassByIds(ids);
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
     * 删除接口类名信息
     *
     * @param id 接口类名ID
     * @return 结果
     */
    @Override
    public GenServerResult<Integer> deleteApiclassById(Long id) {
        try {
            Integer renewal = apiclassDao.deleteApiclassById(id);
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
}
