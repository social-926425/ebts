package com.ebts.generator.dao;

import com.ebts.generator.entity.UniCon;
import com.ebts.generator.entity.UniQuery;
import com.ebts.generator.utils.entity.GenMenu;

import java.util.List;
import java.util.Map;

/**
 * @Author 18209
 * @Date 2021/1/30 18:52
 * @Version 1.0
 */
public interface QueryDao {

    /**
     * 通过id获取到万能查询的基本信息
     * @param id
     * @return
     */
    UniQuery selectQueryInfo(Long id);

    /**
     * 删除万能查询
     * @param uqId
     * @return
     */
    Integer deleteUniCon(Long uqId);

    /**
     * 新增万能查询
     * @param uniCons
     * @return
     */
    Integer insertUniCon(List<UniCon> uniCons);

    /**
     * 获取查询信息
     * @param paramSQL
     * @return
     */
    List<Map<String, Object>> UniQuery(String paramSQL);

    /**
     * 查询是否发布
     * @param id
     * @return
     */
    Integer Release(Long id);

    /**
     * 改变发布状态
     * @param uniQuery
     * @return
     */
    Integer changeRelease(UniQuery uniQuery);

    /**
     * 插入到菜单
     * @param genMenu
     * @return
     */
    Integer insertMenu(GenMenu genMenu);

    /**
     * 删除菜单
     * @param path
     * @return
     */
    Integer deleteMenu(String path);

}
