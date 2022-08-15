package com.hjc.db.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class TbModule implements Serializable {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 模块编号
     */
    private String moduleCode;

    /**
     * 模块名称
     */
    private String moduleName;

    private static final long serialVersionUID = 1L;
}