package com.hetongxue.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hetongxue.system.domain.common.PublicProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Description: 权限实体
 * @ClassNmae: Permission
 * @Author: 何同学
 * @DateTime: 2022-07-07 14:16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("sys_permission")
public class Permission extends PublicProperty implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;// 权限ID
    private String title;// 权限名称/菜单标题
    private Long parentId;// 父ID[默认0]
    private Integer menuType;// 菜单类型(1:菜单[默认] 2:菜单项 3:按钮)
    private Integer menuSort;// 菜单排序
    private String menuIcon;// 菜单图标
    private String menuPermission;// 菜单权限
    private String name;// 路由名称
    private String path;// 路由/菜单地址
    private Boolean cache;// 是否缓存
    private String componentPath;// 组件地址

}