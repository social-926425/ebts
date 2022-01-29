package com.ebts.generator.dao;

import com.ebts.generator.entity.ApiClass;

import java.util.List;


/**
 * 接口类名Mapper接口
 *
 * @author binlin
 * @date 2021-01-24
 */
public interface ApiClassDao {

    /**
     * 查询接口类名
     *
     * @param id 接口类名ID
     * @return 接口类名
     */
    ApiClass selectApiclassById(Long id);

    /**
     * 查询接口类名列表
     *
     * @param apiclass 接口类名
     * @return 接口类名集合
     */
    List<ApiClass> selectApiclassList(ApiClass apiclass);

    /**
     * 新增接口类名
     *
     * @param apiclass 接口类名
     * @return 结果
     */
    int insertApiclass(ApiClass apiclass);

    /**
     * 修改接口类名
     *
     * @param apiclass 接口类名
     * @return 结果
     */
    int updateApiclass(ApiClass apiclass);

    /**
     * 删除接口类名
     *
     * @param id 接口类名ID
     * @return 结果
     */
    int deleteApiclassById(Long id);

    /**
     * 批量删除接口类名
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteApiclassByIds(Long[] ids);
}
