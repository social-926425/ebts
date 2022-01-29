package com.ebts.generator.service;

import java.util.List;

import com.ebts.generator.entity.UniQuery;
import com.ebts.generator.utils.GenServerResult;

/**
 * 万能查询Service接口
 *
 * @author binlin
 * @date 2021-01-30
 */
public interface UniQueryService {
    /**
     * 查询万能查询
     *
     * @param id 万能查询ID
     * @return 万能查询
     */
    GenServerResult<UniQuery> selectUniQueryById(Long id);

    /**
     * 查询万能查询列表
     *
     * @param uniQuery 万能查询
     * @return 万能查询集合
     */
    GenServerResult<List<UniQuery>> selectUniQueryList(UniQuery uniQuery);

    /**
     * 新增万能查询
     *
     * @param uniQuery 万能查询
     * @return 结果
     */
    GenServerResult<Integer> insertUniQuery(UniQuery uniQuery);

    /**
     * 修改万能查询
     *
     * @param uniQuery 万能查询
     * @return 结果
     */
    GenServerResult<Integer> updateUniQuery(UniQuery uniQuery);

    /**
     * 批量删除万能查询
     *
     * @param ids 需要删除的万能查询ID
     * @return 结果
     */
    GenServerResult<Integer> deleteUniQueryByIds(Long[] ids);

    /**
     * 删除万能查询信息
     *
     * @param id 万能查询ID
     * @return 结果
     */
    GenServerResult<Integer> deleteUniQueryById(Long id);
}
