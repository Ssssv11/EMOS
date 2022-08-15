package com.hjc.db.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class TbHolidays implements Serializable {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 日期
     */
    private Date date;

    private static final long serialVersionUID = 1L;
}