package com.vue.api.service.impl;

import com.vue.api.entity.Blog;
import com.vue.api.dao.BlogMapper;
import com.vue.api.service.BlogService;
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
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {

}
