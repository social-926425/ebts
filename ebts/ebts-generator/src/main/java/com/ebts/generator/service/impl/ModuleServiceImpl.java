package com.ebts.generator.service.impl;

import java.util.List;

import com.ebts.generator.dao.ModuleDao;
import com.ebts.generator.entity.Module;
import com.ebts.generator.service.ModuleService;
import com.ebts.generator.utils.GenReturnConstants;
import com.ebts.generator.utils.GenSecurityUtils;
import com.ebts.generator.utils.GenServerResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 模块管理Service业务层处理
 *
 * @author binlin
 * @date 2021-01-24
 */
@Service
public class ModuleServiceImpl implements ModuleService {
    private Logger logger = LoggerFactory.getLogger(ModuleServiceImpl.class);

    @Autowired
    private ModuleDao moduleDao;

    /**
     * 查询模块管理
     *
     * @param id 模块管理ID
     * @return 模块管理
     */
    @Override
    public GenServerResult<Module> selectModuleById(Long id) {
        try {
            Module module = moduleDao.selectModuleById(id);
            if (module != null) {
                return new GenServerResult<Module>(true, module);
            } else {
                return new GenServerResult<Module>(false, GenReturnConstants.RESULT_EMPTY);
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return new GenServerResult<Module>(false, GenReturnConstants.DB_EX);
        }
    }

    /**
     * 查询模块管理列表
     *
     * @param module 模块管理
     * @return 模块管理
     */
    @Override
    public GenServerResult<List<Module>> selectModuleList(Module module) {
        try {
            List<Module> moduleList = moduleDao.selectModuleList(module);
            if (moduleList.size() > 0) {
                return new GenServerResult<List<Module>>(true, moduleList);
            } else {
                return new GenServerResult<List<Module>>(false, GenReturnConstants.RESULT_EMPTY);
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return new GenServerResult<List<Module>>(false, GenReturnConstants.DB_EX);
        }
    }

    /**
     * 新增模块管理
     *
     * @param module 模块管理
     * @return 结果
     */
    @Override
    public GenServerResult<Integer> insertModule(Module module) {
        try {
            module.setCreateBy(GenSecurityUtils.getUserId());
            Integer renewal = moduleDao.insertModule(module);
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
     * 修改模块管理
     *
     * @param module 模块管理
     * @return 结果
     */
    @Override
    public GenServerResult<Integer> updateModule(Module module) {
        try {
            module.setUpdateBy(GenSecurityUtils.getUserId());
            Integer renewal = moduleDao.updateModule(module);
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
     * 批量删除模块管理
     *
     * @param ids 需要删除的模块管理ID
     * @return 结果
     */
    @Override
    public GenServerResult<Integer> deleteModuleByIds(Long[] ids) {
        try {
            Integer renewal = moduleDao.deleteModuleByIds(ids);
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
     * 删除模块管理信息
     *
     * @param id 模块管理ID
     * @return 结果
     */
    @Override
    public GenServerResult<Integer> deleteModuleById(Long id) {
        try {
            Integer renewal = moduleDao.deleteModuleById(id);
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
