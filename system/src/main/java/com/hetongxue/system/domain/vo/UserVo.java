package com.hetongxue.system.domain.vo;

import com.hetongxue.system.domain.vo.permission.PermissionVo;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 用户Vo
 * @ClassNmae: UserVo
 * @Author: 何同学
 * @DateTime: 2022-07-09 14:48
 */
public class UserVo implements Serializable {
    private Long id;// 用户ID
    private String username;// 用户名
    private String password;// 用户密码
    private String nickName;// 用户昵称
    private String realName;// 真实姓名
    private String phone;// 用户电话
    private String email;// 用户邮箱
    private String gender;// 用户性别(0:男(默认) 1:女 2:保密)
    private String Introduction;// 用户简介
    private String avatarPath;// 用户头像地址
    private boolean status;// 账户状态(0:不可用 1:可用(默认))
    private List<PermissionVo> permission;// 权限信息
}