package com.ebts.web.controller.system;

import com.ebts.common.constant.ReturnConstants;
import com.ebts.common.core.controller.BaseController;
import com.ebts.common.core.entity.AjaxResult;
import com.ebts.common.utils.ServerResult;
import com.ebts.common.utils.MapExcelUtil;
import com.ebts.system.entity.RealUniQuery;
import com.ebts.system.service.RealQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Author 18209
 * @Date 2021/2/22 18:02
 * @Version 1.0
 */
@RestController
@RequestMapping("/data")
public class RealQueryController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(RealQueryController.class);

    @Autowired
    private RealQueryService realQueryService;

    /**
     * 用户导出
     * @param realUniQuery
     * @return
     */
    @PreAuthorize("@ebts.hasAnyPermi('data:real:export')")
    @PutMapping("/real/export")
    public AjaxResult exportReal(@Validated @RequestBody RealUniQuery realUniQuery) {
        try {
            ServerResult<List<Map<String, Object>>> serverResult = realQueryService.RealData(realUniQuery, 1);
            if (serverResult.isStart()) {
                return new MapExcelUtil().exportExcel(serverResult.getData(), serverResult.getSheetName());
            } else {
                return AjaxResult.error(serverResult.getMsg());
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return AjaxResult.error(ReturnConstants.SYS_ERROR);
        }
    }

    /**
     * 获取查询基本信息
     * @param id
     * @return
     */
    @PreAuthorize("@ebts.hasPermi('data:real:data')")
    @GetMapping("/real/{id}")
    public AjaxResult RealInfo(@PathVariable("id") Long id) {
        try {
            if (id == null) {
                return AjaxResult.error("id不能为空!");
            }
            ServerResult<List<RealUniQuery>> serverResult = realQueryService.RealInfo(id);
            if (serverResult.isStart()) {
                return AjaxResult.success(serverResult.getData());
            } else {
                return AjaxResult.error(serverResult.getMsg());
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return AjaxResult.error(ReturnConstants.SYS_ERROR);
        }
    }


    /**
     * 获取查询数据
     * @param realUniQuery
     * @return
     */
    @PreAuthorize("@ebts.hasAnyPermi('data:real:list')")
    @PutMapping("/real")
    public Serializable RealData(@Validated @RequestBody RealUniQuery realUniQuery) {
        try {
            ServerResult<List<Map<String, Object>>> serverResult = realQueryService.RealData(realUniQuery, 2);
            if (serverResult.isStart()) {
                return getDataTable(serverResult.getData());
            } else {
                return AjaxResult.error(serverResult.getMsg());
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return AjaxResult.error(ReturnConstants.SYS_ERROR);
        }
    }
}
