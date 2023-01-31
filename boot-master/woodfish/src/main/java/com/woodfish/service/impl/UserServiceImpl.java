package com.woodfish.service.impl;

import com.woodfish.common.Result;
import com.woodfish.entity.User;
import com.woodfish.mapper.UserMapper;
import com.woodfish.service.IUserService;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
