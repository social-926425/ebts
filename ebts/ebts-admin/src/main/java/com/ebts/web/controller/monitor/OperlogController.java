package com.ebts.web.controller.monitor;

import java.io.Serializable;
import java.util.List;

import com.ebts.common.constant.ReturnConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ebts.common.annotation.Log;
import com.ebts.common.core.controller.BaseController;
import com.ebts.common.core.entity.AjaxResult;
import com.ebts.common.enums.BusinessType;
import com.ebts.common.utils.poi.ExcelUtil;
import com.ebts.system.entity.OperLog;
import com.ebts.system.service.OperLogService;

/**
 * 操作日志记录
 *
 * @author binlin
 */
@RestController
@RequestMapping("/monitor/operlog")
public class OperlogController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(OperlogController.class);

    @Autowired
    private OperLogService operLogService;

    @PreAuthorize("@ebts.hasPermi('monitor:operlog:list')")
    @GetMapping("/list")
    public Serializable list(OperLog operLog) {
        try {
            startPage();
            List<OperLog> list = operLogService.selectOperLogList(operLog);
            return getDataTable(list);
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            return AjaxResult.error(ReturnConstants.SYS_ERROR);
        }
    }

    @Log(title = "操作日志", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ebts.hasPermi('monitor:operlog:export')")
    @GetMapping("/export")
    public AjaxResult export(OperLog operLog) {
        try {
            List<OperLog> list = operLogService.selectOperLogList(operLog);
            ExcelUtil<OperLog> util = new ExcelUtil<OperLog>(OperLog.class);
            return util.exportExcel(list, "操作日志");
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            return AjaxResult.error(ReturnConstants.SYS_ERROR);
        }
    }

    @PreAuthorize("@ebts.hasPermi('monitor:operlog:remove')")
    @DeleteMapping("/{operIds}")
    public AjaxResult remove(@PathVariable Long[] operIds) {
        try {
            return toAjax(operLogService.deleteOperLogByIds(operIds));
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            return AjaxResult.error(ReturnConstants.SYS_ERROR);
        }
    }

    @Log(title = "操作日志", businessType = BusinessType.CLEAN)
    @PreAuthorize("@ebts.hasPermi('monitor:operlog:remove')")
    @DeleteMapping("/clean")
    public AjaxResult clean() {
        try {
            operLogService.cleanOperLog();
            return AjaxResult.success();
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            return AjaxResult.error(ReturnConstants.SYS_ERROR);
        }
    }
}
