package com.hetongxue.system.domain.vo.permission;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 路由VO
 * @ClassNmae: RouterVo
 * @Author: 何同学
 * @DateTime: 2022-07-16 22:36
 */
@Data
@Accessors(chain = true)
public class RouterVo implements Serializable {

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
    private List<RouterVo> children;


    @Data
    @Accessors(chain = true)
    public static class MetaVo implements Serializable {

        /**
         * 路由标题
         */
        private String title;
        /**
         * 路由图标
         */
        private String icon;
        /**
         * 是否缓存
         */
        private Boolean keepAlive;
        /**
         * 是否需要权限
         */
        private Boolean requireAuth;
        /**
         * 权限信息
         */
        private String[] roles;

    }

}