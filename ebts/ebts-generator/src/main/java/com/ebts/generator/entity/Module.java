package com.ebts.generator.entity;

import com.ebts.generator.aop.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

/**
 * 模块管理对象 sys_module
 *
 * @author binlin
 * @date 2021-01-24
 */
public class Module extends GenBaseEntity {


    /**
     * 模块id
     */
    private Long id;

    /**
     * 模块名称
     */
    @Excel(name = "模块名称")
    private String mName;

    /**
     * 模块描述
     */
    @Excel(name = "模块描述")
    private String mDescribe;

    private List<ApiClass> apiClassList;

    public List<ApiClass> getApiclassList() {
        return apiClassList;
    }

    public void setApiclassList(List<ApiClass> apiClassList) {
        this.apiClassList = apiClassList;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmName() {
        return mName;
    }

    public void setmDescribe(String mDescribe) {
        this.mDescribe = mDescribe;
    }

    public String getmDescribe() {
        return mDescribe;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("mName", getmName())
                .append("mDescribe", getmDescribe())
                .append("createTime", getCreateTime())
                .append("createBy", getCreateBy())
                .append("updateTime", getUpdateTime())
                .append("updateBy", getUpdateBy())
                .toString();
    }
}
