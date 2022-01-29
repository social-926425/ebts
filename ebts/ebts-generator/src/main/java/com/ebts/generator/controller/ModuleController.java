package com.ebts.generator.controller;

import java.io.Serializable;
import java.util.List;


import com.ebts.generator.aop.Log;
import com.ebts.generator.enums.GenBusinessType;
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
import com.ebts.generator.entity.Module;
import com.ebts.generator.service.ModuleService;

/**
 * 模块管理Controller
 *
 * @author binlin
 * @date 2021-01-24
 */
@RestController
@RequestMapping("/tool/module")
public class ModuleController extends GenBaseController {
    protected final Logger logger = LoggerFactory.getLogger(ModuleController.class);

    @Autowired
    private ModuleService moduleService;


    /**
     * 获取到api类的select选项
     *
     * @return
     */
    @PreAuthorize("@ebts.hasAnyPermi('tool:module:querylist')")
    @GetMapping("querylist")
    public AjaxResult queryList() {
        try {
            GenServerResult<List<Module>> genServerResult = moduleService.selectModuleList(new Module());
            if (genServerResult.isStart()) {
                return AjaxResult.success(genServerResult.getData());
            } else {
                return AjaxResult.error(genServerResult.getMsg());
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return AjaxResult.error(GenReturnConstants.SYS_ERROR);
        }
    }


    /**
     * 查询模块管理列表
     */
    @PreAuthorize("@ebts.hasPermi('tool:module:list')")
    @GetMapping("/list")
    public Serializable list(Module module) {
        try {
            startPage();
            GenServerResult<List<Module>> genServerResult = moduleService.selectModuleList(module);
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
     * 导出模块管理列表
     */
    @PreAuthorize("@ebts.hasPermi('tool:module:export')")
    @Log(title = "模块管理", businessType = GenBusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(Module module) {
        try {
            GenServerResult<List<Module>> genServerResult = moduleService.selectModuleList(module);
            GenExcelUtil<Module> util = new GenExcelUtil<Module>(Module.class);
            if (genServerResult.isStart()) {
                return util.exportExcel(genServerResult.getData(), "module");
            } else {
                return AjaxResult.error(genServerResult.getMsg());
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return AjaxResult.error(GenReturnConstants.SYS_ERROR);
        }
    }

    /**
     * 获取模块管理详细信息
     */
    @PreAuthorize("@ebts.hasPermi('tool:module:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        try {
            GenServerResult<Module> genServerResult = moduleService.selectModuleById(id);
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
     * 新增模块管理
     */
    @PreAuthorize("@ebts.hasPermi('tool:module:add')")
    @Log(title = "模块管理", businessType = GenBusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Module module) {
        if (module.getmName() == null || module.getmName().equals("")) {
            return AjaxResult.error("模块名称不能为空!");
        }
        if (module.getmDescribe() == null || module.getmDescribe().equals("")) {
            return AjaxResult.error("模块描述不能为空!");
        }
        try {
            GenServerResult<Integer> genServerResult = moduleService.insertModule(module);
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
     * 修改模块管理
     */
    @PreAuthorize("@ebts.hasPermi('tool:module:edit')")
    @Log(title = "模块管理", businessType = GenBusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Module module) {
        try {

            if (module.getmName() == null || module.getmName().equals("")) {
                return AjaxResult.error("模块名称不能为空!");
            }
            if (module.getmDescribe() == null || module.getmDescribe().equals("")) {
                return AjaxResult.error("模块描述不能为空!");
            }
            GenServerResult<Integer> genServerResult = moduleService.updateModule(module);
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
     * 删除模块管理
     */
    @PreAuthorize("@ebts.hasPermi('tool:module:remove')")
    @Log(title = "模块管理", businessType = GenBusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        try {
            if (ids.length < 0) {
                return AjaxResult.error("id不能为空!");
            }
            GenServerResult<Integer> genServerResult = moduleService.deleteModuleByIds(ids);
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
