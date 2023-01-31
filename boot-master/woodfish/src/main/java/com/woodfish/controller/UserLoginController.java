package com.woodfish.controller;

import com.woodfish.common.Result;
import com.woodfish.service.ILoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserLoginController {

    @Autowired
    private ILoginService loginService;

    @PostMapping("/user/login")
    public Result getToke(@RequestBody Map<String, String> map){
        String username = map.get("username");
        String password = map.get("password");

        Map<String, String> pas = loginService.getToken(username, password);
        if(pas.get("error_message").equals("success")){
            pas.put("username",username);
            return Result.success("登录成功",pas);
        }

        return Result.error();
    }

}
