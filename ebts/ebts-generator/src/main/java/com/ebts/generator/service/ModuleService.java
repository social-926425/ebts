package com.ebts.generator.service;


import com.ebts.generator.entity.Module;
import com.ebts.generator.utils.GenServerResult;

import java.util.List;

/**
 * 模块管理Service接口
 *
 * @author binlin
 * @date 2021-01-24
 */
public interface ModuleService {
    /**
     * 查询模块管理
     *
     * @param id 模块管理ID
     * @return 模块管理
     */
    GenServerResult<Module> selectModuleById(Long id);

    /**
     * 查询模块管理列表
     *
     * @param module 模块管理
     * @return 模块管理集合
     */
    GenServerResult<List<Module>> selectModuleList(Module module);

    /**
     * 新增模块管理
     *
     * @param module 模块管理
     * @return 结果
     */
    GenServerResult<Integer> insertModule(Module module);

    /**
     * 修改模块管理
     *
     * @param module 模块管理
     * @return 结果
     */
    GenServerResult<Integer> updateModule(Module module);

    /**
     * 批量删除模块管理
     *
     * @param ids 需要删除的模块管理ID
     * @return 结果
     */
    GenServerResult<Integer> deleteModuleByIds(Long[] ids);

    /**
     * 删除模块管理信息
     *
     * @param id 模块管理ID
     * @return 结果
     */
    GenServerResult<Integer> deleteModuleById(Long id);
}
