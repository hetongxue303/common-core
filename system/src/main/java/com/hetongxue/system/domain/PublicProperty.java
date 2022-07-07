package com.hetongxue.system.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 公共属性
 * @ClassNmae: PublicProperty
 * @Author: 何同学
 * @DateTime: 2022-07-07 14:11
 */
@Data
public class PublicProperty implements Serializable {

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;// 创建时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;// 更新时间

}