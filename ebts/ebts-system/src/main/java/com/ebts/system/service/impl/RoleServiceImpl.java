package com.ebts.system.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.ebts.common.constant.ReturnConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ebts.common.annotation.DataScope;
import com.ebts.common.constant.UserConstants;
import com.ebts.common.core.entity.entity.Role;
import com.ebts.common.exception.CustomException;
import com.ebts.common.utils.StringUtils;
import com.ebts.common.utils.spring.SpringUtils;
import com.ebts.system.entity.RoleDept;
import com.ebts.system.entity.RoleMenu;
import com.ebts.system.dao.RoleDeptDao;
import com.ebts.system.dao.RoleDao;
import com.ebts.system.dao.RoleMenuDao;
import com.ebts.system.dao.UserRoleDao;
import com.ebts.system.service.RoleService;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 * 角色 业务层处理
 *
 * @author binlin
 */
@Service
public class RoleServiceImpl implements RoleService {
    private Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

    @Autowired
    private RoleDao roleMapper;

    @Autowired
    private RoleMenuDao roleMenuMapper;

    @Autowired
    private UserRoleDao userRoleMapper;

    @Autowired
    private RoleDeptDao roleDeptMapper;

    /**
     * 根据条件分页查询角色数据
     *
     * @param role 角色信息
     * @return 角色数据集合信息
     */
    @Override
    @DataScope(deptAlias = "d")
    public List<Role> selectRoleList(Role role) {
        try {
            return roleMapper.selectRoleList(role);
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectRolePermissionByUserId(Long userId) {
        try {
            List<Role> perms = roleMapper.selectRolePermissionByUserId(userId);
            Set<String> permsSet = new HashSet<>();
            for (Role perm : perms) {
                if (StringUtils.isNotNull(perm)) {
                    permsSet.addAll(Arrays.asList(perm.getRoleKey().trim().split(",")));
                }
            }
            return permsSet;
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 查询所有角色
     *
     * @param type
     * @return 角色列表
     */
    @Override
    public List<Role> selectRoleAll(Integer type) {
        try {
            return SpringUtils.getAopProxy(this).selectRoleList(new Role(type));
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 根据用户ID获取角色选择框列表
     *
     * @param userId 用户ID
     * @return 选中角色ID列表
     */
    @Override
    public List<Integer> selectRoleListByUserId(Long userId) {
        try {
            return roleMapper.selectRoleListByUserId(userId);
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 通过角色ID查询角色
     *
     * @param roleId 角色ID
     * @return 角色对象信息
     */
    @Override
    public Role selectRoleById(Long roleId) {
        try {
            return roleMapper.selectRoleById(roleId);
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 校验角色名称是否唯一
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public String checkRoleNameUnique(Role role) {
        try {
            Long roleId = StringUtils.isNull(role.getRoleId()) ? -1L : role.getRoleId();
            Role info = roleMapper.checkRoleNameUnique(role.getRoleName());
            if (StringUtils.isNotNull(info) && info.getRoleId().longValue() != roleId.longValue()) {
                return UserConstants.NOT_UNIQUE;
            }
            return UserConstants.UNIQUE;
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 校验角色权限是否唯一
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public String checkRoleKeyUnique(Role role) {
        try {
            Long roleId = StringUtils.isNull(role.getRoleId()) ? -1L : role.getRoleId();
            Role info = roleMapper.checkRoleKeyUnique(role.getRoleKey());
            if (StringUtils.isNotNull(info) && info.getRoleId().longValue() != roleId.longValue()) {
                return UserConstants.NOT_UNIQUE;
            }
            return UserConstants.UNIQUE;
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 校验角色是否允许操作
     *
     * @param role 角色信息
     */
    @Override
    public void checkRoleAllowed(Role role) {
        try {
            if (StringUtils.isNotNull(role.getRoleId()) && role.isAdmin()) {
                throw new CustomException("不允许操作超级管理员角色");
            }
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 通过角色ID查询角色使用数量
     *
     * @param roleId 角色ID
     * @return 结果
     */
    @Override
    public int countUserRoleByRoleId(Long roleId) {
        try {
            return userRoleMapper.countUserRoleByRoleId(roleId);
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 新增保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertRole(Role role) {
        try {
            // 新增角色信息
            roleMapper.insertRole(role);
            return insertRoleMenu(role);
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 修改保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateRole(Role role) {
        try {
            // 修改角色信息
            roleMapper.updateRole(role);
            // 删除角色与菜单关联
            roleMenuMapper.deleteRoleMenuByRoleId(role.getRoleId());
            return insertRoleMenu(role);
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 修改角色状态
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public int updateRoleStatus(Role role) {
        try {
            return roleMapper.updateRole(role);
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 修改数据权限信息
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional
    public int authDataScope(Role role) {
        try {
            // 修改角色信息
            roleMapper.updateRole(role);
            // 删除角色与部门关联
            roleDeptMapper.deleteRoleDeptByRoleId(role.getRoleId());
            // 新增角色和部门信息（数据权限）
            return insertRoleDept(role);
        }catch (RuntimeException e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 新增角色菜单信息
     *
     * @param role 角色对象
     */
    public int insertRoleMenu(Role role) {
        int rows = 1;
        // 新增用户与角色管理
        List<RoleMenu> list = new ArrayList<RoleMenu>();
        for (Long menuId : role.getMenuIds()) {
            RoleMenu rm = new RoleMenu();
            rm.setRoleId(role.getRoleId());
            rm.setMenuId(menuId);
            list.add(rm);
        }
        if (list.size() > 0) {
            rows = roleMenuMapper.batchRoleMenu(list);
        }
        return rows;
    }

    /**
     * 新增角色部门信息(数据权限)
     *
     * @param role 角色对象
     */
    public int insertRoleDept(Role role) {
        int rows = 1;
        // 新增角色与部门（数据权限）管理
        List<RoleDept> list = new ArrayList<RoleDept>();
        for (Long deptId : role.getDeptIds()) {
            RoleDept rd = new RoleDept();
            rd.setRoleId(role.getRoleId());
            rd.setDeptId(deptId);
            list.add(rd);
        }
        if (list.size() > 0) {
            rows = roleDeptMapper.batchRoleDept(list);
        }
        return rows;
    }

    /**
     * 通过角色ID删除角色
     *
     * @param roleId 角色ID
     * @return 结果
     */
    @Override
    public int deleteRoleById(Long roleId) {
        return roleMapper.deleteRoleById(roleId);
    }

    /**
     * 批量删除角色信息
     *
     * @param roleIds 需要删除的角色ID
     * @return 结果
     */
    @Override
    public int deleteRoleByIds(Long[] roleIds) {
        for (Long roleId : roleIds) {
            checkRoleAllowed(new Role(roleId));
            Role role = selectRoleById(roleId);
            if (countUserRoleByRoleId(roleId) > 0) {
                throw new CustomException(String.format("%1$s已分配,不能删除", role.getRoleName()));
            }
        }
        return roleMapper.deleteRoleByIds(roleIds);
    }
}
