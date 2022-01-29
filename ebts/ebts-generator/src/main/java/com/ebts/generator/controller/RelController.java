package com.ebts.generator.controller;

import com.ebts.generator.entity.RelColumn;
import com.ebts.generator.entity.RelTable;
import com.ebts.generator.service.RelService;
import com.ebts.generator.utils.GenServerResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Author 18209
 * @Date 2021/2/24 16:22
 * @Version 1.0
 */
@RestController
@RequestMapping("/rel")
public class RelController {
    private Logger logger = LoggerFactory.getLogger(RelController.class);
    @Autowired
    private RelService relService;

    /**
     * 获取表的基础信息
     * @return
     */
    @PreAuthorize("@ebts.hasAnyPermi('rel:tableinfo')")
    @GetMapping("/tableinfo")
    public AjaxResult tableinfos(){
        try {
            GenServerResult<List<Map<String,Object>>> genServerResult = relService.tableInfos();
            if (genServerResult.isStart()){
                return AjaxResult.success(genServerResult.getData());
            }else {
                return AjaxResult.error(genServerResult.getMsg());
            }
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            return AjaxResult.error();
        }
    }

    /**
     * 获取表的字段信息
     * @param tableName
     * @param relId
     * @return
     */
    @PreAuthorize("@ebts.hasAnyPermi('rel:tableinfo')")
    @GetMapping("/{tableName}/{relId}")
    public AjaxResult relColumns(@PathVariable("tableName") String tableName, @PathVariable("relId") Long relId){
        try {
            GenServerResult<List<RelColumn>> genServerResult = relService.relColumns(tableName,relId);
            if (genServerResult.isStart()){
                return AjaxResult.success(genServerResult.getData());
            }else {
                return AjaxResult.error(genServerResult.getMsg());
            }
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            return AjaxResult.error();
        }
    }

    @PreAuthorize("@ebts.hasAnyPermi('rel:colums')")
    @GetMapping("/{tableName}")
    public AjaxResult tableColumns(@PathVariable("tableName")String tableName){
        try {
            GenServerResult<List<Map<String,Object>>> genServerResult = relService.tableColumns(tableName);
            if (genServerResult.isStart()){
                return AjaxResult.success(genServerResult.getData());
            }else {
                return AjaxResult.error(genServerResult.getMsg());
            }
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            return AjaxResult.error();
        }
    }

    @PreAuthorize("@ebts.hasAnyPermi('rel:colums')")
    @GetMapping("/colums/{tableId}")
    public AjaxResult relTableByTableId(@PathVariable("tableId")Long tableId){
        try {
            GenServerResult<List<RelTable>> genServerResult = relService.relTableByTableId(tableId);
            if (genServerResult.isStart()){
                return AjaxResult.success(genServerResult.getData());
            }else {
                return AjaxResult.error(genServerResult.getMsg());
            }
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            return AjaxResult.error();
        }
    }


}
