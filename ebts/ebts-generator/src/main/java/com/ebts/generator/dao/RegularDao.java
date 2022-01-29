package com.ebts.generator.dao;

import java.util.List;

import com.ebts.generator.entity.Regular;

/**
 * 校验规则Dao接口
 *
 * @author binlin
 * @date 2021-01-18
 */
public interface RegularDao {
    /**
     * 查询校验规则
     *
     * @param id 校验规则ID
     * @return 校验规则
     */
    Regular selectRegularById(Long id);

    /**
     * 查询校验规则列表
     *
     * @param regular 校验规则
     * @return 校验规则集合
     */
    List<Regular> selectRegularList(Regular regular);

    /**
     * 新增校验规则
     *
     * @param regular 校验规则
     * @return 结果
     */
    int insertRegular(Regular regular);

    /**
     * 修改校验规则
     *
     * @param regular 校验规则
     * @return 结果
     */
    int updateRegular(Regular regular);

    /**
     * 删除校验规则
     *
     * @param id 校验规则ID
     * @return 结果
     */
    int deleteRegularById(Long id);

    /**
     * 批量删除校验规则
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteRegularByIds(Long[] ids);
}
