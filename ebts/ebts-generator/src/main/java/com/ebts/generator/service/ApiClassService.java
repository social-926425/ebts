package com.ebts.generator.service;

import java.util.List;

import com.ebts.generator.entity.ApiClass;
import com.ebts.generator.utils.GenServerResult;

/**
 * 接口类名Service接口
 *
 * @author binlin
 * @date 2021-01-24
 */
public interface ApiClassService {
    /**
     * 查询接口类名
     *
     * @param id 接口类名ID
     * @return 接口类名
     */
    GenServerResult<ApiClass> selectApiclassById(Long id);

    /**
     * 查询接口类名列表
     *
     * @param apiclass 接口类名
     * @return 接口类名集合
     */
    GenServerResult<List<ApiClass>> selectApiclassList(ApiClass apiclass);

    /**
     * 新增接口类名
     *
     * @param apiclass 接口类名
     * @return 结果
     */
    GenServerResult<Integer> insertApiclass(ApiClass apiclass);

    /**
     * 修改接口类名
     *
     * @param apiclass 接口类名
     * @return 结果
     */
    GenServerResult<Integer> updateApiclass(ApiClass apiclass);

    /**
     * 批量删除接口类名
     *
     * @param ids 需要删除的接口类名ID
     * @return 结果
     */
    GenServerResult<Integer> deleteApiclassByIds(Long[] ids);

    /**
     * 删除接口类名信息
     *
     * @param id 接口类名ID
     * @return 结果
     */
    GenServerResult<Integer> deleteApiclassById(Long id);
}
