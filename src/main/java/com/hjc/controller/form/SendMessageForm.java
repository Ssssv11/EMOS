package com.hjc.controller.form;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;

@Data
@ApiModel
public class SendMessageForm {

    @NotBlank
    private String text;

    @NotNull
    private ArrayList<Integer> members;
}
