package com.hetongxue.system.domain.vo.permission;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Description: meta信息
 * @ClassNmae: Meta
 * @Author: 何同学
 * @DateTime: 2022-07-09 14:55
 */
@Data
@Accessors(chain = true)
public class MetaVo implements Serializable {

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

}