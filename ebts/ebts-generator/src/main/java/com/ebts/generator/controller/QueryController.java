package com.ebts.generator.controller;

import com.github.pagehelper.PageHelper;
import com.ebts.generator.entity.UniCon;
import com.ebts.generator.entity.UniQuery;
import com.ebts.generator.service.QueryService;
import com.ebts.generator.utils.*;
import com.ebts.generator.utils.sql.GenSqlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Author 18209
 * Date 2021/1/30 18:25
 * Version 1.0
 */
@RestController
@RequestMapping("/query")
public class QueryController extends GenBaseController {
    private Logger logger = LoggerFactory.getLogger(QueryController.class);


    @Autowired
    private QueryService queryService;

    /**
     * 获取编辑sql的基本信息
     * @param id
     * @return
     */
    @PreAuthorize("@ebts.hasPermi('query:list')")
    @GetMapping("/{id}")
    public AjaxResult Info(@PathVariable("id") Long id) {
        try {
            GenServerResult<UniQuery> genServerResult = queryService.selectQueryById(id);
            if (genServerResult.isStart()) {
                UniQuery uniQuery = genServerResult.getData();
                Map<String, Object> modeMap = new HashMap<>();
                List<UniCon> uniCons = uniQuery.getUniCons();
                uniQuery.setUniCons(null);
                modeMap.put("info", uniQuery);
                modeMap.put("list", uniCons);
                return AjaxResult.success(modeMap);
            } else {
                return AjaxResult.error(genServerResult.getMsg());
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return AjaxResult.error(GenReturnConstants.SYS_ERROR);
        }
    }

    /**
     * 更新万能查询编辑信息 info 和查询信息
     * @param uniQuery
     * @return
     */
    @PreAuthorize("@ebts.hasPermi('query:edit')")
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody UniQuery uniQuery) {
        try {
            GenServerResult genServerResult = queryService.updateQueryInfo(uniQuery);
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
     * 导出预览数据
     * @param uniQuery
     * @return
     */
    @PreAuthorize("@ebts.hasAnyPermi('query:export')")
    @PutMapping("export")
    public AjaxResult export(@Validated @RequestBody UniQuery uniQuery) {
        try {
            GenServerResult<List<Map<String, Object>>> genServerResult = queryService.previewQuery(uniQuery);
            if (genServerResult.isStart()) {
                return new GenMapExcelUtil().exportExcel(genServerResult.getData(), uniQuery.getUqName());
            } else {
                return AjaxResult.error(genServerResult.getMsg());
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return AjaxResult.error(GenReturnConstants.SYS_ERROR);
        }
    }

    /**
     * 返回预览信息
     * @param uniQuery
     * @return
     */
    @PreAuthorize("@ebts.hasPermi('query:preview')")
    @PutMapping("preview")
    public Serializable Preview(@Validated @RequestBody UniQuery uniQuery) {
        try {
            startPage(uniQuery);
            GenServerResult<List<Map<String, Object>>> genServerResult = queryService.previewQuery(uniQuery);
            if (genServerResult.isStart()) {
                return getDataTable(genServerResult.getData());
            } else {
                return AjaxResult.error(genServerResult.getMsg());
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return AjaxResult.error(GenReturnConstants.SYS_ERROR);
        }
    }

    /**
     * 发布上线
     * @param uniQuery
     * @return
     */
    @PreAuthorize("@ebts.hasPermi('query:release')")
    @PutMapping("/release")
    public AjaxResult Release(@RequestBody UniQuery uniQuery) {
        try {
            if (uniQuery.getId() == null) {
                return AjaxResult.error("id不能为空!");
            } else {
                if (uniQuery.getIsRelease() == 1 || uniQuery.getIsRelease() == 2) {
                    GenServerResult<Integer> genServerResult = queryService.Release(uniQuery);
                    if (genServerResult.isStart()) {
                        return AjaxResult.success();
                    } else {
                        return AjaxResult.error(genServerResult.getMsg());
                    }
                } else {
                    return AjaxResult.error("状态输入错误!");
                }
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return AjaxResult.error(GenReturnConstants.SYS_ERROR);
        }
    }

    /**
     * 设置请求分页数据
     */
    protected void startPage(UniQuery uniQuery) {
        Integer pageNum = uniQuery.getPageNum();
        Integer pageSize = uniQuery.getPageSize();
        if (GenStringUtils.isNotNull(pageNum) && GenStringUtils.isNotNull(pageSize)) {
            String orderBy = GenSqlUtil.escapeOrderBySql("");
            PageHelper.startPage(pageNum, pageSize, orderBy);
        }
    }
}
