package com.ebts.generator.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;


import com.ebts.generator.aop.Log;
import com.ebts.generator.dto.InterTableDto;
import com.ebts.generator.enums.GenBusinessType;
import com.ebts.generator.utils.GenBaseController;
import com.ebts.generator.utils.GenReturnConstants;
import com.ebts.generator.utils.GenServerResult;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ebts.generator.entity.InterTable;
import com.ebts.generator.service.InterTableService;

import javax.servlet.http.HttpServletResponse;

/**
 * 接口信息Controller
 *
 * @author binlin
 * @date 2021-01-25
 */

@RestController
@RequestMapping("/generator/intertable")
public class InterTableController extends GenBaseController {
    protected final Logger logger = LoggerFactory.getLogger(InterTableController.class);

    @Autowired
    private InterTableService interTableService;

    /**
     * 查询接口信息列表
     */
    @PreAuthorize("@ebts.hasPermi('generator:intertable:list')")
    @GetMapping("/list")
    public Serializable list(InterTable interTable) {
        try {
            GenServerResult<Map<String, Object>> genServerResult = interTableService.selectInterTableList(interTable);
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
     * 预览代码(工作流)
     */

    @PreAuthorize("@ebts.hasPermi('generator:intertable:classpreview')")
    @Log(title = "预览代码(工作流)", businessType = GenBusinessType.GENCODE)
    @GetMapping("/classpreview/{id}")
    public AjaxResult classPreview(@PathVariable("id") Long id) {
        try {
            GenServerResult<Map<String, String>> genServerResult = interTableService.previewCodeCalss(id);
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
     * 预览代码(工作台)
     */
    @PreAuthorize("@ebts.hasPermi('generator:intertable:modulepreview')")
    @Log(title = "预览代码(工作台)", businessType = GenBusinessType.GENCODE)
    @GetMapping("/modulepreview/{id}")
    public AjaxResult modulePreview(@PathVariable("id") Long id) {
        try {
            GenServerResult<Map<String, Object>> genServerResult = interTableService.previewCodeMoudle(id);
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
     * 生成代码(工作流)
     *
     * @param id
     * @return
     */
    @PreAuthorize("@ebts.hasPermi('generator:intertable:classgen')")
    @Log(title = "生成代码(工作流)", businessType = GenBusinessType.GENCODE)
    @GetMapping("/classgen/{id}")
    public AjaxResult classGenerator(@PathVariable("id") Long id, HttpServletResponse response) {
        try {
            GenServerResult<byte[]> genServerResult = interTableService.generatorCodeClass(id);
            if (genServerResult.isStart()) {
                genCode(response, genServerResult.getData());
                return AjaxResult.success();
            } else {
                return AjaxResult.error(genServerResult.getMsg());
            }
        } catch (RuntimeException | IOException e) {
            logger.error(e.getMessage());
            return AjaxResult.error(GenReturnConstants.SYS_ERROR);
        }
    }

    /**
     * 生成代码(工作台)
     *
     * @param id
     * @param response
     * @return
     */
    @PreAuthorize("@ebts.hasPermi('generator:intertable:moudlegen')")
    @Log(title = "生成代码(工作台)", businessType = GenBusinessType.GENCODE)
    @GetMapping("/moudlegen/{id}")
    public AjaxResult moudleGenerator(@PathVariable("id") Long id, HttpServletResponse response) {
        try {
            GenServerResult<byte[]> genServerResult = interTableService.generatorCodeMoudle(id);
            if (genServerResult.isStart()) {
                genCode(response, genServerResult.getData());
                return AjaxResult.success();
            } else {
                return AjaxResult.error(genServerResult.getMsg());
            }
        } catch (RuntimeException | IOException e) {
            logger.error(e.getMessage());
            return AjaxResult.error(GenReturnConstants.SYS_ERROR);
        }
    }


    /**
     * 修改接口信息
     */
    @PreAuthorize("@ebts.hasPermi('generator:intertable:edit')")
    @Log(title = "接口信息", businessType = GenBusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody InterTableDto interTableDto) {
        try {
            GenServerResult<Integer> genServerResult = interTableService.updateInterTable(interTableDto);
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


//    /**
//     * 导出接口信息列表
//     */
//    @PreAuthorize("@ebts.hasPermi('generator:intertable:export')")
//    @Log(title = "接口信息", businessType = GenBusinessType.EXPORT)
//    @GetMapping("/export")
//    public GenResult export(InterTable interTable) {
//        try {
//            GenServerResult<List<InterTable>> serverResult = interTableService.selectInterTableList(interTable);
//            GenExcelUtil<InterTable> util = new GenExcelUtil<InterTable>(InterTable. class);
//            if (serverResult.isStart()) {
//                return util.exportExcel(serverResult.getData(), "intertable");
//            } else {
//                return GenResult.error(serverResult.getMsg());
//            }
//        } catch (RuntimeException e) {
//            logger.error(e.getMessage());
//            return GenResult.error(GenReturnConstants.SYS_ERROR);
//        }
//    }

//    /**
//     * 获取接口信息详细信息
//     */
//    @PreAuthorize("@ebts.hasPermi('generator:intertable:query')")
//    @GetMapping(value = "/{id}")
//    public GenResult Info(@PathVariable("id") Long id) {
//        try {
//            GenServerResult<InterTable> serverResult = interTableService.selectInterTableById(id);
//            if (serverResult.isStart()) {
//                return GenResult.success(serverResult.getData());
//            } else {
//                return GenResult.info(serverResult.getMsg());
//            }
//        } catch (RuntimeException e) {
//            logger.error(e.getMessage());
//            return GenResult.error(GenReturnConstants.SYS_ERROR);
//        }
//    }
//
//    /**
//     * 新增接口信息
//     */
//    @PreAuthorize("@ebts.hasPermi('generator:intertable:add')")
//    @Log(title = "接口信息", businessType = GenBusinessType.INSERT)
//    @PostMapping
//    public GenResult add(@RequestBody InterTable interTable) {
//        try {
//            GenServerResult<Integer> serverResult = interTableService.insertInterTable(interTable);
//            if (serverResult.isStart()) {
//                return GenResult.success();
//            } else {
//                return GenResult.error(serverResult.getMsg());
//            }
//        } catch (RuntimeException e) {
//            logger.error(e.getMessage());
//            return GenResult.error(GenReturnConstants.SYS_ERROR);
//        }
//    }
//
//    /**
//     * 删除接口信息
//     */
//    @PreAuthorize("@ebts.hasPermi('generator:intertable:remove')")
//    @Log(title = "接口信息", businessType = GenBusinessType.DELETE)
//    @DeleteMapping("/{ids}")
//    public GenResult remove(@PathVariable Long[] ids) {
//        try {
//            if (ids.length<0){
//                return GenResult.error("id不能为空!");
//            }
//            GenServerResult<Integer> serverResult = interTableService.deleteInterTableByIds(ids);
//            if (serverResult.isStart()) {
//                return GenResult.success();
//            } else {
//                return GenResult.error(serverResult.getMsg());
//            }
//        }catch (RuntimeException e){
//            logger.error(e.getMessage());
//            return GenResult.error(GenReturnConstants.SYS_ERROR);
//        }
//    }
}