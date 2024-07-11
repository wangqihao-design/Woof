package org.lls.woof.woofauth.controller;



import jakarta.servlet.http.HttpServletRequest;
import org.lls.woof.woofauth.entity.dto.User;
import org.lls.woof.woofauth.service.UserService;
import org.lls.woof.woofauth.utils.JwtUtils;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class TestController {

    private final UserService userService;

    public TestController(JwtUtils jwtUtils, UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password, HttpServletRequest request) {
        return userService.login(username, password, request.getRemoteAddr());
    }

    @PostMapping("/register")
    public String register(@RequestBody User user) {

        userService.register(user);

        return "1";
    }

}
