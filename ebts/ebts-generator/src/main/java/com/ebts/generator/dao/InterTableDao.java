package com.ebts.generator.dao;

import java.util.List;

import com.ebts.generator.entity.ApiClass;
import com.ebts.generator.entity.InterTable;

/**
 * 接口信息Mapper接口
 *
 * @author binlin
 * @date 2021-01-25
 */
public interface InterTableDao {
    /**
     * 查询接口信息
     *
     * @param id 接口信息ID
     * @return 接口信息
     */
    InterTable selectInterTableById(Long id);

    /**
     * 查询接口信息列表
     *
     * @param interTable 接口信息
     * @return 接口信息集合
     */
    List<InterTable> selectInterTableList(InterTable interTable);

    /**
     * 新增接口信息
     *
     * @param interTable 接口信息
     * @return 结果
     */
    int insertInterTable(InterTable interTable);

    /**
     * 批量新增接口信息
     *
     * @param interTables
     * @return
     */
    int insertInterTables(List<InterTable> interTables);

    /**
     * 通过类id删除接口
     *
     * @param cId
     * @return
     */
    int deleteInterTableByClassId(Long cId);

    /**
     * 通过模块id删除接口
     *
     * @param mId
     * @return
     */
    int deleteInterTableByModuleId(Long mId);

    /**
     * 修改接口信息
     *
     * @param interTable 接口信息
     * @return 结果
     */
    int updateInterTable(InterTable interTable);


    /**
     * 删除接口信息
     *
     * @param id 接口信息ID
     * @return 结果
     */
    int deleteInterTableById(Long id);

    /**
     * 批量删除接口信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteInterTableByIds(Long[] ids);

    ApiClass selectInterTableClass(Long id);

    List<InterTable> selectInterTableModule(Long id);
}