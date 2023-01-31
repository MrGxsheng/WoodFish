package com.woodfish.controller;

import com.woodfish.common.Result;
import com.woodfish.entity.Cost;
import com.woodfish.mapper.CostMapper;
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
@RequestMapping("/cost")
public class CostController {

    @Autowired
    CostMapper costMapper;

    @GetMapping("/{id}")
    public Result query(@PathVariable int id){
        Cost cost = costMapper.selectById(id);
        return Result.success(cost);
    }

    @GetMapping()
    public Result queryAll(){
        List<Cost> costs = costMapper.selectList(null);
        return Result.success(costs);
    }

    @PutMapping()
    public Result modify(@RequestBody Cost cost){
        costMapper.updateById(cost);
        return Result.success("修改成功");
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable int id){
        costMapper.deleteById(id);
        return Result.success("删除成功");
    }

    @PostMapping
    public Result save(@RequestBody Cost cost){
        costMapper.insert(cost);
        return Result.success("添加成功");
    }
}
