package com.ebts.generator.entity;

public class RelColumn {

    //表id
    private long id;
    //父表id
    private long relId;
    //列名称
    private String columnName;
    //列描述
    private String columnComment;
    //列类型
    private String columnType;
    //java类型
    private String javaType;
    //java字段名
    private String javaField;
    //是否列表显示(1是)
    private String isList;
    //是否可查询(1可)
    private String isQuery;
    //查询方式(等于,不等于,小于,大于,区间)
    private String queryType;
    //控件显示类型
    private String htmlType;
    //字典类型
    private String dictType;
    //排序
    private Integer sort;
    //创建时间
    private long createBy;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRelId() {
        return relId;
    }

    public void setRelId(long relId) {
        this.relId = relId;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnComment() {
        return columnComment;
    }

    public void setColumnComment(String columnComment) {
        this.columnComment = columnComment;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public String getJavaField() {
        return javaField;
    }

    public void setJavaField(String javaField) {
        this.javaField = javaField;
    }

    public String getIsList() {
        return isList;
    }

    public void setIsList(String isList) {
        this.isList = isList;
    }

    public String getIsQuery() {
        return isQuery;
    }

    public void setIsQuery(String isQuery) {
        this.isQuery = isQuery;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    public String getHtmlType() {
        return htmlType;
    }

    public void setHtmlType(String htmlType) {
        this.htmlType = htmlType;
    }

    public String getDictType() {
        if (dictType == null) {
            dictType = "";
        }
        return dictType;
    }

    public void setDictType(String dictType) {
        if (dictType == null) {
            dictType = "";
        }
        this.dictType = dictType;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(long createBy) {
        this.createBy = createBy;
    }
}
