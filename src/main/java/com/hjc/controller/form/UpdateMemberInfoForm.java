package com.hjc.controller.form;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel
public class UpdateMemberInfoForm {
    @NotNull
    private Integer id;

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @NotBlank
    private String sex;

    @NotNull
    @NotBlank
    private String tel;

    @NotNull
    @NotBlank
    private String email;

    @NotNull
    @NotBlank
    private String hiredate;

    @NotNull
    private Long deptId;
}
