package com.ebts.system.entity;

import com.ebts.common.annotation.Excel;
import com.ebts.common.core.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 文件信息对象 sys_file
 *
 * @author binlin
 * @date 2021-02-17
 */
@ApiModel("文件信息")
public class SysFile extends BaseEntity {

    /**
     * 文件id
     */
    @ApiModelProperty("文件id")
    private Long fileId;
    /**
     * 文件夹id
     */
    @ApiModelProperty("文件夹id")
    private Long pId;

    @ApiModelProperty("上级id")
    private Long unionId;

    /**
     * 角色id
     */
    @ApiModelProperty("角色id")
    private String roleIds;
    /**
     * 文件夹继承权限
     */
    @ApiModelProperty("文件夹继承权限")
    private String inherit;
    /**
     * 文件名称
     */
    @Excel(name = "文件名称")
    @ApiModelProperty("文件名称")
    private String fileName;
    /**
     * 映射名称
     */
    @ApiModelProperty("映射名称")
    private String mapping;
    /**
     * 文件路径
     */
    @Excel(name = "文件路径")
    @ApiModelProperty("文件路径")
    private String fileAddr;
    /**
     * 文件类型(文件夹:folder,文件:file)
     */
    @Excel(name = "文件类型")
    @ApiModelProperty("文件类型")
    private String fileType;
    /**
     * 文件大小(MB)
     */
    @Excel(name = "文件大小(MB)")
    @ApiModelProperty("文件大小(MB)")
    private Long fileSize;
    /**
     * 是否公开
     */
    @Excel(name = "是否公开")
    @ApiModelProperty("是否公开")
    private String isPublic;

    public Long getUnionId() {
        return unionId;
    }

    public void setUnionId(Long unionId) {
        this.unionId = unionId;
    }

    public String getInherit() {
        return inherit;
    }

    public void setInherit(String inherit) {
        this.inherit = inherit;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setpId(Long pId) {
        this.pId = pId;
    }

    public Long getpId() {
        return pId;
    }

    public String getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(String roleIds) {
        this.roleIds = roleIds;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setMapping(String mapping) {
        this.mapping = mapping;
    }

    public String getMapping() {
        return mapping;
    }

    public void setFileAddr(String fileAddr) {
        this.fileAddr = fileAddr;
    }

    public String getFileAddr() {
        return fileAddr;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileType() {
        return fileType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public void setIsPublic(String isPublic) {
        this.isPublic = isPublic;
    }

    public String getIsPublic() {
        return isPublic;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("fileId", getFileId())
                .append("pId", getpId())
                .append("roleIds", getRoleIds())
                .append("fileName", getFileName())
                .append("mapping", getMapping())
                .append("fileAddr", getFileAddr())
                .append("fileType", getFileType())
                .append("fileSize", getFileSize())
                .append("isPublic", getIsPublic())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
