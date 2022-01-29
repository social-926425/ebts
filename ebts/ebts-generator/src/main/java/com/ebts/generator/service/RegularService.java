package com.ebts.generator.service;

import com.ebts.generator.entity.Regular;
import com.ebts.generator.utils.GenServerResult;

import java.util.List;

/**
 * 校验规则Service接口
 *
 * @author binlin
 * @date 2021-01-18
 */
public interface RegularService {
    /**
     * 查询校验规则
     *
     * @param id 校验规则ID
     * @return 校验规则
     */
    GenServerResult<Regular> selectRegularById(Long id);

    /**
     * 查询校验规则列表
     *
     * @param regular 校验规则
     * @return 校验规则集合
     */
    GenServerResult<List<Regular>> selectRegularList(Regular regular);

    /**
     * 新增校验规则
     *
     * @param regular 校验规则
     * @return 结果
     */
    GenServerResult<Integer> insertRegular(Regular regular);

    /**
     * 修改校验规则
     *
     * @param regular 校验规则
     * @return 结果
     */
    GenServerResult<Integer> updateRegular(Regular regular);

    /**
     * 批量删除校验规则
     *
     * @param ids 需要删除的校验规则ID
     * @return 结果
     */
    GenServerResult<Integer> deleteRegularByIds(Long[] ids);

    /**
     * 删除校验规则信息
     *
     * @param id 校验规则ID
     * @return 结果
     */
    GenServerResult<Integer> deleteRegularById(Long id);
}
