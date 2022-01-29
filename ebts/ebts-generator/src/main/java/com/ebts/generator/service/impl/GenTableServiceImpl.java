package com.ebts.generator.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.ebts.generator.dao.RelDao;
import com.ebts.generator.entity.*;
import com.ebts.generator.service.GenTableService;
import com.ebts.generator.utils.*;
import com.ebts.generator.utils.constant.Constants;
import com.ebts.generator.utils.constant.GenConstants;
import com.ebts.generator.utils.exception.GenCustomException;
import com.ebts.generator.utils.text.CharsetKit;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ebts.generator.dao.GenTableColumnDao;
import com.ebts.generator.dao.GenTableDao;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 * 业务 服务层实现
 *
 * @author binlin
 */
@Service
public class GenTableServiceImpl implements GenTableService {
    private static final Logger logger = LoggerFactory.getLogger(GenTableServiceImpl.class);

    @Autowired
    private GenTableDao genTableDao;

    @Autowired
    private GenTableColumnDao genTableColumnDao;


    @Autowired
    private RelDao relDao;

    /**
     * 查询业务信息
     *
     * @param id 业务ID
     * @return 业务信息
     */
    @Override
    public GenTable selectGenTableById(Long id) {
        try {
            GenTable genTable = genTableDao.selectGenTableById(id);
            setTableFromOptions(genTable);
            return genTable;
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            throw new GenCustomException(GenReturnConstants.OP_ERROR);
        }
    }

    /**
     * 查询业务列表
     *
     * @param genTable 业务信息
     * @return 业务集合
     */
    @Override
    public List<GenTable> selectGenTableList(GenTable genTable) {
        try {
            return genTableDao.selectGenTableList(genTable);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            throw new GenCustomException(GenReturnConstants.OP_ERROR);
        }
    }

    /**
     * 查询据库列表
     *
     * @param genTable 业务信息
     * @return 数据库表集合
     */
    @Override
    public List<GenTable> selectDbTableList(GenTable genTable) {
        try {
            return genTableDao.selectDbTableList(genTable);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            throw new GenCustomException(GenReturnConstants.OP_ERROR);
        }
    }

    /**
     * 查询据库列表
     *
     * @param tableNames 表名称组
     * @return 数据库表集合
     */
    @Override
    public List<GenTable> selectDbTableListByNames(String[] tableNames) {
        try {
            return genTableDao.selectDbTableListByNames(tableNames);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            throw new GenCustomException(GenReturnConstants.OP_ERROR);
        }
    }

    /**
     * 查询所有表信息
     *
     * @return 表信息集合
     */
    @Override
    public List<GenTable> selectGenTableAll() {
        try {
            return genTableDao.selectGenTableAll();
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            throw new GenCustomException(GenReturnConstants.OP_ERROR);
        }
    }

    /**
     * todo 修改业务
     *
     * @param genTable 业务信息
     * @return 结果
     */
    @Override
    @Transactional
    public void updateGenTable(GenTable genTable) {
        try {
            String options = JSON.toJSONString(genTable.getParams());
            genTable.setOptions(options);
            int row = genTableDao.updateGenTable(genTable);
            if (row > 0) {
                genTableColumnDao.updateGenTableColumn(genTable.getColumns());
                if (genTable.getTplCategory().equals("rel")){
                    List<RelTable> relTables = relDao.relTableByTableId(genTable.getTableId());
                    for (RelTable relTable : relTables) {
                        relDao.deleteRelColumnByRelId(relTable.getId());
                    }
                    relDao.deleteRelTableByTableId(genTable.getTableId());
                    relTables = genTable.getRelTables();
                    for (int i = 0; i < relTables.size(); i++) {
                        RelTable relTable = relTables.get(i);
                        relTable.setTableAs(GenStringUtils.sqlFormat(relTable.getTableName()));
                        relTable.setRelAs(GenStringUtils.sqlFormat(relTable.getRelName()));
                        relTable.setRelClass(GenUtils.convertClassName(relTable.getRelName()));
                        relTable.setCreateBy(GenSecurityUtils.getUserId());
                        relTable.setRelclass(GenStringUtils.uncapitalize(relTable.getRelClass()));
                        relTables.set(i,relTable);
                    }
                    if (relTables != null) {
                        relDao.insertRelTables(relTables);
                        for (RelTable relTable : relTables) {
                            relDao.insertRelColumns(relTable.getRelColumns());
                        }
                    }
                }
            }
        } catch (GenCustomException e) {
            logger.error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new GenCustomException(GenReturnConstants.DB_EX);
        }
    }

    /**
     * 删除业务对象
     *
     * @param tableIds 需要删除的数据ID
     * @return 结果
     */
    @Override
    @Transactional
    public GenServerResult<Integer> deleteGenTableByIds(Long[] tableIds) {
        try {
            genTableDao.deleteGenTableByIds(tableIds);
            genTableColumnDao.deleteGenTableColumnByIds(tableIds);
            for (Long tableId : tableIds) {
                List<RelTable> relTables = relDao.relTableByTableId(tableId);
                if (relTables.size()>0){
                    relDao.deleteRelTableByTableId(tableId);
                    for (RelTable relTable : relTables) {
                        relDao.deleteRelColumnByRelId(relTable.getId());
                    }
                }
            }
            return new GenServerResult<>(true);
        } catch (RuntimeException e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.error(e.getMessage());
            return new GenServerResult<>(false, GenReturnConstants.DB_EX);
        }
    }

    /**
     * 导入表结构
     *
     * @param tableList 导入表列表
     */
    @Override
    @Transactional
    public void importGenTable(List<GenTable> tableList) {
        Long operName = GenSecurityUtils.getUserId();
        try {
            for (GenTable table : tableList) {
                String tableName = table.getTableName();
                GenUtils.initTable(table, operName);
                int row = genTableDao.insertGenTable(table);
                if (row > 0) {
                    // 保存列信息
                    List<GenTableColumn> genTableColumns = genTableColumnDao.selectDbTableColumnsByName(tableName);
                    for (GenTableColumn column : genTableColumns) {
                        GenUtils.initColumnField(column, table);
                        genTableColumnDao.insertGenTableColumn(column);
                    }
                }
            }
        } catch (Exception e) {
            throw new GenCustomException("导入失败：" + e.getMessage());
        }
    }

    /**
     * 预览代码
     *
     * @param tableId 表编号
     * @return 预览数据列表
     */
    @Override
    public Map<String, String> previewCode(Long tableId) {
        try {
            Map<String, String> dataMap = new LinkedHashMap<>();
            // 查询表信息
            GenTable table = genTableDao.selectGenTableById(tableId);
            if (table.getTplCategory().equals(GenConstants.TPL_SUB)) {
                // 设置主子表信息
                setSubTable(table);
            }
            if (table.getTplCategory().equals(GenConstants.TPL_REL)) {
                List<RelTable> relTables = relDao.relTableByTableId(tableId);
                relTables = relTables.stream().sorted(Comparator.comparing(RelTable::getSort)).collect(Collectors.toList());
                table.setRelTables(relTables);
            }
            // 设置主键列信息
            setPkColumn(table);
            VelocityInitializer.initVelocity();

            VelocityContext context = VelocityUtils.prepareContext(table);

            // 获取模板列表
            List<String> templates = VelocityUtils.getTemplateList(table.getTplCategory());
            for (String template : templates) {
                // 渲染模板
                if (template.equals("vm/java/rel-entity.java.vm")){
                    List<RelTable> relTables = table.getRelTables();
                    for (RelTable relTable : relTables) {
                        List<RelColumn> relColumns = relTable.getRelColumns();
                        relColumns = relColumns.stream().sorted(Comparator.comparing(RelColumn::getSort)).collect(Collectors.toList());
                        relTable.setRelColumns(relColumns);
                        context.put("relTable",relTable);
                        StringWriter stringWriter = new StringWriter();
                        Template temp = Velocity.getTemplate(template,Constants.UTF8);
                        temp.merge(context,stringWriter);
                        dataMap.put(VelocityUtils.getPreviewName(template,relTable.getRelClass()),stringWriter.toString());
                    }
                    continue;
                }
                StringWriter sw = new StringWriter();
                Template tpl = Velocity.getTemplate(template, Constants.UTF8);
                tpl.merge(context, sw);
                dataMap.put(VelocityUtils.getPreviewName(template,table.getClassName()), sw.toString());
            }
            return dataMap;
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            throw new GenCustomException(e.getMessage());
        }
    }

    /**
     * 生成代码（下载方式）
     *
     * @param tableName 表名称
     * @return 数据
     */
    @Override
    public byte[] downloadCode(String tableName) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ZipOutputStream zip = new ZipOutputStream(outputStream);
            generatorCode(tableName, zip);
            IOUtils.closeQuietly(zip);
            return outputStream.toByteArray();
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            throw new GenCustomException(GenReturnConstants.OP_ERROR);
        }
    }

    /**
     * 生成代码（自定义路径）
     *
     * @param tableName 表名称
     * @return
     */
    @Override
    public boolean generatorCode(String tableName) {
        try {
            // 查询表信息
            GenTable table = genTableDao.selectGenTableByName(tableName);
            // 设置主子表信息
            setSubTable(table);
            // 设置主键列信息
            setPkColumn(table);

            VelocityInitializer.initVelocity();

            VelocityContext context = VelocityUtils.prepareContext(table);

            // 获取模板列表
            List<String> templates = VelocityUtils.getTemplateList(table.getTplCategory());
            for (String template : templates) {
                if (!GenStringUtils.containsAny(template, "table.sql.vm", "api.js.vm", "index.vue.vm", "index-tree.vue.vm")) {
                    // 渲染模板
                    if (template.equals("vm/java/rel-entity.java.vm")){
                        List<RelTable> relTables = table.getRelTables();
                        for (RelTable relTable : relTables) {
                            List<RelColumn> relColumns = relTable.getRelColumns();
                            relColumns = relColumns.stream().sorted(Comparator.comparing(RelColumn::getSort)).collect(Collectors.toList());
                            relTable.setRelColumns(relColumns);
                            context.put("relTable",relTable);
                            StringWriter stringWriter = new StringWriter();
                            Template temp = Velocity.getTemplate(template,Constants.UTF8);
                            temp.merge(context,stringWriter);
                            try {
                                String path = getGenPath(table, template);
                                FileUtils.writeStringToFile(new File(path), stringWriter.toString(), CharsetKit.UTF_8);
                            } catch (IOException e) {
                                logger.error("渲染模板失败，表名：" + table.getTableName());
                                return false;
                            }
                        }
                        continue;
                    }
                    StringWriter sw = new StringWriter();
                    Template tpl = Velocity.getTemplate(template, Constants.UTF8);
                    tpl.merge(context, sw);
                    if (template.equals("vm/sql/table.sql.vm")) {
                        Boolean start = insertMenuItem(sw);
                        if (start) {
                            return false;
                        }
                    } else {
                        try {
                            String path = getGenPath(table, template);
                            FileUtils.writeStringToFile(new File(path), sw.toString(), CharsetKit.UTF_8);
                        } catch (IOException e) {
                            logger.error("渲染模板失败，表名：" + table.getTableName());
                            return false;
                        }
                    }
                }
            }
            return true;
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            throw new GenCustomException(GenReturnConstants.OP_ERROR);
        }
    }

    /**
     * 同步数据库
     *
     * @param tableName 表名称
     */
    @Override
    @Transactional
    public void synchDb(String tableName) {
        try {
            GenTable table = genTableDao.selectGenTableByName(tableName);
            List<GenTableColumn> tableColumns = table.getColumns();
            List<String> tableColumnNames = tableColumns.stream().map(GenTableColumn::getColumnName).collect(Collectors.toList());

            List<GenTableColumn> dbTableColumns = genTableColumnDao.selectDbTableColumnsByName(tableName);
            if (GenStringUtils.isEmpty(dbTableColumns)) {
                throw new GenCustomException("同步数据失败，原表结构不存在");
            }
            List<String> dbTableColumnNames = dbTableColumns.stream().map(GenTableColumn::getColumnName).collect(Collectors.toList());

            dbTableColumns.forEach(column -> {
                if (!tableColumnNames.contains(column.getColumnName())) {
                    GenUtils.initColumnField(column, table);
                    genTableColumnDao.insertGenTableColumn(column);
                }
            });

            List<GenTableColumn> delColumns = tableColumns.stream().filter(column -> !dbTableColumnNames.contains(column.getColumnName())).collect(Collectors.toList());
            if (GenStringUtils.isNotEmpty(delColumns)) {
                genTableColumnDao.deleteGenTableColumns(delColumns);
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            throw new GenCustomException(GenReturnConstants.OP_ERROR);
        }
    }

    /**
     * 批量生成代码（下载方式）
     *
     * @param tableNames 表数组
     * @return 数据
     */
    @Override
    public byte[] downloadCode(String[] tableNames) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ZipOutputStream zip = new ZipOutputStream(outputStream);
            for (String tableName : tableNames) {
                zip = generatorCode(tableName, zip);
//            generatorCode(tableName, zip);
                if (zip == null) {
                    return null;
                }
            }
            IOUtils.closeQuietly(zip);
            return outputStream.toByteArray();
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            throw new GenCustomException(GenReturnConstants.OP_ERROR);
        }
    }

    @Transactional
    boolean insertMenuItem(StringWriter stringWriter) {
        try {
            String sql = stringWriter.toString();
            int start = genTableDao.insertMenu(sql);
            if (start > 0) {
                return false;
            } else {
                return true;
            }
        } catch (RuntimeException e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.error(e.getMessage());
            return true;
        }
    }

    /**
     * todo 查询表信息并生成代码
     */
    private ZipOutputStream generatorCode(String tableName, ZipOutputStream zip) {
        // 查询表信息
        GenTable table = genTableDao.selectGenTableByName(tableName);
        // 设置主子表信息
        setSubTable(table);
        // 设置主键列信息
        setPkColumn(table);
        if (table.getTplCategory().equals(GenConstants.TPL_REL)) {
            List<RelTable> relTables = relDao.relTableByTableId(table.getTableId());
            relTables = relTables.stream().sorted(Comparator.comparing(RelTable::getSort)).collect(Collectors.toList());
            table.setRelTables(relTables);
        }
        VelocityInitializer.initVelocity();

        VelocityContext context = VelocityUtils.prepareContext(table);

        // 获取模板列表
        List<String> templates = VelocityUtils.getTemplateList(table.getTplCategory());
        for (String template : templates) {

            // 渲染模板
            if (template.equals("vm/java/rel-entity.java.vm")){
                List<RelTable> relTables = table.getRelTables();
                for (RelTable relTable : relTables) {
                    List<RelColumn> relColumns = relTable.getRelColumns();
                    relColumns = relColumns.stream().sorted(Comparator.comparing(RelColumn::getSort)).collect(Collectors.toList());
                    relTable.setRelColumns(relColumns);
                    context.put("relTable",relTable);
                    StringWriter stringWriter = new StringWriter();
                    Template temp = Velocity.getTemplate(template,Constants.UTF8);
                    temp.merge(context,stringWriter);
                    try {
                        // 添加到zip
                        zip.putNextEntry(new ZipEntry(VelocityUtils.getRelFileName(template, table,relTable)));
                        IOUtils.write(stringWriter.toString(), zip, Constants.UTF8);
                        IOUtils.closeQuietly(stringWriter);
                        zip.flush();
                        zip.closeEntry();
                    } catch (IOException e) {
                        logger.error("渲染模板失败，表名：" + table.getTableName(), e);
                    }
                }
                continue;
            }
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, Constants.UTF8);
            tpl.merge(context, sw);

            if (template.equals("vm/sql/table.sql.vm")) {
                Boolean start = insertMenuItem(sw);
                if (start) {
                    return null;
                }
            }
            try {
                // 添加到zip
                zip.putNextEntry(new ZipEntry(VelocityUtils.getFileName(template, table)));
                IOUtils.write(sw.toString(), zip, Constants.UTF8);
                IOUtils.closeQuietly(sw);
                zip.flush();
                zip.closeEntry();
            } catch (IOException e) {
                logger.error("渲染模板失败，表名：" + table.getTableName(), e);
            }
        }
        return zip;
    }

    /**
     * todo 修改保存参数校验
     *
     * @param genTable 业务信息
     */
    @Override
    public void validateEdit(GenTable genTable) {
        if (GenConstants.TPL_TREE.equals(genTable.getTplCategory())) {
            String options = JSON.toJSONString(genTable.getParams());
            JSONObject paramsObj = JSONObject.parseObject(options);
            if (GenStringUtils.isEmpty(paramsObj.getString(GenConstants.TREE_CODE))) {
                throw new GenCustomException("树编码字段不能为空");
            } else if (GenStringUtils.isEmpty(paramsObj.getString(GenConstants.TREE_PARENT_CODE))) {
                throw new GenCustomException("树父编码字段不能为空");
            } else if (GenStringUtils.isEmpty(paramsObj.getString(GenConstants.TREE_NAME))) {
                throw new GenCustomException("树名称字段不能为空");
            } else if (GenConstants.TPL_SUB.equals(genTable.getTplCategory())) {
                if (GenStringUtils.isEmpty(genTable.getSubTableName())) {
                    throw new GenCustomException("关联子表的表名不能为空");
                } else if (GenStringUtils.isEmpty(genTable.getSubTableFkName())) {
                    throw new GenCustomException("子表关联的外键名不能为空");
                }
            } else if (GenConstants.TPL_ASS.equals(genTable.getTplCategory())) {
//                todo 规则校验
            }
        }
    }

    /**
     * 设置主键列信息
     *
     * @param table 业务表信息
     */
    public void setPkColumn(GenTable table) {
        for (GenTableColumn column : table.getColumns()) {
            if (column.isPk()) {
                table.setPkColumn(column);
                break;
            }
        }
        if (GenStringUtils.isNull(table.getPkColumn())) {
            table.setPkColumn(table.getColumns().get(0));
        }
        if (GenConstants.TPL_SUB.equals(table.getTplCategory())) {
            for (GenTableColumn column : table.getSubTable().getColumns()) {
                if (column.isPk()) {
                    table.getSubTable().setPkColumn(column);
                    break;
                }
            }
            if (GenStringUtils.isNull(table.getSubTable().getPkColumn())) {
                table.getSubTable().setPkColumn(table.getSubTable().getColumns().get(0));
            }
        }
    }

    /**
     * 设置主子表信息
     *
     * @param table 业务表信息
     */
    public void setSubTable(GenTable table) {
        String subTableName = table.getSubTableName();
        if (GenStringUtils.isNotEmpty(subTableName)) {
            table.setSubTable(genTableDao.selectGenTableByName(subTableName));
        }
    }

    /**
     * 设置代码生成其他选项值
     *
     * @param genTable 设置后的生成对象
     */
    public void setTableFromOptions(GenTable genTable) {
        JSONObject paramsObj = JSONObject.parseObject(genTable.getOptions());
        if (GenStringUtils.isNotNull(paramsObj)) {
            String treeCode = paramsObj.getString(GenConstants.TREE_CODE);
            String treeParentCode = paramsObj.getString(GenConstants.TREE_PARENT_CODE);
            String treeName = paramsObj.getString(GenConstants.TREE_NAME);
            String parentMenuId = paramsObj.getString(GenConstants.PARENT_MENU_ID);
            String parentMenuName = paramsObj.getString(GenConstants.PARENT_MENU_NAME);

            genTable.setTreeCode(treeCode);
            genTable.setTreeParentCode(treeParentCode);
            genTable.setTreeName(treeName);
            genTable.setParentMenuId(parentMenuId);
            genTable.setParentMenuName(parentMenuName);
        }
    }

    /**
     * 获取代码生成地址
     *
     * @param table    业务表信息
     * @param template 模板文件路径
     * @return 生成地址
     */
    public static String getGenPath(GenTable table, String template) {
        String genPath = table.getGenPath();
        if (GenStringUtils.equals(genPath, "/")) {
            return System.getProperty("user.dir") + File.separator + "src" + File.separator + VelocityUtils.getFileName(template, table);
        }
        return genPath + File.separator + VelocityUtils.getFileName(template, table);
    }
}