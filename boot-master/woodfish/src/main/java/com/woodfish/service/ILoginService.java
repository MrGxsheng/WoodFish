package com.woodfish.service;

import com.woodfish.entity.Logs;
import com.baomidou.mybatisplus.extension.service.IService;
import com.woodfish.entity.User;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sheng
 * @since 2023-01-14
 */
public interface ILoginService  {
    public Map<String , String > getToken(String username , String password);
}
