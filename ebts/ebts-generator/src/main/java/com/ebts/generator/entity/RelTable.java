package com.ebts.generator.entity;

import java.util.List;

public class RelTable {


  //表id
  private long id;
  //主表id
  private long tableId;
  //关联子表表名
  private String relName;
  //子表表名称简写
  private String relAs;
  //父表名称
  private String tableName;
  //父表名称简写
  private String tableAs;
  //关联子表描述
  private String relComment;
  //关联子表的字段
  private String relColumn;
  //关联父表字段
  private String tableColumn;
  //实体类名称(子表)
  private String relClass;
  //实体小写类名称(子表)
  private String relclass;
  //查询方式
  private String queryType;
  //排序
  private Integer sort;
  //创建者
  private long createBy;

  private List<RelColumn> relColumns;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public long getTableId() {
    return tableId;
  }

  public void setTableId(long tableId) {
    this.tableId = tableId;
  }

  public String getRelName() {
    return relName;
  }

  public void setRelName(String relName) {
    this.relName = relName;
  }

  public String getRelAs() {
    return relAs;
  }

  public void setRelAs(String relAs) {
    this.relAs = relAs;
  }

  public String getTableName() {
    return tableName;
  }

  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

  public String getTableAs() {
    return tableAs;
  }

  public void setTableAs(String tableAs) {
    this.tableAs = tableAs;
  }

  public String getRelComment() {
    return relComment;
  }

  public void setRelComment(String relComment) {
    this.relComment = relComment;
  }

  public String getRelColumn() {
    return relColumn;
  }

  public void setRelColumn(String relColumn) {
    this.relColumn = relColumn;
  }

  public String getTableColumn() {
    return tableColumn;
  }

  public void setTableColumn(String tableColumn) {
    this.tableColumn = tableColumn;
  }

  public String getRelClass() {
    return relClass;
  }

  public void setRelClass(String relClass) {
    this.relClass = relClass;
  }

  public String getRelclass() {
    return relclass;
  }

  public void setRelclass(String relclass) {
    this.relclass = relclass;
  }

  public String getQueryType() {
    return queryType;
  }

  public void setQueryType(String queryType) {
    this.queryType = queryType;
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

  public List<RelColumn> getRelColumns() {
    return relColumns;
  }

  public void setRelColumns(List<RelColumn> relColumns) {
    this.relColumns = relColumns;
  }
}
