package com.ebts.generator.entity;


/**
 * 万能查询条件对象 sys_uni_con
 *
 * @author binlin
 * @date 2021-01-30
 */
public class UniCon {


    /**
     * id
     */
    private Long id;

    /**
     * 万能查询tableid
     */
    private Long uqId;

    /**
     * 名称(lable)
     */
    private String ucName;

    /**
     * 关键字
     */
    private String ucKey;

    /**
     * 查询条件
     */
    private String ucCon;

    /**
     * 模拟数据
     */
    private String ucMock;

    /**
     * 真实数据
     */
    private Object ucReal;

    /**
     * 描述
     */
    private String ucDescribe;

    /**
     * 输入类型
     */
    private String ucType;

    /**
     * 显示类型
     */
    private Integer type;

    public String getUcType() {
        return ucType;
    }

    public void setUcType(String ucType) {
        this.ucType = ucType;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setUqId(Long uqId) {
        this.uqId = uqId;
    }

    public Long getUqId() {
        return uqId;
    }

    public void setUcName(String ucName) {
        this.ucName = ucName;
    }

    public String getUcName() {
        return ucName;
    }

    public void setUcKey(String ucKey) {
        this.ucKey = ucKey;
    }

    public String getUcKey() {
        return ucKey;
    }

    public void setUcCon(String ucCon) {
        this.ucCon = ucCon;
    }

    public String getUcCon() {
        return ucCon;
    }

    public String getUcMock() {
        return ucMock;
    }

    public void setUcMock(String ucMock) {
        this.ucMock = ucMock;
    }

    public String getUcDescribe() {
        return ucDescribe;
    }

    public void setUcDescribe(String ucDescribe) {
        this.ucDescribe = ucDescribe;
    }

    public Object getUcReal() {
        return ucReal;
    }

    public void setUcReal(Object ucReal) {
        this.ucReal = ucReal;
    }
}