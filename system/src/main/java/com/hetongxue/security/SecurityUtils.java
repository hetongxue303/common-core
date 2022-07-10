package com.hetongxue.security;

import com.hetongxue.system.domain.Permission;
import com.hetongxue.system.domain.User;
import com.hetongxue.system.domain.vo.permission.MetaVo;
import com.hetongxue.system.domain.vo.permission.PermissionVo;
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
}