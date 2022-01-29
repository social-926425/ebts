package com.ebts.generator.dao;

import com.ebts.generator.entity.Module;

import java.util.List;

/**
 * 模块管理Mapper接口
 *
 * @author binlin
 * @date 2021-01-24
 */
public interface ModuleDao {
    /**
     * 查询模块管理
     *
     * @param id 模块管理ID
     * @return 模块管理
     */
    Module selectModuleById(Long id);

    /**
     * 查询模块管理列表
     *
     * @param module 模块管理
     * @return 模块管理集合
     */
    List<Module> selectModuleList(Module module);

    /**
     * 新增模块管理
     *
     * @param module 模块管理
     * @return 结果
     */
    int insertModule(Module module);

    /**
     * 修改模块管理
     *
     * @param module 模块管理
     * @return 结果
     */
    int updateModule(Module module);

    /**
     * 删除模块管理
     *
     * @param id 模块管理ID
     * @return 结果
     */
    int deleteModuleById(Long id);

    /**
     * 批量删除模块管理
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteModuleByIds(Long[] ids);
}
