package com.ebts.web.controller.monitor;

import com.ebts.common.constant.ReturnConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ebts.common.core.controller.BaseController;
import com.ebts.common.core.entity.AjaxResult;
import com.ebts.framework.web.domain.Server;

/**
 * 服务器监控
 *
 * @author binlin
 */
@RestController
@RequestMapping("/monitor/server")
public class ServerController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(ServerController.class);

    @PreAuthorize("@ebts.hasPermi('monitor:server:list')")
    @GetMapping()
    public AjaxResult getInfo() throws Exception {
        try {
            Server server = new Server();
            server.copyTo();
            return AjaxResult.success(server);
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            return AjaxResult.error(ReturnConstants.SYS_ERROR);
        }
    }
}
