package com.hetongxue.system.domain.vo;

import com.hetongxue.system.domain.vo.permission.MenuVo;
import com.hetongxue.system.domain.vo.permission.RouterVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    private List<RouterVo> routers = new ArrayList<>();
    private List<MenuVo> menus = new ArrayList<>();
    private Collection<? extends GrantedAuthority> authorities;

}