package com.ebts.generator.dao;


import com.ebts.generator.entity.UniQuery;

import java.util.List;

/**
 * 万能查询Mapper接口
 *
 * @author binlin
 * @date 2021-01-30
 */
public interface UniQueryDao {
    /**
     * 查询万能查询
     *
     * @param id 万能查询ID
     * @return 万能查询
     */
    UniQuery selectUniQueryById(Long id);

    /**
     * 查询万能查询列表
     *
     * @param uniQuery 万能查询
     * @return 万能查询集合
     */
    List<UniQuery> selectUniQueryList(UniQuery uniQuery);

    /**
     * 新增万能查询
     *
     * @param uniQuery 万能查询
     * @return 结果
     */
    int insertUniQuery(UniQuery uniQuery);

    /**
     * 修改万能查询
     *
     * @param uniQuery 万能查询
     * @return 结果
     */
    int updateUniQuery(UniQuery uniQuery);

    /**
     * 删除万能查询
     *
     * @param id 万能查询ID
     * @return 结果
     */
    int deleteUniQueryById(Long id);

    /**
     * 批量删除万能查询
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteUniQueryByIds(Long[] ids);

    int deleteUniQueryByUqId(Long id);


}
