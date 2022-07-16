package com.hetongxue.system.domain.vo.permission;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 菜单VO
 * @ClassNmae: MenuVo
 * @Author: 何同学
 * @DateTime: 2022-07-16 23:53
 */
@Data
@Accessors(chain = true)
public class MenuVo implements Serializable {

    /**
     * 菜单名称
     */
    private String name;
    /**
     * 菜单地址
     */
    private String path;
    /**
     * 菜单图标
     */
    private String icon;
    /**
     * 子菜单
     */
    private List<MenuVo> children;

}