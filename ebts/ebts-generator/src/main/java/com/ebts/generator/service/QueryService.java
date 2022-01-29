package com.ebts.generator.service;

import com.ebts.generator.entity.UniQuery;
import com.ebts.generator.utils.GenServerResult;

import java.util.List;
import java.util.Map;

/**
 * @Author 18209
 * @Date 2021/1/30 18:54
 * @Version 1.0
 */
public interface QueryService {
    /**
     * 通过id查询基础信息
     *
     * @param id
     * @return
     */
    GenServerResult<UniQuery> selectQueryById(Long id);

    /**
     * 修改基础信息
     *
     * @param uniQuery
     * @return
     */
    GenServerResult<Integer> updateQueryInfo(UniQuery uniQuery);

    /**
     * 预览万能查询数据
     *
     * @param uniQuery
     * @return
     */
    GenServerResult<List<Map<String, Object>>> previewQuery(UniQuery uniQuery);

    /**
     * @param uniQuery
     * @return
     */
    GenServerResult<Integer> Release(UniQuery uniQuery);
}
