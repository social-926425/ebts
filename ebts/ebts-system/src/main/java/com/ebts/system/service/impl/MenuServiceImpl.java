package com.ebts.system.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.ebts.common.constant.ReturnConstants;
import com.ebts.common.exception.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ebts.common.constant.UserConstants;
import com.ebts.common.core.entity.TreeSelect;
import com.ebts.common.core.entity.entity.Menu;
import com.ebts.common.core.entity.entity.Role;
import com.ebts.common.core.entity.entity.User;
import com.ebts.common.utils.SecurityUtils;
import com.ebts.common.utils.StringUtils;
import com.ebts.system.entity.vo.MetaVo;
import com.ebts.system.entity.vo.RouterVo;
import com.ebts.system.dao.MenuDao;
import com.ebts.system.dao.RoleDao;
import com.ebts.system.dao.RoleMenuDao;
import com.ebts.system.service.MenuService;

/**
 * 菜单 业务层处理
 *
 * @author binlin
 */
@Service
public class MenuServiceImpl implements MenuService {
    private Logger logger = LoggerFactory.getLogger(MenuServiceImpl.class);

    public static final String PREMISSION_STRING = "perms[\"{0}\"]";

    @Autowired
    private MenuDao menuMapper;

    @Autowired
    private RoleDao roleMapper;

    @Autowired
    private RoleMenuDao roleMenuMapper;

    /**
     * 根据用户查询系统菜单列表
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    @Override
    public List<Menu> selectMenuList(Long userId) {
        try {
            return selectMenuList(new Menu(), userId);
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 查询系统菜单列表
     *
     * @param menu 菜单信息
     * @return 菜单列表
     */
    @Override
    public List<Menu> selectMenuList(Menu menu, Long userId) {
        try {
            List<Menu> menuList = null;
            // 管理员显示所有菜单信息
            if (User.isAdmin(userId)) {
                menuList = menuMapper.selectMenuList(menu);
            } else {
                menu.getParams().put("userId", userId);
                menuList = menuMapper.selectMenuListByUserId(menu);
            }
            return menuList;
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
    public Set<String> selectMenuPermsByUserId(Long userId) {
        try {
            List<String> perms = menuMapper.selectMenuPermsByUserId(userId);
            Set<String> permsSet = new HashSet<>();
            for (String perm : perms) {
                if (StringUtils.isNotEmpty(perm)) {
                    permsSet.addAll(Arrays.asList(perm.trim().split(",")));
                }
            }
            return permsSet;
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 根据用户ID查询菜单
     *
     * @param userId 用户名称
     * @return 菜单列表
     */
    @Override
    public List<Menu> selectMenuTreeByUserId(Long userId) {
        try {
            List<Menu> menus = null;
            if (SecurityUtils.isAdmin(userId)) {
                menus = menuMapper.selectMenuTreeAll();
            } else {
                menus = menuMapper.selectMenuTreeByUserId(userId);
            }
            return getChildPerms(menus, 0);
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 根据角色ID查询菜单树信息
     *
     * @param roleId 角色ID
     * @return 选中菜单列表
     */
    @Override
    public List<Integer> selectMenuListByRoleId(Long roleId) {
        try {
            Role role = roleMapper.selectRoleById(roleId);
            return menuMapper.selectMenuListByRoleId(roleId, role.isMenuCheckStrictly());
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 构建前端路由所需要的菜单
     *
     * @param menus 菜单列表
     * @return 路由列表
     */
    @Override
    public List<RouterVo> buildMenus(List<Menu> menus) {
        try {
            List<RouterVo> routers = new LinkedList<RouterVo>();
            for (Menu menu : menus) {
                RouterVo router = new RouterVo();
                router.setHidden("1".equals(menu.getVisible()));
                router.setName(getRouteName(menu));
                router.setPath(getRouterPath(menu));
                router.setComponent(getComponent(menu));
                router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), StringUtils.equals("1", menu.getIsCache())));
                List<Menu> cMenus = menu.getChildren();
                if (!cMenus.isEmpty() && cMenus.size() > 0 && UserConstants.TYPE_DIR.equals(menu.getMenuType())) {
                    router.setAlwaysShow(true);
                    router.setRedirect("noRedirect");
                    router.setChildren(buildMenus(cMenus));
                } else if (isMeunFrame(menu)) {
                    List<RouterVo> childrenList = new ArrayList<RouterVo>();
                    RouterVo children = new RouterVo();
                    children.setPath(menu.getPath());
                    children.setComponent(menu.getComponent());
                    children.setName(StringUtils.capitalize(menu.getPath()));
                    children.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), StringUtils.equals("1", menu.getIsCache())));
                    childrenList.add(children);
                    router.setChildren(childrenList);
                }
                routers.add(router);
            }
            return routers;
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 构建前端所需要树结构
     *
     * @param menus 菜单列表
     * @return 树结构列表
     */
    @Override
    public List<Menu> buildMenuTree(List<Menu> menus) {
        try {
            List<Menu> returnList = new ArrayList<Menu>();
            List<Long> tempList = new ArrayList<Long>();
            for (Menu dept : menus) {
                tempList.add(dept.getMenuId());
            }
            for (Iterator<Menu> iterator = menus.iterator(); iterator.hasNext(); ) {
                Menu menu = (Menu) iterator.next();
                // 如果是顶级节点, 遍历该父节点的所有子节点
                if (!tempList.contains(menu.getParentId())) {
                    recursionFn(menus, menu);
                    returnList.add(menu);
                }
            }
            if (returnList.isEmpty()) {
                returnList = menus;
            }
            return returnList;
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 构建前端所需要下拉树结构
     *
     * @param menus 菜单列表
     * @return 下拉树结构列表
     */
    @Override
    public List<TreeSelect> buildMenuTreeSelect(List<Menu> menus) {
        try {
            List<Menu> menuTrees = buildMenuTree(menus);
            return menuTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 根据菜单ID查询信息
     *
     * @param menuId 菜单ID
     * @return 菜单信息
     */
    @Override
    public Menu selectMenuById(Long menuId) {
        try {
            return menuMapper.selectMenuById(menuId);
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 是否存在菜单子节点
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    @Override
    public boolean hasChildByMenuId(Long menuId) {
        try {
            int result = menuMapper.hasChildByMenuId(menuId);
            return result > 0 ? true : false;
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 查询菜单使用数量
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    @Override
    public boolean checkMenuExistRole(Long menuId) {
        try {
            int result = roleMenuMapper.checkMenuExistRole(menuId);
            return result > 0 ? true : false;
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 新增保存菜单信息
     *
     * @param menu 菜单信息
     * @return 结果
     */
    @Override
    public int insertMenu(Menu menu) {
        try {
            return menuMapper.insertMenu(menu);
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 修改保存菜单信息
     *
     * @param menu 菜单信息
     * @return 结果
     */
    @Override
    public int updateMenu(Menu menu) {
        try {
            return menuMapper.updateMenu(menu);
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 删除菜单管理信息
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    @Override
    public int deleteMenuById(Long menuId) {
        try {
            return menuMapper.deleteMenuById(menuId);
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 校验菜单名称是否唯一
     *
     * @param menu 菜单信息
     * @return 结果
     */
    @Override
    public String checkMenuNameUnique(Menu menu) {
        try {
            Long menuId = StringUtils.isNull(menu.getMenuId()) ? -1L : menu.getMenuId();
            Menu info = menuMapper.checkMenuNameUnique(menu.getMenuName(), menu.getParentId());
            if (StringUtils.isNotNull(info) && info.getMenuId().longValue() != menuId.longValue()) {
                return UserConstants.NOT_UNIQUE;
            }
            return UserConstants.UNIQUE;
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 获取路由名称
     *
     * @param menu 菜单信息
     * @return 路由名称
     */
    public String getRouteName(Menu menu) {
        String routerName = StringUtils.capitalize(menu.getPath());
        // 非外链并且是一级目录（类型为目录）
        if (isMeunFrame(menu)) {
            routerName = StringUtils.EMPTY;
        }
        return routerName;
    }

    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    public String getRouterPath(Menu menu) {
        String routerPath = menu.getPath();
        // 非外链并且是一级目录（类型为目录）
        if (0 == menu.getParentId().intValue() && UserConstants.TYPE_DIR.equals(menu.getMenuType())
                && UserConstants.NO_FRAME.equals(menu.getIsFrame())) {
            routerPath = "/" + menu.getPath();
        }
        // 非外链并且是一级目录（类型为菜单）
        else if (isMeunFrame(menu)) {
            routerPath = "/";
        }
        return routerPath;
    }

    /**
     * 获取组件信息
     *
     * @param menu 菜单信息
     * @return 组件信息
     */
    public String getComponent(Menu menu) {
        String component = UserConstants.LAYOUT;
        if (StringUtils.isNotEmpty(menu.getComponent()) && !isMeunFrame(menu)) {
            component = menu.getComponent();
        }
        return component;
    }

    /**
     * 是否为菜单内部跳转
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isMeunFrame(Menu menu) {
        return menu.getParentId().intValue() == 0 && UserConstants.TYPE_MENU.equals(menu.getMenuType())
                && menu.getIsFrame().equals(UserConstants.NO_FRAME);
    }

    /**
     * 根据父节点的ID获取所有子节点
     *
     * @param list     分类表
     * @param parentId 传入的父节点ID
     * @return String
     */
    public List<Menu> getChildPerms(List<Menu> list, int parentId) {
        List<Menu> returnList = new ArrayList<Menu>();
        for (Iterator<Menu> iterator = list.iterator(); iterator.hasNext(); ) {
            Menu t = (Menu) iterator.next();
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (t.getParentId() == parentId) {
                recursionFn(list, t);
                returnList.add(t);
            }
        }
        return returnList;
    }

    /**
     * 递归列表
     *
     * @param list
     * @param t
     */
    private void recursionFn(List<Menu> list, Menu t) {
        // 得到子节点列表
        List<Menu> childList = getChildList(list, t);
        t.setChildren(childList);
        for (Menu tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<Menu> getChildList(List<Menu> list, Menu t) {
        List<Menu> tlist = new ArrayList<Menu>();
        Iterator<Menu> it = list.iterator();
        while (it.hasNext()) {
            Menu n = (Menu) it.next();
            if (n.getParentId().longValue() == t.getMenuId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<Menu> list, Menu t) {
        return getChildList(list, t).size() > 0 ? true : false;
    }
}
