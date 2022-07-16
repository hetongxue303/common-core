package com.hetongxue.system.domain.vo;

import com.hetongxue.system.domain.User;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 用户查询Vo
 * @ClassNmae: UserQueryVo
 * @Author: 何同学
 * @DateTime: 2022-07-09 14:48
 */
@Data
public class UserQueryVo extends User implements Serializable {

    /**
     * 当前页
     */
    private Long page = 1L;
    /**
     * 页面大小
     */
    private Long size = 10L;

}