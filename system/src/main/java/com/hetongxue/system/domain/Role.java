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
 * @Description: 角色实体
 * @ClassNmae: Role
 * @Author: 何同学
 * @DateTime: 2022-07-07 14:10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("sys_role")
public class Role extends PublicProperty implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;// 角色ID
    private String name;// 角色名称
    private String illustrate;// 角色说明

}