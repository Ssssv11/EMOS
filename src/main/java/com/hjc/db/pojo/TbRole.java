package com.hjc.db.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class TbRole implements Serializable {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 权限集合
     */
    private Object permissions;

    private static final long serialVersionUID = 1L;
}