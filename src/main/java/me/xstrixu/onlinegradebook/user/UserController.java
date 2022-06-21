package me.xstrixu.onlinegradebook.user;

import me.xstrixu.onlinegradebook.auth.AuthFacade;
import me.xstrixu.onlinegradebook.user.details.ManagementUserDetails;
import me.xstrixu.onlinegradebook.user.dto.ManagementLoginUserDto;
import me.xstrixu.onlinegradebook.user.dto.UserDto;
import me.xstrixu.onlinegradebook.user.dto.UserLoginDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
record UserController(UserService userService, AuthFacade authFacade) {

    @PostMapping("/login")
    UserDto login(
            HttpServletResponse response,
            @Valid @RequestBody UserLoginDto userLoginDto
    ) {
        authFacade.login(response, userLoginDto);

        return userService.findByEmail(userLoginDto.getEmail());
    }

    @PostMapping("/management/login")
    void managementLogin(
            HttpServletResponse response,
            @Valid @RequestBody ManagementLoginUserDto managementLoginUserDto
    ) {
        authFacade.managementLogin(response, managementLoginUserDto);
    }

    @PostMapping("/logout")
    void logout(HttpServletResponse response) {
        authFacade.logout(response);
    }

    @GetMapping("/me")
    ResponseEntity<Object> getAuthenticatedUserInfo(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (userDetails instanceof ManagementUserDetails) {
            return new ResponseEntity<>(HttpStatus.OK);
        }

        UserDto userDto = userService.findByEmail(userDetails.getUsername());

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
}
