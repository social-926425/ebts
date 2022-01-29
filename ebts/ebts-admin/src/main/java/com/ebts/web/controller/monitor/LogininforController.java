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
import com.ebts.system.entity.Logininfor;
import com.ebts.system.service.LogininforService;

/**
 * 系统访问记录
 *
 * @author binlin
 */
@RestController
@RequestMapping("/monitor/logininfor")
public class LogininforController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(LogininforController.class);

    @Autowired
    private LogininforService logininforService;

    @PreAuthorize("@ebts.hasPermi('monitor:logininfor:list')")
    @GetMapping("/list")
    public Serializable list(Logininfor logininfor) {
        try {
            startPage();
            List<Logininfor> list = logininforService.selectLogininforList(logininfor);
            return getDataTable(list);
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            return AjaxResult.error(ReturnConstants.SYS_ERROR);
        }
    }

    @Log(title = "登录日志", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ebts.hasPermi('monitor:logininfor:export')")
    @GetMapping("/export")
    public AjaxResult export(Logininfor logininfor) {
        try {
            List<Logininfor> list = logininforService.selectLogininforList(logininfor);
            ExcelUtil<Logininfor> util = new ExcelUtil<Logininfor>(Logininfor.class);
            return util.exportExcel(list, "登录日志");
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            return AjaxResult.error(ReturnConstants.SYS_ERROR);
        }
    }

    @PreAuthorize("@ebts.hasPermi('monitor:logininfor:remove')")
    @Log(title = "登录日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{infoIds}")
    public AjaxResult remove(@PathVariable Long[] infoIds) {
        try {
            return toAjax(logininforService.deleteLogininforByIds(infoIds));
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            return AjaxResult.error(ReturnConstants.SYS_ERROR);
        }
    }

    @PreAuthorize("@ebts.hasPermi('monitor:logininfor:remove')")
    @Log(title = "登录日志", businessType = BusinessType.CLEAN)
    @DeleteMapping("/clean")
    public AjaxResult clean() {
        try {
            logininforService.cleanLogininfor();
            return AjaxResult.success();
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            return AjaxResult.error(ReturnConstants.SYS_ERROR);
        }
    }
}
