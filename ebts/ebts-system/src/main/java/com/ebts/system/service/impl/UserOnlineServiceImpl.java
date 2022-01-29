package com.ebts.system.service.impl;

import com.ebts.common.constant.ReturnConstants;
import com.ebts.common.exception.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.ebts.common.core.entity.model.LoginUser;
import com.ebts.common.utils.StringUtils;
import com.ebts.system.entity.UserOnline;
import com.ebts.system.service.UserOnlineService;

/**
 * 在线用户 服务层处理
 *
 * @author binlin
 */
@Service
public class UserOnlineServiceImpl implements UserOnlineService {
    private Logger logger = LoggerFactory.getLogger(UserOnlineServiceImpl.class);

    /**
     * 通过登录地址查询信息
     *
     * @param ipaddr 登录地址
     * @param user   用户信息
     * @return 在线用户信息
     */
    @Override
    public UserOnline selectOnlineByIpaddr(String ipaddr, LoginUser user) {
        try {
            if (StringUtils.equals(ipaddr, user.getIpaddr())) {
                return loginUserToUserOnline(user);
            }
            return null;
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 通过用户名称查询信息
     *
     * @param userName 用户名称
     * @param user     用户信息
     * @return 在线用户信息
     */
    @Override
    public UserOnline selectOnlineByUserName(String userName, LoginUser user) {
        try {
            if (StringUtils.equals(userName, user.getUsername())) {
                return loginUserToUserOnline(user);
            }
            return null;
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 通过登录地址/用户名称查询信息
     *
     * @param ipaddr   登录地址
     * @param userName 用户名称
     * @param user     用户信息
     * @return 在线用户信息
     */
    @Override
    public UserOnline selectOnlineByInfo(String ipaddr, String userName, LoginUser user) {
        try {
            if (StringUtils.equals(ipaddr, user.getIpaddr()) && StringUtils.equals(userName, user.getUsername())) {
                return loginUserToUserOnline(user);
            }
            return null;
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 设置在线用户信息
     *
     * @param user 用户信息
     * @return 在线用户
     */
    @Override
    public UserOnline loginUserToUserOnline(LoginUser user) {
        try {
            if (StringUtils.isNull(user) || StringUtils.isNull(user.getUser())) {
                return null;
            }
            UserOnline userOnline = new UserOnline();
            userOnline.setTokenId(user.getToken());
            userOnline.setUserName(user.getUsername());
            userOnline.setIpaddr(user.getIpaddr());
            userOnline.setLoginLocation(user.getLoginLocation());
            userOnline.setBrowser(user.getBrowser());
            userOnline.setOs(user.getOs());
            userOnline.setLoginTime(user.getLoginTime());
            if (StringUtils.isNotNull(user.getUser().getDept())) {
                userOnline.setDeptName(user.getUser().getDept().getDeptName());
            }
            return userOnline;
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }
}
