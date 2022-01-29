package com.ebts.web.controller.system;

import com.ebts.common.core.entity.AjaxResult;
import com.ebts.system.service.SequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author 18209
 * @Date 2021/2/24 17:52
 * @Version 1.0
 */
@RestController
@RequestMapping("/seq")
public class SequenceController {

    @Autowired
    private SequenceService sequenceService;

    @PreAuthorize("@ebts.hasPermi('sequence:curr')")
    @GetMapping("/curr/{tableName}")
    public AjaxResult currval(@PathVariable String tableName){
        return AjaxResult.success(sequenceService.currval(tableName));
    }

    @PreAuthorize("@ebts.hasPermi('sequence:next')")
    @GetMapping("/next/{tableName}")
    public AjaxResult nextval(@PathVariable String tableName){
        return AjaxResult.success(sequenceService.nextval(tableName));
    }

}
