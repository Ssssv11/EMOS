package com.hjc.controller.form;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel
public class UpdateUserInfoForm {
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
}
