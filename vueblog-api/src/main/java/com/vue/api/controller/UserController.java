package com.vue.api.controller;

import com.vue.api.entity.User;
import com.vue.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping("/test/{id}")
    public Object getUser(@PathVariable("id") Long id) {
        User user = userService.getById(id);
        return user;
    }
}
