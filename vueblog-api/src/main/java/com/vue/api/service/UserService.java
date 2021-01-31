package com.vue.api.service;

import com.vue.api.constant.Result;
import com.vue.api.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dctang
 * @since 2021-01-29
 */
public interface UserService extends IService<User> {

    /**
     * 通过用户账号查询用户
     * @param username
     * @return
     */
    User getUserByName(String username);

    /**
     * 校验用户是否有效
     *
     * @param user
     * @return
     */
    Result checkUserIsEffective(User user);

}
