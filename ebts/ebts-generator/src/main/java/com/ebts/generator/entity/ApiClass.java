package com.ebts.generator.entity;

import com.ebts.generator.aop.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * 接口类名对象 sys_apiclass
 *
 * @author binlin
 * @date 2021-01-24
 */
public class ApiClass extends GenBaseEntity {


    /**
     * 类id
     */
    private Long id;

    /**
     * 模块id
     */
    @Excel(name = "模块id")
    private Long mId;

    /**
     * 类名
     */
    @Excel(name = "类名")
    private String cName;

    /**
     * 类描述
     */
    @Excel(name = "类描述")
    private String cDescribe;

    /**
     * 包名
     */
    @Excel(name = "包名")
    private String packageName;

    /**
     * 前缀
     */
    @Excel(name = "前缀")
    private String prefix;
    /**
     * 作者
     */
    @Excel(name = "作者")
    private String author;

    /**
     * 电子邮件
     */
    @Excel(name = "电子邮件")
    private String email;

    /**
     * 模块类
     */
    private Module module;

    private List<InterTable> interTables;

    public List<InterTable> getInterTables() {
        return interTables;
    }

    public void setInterTables(List<InterTable> interTables) {
        if (interTables == null) {
            interTables = new ArrayList<InterTable>();
        }
        this.interTables = interTables;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
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

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getcName() {
        return cName;
    }

    public void setcDescribe(String cDescribe) {
        this.cDescribe = cDescribe;
    }

    public String getcDescribe() {
        return cDescribe;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("mId", getmId())
                .append("cName", getcName())
                .append("cDescribe", getcDescribe())
                .append("createTime", getCreateTime())
                .append("createBy", getCreateBy())
                .append("updateTime", getUpdateTime())
                .append("updateBy", getUpdateBy())
                .toString();
    }
}
