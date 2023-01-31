package com.woodfish.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.woodfish.entity.User;
import com.woodfish.mapper.UserMapper;
import com.woodfish.service.IRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class RegisterServiceImpl implements IRegisterService {


    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Map<String, String> register(String username, String password) {
        Map<String, String> map = new HashMap<>();
        System.out.println(username + " " + password);
        if (username == null) {
            map.put("error_message", "name is null");
            return map;
        }

        if (password == null) {
            map.put("error_message", "password is null");
            return map;
        }

        username = username.trim();

        if (username.length() == 0) {
            map.put("error_message", "name is null");
            return map;
        }

        if (username.length() > 100) {
            map.put("erroe_message", "name length > 100");
            return map;
        }

        if (password.length() == 0) {
            map.put("error_message", "password is null");
            return map;
        }

        if (password.length() > 100) {
            map.put("error_message", "password length > 100");
            return map;
        }



        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        List<User> list = userMapper.selectList(queryWrapper);
        if (!list.isEmpty()) {
            map.put("error_message", "name is existence");
            return map;
        }

        String encodedPassword = passwordEncoder.encode(password);
        System.out.println(encodedPassword);
        User user = new User(null, username, encodedPassword,0,0,LocalDateTime.now(),"未知");
        userMapper.insert(user);

        map.put("error_message", "success");

        return map;
    }
}



