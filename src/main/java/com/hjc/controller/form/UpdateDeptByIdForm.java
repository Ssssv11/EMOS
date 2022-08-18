package com.hjc.controller.form;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel
@Data
public class UpdateDeptByIdForm {
    @NotNull
    @Min(1)
    private Integer deptId;

    @NotNull
    @NotBlank
    private String deptName;
}
