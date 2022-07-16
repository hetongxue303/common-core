package com.hetongxue.security;

import com.hetongxue.system.domain.Permission;
import com.hetongxue.system.domain.User;
import com.hetongxue.system.domain.vo.permission.MetaVo;
import com.hetongxue.system.domain.vo.permission.PermissionVo;
import com.hetongxue.system.domain.vo.permission.RouterVo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @Description: Security工具类
 * @ClassNmae: SecurityUtils
 * @Author: 何同学
 * @DateTime: 2022-07-10 13:05
 */
public class SecurityUtils {

    /**
     * 生成树
     */
    public static List<PermissionVo> generatePermission(List<Permission> permissions, Long parentId) {
        ArrayList<PermissionVo> list = new ArrayList<>();
        Optional.ofNullable(permissions)
                .orElse(new ArrayList<Permission>())
                .stream()
                .filter(item -> item != null && Objects.equals(item.getParentId(), parentId) && item.getMenuType() != 3)
                .forEach(item -> {
                    list.add(new PermissionVo().setName(item.getName())
                            .setPath(item.getPath())
                            .setComponent(item.getComponentPath())
                            .setMeta(new MetaVo()
                                    .setTitle(item.getTitle())
                                    .setIcon(item.getMenuIcon())
                                    .setKeepAlive(true)
                                    .setRequireAuth(true))
                            .setChildren(generatePermission(permissions, item.getId())));
                });
        return list;
    }

    /**
     * 生成权限编码
     */
    public static List<GrantedAuthority> generateAuthority(List<Permission> permissions) {
        return AuthorityUtils.createAuthorityList(
                permissions.stream()
                        // 过滤不为空的
                        .filter(Objects::nonNull)
                        // 拿到权限编码
                        .map(Permission::getMenuPermission)
                        // 再次过滤不为空的
                        .filter(Objects::nonNull)
                        // 转换为数组
                        .toArray(String[]::new));
    }

    /**
     * 获取用户信息
     */
    public static User getUserInfo() {
        return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }

    /**
     * 生成路由
     */
    public static List<RouterVo> generateRouter(List<Permission> permissions, Long parentId) {
        ArrayList<RouterVo> routerList = new ArrayList<>();
        // 判断是否为空
        Optional.ofNullable(permissions)
                // 不为空时新建一个数组
                .orElse(new ArrayList<Permission>())
                // 转换成流
                .stream()
                // 过滤不为空的和对应父ID的数据 以及类型不为3的
                .filter(item -> item != null && Objects.equals(item.getParentId(), parentId) && item.getMenuType() != 3)
                // 遍历循环
                .forEach(item -> {
                    routerList.add(new RouterVo()
                            .setName(item.getName())
                            .setPath(item.getPath())
                            .setComponent(item.getComponentPath())
                            .setMeta(new RouterVo.MetaVo()
                                    .setTitle(item.getTitle())
                                    .setIcon(item.getMenuIcon())
                                    .setKeepAlive(true)
                                    .setRequireAuth(true)
                                    // 当类型是目录时 不存在权限代码
                                    .setRoles(item.getMenuType() != 1 ? permissions.stream()
                                            // 过滤权限代码不为空且不能是目录
                                            .filter(strip -> strip.getMenuPermission() != null)
                                            // 过滤父ID等于当前ID的数据(此时不存在list权限 若要存在list权限 则过滤排序顺序一致的数据即可)
                                            .filter(strip -> Objects.equals(strip.getParentId(), item.getId()))
                                            // 获得权限编码
                                            .map(Permission::getMenuPermission)
                                            // 生成string数组
                                            .toArray(String[]::new) : null))
                            .setChildren(generateRouter(permissions, item.getId())));
                });
        return routerList;
    }

}