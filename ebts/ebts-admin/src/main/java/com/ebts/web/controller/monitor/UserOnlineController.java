package com.ebts.web.controller.monitor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.ebts.common.constant.ReturnConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ebts.common.annotation.Log;
import com.ebts.common.constant.Constants;
import com.ebts.common.core.controller.BaseController;
import com.ebts.common.core.entity.AjaxResult;
import com.ebts.common.core.entity.model.LoginUser;
import com.ebts.common.core.redis.RedisCache;
import com.ebts.common.enums.BusinessType;
import com.ebts.common.utils.StringUtils;
import com.ebts.system.entity.UserOnline;
import com.ebts.system.service.UserOnlineService;

/**
 * 在线用户监控
 *
 * @author binlin
 */
@RestController
@RequestMapping("/monitor/online")
public class UserOnlineController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(UserOnlineController.class);

    @Autowired
    private UserOnlineService userOnlineService;

    @Autowired
    private RedisCache redisCache;

    @PreAuthorize("@ebts.hasPermi('monitor:online:list')")
    @GetMapping("/list")
    public Serializable list(String ipaddr, String userName) {
        try {
            Collection<String> keys = redisCache.keys(Constants.LOGIN_TOKEN_KEY + "*");
            List<UserOnline> userOnlineList = new ArrayList<UserOnline>();
            for (String key : keys) {
                LoginUser user = redisCache.getCacheObject(key);
                if (StringUtils.isNotEmpty(ipaddr) && StringUtils.isNotEmpty(userName)) {
                    if (StringUtils.equals(ipaddr, user.getIpaddr()) && StringUtils.equals(userName, user.getUsername())) {
                        userOnlineList.add(userOnlineService.selectOnlineByInfo(ipaddr, userName, user));
                    }
                } else if (StringUtils.isNotEmpty(ipaddr)) {
                    if (StringUtils.equals(ipaddr, user.getIpaddr())) {
                        userOnlineList.add(userOnlineService.selectOnlineByIpaddr(ipaddr, user));
                    }
                } else if (StringUtils.isNotEmpty(userName) && StringUtils.isNotNull(user.getUser())) {
                    if (StringUtils.equals(userName, user.getUsername())) {
                        userOnlineList.add(userOnlineService.selectOnlineByUserName(userName, user));
                    }
                } else {
                    userOnlineList.add(userOnlineService.loginUserToUserOnline(user));
                }
            }
            Collections.reverse(userOnlineList);
            userOnlineList.removeAll(Collections.singleton(null));
            return getDataTable(userOnlineList);
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            return AjaxResult.error(ReturnConstants.SYS_ERROR);
        }
    }

    /**
     * 强退用户
     */
    @PreAuthorize("@ebts.hasPermi('monitor:online:forceLogout')")
    @Log(title = "在线用户", businessType = BusinessType.FORCE)
    @DeleteMapping("/{tokenId}")
    public AjaxResult forceLogout(@PathVariable String tokenId) {
        try {
            redisCache.deleteObject(Constants.LOGIN_TOKEN_KEY + tokenId);
            return AjaxResult.success();
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            return AjaxResult.error(ReturnConstants.SYS_ERROR);
        }
    }
}
