package com.hjc.controller.form;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel
public class AddRoleForm {
    @NotBlank
    private String roleName;

    @NotNull
    private List permissions;
}
