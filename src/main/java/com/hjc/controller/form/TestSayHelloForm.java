package com.hjc.controller.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Validation 后端验证 Form 封装类
 */
@ApiModel
@Data
public class TestSayHelloForm {
//    @NotBlank
//    @Pattern(regexp = "^[\\u4e00-\\u9fa5]{2,15}$", message = "不符合正则表达式")
    @ApiModelProperty("姓名")
    private String name;
}