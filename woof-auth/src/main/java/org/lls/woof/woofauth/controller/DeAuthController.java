package org.lls.woof.woofauth.controller;

import org.lls.woof.woofauth.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/deAuth")
public class DeAuthController {

    private final UserService userService;

    public DeAuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/logout")
    public String logout(@RequestParam String userId, @RequestParam String jwtToken) {
        userService.logout(userId, jwtToken);

        return "logout";
    }
}
