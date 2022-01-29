package com.ebts.generator.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.ebts.generator.entity.RelTable;
import com.ebts.generator.utils.constant.GenConstants;
import org.apache.velocity.VelocityContext;
import com.alibaba.fastjson.JSONObject;
import com.ebts.generator.entity.GenTable;
import com.ebts.generator.entity.GenTableColumn;

/**
 * 模板处理工具类
 *
 * @author binlin
 */
public class VelocityUtils {
    /**
     * 项目空间路径
     */
    private static final String PROJECT_PATH = "main/java";

    /**
     * mybatis空间路径
     */
    private static final String MYBATIS_PATH = "main/resources/mapper";

    /**
     * 默认上级菜单，系统工具
     */
    private static final String DEFAULT_PARENT_MENU_ID = "3";

    /**
     * 设置模板变量信息
     *
     * @return 模板列表
     */
    public static VelocityContext prepareContext(GenTable genTable) {
        String moduleName = genTable.getModuleName();
        String businessName = genTable.getBusinessName();
        String packageName = genTable.getPackageName();
        String tplCategory = genTable.getTplCategory();
        String functionName = genTable.getFunctionName();

        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("tplCategory", genTable.getTplCategory());
        velocityContext.put("tableName", genTable.getTableName());
        velocityContext.put("functionName", GenStringUtils.isNotEmpty(functionName) ? functionName : "【请填写功能名称】");
        velocityContext.put("ClassName", genTable.getClassName());
        velocityContext.put("className", GenStringUtils.uncapitalize(genTable.getClassName()));
        velocityContext.put("moduleName", genTable.getModuleName());
        velocityContext.put("BusinessName", GenStringUtils.capitalize(genTable.getBusinessName()));
        velocityContext.put("businessName", genTable.getBusinessName());
        velocityContext.put("basePackage", getPackagePrefix(packageName));
        velocityContext.put("packageName", packageName);
        velocityContext.put("author", genTable.getFunctionAuthor());
        velocityContext.put("datetime", GenDateUtils.getDate());
        velocityContext.put("createBy", GenSecurityUtils.getUserId());
        velocityContext.put("pkColumn", genTable.getPkColumn());
        velocityContext.put("importList", getImportList(genTable));
        velocityContext.put("permissionPrefix", getPermissionPrefix(moduleName, businessName));
        velocityContext.put("columns", genTable.getColumns());
        velocityContext.put("table", genTable);
        setMenuVelocityContext(velocityContext, genTable);
        if (GenConstants.TPL_TREE.equals(tplCategory)) {
            setTreeVelocityContext(velocityContext, genTable);
        }
        if (GenConstants.TPL_SUB.equals(tplCategory)) {
            setSubVelocityContext(velocityContext, genTable);
        }
        if (GenConstants.TPL_REL.equals(tplCategory)) {
            setRelVelocityContext(velocityContext, genTable);
        }
        return velocityContext;
    }

    public static void setRelVelocityContext(VelocityContext context, GenTable genTable) {
        context.put("relTables", genTable.getRelTables());
        context.put("tableMapping", GenStringUtils.sqlFormat(genTable.getTableName()));
    }

    public static void setMenuVelocityContext(VelocityContext context, GenTable genTable) {
        String options = genTable.getOptions();
        JSONObject paramsObj = JSONObject.parseObject(options);
        String parentMenuId = getParentMenuId(paramsObj);
        context.put("parentMenuId", parentMenuId);
    }

    public static void setTreeVelocityContext(VelocityContext context, GenTable genTable) {
        String options = genTable.getOptions();
        JSONObject paramsObj = JSONObject.parseObject(options);
        String treeCode = getTreecode(paramsObj);
        String treeParentCode = getTreeParentCode(paramsObj);
        String treeName = getTreeName(paramsObj);

        context.put("treeCode", treeCode);
        context.put("treeParentCode", treeParentCode);
        context.put("treeName", treeName);
        context.put("expandColumn", getExpandColumn(genTable));
        if (paramsObj.containsKey(GenConstants.TREE_PARENT_CODE)) {
            context.put("tree_parent_code", paramsObj.getString(GenConstants.TREE_PARENT_CODE));
        }
        if (paramsObj.containsKey(GenConstants.TREE_NAME)) {
            context.put("tree_name", paramsObj.getString(GenConstants.TREE_NAME));
        }
    }

    public static void setSubVelocityContext(VelocityContext context, GenTable genTable) {
        GenTable subTable = genTable.getSubTable();
        String subTableName = genTable.getSubTableName();
        String subTableFkName = genTable.getSubTableFkName();
        String subClassName = genTable.getSubTable().getClassName();
        String subTableFkClassName = GenStringUtils.convertToCamelCase(subTableFkName);

        context.put("tableMapping", GenStringUtils.sqlFormat(genTable.getTableName()));
        context.put("subMapping", GenStringUtils.sqlFormat(subTable.getTableName()));
        context.put("subTable", subTable);
        context.put("subTableName", subTableName);
        context.put("subTableFkName", subTableFkName);
        context.put("subTableFkClassName", subTableFkClassName);
        context.put("subTableFkclassName", GenStringUtils.uncapitalize(subTableFkClassName));
        context.put("subClassName", subClassName);
        context.put("subclassName", GenStringUtils.uncapitalize(subClassName));
        context.put("subImportList", getImportList(genTable.getSubTable()));
    }

    /**
     * 获取模板信息
     *
     * @return 模板列表
     */
    public static List<String> getTemplateList(String tplCategory) {
        List<String> templates = new ArrayList<String>();
        templates.add("vm/java/entity.java.vm");
        templates.add("vm/java/dao.java.vm");
        templates.add("vm/java/service.java.vm");
        templates.add("vm/java/serviceImpl.java.vm");
        templates.add("vm/java/controller.java.vm");
        templates.add("vm/xml/mapper.xml.vm");
        templates.add("vm/sql/table.sql.vm");
        templates.add("vm/js/api.js.vm");
        if (GenConstants.TPL_CRUD.equals(tplCategory)) {
            templates.add("vm/vue/index.vue.vm");
        } else if (GenConstants.TPL_TREE.equals(tplCategory)) {
            templates.add("vm/vue/index-tree.vue.vm");
        } else if (GenConstants.TPL_SUB.equals(tplCategory)) {
            templates.add("vm/vue/index.vue.vm");
            templates.add("vm/java/sub-entity.java.vm");
        } else if (GenConstants.TPL_REL.equals(tplCategory)) {
            templates.add("vm/vue/index.vue.vm");
            templates.add("vm/java/rel-entity.java.vm");
        }
        return templates;
    }
    public static String getRelFileName(String template, GenTable genTable, RelTable relTable){

        // 文件名称
        String fileName = "";
        // 包路径
        String packageName = genTable.getPackageName();
        // 模块名
        String moduleName = genTable.getModuleName();

        String javaPath = PROJECT_PATH + "/" + GenStringUtils.replace(packageName, ".", "/");
        fileName = GenStringUtils.format("{}/entity/{}.java", javaPath, relTable.getRelClass());
        return fileName;
    }

    public static String getPreviewName(String template, String ClassName) {
        String fileName = "";
        if (template.contains("entity.java.vm")) {
            fileName = GenStringUtils.format("{}.java", ClassName);
        }
        if (template.contains("sub-entity.java.vm") && GenStringUtils.equals(GenConstants.TPL_SUB, ClassName)) {
            fileName = GenStringUtils.format("{}.java", ClassName);
        } else if (template.contains("rel-entity.java.vm")) {
            fileName = GenStringUtils.format("{}.java", ClassName);
        } else if (template.contains("dao.java.vm")) {
            fileName = GenStringUtils.format("{}Dao.java", ClassName);
        } else if (template.contains("service.java.vm")) {
            fileName = GenStringUtils.format("{}Service.java", ClassName);
        } else if (template.contains("serviceImpl.java.vm")) {
            fileName = GenStringUtils.format("{}ServiceImpl.java", ClassName);
        } else if (template.contains("controller.java.vm")) {
            fileName = GenStringUtils.format("{}Controller.java",ClassName);
        } else if (template.contains("mapper.xml.vm")) {
            fileName = GenStringUtils.format("{}Mapper.xml",ClassName);
        } else if (template.contains("table.sql.vm")) {
            fileName = ClassName + "menu.sql";
        } else if (template.contains("api.js.vm")) {
            fileName = GenStringUtils.format("{}.js", GenStringUtils.toLoad(ClassName));
        } else if (template.contains("index.vue.vm")) {
            fileName = "index.vue";
        } else if (template.contains("index-tree.vue.vm")) {
            fileName = "index.vue";
        }
        return fileName;
    }

    /**
     * 获取文件名
     */
    public static String getFileName(String template, GenTable genTable) {
        // 文件名称
        String fileName = "";
        // 包路径
        String packageName = genTable.getPackageName();
        // 模块名
        String moduleName = genTable.getModuleName();
        // 大写类名
        String className = genTable.getClassName();
        // 业务名称
        String businessName = genTable.getBusinessName();

        String javaPath = PROJECT_PATH + "/" + GenStringUtils.replace(packageName, ".", "/");
        String mybatisPath = MYBATIS_PATH + "/" + moduleName;
        String vuePath = "vue";

        if (template.contains("entity.java.vm")) {
            fileName = GenStringUtils.format("{}/entity/{}.java", javaPath, className);
        }
        if (template.contains("sub-entity.java.vm") && GenStringUtils.equals(GenConstants.TPL_SUB, genTable.getTplCategory())) {
            fileName = GenStringUtils.format("{}/entity/{}.java", javaPath, genTable.getSubTable().getClassName());
        } else if (template.contains("ass-entity.java.vm")) {
            fileName = GenStringUtils.format("{}/entity/{}.java", javaPath, GenUtils.convertClassName(genTable.getAssTableName()));
        } else if (template.contains("dao.java.vm")) {
            fileName = GenStringUtils.format("{}/dao/{}Dao.java", javaPath, className);
        } else if (template.contains("service.java.vm")) {
            fileName = GenStringUtils.format("{}/service/{}Service.java", javaPath, className);
        } else if (template.contains("serviceImpl.java.vm")) {
            fileName = GenStringUtils.format("{}/service/impl/{}ServiceImpl.java", javaPath, className);
        } else if (template.contains("controller.java.vm")) {
            fileName = GenStringUtils.format("{}/controller/{}Controller.java", javaPath, className);
        } else if (template.contains("mapper.xml.vm")) {
            fileName = GenStringUtils.format("{}/{}Mapper.xml", mybatisPath, className);
        } else if (template.contains("table.sql.vm")) {
            fileName = businessName + "menu.sql";
        } else if (template.contains("api.js.vm")) {
            fileName = GenStringUtils.format("{}/api/{}/{}.js", vuePath, moduleName, businessName);
        } else if (template.contains("index.vue.vm")) {
            fileName = GenStringUtils.format("{}/views/{}/{}/index.vue", vuePath, moduleName, businessName);
        } else if (template.contains("index-tree.vue.vm")) {
            fileName = GenStringUtils.format("{}/views/{}/{}/index.vue", vuePath, moduleName, businessName);
        }
        return fileName;
    }

    /**
     * 获取包前缀
     *
     * @param packageName 包名称
     * @return 包前缀名称
     */
    public static String getPackagePrefix(String packageName) {
        int lastIndex = packageName.lastIndexOf(".");
        String basePackage = GenStringUtils.substring(packageName, 0, lastIndex);
        return basePackage;
    }

    /**
     * 根据列类型获取导入包
     *
     * @param genTable 业务表对象
     * @return 返回需要导入的包列表
     */
    public static HashSet<String> getImportList(GenTable genTable) {
        List<GenTableColumn> columns = genTable.getColumns();
        GenTable subGenTable = genTable.getSubTable();
        HashSet<String> importList = new HashSet<String>();
        if (GenStringUtils.isNotNull(subGenTable)) {
            importList.add("java.util.List");
        }
        for (GenTableColumn column : columns) {
            if (!column.isSuperColumn() && GenConstants.TYPE_DATE.equals(column.getJavaType())) {
                importList.add("java.util.Date");
                importList.add("com.fasterxml.jackson.annotation.JsonFormat");
            } else if (!column.isSuperColumn() && GenConstants.TYPE_BIGDECIMAL.equals(column.getJavaType())) {
                importList.add("java.math.BigDecimal");
            }
        }
        return importList;
    }

    /**
     * 获取权限前缀
     *
     * @param moduleName   模块名称
     * @param businessName 业务名称
     * @return 返回权限前缀
     */
    public static String getPermissionPrefix(String moduleName, String businessName) {
        return GenStringUtils.format("{}:{}", moduleName, businessName);
    }

    /**
     * 获取上级菜单ID字段
     *
     * @param paramsObj 生成其他选项
     * @return 上级菜单ID字段
     */
    public static String getParentMenuId(JSONObject paramsObj) {
        if (GenStringUtils.isNotEmpty(paramsObj) && paramsObj.containsKey(GenConstants.PARENT_MENU_ID)) {
            return paramsObj.getString(GenConstants.PARENT_MENU_ID);
        }
        return DEFAULT_PARENT_MENU_ID;
    }

    /**
     * 获取树编码
     *
     * @param paramsObj 生成其他选项
     * @return 树编码
     */
    public static String getTreecode(JSONObject paramsObj) {
        if (paramsObj.containsKey(GenConstants.TREE_CODE)) {
            return GenStringUtils.toCamelCase(paramsObj.getString(GenConstants.TREE_CODE));
        }
        return GenStringUtils.EMPTY;
    }

    /**
     * 获取树父编码
     *
     * @param paramsObj 生成其他选项
     * @return 树父编码
     */
    public static String getTreeParentCode(JSONObject paramsObj) {
        if (paramsObj.containsKey(GenConstants.TREE_PARENT_CODE)) {
            return GenStringUtils.toCamelCase(paramsObj.getString(GenConstants.TREE_PARENT_CODE));
        }
        return GenStringUtils.EMPTY;
    }

    /**
     * 获取树名称
     *
     * @param paramsObj 生成其他选项
     * @return 树名称
     */
    public static String getTreeName(JSONObject paramsObj) {
        if (paramsObj.containsKey(GenConstants.TREE_NAME)) {
            return GenStringUtils.toCamelCase(paramsObj.getString(GenConstants.TREE_NAME));
        }
        return GenStringUtils.EMPTY;
    }

    /**
     * 获取需要在哪一列上面显示展开按钮
     *
     * @param genTable 业务表对象
     * @return 展开按钮列序号
     */
    public static int getExpandColumn(GenTable genTable) {
        String options = genTable.getOptions();
        JSONObject paramsObj = JSONObject.parseObject(options);
        String treeName = paramsObj.getString(GenConstants.TREE_NAME);
        int num = 0;
        for (GenTableColumn column : genTable.getColumns()) {
            if (column.isList()) {
                num++;
                String columnName = column.getColumnName();
                if (columnName.equals(treeName)) {
                    break;
                }
            }
        }
        return num;
    }
}
