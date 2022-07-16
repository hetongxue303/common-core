package com.hetongxue.system.controller;

import com.hetongxue.aop.log.LogAnnotation;
import com.hetongxue.response.Result;
import com.hetongxue.system.domain.Permission;
import com.hetongxue.system.domain.User;
import com.hetongxue.system.domain.vo.UserQueryVo;
import com.hetongxue.system.domain.vo.UserinfoVo;
import com.hetongxue.system.service.PermissionService;
import com.hetongxue.system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/**
 * @Description: 用户模块
 * @ClassNmae: UserController
 * @Author: 何同学
 * @DateTime: 2022-07-04 16:54
 */
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PermissionService permissionService;

    @GetMapping("/getUserinfo")
    @LogAnnotation(module = "用户模块", operate = "获取用户信息")
    public Result getUserinfo() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (ObjectUtils.isEmpty(user)) {
            return Result.Error().setMessage("获取用户信息失败");
        }
        List<Permission> permissions = permissionService.selectPermissionByUserId(user.getId());
        Object[] objects = permissions.stream().filter(Objects::nonNull).map(Permission::getMenuPermission).toArray();
        UserinfoVo userinfoVo = new UserinfoVo();
        BeanUtils.copyProperties(user, userinfoVo);
        userinfoVo.setRoles(objects);
        return Result.Success(userinfoVo).setMessage("获取用户信息成功");
    }

    @GetMapping("/list")
    @LogAnnotation(module = "用户列表", operate = "分页获取用户列表")
    public Result getUserList(UserQueryVo userQuery) {
        return Result.Success().setMessage("查询成功").setData(userService.selectUserList(userQuery));
    }

}