package com.ebts.generator.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.ebts.generator.dao.ApiClassDao;
import com.ebts.generator.dao.GenTableDao;
import com.ebts.generator.dao.ModuleDao;
import com.ebts.generator.dto.InterTableDto;
import com.ebts.generator.entity.ApiClass;
import com.ebts.generator.entity.Module;
import com.ebts.generator.utils.*;
import com.ebts.generator.utils.constant.Constants;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ebts.generator.dao.InterTableDao;
import com.ebts.generator.entity.InterTable;
import com.ebts.generator.service.InterTableService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 * 接口信息Service业务层处理
 *
 * @author binlin
 * @date 2021-01-25
 */
@Service
public class InterTableServiceImpl implements InterTableService {
    private Logger logger = LoggerFactory.getLogger(InterTableServiceImpl.class);

    @Autowired
    private InterTableDao interTableDao;
    @Autowired
    private ApiClassDao apiclassDao;
    @Autowired
    private ModuleDao moduleDao;
    @Autowired
    private GenTableDao genTableDao;

    /**
     * 查询接口信息
     *
     * @param id 接口信息ID
     * @return 接口信息
     */
    @Override
    public GenServerResult<InterTable> selectInterTableById(Long id) {
        try {
            InterTable interTable = interTableDao.selectInterTableById(id);
            if (interTable != null) {
                return new GenServerResult<InterTable>(true, interTable);
            } else {
                return new GenServerResult<InterTable>(false, GenReturnConstants.RESULT_EMPTY);
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return new GenServerResult<InterTable>(false, GenReturnConstants.DB_EX);
        }
    }

    /**
     * 查询接口信息列表
     *
     * @param interTable 接口信息
     * @return 接口信息
     */
    @Override
    public GenServerResult<Map<String, Object>> selectInterTableList(InterTable interTable) {
        try {
            Map<String, Object> modeMap = new HashMap<String, Object>();
            if (interTable.getType() == 1) {
                Module module = moduleDao.selectModuleById(interTable.getmId());
                modeMap.put("info", module);
            } else if (interTable.getType() == 2) {
                ApiClass apiclass = apiclassDao.selectApiclassById(interTable.getcId());
                modeMap.put("info", apiclass);
            }
            List<InterTable> interTableList = interTableDao.selectInterTableList(interTable);
            modeMap.put("rows", interTableList);
            return new GenServerResult<Map<String, Object>>(true, modeMap);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return new GenServerResult<Map<String, Object>>(false, GenReturnConstants.DB_EX);
        }
    }

    /**
     * 新增接口信息
     *
     * @param interTable 接口信息
     * @return 结果
     */
    @Override
    public GenServerResult<Integer> insertInterTable(InterTable interTable) {
        try {
            interTable.setCreateBy(GenSecurityUtils.getUserId());
            Integer renewal = interTableDao.insertInterTable(interTable);
            if (renewal > 0) {
                return new GenServerResult<Integer>(true, renewal);
            } else {
                return new GenServerResult<Integer>(false, GenReturnConstants.SYS_FAILL);
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return new GenServerResult<Integer>(false, GenReturnConstants.DB_EX);
        }
    }

    /**
     * 修改接口信息
     *
     * @param interTableDto 接口信息
     * @return 结果
     */
    @Override
    @Transactional
    public GenServerResult<Integer> updateInterTable(InterTableDto interTableDto) {
        try {
            List<InterTable> interTables = interTableDto.getInterTables();
            Integer renewal = 0;
            if (interTableDto.getModule() == null) {
                ApiClass apiclass = interTableDto.getApiclass();
                renewal = apiclassDao.updateApiclass(apiclass);
                interTableDao.deleteInterTableByClassId(apiclass.getId());
            } else if (interTableDto.getApiclass() == null) {
                Module module = interTableDto.getModule();
                renewal = moduleDao.updateModule(module);
                interTableDao.deleteInterTableByModuleId(module.getId());
            }
            Integer renewalTables = 0;
            if (interTables.size() > 0) {
                for (int i = 0; i < interTables.size(); i++) {
                    interTables.get(i).setCreateBy(GenSecurityUtils.getUserId());
                }
                renewalTables = interTableDao.insertInterTables(interTables);
            }
            if (renewalTables > 0 || renewal > 0) {
                return new GenServerResult<Integer>(true, renewalTables);
            } else {
                return new GenServerResult<Integer>(false, GenReturnConstants.SYS_FAILL);
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new GenServerResult<Integer>(false, GenReturnConstants.DB_EX);
        }
    }

    /**
     * 批量删除接口信息
     *
     * @param ids 需要删除的接口信息ID
     * @return 结果
     */
    @Override
    public GenServerResult<Integer> deleteInterTableByIds(Long[] ids) {
        try {
            Integer renewal = interTableDao.deleteInterTableByIds(ids);
            if (renewal > 0) {
                return new GenServerResult<Integer>(true, renewal);
            } else {
                return new GenServerResult<Integer>(false, GenReturnConstants.SYS_FAILL);
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return new GenServerResult<Integer>(false, GenReturnConstants.DB_EX);
        }
    }

    /**
     * 删除接口信息信息
     *
     * @param id 接口信息ID
     * @return 结果
     */
    @Override
    public GenServerResult<Integer> deleteInterTableById(Long id) {
        try {
            Integer renewal = interTableDao.deleteInterTableById(id);
            if (renewal > 0) {
                return new GenServerResult<Integer>(true, renewal);
            } else {
                return new GenServerResult<Integer>(false, GenReturnConstants.SYS_FAILL);
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return new GenServerResult<Integer>(false, GenReturnConstants.DB_EX);
        }
    }

    @Override
    public GenServerResult<Map<String, String>> previewCodeCalss(Long cid) {
        try {
            Map<String, String> dataMap = new LinkedHashMap<String, String>();
            ApiClass apiclass = interTableDao.selectInterTableClass(cid);
            if (apiclass == null) {
                return new GenServerResult<Map<String, String>>(false, GenReturnConstants.INTER_NULL);
            }
            VelocityInitializer.initVelocity();
            VelocityContext context = GenInterTableUtils.prepareClassContext(apiclass);
            Map<String, String> templates = GenInterTableUtils.getTemplateList(2);
            StringWriter sw = new StringWriter();
            Template template = Velocity.getTemplate(templates.get("java"), Constants.UTF8);
            template.merge(context, sw);
            dataMap.put(getJavaClassName(apiclass.getcName()), sw.toString());

            sw = new StringWriter();
            template = Velocity.getTemplate(templates.get("sql"), Constants.UTF8);
            template.merge(context, sw);
            dataMap.put(getSqlName(apiclass.getcName()), sw.toString());
            return new GenServerResult<Map<String, String>>(true, dataMap);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return new GenServerResult<Map<String, String>>(false, GenReturnConstants.DB_EX);
        }
    }

    @Override
    public GenServerResult<Map<String, Object>> previewCodeMoudle(Long mid) {
        try {
            Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
            List<InterTable> interTableList = interTableDao.selectInterTableModule(mid);
            if (interTableList.size() == 0) {
                return new GenServerResult<Map<String, Object>>(false, "该模块下接口为空,请先创建接口!");
            }
            VelocityInitializer.initVelocity();
            List<VelocityContext> contextList = GenInterTableUtils.prepareMoudleContext(interTableList);
            Map<String, String> templates = GenInterTableUtils.getTemplateList(2);
            StringWriter sqlsw = new StringWriter();
            Template sqltemplate = Velocity.getTemplate(templates.get("sql"), Constants.UTF8);
            for (VelocityContext context : contextList) {
                StringWriter sw = new StringWriter();
                Template template = Velocity.getTemplate(templates.get("java"), Constants.UTF8);
                template.merge(context, sw);
                sqltemplate.merge(context, sqlsw);
                dataMap.put(getJavaClassName((String) context.get("ClassName")), sw.toString());
            }
            dataMap.put(getSqlName("module"), sqlsw.toString());
            return new GenServerResult<Map<String, Object>>(true, dataMap);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return new GenServerResult<Map<String, Object>>(false, GenReturnConstants.DB_EX);
        }
    }

    @Override
    @Transactional
    public GenServerResult<byte[]> generatorCodeClass(Long cid) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ZipOutputStream zip = new ZipOutputStream(outputStream);
            ApiClass apiclass = interTableDao.selectInterTableClass(cid);
            VelocityInitializer.initVelocity();
            VelocityContext context = GenInterTableUtils.prepareClassContext(apiclass);
            Map<String, String> templates = GenInterTableUtils.getTemplateList(2);
            StringWriter sw = new StringWriter();
            Template template = Velocity.getTemplate(templates.get("java"), Constants.UTF8);
            template.merge(context, sw);
            zip = genCode(getJavaClassName(apiclass.getcName()), zip, sw);
            if (zip == null) {
                return new GenServerResult<byte[]>(false, "代码生成失败!");
            }
            sw = new StringWriter();
            template = Velocity.getTemplate(templates.get("sql"), Constants.UTF8);
            template.merge(context, sw);

            zip = genCode(getSqlName(apiclass.getcName()), zip, sw);
            if (zip == null) {
                return new GenServerResult<byte[]>(false, "代码生成失败!");
            }
            int start = genTableDao.insertMenu(sw.toString());
            if (!(start > 0)) {
                return new GenServerResult<byte[]>(false, GenReturnConstants.DB_EX);
            }
            IOUtils.closeQuietly(zip);
            return new GenServerResult<byte[]>(true, outputStream.toByteArray());

        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new GenServerResult<byte[]>(false, "代码生成失败!");
        }
    }

    @Override
    @Transactional
    public GenServerResult<byte[]> generatorCodeMoudle(Long mid) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ZipOutputStream zip = new ZipOutputStream(outputStream);
            List<InterTable> interTableList = interTableDao.selectInterTableModule(mid);
            if (interTableList.size() == 0) {
                return new GenServerResult<byte[]>(false, "该模块下接口为空,请先创建接口!");
            }
            VelocityInitializer.initVelocity();
            List<VelocityContext> contextList = GenInterTableUtils.prepareMoudleContext(interTableList);
            Map<String, String> templates = GenInterTableUtils.getTemplateList(2);
            StringWriter sqlsw = new StringWriter();
            Template sqltemplate = Velocity.getTemplate(templates.get("sql"), Constants.UTF8);
            for (VelocityContext context : contextList) {
                StringWriter sw = new StringWriter();
                Template template = Velocity.getTemplate(templates.get("java"), Constants.UTF8);
                template.merge(context, sw);
                sqltemplate.merge(context, sqlsw);
                zip = genCode(getJavaClassName((String) context.get("ClassName")), zip, sw);
            }
            zip = genCode(getSqlName("module"), zip, sqlsw);
            if (zip != null) {
                int start = genTableDao.insertMenu(sqlsw.toString());
                if (!(start > 0)) {
                    return new GenServerResult<byte[]>(false, GenReturnConstants.DB_EX);
                }
            }
            IOUtils.closeQuietly(zip);
            return new GenServerResult<byte[]>(true, outputStream.toByteArray());
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new GenServerResult<byte[]>(false, "代码生成失败!");
        }
    }


    public ZipOutputStream genCode(String className, ZipOutputStream zip, StringWriter sw) {
        try {
            zip.putNextEntry(new ZipEntry(className));
            IOUtils.write(sw.toString(), zip, Constants.UTF8);
            IOUtils.closeQuietly(sw);
            zip.flush();
            zip.closeEntry();
            return zip;
        } catch (IOException e) {
            logger.error(e.getMessage());
            return null;
        }
    }


    public String getJavaClassName(String cName) {
        return GenInterTableUtils.getUpperCase(cName) + "Controller.java";
    }

    public String getSqlName(String cName) {
        return GenInterTableUtils.getUpperCase(cName) + ".sql";
    }
}