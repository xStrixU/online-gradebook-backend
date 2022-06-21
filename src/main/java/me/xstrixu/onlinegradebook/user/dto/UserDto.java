package me.xstrixu.onlinegradebook.user.dto;

import me.xstrixu.onlinegradebook.user.enums.UserRole;

import java.util.Set;

public record UserDto(String firstName, String lastName, String email, Set<UserRole> roles) {

}
