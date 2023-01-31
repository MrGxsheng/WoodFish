package com.woodfish.service.impl;

import com.woodfish.entity.Item;
import com.woodfish.mapper.ItemMapper;
import com.woodfish.service.IItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author sheng
 * @since 2023-01-14
 */
@Service
public class ItemServiceImpl extends ServiceImpl<ItemMapper, Item> implements IItemService {

}
