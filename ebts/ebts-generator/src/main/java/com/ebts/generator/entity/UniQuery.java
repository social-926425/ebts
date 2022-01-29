package com.ebts.generator.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * 万能查询对象 sys_uni_query
 *
 * @author binlin
 * @date 2021-01-30
 */
public class UniQuery extends GenBaseEntity {

    /**
     * id
     */
    private Long id;

    /**
     * 名称
     */
    private String uqName;

    /**
     * sql语句
     */
    private String uqSql;

    /**
     * 描述
     */
    private String uqDescribe;

    /**
     * 是否发布(0:否,1:发布)
     */
    private Integer isRelease;

    private List<UniCon> uniCons;

    private Integer pageNum;

    private Integer pageSize;



    public Integer getIsRelease() {
        return isRelease;
    }

    public void setIsRelease(Integer isRelease) {
        this.isRelease = isRelease;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public List<UniCon> getUniCons() {
        if (uniCons == null) {
            return uniCons = new ArrayList<UniCon>();
        } else {
            return uniCons;
        }
    }

    public void setUniCons(List<UniCon> uniCons) {
        this.uniCons = uniCons;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setUqName(String uqName) {
        this.uqName = uqName;
    }

    public String getUqName() {
        return uqName;
    }

    public void setUqSql(String uqSql) {
        this.uqSql = uqSql;
    }

    public String getUqSql() {
        return uqSql;
    }

    public void setUqDescribe(String uqDescribe) {
        this.uqDescribe = uqDescribe;
    }

    public String getUqDescribe() {
        return uqDescribe;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("uqName", getUqName())
                .append("uqSql", getUqSql())
                .append("uqDescribe", getUqDescribe())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
