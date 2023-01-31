package com.woodfish.controller;

import com.woodfish.common.Result;
import com.woodfish.entity.Platform;
import com.woodfish.mapper.PlatformMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author sheng
 * @since 2023-01-14
 */
@RestController
@RequestMapping("/platform")
public class PlatformController {

    @Autowired
    PlatformMapper platformMapper;

    @GetMapping("/{id}")
    public Result query(@PathVariable int id) {
        Platform platform = platformMapper.selectById(id);
        return Result.success(platform);
    }

    @GetMapping()
    public Result queryAll() {
        List<Platform> platforms = platformMapper.selectList(null);
        return Result.success(platforms);
    }

    @PutMapping()
    public Result modify(@RequestBody Platform platform) {
        platformMapper.updateById(platform);
        return Result.success("修改成功");
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable int id) {
        platformMapper.deleteById(id);
        return Result.success("删除成功");
    }

    @PostMapping
    public Result save(@RequestBody Platform platform) {
        platformMapper.insert(platform);
        return Result.success("添加成功");
    }
}
