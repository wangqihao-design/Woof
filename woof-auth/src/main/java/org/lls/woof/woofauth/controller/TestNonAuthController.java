package org.lls.woof.woofauth.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.lls.woof.woofauth.service.UserService;
import org.lls.woof.woofauth.service.impl.RedisTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestNonAuthController {

    public TestNonAuthController(RedisTokenService redisTokenService, UserService userService) {
        this.redisTokenService = redisTokenService;
        this.userService = userService;
    }

    @RequestMapping("/test")
    public String test() {
        return "test";
    }

    private final RedisTokenService redisTokenService;

    @RequestMapping("/redis")
    public String redis(@RequestParam String userId, @RequestParam String token, HttpServletRequest httpServletRequest) {

        redisTokenService.addToSet(userId, token);
        redisTokenService.addUserTokenToHash(userId, "Charlie", "1977-1-23", "79.123.82.1");
        return "ok";
    }

    private final UserService userService;

    @PostMapping("/logout")
    public String logout(@RequestParam String userId, @RequestParam String jwtToken) {
        userService.logout(userId, jwtToken);

        return "ok";
    }
}
