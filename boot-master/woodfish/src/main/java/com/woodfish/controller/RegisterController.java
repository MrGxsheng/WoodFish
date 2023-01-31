package com.woodfish.controller;

import com.woodfish.common.Result;
import com.woodfish.service.IRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RegisterController {

    @Autowired
    private IRegisterService registerService;

    @PostMapping("/user/reg/")
    public Result register(@RequestBody Map<String,String> map){
        String username = map.get("username");
        String password = map.get("password");

        Map<String, String> ans = registerService.register(username, password);


        if(ans.get("error_message").equals("success")){
            return Result.success("注册成功");
        }else{
            return Result.error(ans.get("error_message"));
        }
    }
}
