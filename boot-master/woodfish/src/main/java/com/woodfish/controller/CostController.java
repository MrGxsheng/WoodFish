package com.woodfish.controller;

import com.woodfish.common.Result;
import com.woodfish.entity.Cost;
import com.woodfish.mapper.CostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/list")
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
    public Result save(@RequestBody Map<String , String > map){
        Cost cost = new Cost();
        cost.setCost(Integer.parseInt(map.get("cost")));
        cost.setCreatetime(LocalDateTime.now());
        cost.setItemid(Integer.parseInt(map.get("itemId")));
        cost.setPlatformid(Integer.parseInt(map.get("platformId")));
        cost.setUserid(Integer.parseInt(map.get("userId")));
        costMapper.insert(cost);
        return Result.success("添加成功");
    }
}
