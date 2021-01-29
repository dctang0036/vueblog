package com.vue.api.service.impl;

import com.vue.api.entity.User;
import com.vue.api.dao.UserMapper;
import com.vue.api.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dctang
 * @since 2021-01-29
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
