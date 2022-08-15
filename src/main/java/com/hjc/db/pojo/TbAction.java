package com.hjc.db.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class TbAction implements Serializable {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 行为编号
     */
    private String actionCode;

    /**
     * 行为名称
     */
    private String actionName;

    private static final long serialVersionUID = 1L;
}