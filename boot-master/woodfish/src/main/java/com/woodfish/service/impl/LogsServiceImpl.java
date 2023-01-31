package com.woodfish.service.impl;

import com.woodfish.entity.Logs;
import com.woodfish.mapper.LogsMapper;
import com.woodfish.service.ILogsService;
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
public class LogsServiceImpl extends ServiceImpl<LogsMapper, Logs> implements ILogsService {

}
