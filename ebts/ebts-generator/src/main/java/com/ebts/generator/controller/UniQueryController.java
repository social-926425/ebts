package com.ebts.generator.controller;

import java.io.Serializable;
import java.util.List;


import com.ebts.generator.aop.Log;
import com.ebts.generator.entity.UniQuery;
import com.ebts.generator.enums.GenBusinessType;
import com.ebts.generator.service.UniQueryService;
import com.ebts.generator.utils.GenBaseController;
import com.ebts.generator.utils.GenReturnConstants;
import com.ebts.generator.utils.GenServerResult;
import com.ebts.generator.utils.poi.GenExcelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 万能查询Controller
 *
 * @author binlin
 * @date 2021-01-30
 */
@RestController
@RequestMapping("/tool/query")
public class UniQueryController extends GenBaseController {
    protected final Logger logger = LoggerFactory.getLogger(UniQueryController.class);

    @Autowired
    private UniQueryService uniQueryService;

    /**
     * 查询万能查询列表
     */
    @PreAuthorize("@ebts.hasPermi('tool:query:list')")
    @GetMapping("/list")
    public Serializable list(UniQuery uniQuery) {
        try {
            startPage();
            GenServerResult<List<UniQuery>> genServerResult = uniQueryService.selectUniQueryList(uniQuery);
            if (genServerResult.isStart()) {
                return getDataTable(genServerResult.getData());
            } else {
                return AjaxResult.info(genServerResult.getMsg());
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return AjaxResult.error(GenReturnConstants.SYS_ERROR);
        }
    }

    /**
     * 导出万能查询列表
     */
    @PreAuthorize("@ebts.hasPermi('tool:query:export')")
    @Log(title = "万能查询", businessType = GenBusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(UniQuery uniQuery) {
        try {
            GenServerResult<List<UniQuery>> genServerResult = uniQueryService.selectUniQueryList(uniQuery);
            GenExcelUtil<UniQuery> util = new GenExcelUtil<UniQuery>(UniQuery.class);
            if (genServerResult.isStart()) {
                return util.exportExcel(genServerResult.getData(), "query");
            } else {
                return AjaxResult.error(genServerResult.getMsg());
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return AjaxResult.error(GenReturnConstants.SYS_ERROR);
        }
    }

    /**
     * 获取万能查询详细信息
     */
    @PreAuthorize("@ebts.hasPermi('tool:query:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        try {
            GenServerResult<UniQuery> genServerResult = uniQueryService.selectUniQueryById(id);
            if (genServerResult.isStart()) {
                return AjaxResult.success(genServerResult.getData());
            } else {
                return AjaxResult.info(genServerResult.getMsg());
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return AjaxResult.error(GenReturnConstants.SYS_ERROR);
        }
    }

    /**
     * 新增万能查询
     */
    @PreAuthorize("@ebts.hasPermi('tool:query:add')")
    @Log(title = "万能查询", businessType = GenBusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UniQuery uniQuery) {
        if (uniQuery.getUqName() == null || uniQuery.getUqName().equals("")) {
            return AjaxResult.error("名称不能为空!");
        }
        if (uniQuery.getUqDescribe() == null || uniQuery.getUqDescribe().equals("")) {
            return AjaxResult.error("描述不能为空!");
        }
        try {
            GenServerResult<Integer> genServerResult = uniQueryService.insertUniQuery(uniQuery);
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
     * 简单修改万能能查询
     */
    @PreAuthorize("@ebts.hasPermi('tool:query:edit')")
    @Log(title = "万能查询", businessType = GenBusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UniQuery uniQuery) {
        try {

            if (uniQuery.getUqName() == null || uniQuery.getUqName().equals("")) {
                return AjaxResult.error("名称不能为空!");
            }
            if (uniQuery.getUqDescribe() == null || uniQuery.getUqDescribe().equals("")) {
                return AjaxResult.error("描述不能为空!");
            }
            GenServerResult<Integer> genServerResult = uniQueryService.updateUniQuery(uniQuery);
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
     * 删除万能查询
     */
    @PreAuthorize("@ebts.hasPermi('tool:query:remove')")
    @Log(title = "万能查询", businessType = GenBusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        try {
            if (ids.length < 0) {
                return AjaxResult.error("id不能为空!");
            }
            GenServerResult<Integer> genServerResult = uniQueryService.deleteUniQueryByIds(ids);
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
}