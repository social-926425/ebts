package com.ebts.generator.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ebts.generator.aop.Excel;
import io.swagger.annotations.ApiModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 接口信息对象 sys_inter_table
 *
 * @author binlin
 * @date 2021-01-25
 */
@ApiModel("接口信息")
public class InterTable {

    /**
     * id
     */
    private Long id;

    /**
     * 模块id
     */
    @Excel(name = "模块id")
    private Long mId;

    /**
     * 类id
     */
    @Excel(name = "类id")
    private Long cId;

    /**
     * 接口名称
     */
    @Excel(name = "接口名称")
    private String itName;

    /**
     * 描述
     */
    @Excel(name = "描述")
    private String itDescribe;

    /**
     * 是否设置许可
     */
    @Excel(name = "是否设置许可")
    private String isPermission;

    /**
     * 请求路径
     */
    @Excel(name = "请求路径")
    private String requrl;

    /**
     * 请求方式
     */
    @Excel(name = "请求方式")
    private String method;

    /**
     * 是否生成
     */
    @Excel(name = "是否生成")
    private String isGenerate;
    /**
     * 类型
     */
    private Integer type;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 创建者
     */
    private Long createBy;

    private String mName;
    private ApiClass apiclass;

    public ApiClass getApiclass() {
        return apiclass;
    }

    public void setApiclass(ApiClass apiclass) {
        this.apiclass = apiclass;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getItName() {
        return itName;
    }

    public void setItName(String itName) {
        this.itName = itName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setmId(Long mId) {
        this.mId = mId;
    }

    public Long getmId() {
        return mId;
    }

    public void setcId(Long cId) {
        this.cId = cId;
    }

    public Long getcId() {
        return cId;
    }

    public void setItDescribe(String itDescribe) {
        this.itDescribe = itDescribe;
    }

    public String getItDescribe() {
        return itDescribe;
    }

    public void setRequrl(String requrl) {
        this.requrl = requrl;
    }

    public String getRequrl() {
        return requrl;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getIsPermission() {
        return isPermission;
    }

    public void setIsPermission(String isPermission) {
        this.isPermission = isPermission;
    }

    public String getIsGenerate() {
        return isGenerate;
    }

    public void setIsGenerate(String isGenerate) {
        this.isGenerate = isGenerate;
    }

    public String getMethod() {
        return method;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("mId", getmId())
                .append("cId", getcId())
                .append("itDescribe", getItDescribe())
                .append("requrl", getRequrl())
                .append("method", getMethod())
                .toString();
    }
}