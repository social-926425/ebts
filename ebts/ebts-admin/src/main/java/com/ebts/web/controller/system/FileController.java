package com.ebts.web.controller.system;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.ebts.common.constant.ReturnConstants;
import com.ebts.common.core.controller.EBTSController;
import com.ebts.common.core.entity.model.LoginUser;
import com.ebts.common.utils.ServerResult;
import com.ebts.framework.web.service.TokenService;
import com.ebts.system.entity.SysFile;
import com.ebts.system.utils.FtpUtils;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ebts.common.annotation.Log;
import com.ebts.common.core.entity.AjaxResult;
import com.ebts.common.enums.BusinessType;
import com.ebts.system.service.FileService;
import com.ebts.common.utils.poi.ExcelUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 文件信息Controller
 *
 * @author binlin
 * @date 2021-02-17
 */

@Api(value = "文件信息管理", tags = "文件信息管理")
@RestController
@RequestMapping("/system/file")
public class FileController extends EBTSController {
    protected final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileService fileService;

    @Autowired
    private TokenService tokenService;

    /**
     * 查询文件信息列表
     */
    @PreAuthorize("@ebts.hasPermi('system:file:list')")
    @PutMapping("/list")
    public Serializable list(@Validated @RequestBody SysFile sysFile) {
        try {
            startPage(sysFile.getParams());
            ServerResult<List<SysFile>> serverResult = fileService.selectFileList(sysFile);
            if (serverResult.isStart()) {
                return getDataTable(serverResult.getData());
            } else {
                return AjaxResult.info(serverResult.getMsg());
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return AjaxResult.error(ReturnConstants.SYS_ERROR);
        }
    }

    @GetMapping("/folder")
    public AjaxResult getFolder() {
        try {
            ServerResult<List<SysFile>> serverResult = fileService.selectFileFolder();
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
     * 导出文件信息列表
     */
    @PreAuthorize("@ebts.hasPermi('system:file:export')")
    @Log(title = "文件信息", businessType = BusinessType.EXPORT)
    @PutMapping("/export")
    public AjaxResult export(@Validated @RequestBody SysFile sysFile) {
        try {
            ServerResult<List<SysFile>> serverResult = fileService.selectFileList(sysFile);
            ExcelUtil<SysFile> util = new ExcelUtil<SysFile>(SysFile.class);
            if (serverResult.isStart()) {
                return util.exportExcel(serverResult.getData(), "file");
            } else {
                return AjaxResult.error(serverResult.getMsg());
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return AjaxResult.error(ReturnConstants.SYS_ERROR);
        }
    }

    /**
     * 获取文件信息详细信息
     */
    @PreAuthorize("@ebts.hasPermi('system:file:query')")
    @GetMapping(value = "/{fileId}")
    public AjaxResult getInfo(@PathVariable("fileId") Long fileId) {
        try {
            ServerResult<SysFile> serverResult = fileService.selectFileById(fileId);
            if (serverResult.isStart()) {
                return AjaxResult.success(serverResult.getData());
            } else {
                return AjaxResult.info(serverResult.getMsg());
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return AjaxResult.error(ReturnConstants.SYS_ERROR);
        }
    }

    /**
     * 新增文件信息
     */
    @PreAuthorize("@ebts.hasPermi('system:file:add')")
    @Log(title = "文件信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysFile sysFile) {
        try {
            if (sysFile.getFileAddr() == null || sysFile.getFileAddr().equals("")) {
                return AjaxResult.error("上传文件不能为空!");
            }
            if (sysFile.getIsPublic().equals("1") || sysFile.getIsPublic().equals("2")) {
                Map<String, String> modeMap = new HashMap<>();
                ServerResult<SysFile> serverResult = fileService.insertFile(sysFile);
                if (serverResult.isStart()) {
                    if (sysFile.getIsPublic().equals("1")) {
                        String fileUrl = FtpUtils.getResources() + sysFile.getFileAddr().substring(FtpUtils.getPubfiles().length()) + "/" + sysFile.getMapping();
                        modeMap.put("url", fileUrl);
                    }
                    String fileUri = sysFile.getFileAddr() + "/" + sysFile.getMapping();
                    modeMap.put("uri", fileUri);
                    return AjaxResult.success(modeMap);
                } else {
                    return AjaxResult.error(serverResult.getMsg());
                }
            }else {
                return AjaxResult.error("isPublic为空或者状态有误!");
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return AjaxResult.error(ReturnConstants.SYS_ERROR);
        }
    }

    /**
     * 修改文件信息
     */
    @PreAuthorize("@ebts.hasPermi('system:file:edit')")
    @Log(title = "文件信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysFile sysFile) {
        try {

            ServerResult<Integer> serverResult = fileService.updateFile(sysFile);
            if (serverResult.isStart()) {
                return AjaxResult.success();
            } else {
                return AjaxResult.error(serverResult.getMsg());
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return AjaxResult.error(ReturnConstants.SYS_ERROR);
        }
    }

    /**
     * 删除文件信息
     */
    @PreAuthorize("@ebts.hasPermi('system:file:remove')")
    @Log(title = "文件信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{fileIds}")
    public AjaxResult remove(@PathVariable Long[] fileIds) {
        try {
            if (fileIds.length < 0) {
                return AjaxResult.error("id不能为空!");
            }
            ServerResult<Integer> serverResult = fileService.deleteFileByIds(fileIds);
            if (serverResult.isStart()) {
                return AjaxResult.success();
            } else {
                return AjaxResult.error(serverResult.getMsg());
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return AjaxResult.error(ReturnConstants.SYS_ERROR);
        }
    }

    @GetMapping("/download/{fileId}")
    public AjaxResult download(@PathVariable Long fileId, HttpServletRequest request, HttpServletResponse response) {
        try {
            LoginUser user = tokenService.getFileUser(request);
            ServerResult<SysFile> serverResult = fileService.downloadFile(fileId, user);
            if (serverResult.isStart()) {
                return AjaxResult.success(serverResult.getData().getFileName());
            } else {
                return AjaxResult.error(serverResult.getMsg());
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return AjaxResult.error(ReturnConstants.SYS_ERROR);
        }
    }

    @GetMapping("/download/api")
    public AjaxResult api() {
        return AjaxResult.success("8085");
    }
}
