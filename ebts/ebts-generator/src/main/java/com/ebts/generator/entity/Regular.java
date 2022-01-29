package com.ebts.generator.entity;

import com.ebts.generator.aop.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 校验规则对象 sys_regular
 *
 * @author binlin
 * @date 2021-01-18
 */
public class Regular extends GenBaseEntity {


    /**
     * id
     */
    private Long id;

    /**
     * 正则名称
     */
    @Excel(name = "正则名称")
    private String name;

    /**
     * 正则内容
     */
    @Excel(name = "正则内容")
    private String regular;

    /**
     * 验证内容
     */
    @Excel(name = "验证内容")
    private String validation;

    /**
     * 是否启用 1:启动 2:关闭
     */
    @Excel(name = "是否启用 1:启动 2:关闭")
    private Integer enable;


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setRegular(String regular) {
        this.regular = regular;
    }

    public String getRegular() {
        return regular;
    }

    public void setValidation(String validation) {
        this.validation = validation;
    }

    public String getValidation() {
        return validation;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    public Integer getEnable() {
        return enable;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("name", getName())
                .append("regular", getRegular())
                .append("validation", getValidation())
                .append("enable", getEnable())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
