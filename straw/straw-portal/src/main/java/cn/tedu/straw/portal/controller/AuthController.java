package cn.tedu.straw.portal.controller;


import cn.tedu.straw.commons.model.User;
import cn.tedu.straw.portal.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/v1/auth")
@Slf4j
public class AuthController {
    @Resource
    IUserService userService;

    @GetMapping("/user")
    public User getUser(String username) {
        return userService.getUserByUsername(username);
    }

}
