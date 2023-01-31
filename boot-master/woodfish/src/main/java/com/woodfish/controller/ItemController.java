package com.woodfish.controller;

import com.woodfish.common.Result;
import com.woodfish.entity.Item;
import com.woodfish.mapper.ItemMapper;
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
@RequestMapping("/item")
public class ItemController {
    @Autowired
    ItemMapper itemMapper;

    @GetMapping("/{id}")
    public Result query(@PathVariable int id) {
        Item item = itemMapper.selectById(id);
        return Result.success(item);
    }

    @GetMapping("/list")
    public Result queryAll() {
        List<Item> items = itemMapper.selectList(null);
        return Result.success(items);
    }

    @PutMapping()
    public Result modify(@RequestBody Item item) {
        itemMapper.updateById(item);
        return Result.success("修改成功");
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable int id) {
        itemMapper.deleteById(id);
        return Result.success("删除成功");
    }

    @PostMapping
    public Result save(@RequestBody Map<String,String> map) {

        Item item = new Item();
        item.setName(map.get("name"));
        try{
            if(map.get("cost").length() >= 10) return Result.error("花费应该是int类型");
            int cost = Integer.parseInt(map.get("cost"));
            item.setCost(cost);
        } catch (Exception e){
            return Result.error("花费应该是int类型");
        }
        item.setDescription(map.get("description"));
        item.setCreatetime(LocalDateTime.now());
        itemMapper.insert(item);
        return Result.success("添加成功");
    }
}
