package com.vue.api.config.shiro;

import com.vue.api.config.jwt.JWTToken;
import com.vue.api.constant.CommonConstant;
import com.vue.api.entity.User;
import com.vue.api.service.UserService;
import com.vue.api.utils.CommonUtils;
import com.vue.api.utils.JwtUtil;
import com.vue.api.utils.RedisUtil;
import com.vue.api.utils.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class ShiroRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 必须重写此方法，不然会报错
     * @param token
     * @return
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.info("----身份认证方法----");
        String token = (String) authenticationToken.getCredentials();

        if (CommonUtils.isEmpty(token)) {
            log.info("----身份认证失败----IP地址:  " + CommonUtils.getIpAddrByRequest(SpringContextUtils.getHttpServletRequest()));
            throw new AuthenticationException("token为空!");
        }
        // 检验token有效性
        User loginUser = checkUserTokenIsEffect(token);
        return new SimpleAuthenticationInfo(loginUser, token, getName());
    }


    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        log.info("----权限认证开始----");
        return  null;
    }

    /**
     * 检验token的有效性
     * @param token
     * @return
     * @throws AuthenticationException
     */
    public User checkUserTokenIsEffect(String token) throws AuthenticationException{
        // 解密获得username，用于和数据库进行对比
        String username = jwtUtil.getUsername(token);

        if (CommonUtils.isEmpty(username)) {
            throw new AuthenticationException("token为空！");
        }

        User loginUser = new User();
        // 数据库查询用户信息
        User user = userService.getUserByName(username);
        if (CommonUtils.isEmpty(user)) {
            throw new AuthenticationException("用户不存在!");
        }

        // 检验token是否超时失效 & 刷新token
        if (!jwtTokenRefresh(token, username, user.getPassword())) {
            throw new AuthenticationException("Token失效请重新登录!");
        }

        // 判断用户状态
        if (user.getStatus() != 0) {
            throw new AuthenticationException("账号已被删除,请联系管理员!");
        }

        BeanUtils.copyProperties(user, loginUser);
        return loginUser;
    }

    /**
     * JWTToken刷新生命周期 （解决用户一直在线操作，提供Token失效问题）
     * 1、登录成功后将用户的JWT生成的Token作为k、v存储到cache缓存里面
     * 2、当该用户再次请求时，通过JWTFilter层层校验之后会进入到doGetAuthenticationInfo进行身份认证
     * 3、当该用户再次请求，token值还在生命周期内，则会通过重新PUT的方式k、v都为Token值，缓存中的token值生命周期时间重新计算
     * 4、当该用户再次请求，token值已经超时，并在cache中不存在对应的k、v，则表示该用户账户空闲超时，返回用户信息已失效，请重新登录。
     * 5、每次当返回为true情况下，都会给Response的Header中设置Authorization，该Authorization映射的v为cache对应的v值。
     * 6、注：当前端接收到Response的Header中的Authorization值会存储起来，作为以后请求token使用
     *
     * @param token
     * @param userName
     * @param passWord
     * @return
     */
    public boolean jwtTokenRefresh(String token, String userName, String passWord) {
        String cacheToken = String.valueOf(redisUtil.get(CommonConstant.PREFIX_USER_TOKEN + token));

        if (CommonUtils.isNotEmpty(cacheToken)) {

            /*if (!jwtUtil.verify(cacheToken, userName, passWord)) {   // token过时了
                String newAuthorication = jwtUtil.sign(userName, passWord);
                redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, newAuthorication);
                redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, jwtUtil.EXPIRE_TIME / 1000);
            } else {
                redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, cacheToken);
                redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, jwtUtil.EXPIRE_TIME / 1000);
            }*/

            // 判断token是否正确
            if (jwtUtil.verify(cacheToken, userName, passWord)) {
                redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, cacheToken);
                redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, jwtUtil.EXPIRE_TIME / 1000);
                return true;
            } else {
                // token异常（失效、被篡改）
                return false;
            }
        } else {
            return false;
        }
    }





}
