package com.ebts.web.controller.system;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ebts.common.annotation.Log;
import com.ebts.common.core.controller.BaseController;
import com.ebts.common.core.entity.AjaxResult;
import com.ebts.common.enums.BusinessType;
import com.ebts.common.utils.SecurityUtils;
import com.ebts.system.entity.Notice;
import com.ebts.system.service.NoticeService;

/**
 * 公告 信息操作处理
 *
 * @author binlin
 */
@RestController
@RequestMapping("/system/notice")
public class NoticeController extends BaseController {
    @Autowired
    private NoticeService noticeService;

    /**
     * 获取通知公告列表
     */
    @PreAuthorize("@ebts.hasPermi('system:notice:list')")
    @GetMapping("/list")
    public Serializable list(Notice notice) {
            startPage();
            List<Notice> list = noticeService.selectNoticeList(notice);
            return getDataTable(list);
    }

    /**
     * 根据通知公告编号获取详细信息
     */
    @PreAuthorize("@ebts.hasPermi('system:notice:query')")
    @GetMapping(value = "/{noticeId}")
    public AjaxResult getInfo(@PathVariable Long noticeId) {
            return AjaxResult.success(noticeService.selectNoticeById(noticeId));
    }

    /**
     * 新增通知公告
     */
    @PreAuthorize("@ebts.hasPermi('system:notice:add')")
    @Log(title = "通知公告", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody Notice notice) {
            notice.setCreateBy(SecurityUtils.getUserId());
            return toAjax(noticeService.insertNotice(notice));
    }

    /**
     * 修改通知公告
     */
    @PreAuthorize("@ebts.hasPermi('system:notice:edit')")
    @Log(title = "通知公告", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody Notice notice) {
            notice.setUpdateBy(SecurityUtils.getUserId());
            return toAjax(noticeService.updateNotice(notice));
    }

    /**
     * 删除通知公告
     */
    @PreAuthorize("@ebts.hasPermi('system:notice:remove')")
    @Log(title = "通知公告", businessType = BusinessType.DELETE)
    @DeleteMapping("/{noticeIds}")
    public AjaxResult remove(@PathVariable Long[] noticeIds) {
            return toAjax(noticeService.deleteNoticeByIds(noticeIds));
    }
}
