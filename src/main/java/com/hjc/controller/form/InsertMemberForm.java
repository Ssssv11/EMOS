package com.hjc.controller.form;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel
public class InsertMemberForm {
    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @NotBlank
    private String sex;

    private String tel;

    private String email;

    @NotNull
    @NotBlank
    private String hiredate;

    @NotNull
    private Long deptId;

}
