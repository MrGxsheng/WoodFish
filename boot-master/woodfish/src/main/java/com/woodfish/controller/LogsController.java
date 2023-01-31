package com.woodfish.controller;

import com.woodfish.common.Result;
import com.woodfish.entity.Logs;
import com.woodfish.mapper.LogsMapper;
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
@RequestMapping("/logs")
public class LogsController {
    @Autowired
    LogsMapper logsMapper;

    @GetMapping("/{id}")
    public Result query(@PathVariable int id) {
        Logs logs = logsMapper.selectById(id);
        return Result.success(logs);
    }

    @GetMapping()
    public Result queryAll() {
        List<Logs> logs = logsMapper.selectList(null);
        return Result.success(logs);
    }

    @PutMapping()
    public Result modify(@RequestBody Logs logs) {
        logsMapper.updateById(logs);
        return Result.success("修改成功");
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable int id) {
        logsMapper.deleteById(id);
        return Result.success("删除成功");
    }

    @PostMapping
    public Result save(@RequestBody Logs logs) {
        logsMapper.insert(logs);
        return Result.success("添加成功");
    }
}
