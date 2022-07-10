package com.hetongxue.system.domain.vo.permission;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 路由VO
 * @ClassNmae: RouterVo
 * @Author: 何同学
 * @DateTime: 2022-07-09 14:52
 */
@Data
@Accessors(chain = true)
public class PermissionVo implements Serializable {

    /**
     * 路由名称
     */
    private String name;
    /**
     * 路由地址
     */
    private String path;
    /**
     * 路由组件
     */
    private String component;
    /**
     * meta信息
     */
    private MetaVo meta;
    /**
     * 子菜单
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<PermissionVo> children;

}