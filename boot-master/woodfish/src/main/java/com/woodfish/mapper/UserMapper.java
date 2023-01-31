package com.woodfish.mapper;

import com.woodfish.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author sheng
 * @since 2023-01-14
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
