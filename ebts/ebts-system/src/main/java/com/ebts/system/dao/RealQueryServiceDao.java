package com.ebts.system.dao;

import com.ebts.system.entity.RealUniQuery;

import java.util.List;
import java.util.Map;

/**
 * @Author 18209
 * @Date 2021/2/22 18:04
 * @Version 1.0
 */
public interface RealQueryServiceDao {
    /**
     * 查询万能查询
     *
     * @param id 万能查询ID
     * @return 万能查询
     */
    RealUniQuery selectRealUniQueryById(Long id);


    /**
     * 查询结果
     * @param paramSQL
     * @return
     */
    List<Map<String, Object>> realUniQuery(String paramSQL);


    /**
     * 获取页面信息
     * @param id
     * @return
     */
    List<RealUniQuery> queryRealInfo(Long id);

}
