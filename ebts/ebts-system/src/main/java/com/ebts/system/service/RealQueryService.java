package com.ebts.system.service;

import com.ebts.common.utils.ServerResult;
import com.ebts.system.entity.RealUniQuery;

import java.util.List;
import java.util.Map;

/**
 * @Author 18209
 * @Date 2021/2/22 18:03
 * @Version 1.0
 */
public interface RealQueryService {
    /**
     * 获取查询信息
     * @param realUniQuery
     * @param type
     * @return
     */
    ServerResult<List<Map<String, Object>>> RealData(RealUniQuery realUniQuery, Integer type);


    /**
     * 获取真实查询基本信息
     *
     * @param id
     * @return
     */
    ServerResult<List<RealUniQuery>> RealInfo(Long id);
}
