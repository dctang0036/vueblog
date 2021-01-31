package com.vue.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.vue.api.constant.Result;
import com.vue.api.entity.User;
import com.vue.api.dao.UserMapper;
import com.vue.api.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vue.api.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jws.soap.SOAPBinding;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dctang
 * @since 2021-01-29
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User getUserByName(String username) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username)
                .last("limit 1");
        User user = userMapper.selectOne(lambdaQueryWrapper);

        log.info("User real name {}.", user.getUsername());
        return user;
    }

    @Override
    public Result checkUserIsEffective(User user) {
        Result<Object> result = new Result<>();

        // 情况1：根据用户信息查询，该用户不存在
        if (CommonUtils.isEmpty(user)) {
            result.error500("该用户不存在，请注册");
            return result;
        }

        // 情况2：根据用户信息查询，该用户已注销
        if (user.getStatus() == 1) {
            result.error500("该用户已注销");
            return result;
        }

        // 情况3： 根据用户信息查询，该用户已删除
        if (user.getStatus() == 2) {
            result.error500("该用户已被删除");
            return result;
        }

        return result;
    }
}
