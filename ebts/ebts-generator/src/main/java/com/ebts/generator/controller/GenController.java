package com.ebts.generator.controller;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.ebts.generator.aop.Log;
import com.ebts.generator.enums.GenBusinessType;
import com.ebts.generator.utils.GenBaseController;
import com.ebts.generator.utils.GenReturnConstants;
import com.ebts.generator.utils.GenServerResult;
import com.ebts.generator.utils.page.GenTableDataInfo;
import com.ebts.generator.utils.text.Convert;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ebts.generator.entity.GenTable;
import com.ebts.generator.entity.GenTableColumn;
import com.ebts.generator.service.GenTableColumnService;
import com.ebts.generator.service.GenTableService;

/**
 * 代码生成 操作处理
 *
 * @author binlin
 */
@RestController
@RequestMapping("/tool/gen")
public class GenController extends GenBaseController {
    private Logger logger = LoggerFactory.getLogger(GenController.class);

    @Autowired
    private GenTableService genTableService;

    @Autowired
    private GenTableColumnService genTableColumnService;

    /**
     * 查询代码生成列表
     */
    @PreAuthorize("@ebts.hasPermi('tool:gen:list')")
    @GetMapping("/list")
    public Serializable genList(GenTable genTable) {
        startPage();
        List<GenTable> list = genTableService.selectGenTableList(genTable);
        return getDataTable(list);
    }

    /**
     * 修改代码生成业务
     */
    @PreAuthorize("@ebts.hasPermi('tool:gen:query')")
    @GetMapping(value = "/{talbleId}")
    public AjaxResult getInfo(@PathVariable Long talbleId) {
        GenTable table = genTableService.selectGenTableById(talbleId);
        List<GenTable> tables = genTableService.selectGenTableAll();
        List<GenTableColumn> list = genTableColumnService.selectGenTableColumnListByTableId(talbleId);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("info", table);
        map.put("rows", list);
        map.put("tables", tables);
        return AjaxResult.success(map);
    }

    /**
     * 查询数据库列表
     */
    @PreAuthorize("@ebts.hasPermi('tool:gen:list')")
    @GetMapping("/db/list")
    public Serializable dataList(GenTable genTable) {
        startPage();
        List<GenTable> list = genTableService.selectDbTableList(genTable);
        return getDataTable(list);
    }

    /**
     * 查询数据表字段列表
     */
    @PreAuthorize("@ebts.hasPermi('tool:gen:list')")
    @GetMapping(value = "/column/{talbleId}")
    public Serializable columnList(Long tableId) {
        GenTableDataInfo dataInfo = new GenTableDataInfo();
        List<GenTableColumn> list = genTableColumnService.selectGenTableColumnListByTableId(tableId);
        dataInfo.setRows(list);
        dataInfo.setTotal(list.size());
        return dataInfo;
    }

    /**
     * 导入表结构（保存）
     */
    @PreAuthorize("@ebts.hasPermi('tool:gen:list')")
    @Log(title = "代码生成", businessType = GenBusinessType.IMPORT)
    @PostMapping("/importTable")
    public AjaxResult importTableSave(String tables) {
        String[] tableNames = Convert.toStrArray(tables);
        // 查询表信息
        List<GenTable> tableList = genTableService.selectDbTableListByNames(tableNames);
        genTableService.importGenTable(tableList);
        return AjaxResult.success();
    }

    /**
     * todo
     * 修改保存代码生成业务
     */
    @PreAuthorize("@ebts.hasPermi('tool:gen:edit')")
    @Log(title = "代码生成", businessType = GenBusinessType.UPDATE)
    @PutMapping
    public AjaxResult editSave(@Validated @RequestBody GenTable genTable) {
        genTableService.validateEdit(genTable);
        genTableService.updateGenTable(genTable);
        return AjaxResult.success();
    }

    /**
     * 删除代码生成
     */
    @PreAuthorize("@ebts.hasPermi('tool:gen:remove')")
    @Log(title = "代码生成", businessType = GenBusinessType.DELETE)
    @DeleteMapping("/{tableIds}")
    public AjaxResult remove(@PathVariable Long[] tableIds) {
        try {
            GenServerResult<Integer> genServerResult = genTableService.deleteGenTableByIds(tableIds);
            if (genServerResult.isStart()) {
                return AjaxResult.success();
            } else {
                return AjaxResult.error(genServerResult.getMsg());
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return AjaxResult.error(GenReturnConstants.SYS_ERROR);
        }
    }

    /**
     * 预览代码
     */
    @PreAuthorize("@ebts.hasPermi('tool:gen:preview')")
    @GetMapping("/preview/{tableId}")
    public AjaxResult preview(@PathVariable("tableId") Long tableId) throws IOException {
        Map<String, String> dataMap = genTableService.previewCode(tableId);
        return AjaxResult.success(dataMap);
    }

    /**
     * 生成代码（下载方式）
     */
    @PreAuthorize("@ebts.hasPermi('tool:gen:code')")
    @Log(title = "代码生成", businessType = GenBusinessType.GENCODE)
    @GetMapping("/download/{tableName}")
    public void download(HttpServletResponse response, @PathVariable("tableName") String tableName) throws IOException {
        byte[] data = genTableService.downloadCode(tableName);
        genCode(response, data);
    }

    /**
     * 生成代码（自定义路径）
     */
    @PreAuthorize("@ebts.hasPermi('tool:gen:code')")
    @Log(title = "代码生成", businessType = GenBusinessType.GENCODE)
    @GetMapping("/genCode/{tableName}")
    public AjaxResult genCode(@PathVariable("tableName") String tableName) {
        boolean start = genTableService.generatorCode(tableName);
        if (start) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error("模板渲染失败!");
        }
    }

    /**
     * 同步数据库
     */
    @PreAuthorize("@ebts.hasPermi('tool:gen:edit')")
    @Log(title = "代码生成", businessType = GenBusinessType.UPDATE)
    @GetMapping("/synchDb/{tableName}")
    public AjaxResult synchDb(@PathVariable("tableName") String tableName) {
        genTableService.synchDb(tableName);
        return AjaxResult.success();
    }

    /**
     * 批量生成代码
     */
    @PreAuthorize("@ebts.hasPermi('tool:gen:code')")
    @Log(title = "代码生成", businessType = GenBusinessType.GENCODE)
    @GetMapping("/batchGenCode")
    public void batchGenCode(HttpServletResponse response, String tables) throws IOException {
        String[] tableNames = Convert.toStrArray(tables);
        byte[] data = genTableService.downloadCode(tableNames);
        genCode(response, data);
//        if (compress(data)){
//            return AjaxResult.success("ebts.zip");
//        }else {
//            return AjaxResult.error();
//        }
    }

    /**
     * 生成zip文件
     */
    private void genCode(HttpServletResponse response, byte[] data) throws IOException {
        response.reset();
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setHeader("Content-Disposition", "attachment; filename=\"ebts.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        IOUtils.write(data, response.getOutputStream());
    }
}