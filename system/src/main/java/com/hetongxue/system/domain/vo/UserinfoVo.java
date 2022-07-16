package com.hetongxue.system.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Description: 用户信息VO
 * @ClassNmae: UserinfoVo
 * @Author: 何同学
 * @DateTime: 2022-07-16 22:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UserinfoVo implements Serializable {

    private Long id;
    private String username;
    private String nickName;
    private String realName;
    private String phone;
    private String email;
    private String gender;
    private String Introduction;
    private String avatarPath;
    private boolean status;
    private Object[] roles;

}