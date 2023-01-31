package com.woodfish.controller;

import com.woodfish.common.Result;
import com.woodfish.entity.User;
import com.woodfish.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author sheng
 * @since 2023-01-14
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserMapper userMapper;

    @GetMapping("/{id}")
    public Result query(@PathVariable int id){
        User user = userMapper.selectById(id);
        return Result.success(user);
    }

    @GetMapping("/list")
    public Result queryAll(){
        List<User> users = userMapper.selectList(null);
        return Result.success(users);
    }

    @PutMapping()
    public Result modify(@RequestBody User user){
        userMapper.updateById(user);
        return Result.success("修改成功");
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable int id){
        userMapper.deleteById(id);
        return Result.success("删除成功");
    }

    @PostMapping
    public Result save(@RequestBody User user){
        userMapper.insert(user);
        return Result.success("添加成功");
    }


}
