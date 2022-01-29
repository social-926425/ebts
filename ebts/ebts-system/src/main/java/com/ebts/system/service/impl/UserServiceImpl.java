package com.ebts.system.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.ebts.common.constant.ReturnConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ebts.common.annotation.DataScope;
import com.ebts.common.constant.UserConstants;
import com.ebts.common.core.entity.entity.Role;
import com.ebts.common.core.entity.entity.User;
import com.ebts.common.exception.CustomException;
import com.ebts.common.utils.SecurityUtils;
import com.ebts.common.utils.StringUtils;
import com.ebts.system.entity.Post;
import com.ebts.system.entity.UserPost;
import com.ebts.system.entity.UserRole;
import com.ebts.system.dao.PostDao;
import com.ebts.system.dao.RoleDao;
import com.ebts.system.dao.UserDao;
import com.ebts.system.dao.UserPostDao;
import com.ebts.system.dao.UserRoleDao;
import com.ebts.system.service.ConfigService;
import com.ebts.system.service.UserService;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 * 用户 业务层处理
 *
 * @author binlin
 */
@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao userMapper;

    @Autowired
    private RoleDao roleMapper;

    @Autowired
    private PostDao postMapper;

    @Autowired
    private UserRoleDao userRoleMapper;

    @Autowired
    private UserPostDao userPostMapper;

    @Autowired
    private ConfigService configService;

    /**
     * 根据条件分页查询用户列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<User> selectUserList(User user) {
        try {
            return userMapper.selectUserList(user);
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    @Override
    public User selectUserByUserName(String userName) {
        try {
            return userMapper.selectUserByUserName(userName);
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    @Override
    public User selectUserById(Long userId) {
        try {
            return userMapper.selectUserById(userId);
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 查询用户所属角色组
     *
     * @param userName 用户名
     * @return 结果
     */
    @Override
    public String selectUserRoleGroup(String userName) {
        try {
            List<Role> list = roleMapper.selectRolesByUserName(userName);
            StringBuffer idsStr = new StringBuffer();
            for (Role role : list) {
                idsStr.append(role.getRoleName()).append(",");
            }
            if (StringUtils.isNotEmpty(idsStr.toString())) {
                return idsStr.substring(0, idsStr.length() - 1);
            }
            return idsStr.toString();
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 查询用户所属岗位组
     *
     * @param userName 用户名
     * @return 结果
     */
    @Override
    public String selectUserPostGroup(String userName) {
        try {
            List<Post> list = postMapper.selectPostsByUserName(userName);
            StringBuffer idsStr = new StringBuffer();
            for (Post post : list) {
                idsStr.append(post.getPostName()).append(",");
            }
            if (StringUtils.isNotEmpty(idsStr.toString())) {
                return idsStr.substring(0, idsStr.length() - 1);
            }
            return idsStr.toString();
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 校验用户名称是否唯一
     *
     * @param userName 用户名称
     * @return 结果
     */
    @Override
    public String checkUserNameUnique(String userName) {
        try {
            int count = userMapper.checkUserNameUnique(userName);
            if (count > 0) {
                return UserConstants.NOT_UNIQUE;
            }
            return UserConstants.UNIQUE;
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 校验用户名称是否唯一
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public String checkPhoneUnique(User user) {
        try {
            Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
            User info = userMapper.checkPhoneUnique(user.getPhonenumber());
            if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue()) {
                return UserConstants.NOT_UNIQUE;
            }
            return UserConstants.UNIQUE;
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 校验email是否唯一
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public String checkEmailUnique(User user) {
        try {
            Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
            User info = userMapper.checkEmailUnique(user.getEmail());
            if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue()) {
                return UserConstants.NOT_UNIQUE;
            }
            return UserConstants.UNIQUE;
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 校验用户是否允许操作
     *
     * @param user 用户信息
     */
    @Override
    public void checkUserAllowed(User user) {
        try {
            if (StringUtils.isNotNull(user.getUserId()) && user.isAdmin()) {
                throw new CustomException("不允许操作超级管理员用户");
            }
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 新增保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertUser(User user) {
        try {
            // 新增用户信息
            int rows = userMapper.insertUser(user);
            // 新增用户岗位关联
            insertUserPost(user);
            // 新增用户与角色管理
            insertUserRole(user);
            return rows;
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 修改保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateUser(User user) {
        try {
            Long userId = user.getUserId();
            // 删除用户与角色关联
            userRoleMapper.deleteUserRoleByUserId(userId);
            // 新增用户与角色管理
            insertUserRole(user);
            // 删除用户与岗位关联
            userPostMapper.deleteUserPostByUserId(userId);
            // 新增用户与岗位管理
            insertUserPost(user);
            return userMapper.updateUser(user);
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 修改用户状态
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int updateUserStatus(User user) {
        try {
            return userMapper.updateUser(user);
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 修改用户基本信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int updateUserProfile(User user) {
        try {
            return userMapper.updateUser(user);
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 修改用户头像
     *
     * @param userName 用户名
     * @param avatar   头像地址
     * @return 结果
     */
    @Override
    public boolean updateUserAvatar(String userName, String avatar) {
        try {
            return userMapper.updateUserAvatar(userName, avatar) > 0;
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 重置用户密码
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int resetPwd(User user) {
        try {
            return userMapper.updateUser(user);
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 重置用户密码
     *
     * @param userName 用户名
     * @param password 密码
     * @return 结果
     */
    @Override
    public int resetUserPwd(String userName, String password) {
        try {
            return userMapper.resetUserPwd(userName, password);
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 新增用户角色信息
     *
     * @param user 用户对象
     */
    public void insertUserRole(User user) {
        Long[] roles = user.getRoleIds();
        if (StringUtils.isNotNull(roles)) {
            // 新增用户与角色管理
            List<UserRole> list = new ArrayList<UserRole>();
            for (Long roleId : roles) {
                UserRole ur = new UserRole();
                ur.setUserId(user.getUserId());
                ur.setRoleId(roleId);
                list.add(ur);
            }
            if (list.size() > 0) {
                userRoleMapper.batchUserRole(list);
            }
        }
    }

    /**
     * 新增用户岗位信息
     *
     * @param user 用户对象
     */
    public void insertUserPost(User user) {
        Long[] posts = user.getPostIds();
        if (StringUtils.isNotNull(posts)) {
            // 新增用户与岗位管理
            List<UserPost> list = new ArrayList<UserPost>();
            for (Long postId : posts) {
                UserPost up = new UserPost();
                up.setUserId(user.getUserId());
                up.setPostId(postId);
                list.add(up);
            }
            if (list.size() > 0) {
                userPostMapper.batchUserPost(list);
            }
        }
    }

    /**
     * 通过用户ID删除用户
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteUserById(Long userId) {
        try {
            // 删除用户与角色关联
            userRoleMapper.deleteUserRoleByUserId(userId);
            // 删除用户与岗位表
            userPostMapper.deleteUserPostByUserId(userId);
            return userMapper.deleteUserById(userId);
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 批量删除用户信息
     *
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    @Override
    public int deleteUserByIds(Long[] userIds) {
        try {
            for (Long userId : userIds) {
                checkUserAllowed(new User(userId));
            }
            return userMapper.deleteUserByIds(userIds);
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 导入用户数据
     *
     * @param userList        用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    @Transactional
    public String importUser(List<User> userList, Boolean isUpdateSupport, Long operName) {
        try {
            if (StringUtils.isNull(userList) || userList.size() == 0) {
                throw new CustomException("导入用户数据不能为空！");
            }
            int successNum = 0;
            int failureNum = 0;
            StringBuilder successMsg = new StringBuilder();
            StringBuilder failureMsg = new StringBuilder();
            String password = configService.selectConfigByKey("sys.user.initPassword");
            for (User user : userList) {
                try {
                    // 验证是否存在这个用户
                    User u = userMapper.selectUserByUserName(user.getUserName());
                    if (StringUtils.isNull(u)) {
                        user.setPassword(SecurityUtils.encryptPassword(password));
                        user.setCreateBy(operName);
                        this.insertUser(user);
                        successNum++;
                        successMsg.append("<br/>" + successNum + "、账号 " + user.getUserName() + " 导入成功");
                    } else if (isUpdateSupport) {
                        user.setUpdateBy(operName);
                        this.updateUser(user);
                        successNum++;
                        successMsg.append("<br/>" + successNum + "、账号 " + user.getUserName() + " 更新成功");
                    } else {
                        failureNum++;
                        failureMsg.append("<br/>" + failureNum + "、账号 " + user.getUserName() + " 已存在");
                    }
                } catch (Exception e) {
                    failureNum++;
                    String msg = "<br/>" + failureNum + "、账号 " + user.getUserName() + " 导入失败：";
                    failureMsg.append(msg + e.getMessage());
                    logger.error(msg, e);
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
                throw new CustomException(failureMsg.toString());
            } else {
                successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
            }
            return successMsg.toString();
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }
}
