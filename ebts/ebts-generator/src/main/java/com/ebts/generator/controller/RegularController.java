package com.ebts.generator.controller;

import java.io.Serializable;
import java.util.List;
import java.util.regex.Pattern;

import com.ebts.generator.aop.Log;
import com.ebts.generator.entity.Regular;
import com.ebts.generator.enums.GenBusinessType;
import com.ebts.generator.service.RegularService;
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
 * 校验规则Controller
 *
 * @author binlin
 * @date 2021-01-18
 */
@RestController
@RequestMapping("/tool/regular")
public class RegularController extends GenBaseController {
    protected final Logger logger = LoggerFactory.getLogger(RegularController.class);

    @Autowired
    private RegularService regularService;

    /**
     * 查询校验规则列表
     *
     * @param regular
     * @return
     */
    @PreAuthorize("@ebts.hasPermi('tool:regular:list')")
    @GetMapping("/list")
    public Serializable list(Regular regular) {
        try {
            startPage();
            GenServerResult<List<Regular>> genServerResult = regularService.selectRegularList(regular);
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
     * 导出校验规则列表
     *
     * @param regular
     * @return
     */
    @PreAuthorize("@ebts.hasPermi('tool:regular:export')")
    @Log(title = "校验规则", businessType = GenBusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(Regular regular) {
        try {
            GenServerResult<List<Regular>> genServerResult = regularService.selectRegularList(regular);
            GenExcelUtil<Regular> util = new GenExcelUtil<Regular>(Regular.class);
            if (genServerResult.isStart()) {
                return util.exportExcel(genServerResult.getData(), "regular");
            } else {
                return AjaxResult.error(genServerResult.getMsg());
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return AjaxResult.error(GenReturnConstants.SYS_ERROR);
        }
    }

    /**
     * 获取校验规则详细信息
     *
     * @param id
     * @return
     */
    @PreAuthorize("@ebts.hasPermi('tool:regular:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        try {
            GenServerResult<Regular> genServerResult = regularService.selectRegularById(id);
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
     * 新增校验规则
     *
     * @param regular
     * @return
     */
    @PreAuthorize("@ebts.hasPermi('tool:regular:add')")
    @Log(title = "校验规则", businessType = GenBusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Regular regular) {
        try {
            if (regular.getName() == null || regular.getName().equals("")) {
                return AjaxResult.error("正则名称不能为空!");
            }
            if (regular.getRegular() == null || regular.getRegular().equals("")) {
                return AjaxResult.error("正则内容不能为空!");
            }
            if (regular.getEnable() == null || regular.getEnable() < 0) {
                return AjaxResult.error("是否启用不能为空!");
            }
            if (!verifyRegular(regular)) {
                return AjaxResult.error("正则表达式校验不通过!");
            }
            GenServerResult<Integer> genServerResult = regularService.insertRegular(regular);
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
     * 修改校验规则
     *
     * @param regular
     * @return
     */
    @PreAuthorize("@ebts.hasPermi('tool:regular:edit')")
    @Log(title = "校验规则", businessType = GenBusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Regular regular) {
        try {
            if (regular.getName() == null || regular.getName().equals("")) {
                return AjaxResult.error("正则名称不能为空!");
            }
            if (regular.getRegular() == null || regular.getRegular().equals("")) {
                return AjaxResult.error("正则内容不能为空!");
            }
            if (regular.getEnable() == null || regular.getEnable() < 0) {
                return AjaxResult.error("是否启用不能为空!");
            }
            if (!verifyRegular(regular)) {
                return AjaxResult.error("正则表达式校验不通过!");
            }
            GenServerResult<Integer> genServerResult = regularService.updateRegular(regular);
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
     * 删除校验规则
     *
     * @param ids
     * @return
     */
    @PreAuthorize("@ebts.hasPermi('tool:regular:remove')")
    @Log(title = "校验规则", businessType = GenBusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        try {
            if (ids.length == 0) {
                return AjaxResult.error("需要删除id数不能为空!");
            }
            GenServerResult<Integer> genServerResult = regularService.deleteRegularByIds(ids);
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

    public boolean verifyRegular(Regular regular) {
        return Pattern.matches(regular.getRegular(), regular.getValidation());
    }
}
