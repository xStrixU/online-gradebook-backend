package me.xstrixu.onlinegradebook.user.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ManagementLoginUserDto {

    @NotNull
    private String username;

    @NotNull
    private String password;
}
