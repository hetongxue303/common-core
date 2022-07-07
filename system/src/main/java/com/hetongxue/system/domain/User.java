package com.hetongxue.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Description: 用户实体
 * @ClassNmae: User
 * @Author: 何同学
 * @DateTime: 2022-07-04 16:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("sys_user")
public class User extends PublicProperty implements Serializable {

    @TableId(type = IdType.AUTO)
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

}