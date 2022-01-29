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
import com.ebts.generator.entity.ApiClass;
import com.ebts.generator.service.ApiClassService;

/**
 * 接口类名Controller
 *
 * @author binlin
 * @date 2021-01-24
 */
@RestController
@RequestMapping("/tool/apiclass")
public class ApiClassController extends GenBaseController {
    protected final Logger logger = LoggerFactory.getLogger(ApiClassController.class);

    @Autowired
    private ApiClassService apiClassService;


    /**
     * 获取到api类的select选项
     *
     * @return
     */
    @PreAuthorize("@ebts.hasAnyPermi('tool:apiclass:querylist')")
    @GetMapping("querylist")
    public AjaxResult queryList() {
        try {
            GenServerResult<List<ApiClass>> genServerResult = apiClassService.selectApiclassList(new ApiClass());
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
     * 查询接口类名列表
     */
    @PreAuthorize("@ebts.hasPermi('tool:apiclass:list')")
    @GetMapping("/list")
    public Serializable list(ApiClass apiclass) {
        try {
            startPage();
            GenServerResult<List<ApiClass>> genServerResult = apiClassService.selectApiclassList(apiclass);
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
     * 导出接口类名列表
     */
    @PreAuthorize("@ebts.hasPermi('tool:apiclass:export')")
    @Log(title = "接口类名", businessType = GenBusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(ApiClass apiclass) {
        try {
            GenServerResult<List<ApiClass>> genServerResult = apiClassService.selectApiclassList(apiclass);
            GenExcelUtil<ApiClass> util = new GenExcelUtil<ApiClass>(ApiClass.class);
            if (genServerResult.isStart()) {
                return util.exportExcel(genServerResult.getData(), "apiclass");
            } else {
                return AjaxResult.error(genServerResult.getMsg());
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return AjaxResult.error(GenReturnConstants.SYS_ERROR);
        }
    }

    /**
     * 获取接口类名详细信息
     */
    @PreAuthorize("@ebts.hasPermi('tool:apiclass:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        try {
            GenServerResult<ApiClass> genServerResult = apiClassService.selectApiclassById(id);
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
     * 新增接口类名
     */
    @PreAuthorize("@ebts.hasPermi('tool:apiclass:add')")
    @Log(title = "接口类名", businessType = GenBusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ApiClass apiclass) {
        if (apiclass.getmId() == null || apiclass.getmId() < 0) {
            return AjaxResult.error("模块id不能为空!");
        }
        if (apiclass.getcName() == null || apiclass.getcName().equals("")) {
            return AjaxResult.error("类名不能为空!");
        }
        if (apiclass.getcDescribe() == null || apiclass.getcDescribe().equals("")) {
            return AjaxResult.error("类描述不能为空!");
        }
        try {
            GenServerResult<Integer> genServerResult = apiClassService.insertApiclass(apiclass);
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
     * 修改接口类名
     */
    @PreAuthorize("@ebts.hasPermi('tool:apiclass:edit')")
    @Log(title = "接口类名", businessType = GenBusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ApiClass apiclass) {
        try {

            if (apiclass.getmId() == null || apiclass.getmId() < 0) {
                return AjaxResult.error("模块id不能为空!");
            }
            if (apiclass.getcName() == null || apiclass.getcName().equals("")) {
                return AjaxResult.error("类名不能为空!");
            }
            if (apiclass.getcDescribe() == null || apiclass.getcDescribe().equals("")) {
                return AjaxResult.error("类描述不能为空!");
            }
            GenServerResult<Integer> genServerResult = apiClassService.updateApiclass(apiclass);
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
     * 删除接口类名
     */
    @PreAuthorize("@ebts.hasPermi('tool:apiclass:remove')")
    @Log(title = "接口类名", businessType = GenBusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        try {
            if (ids.length < 0) {
                return AjaxResult.error("id不能为空!");
            }
            GenServerResult<Integer> genServerResult = apiClassService.deleteApiclassByIds(ids);
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
