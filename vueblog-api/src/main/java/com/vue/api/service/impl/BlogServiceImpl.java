package com.vue.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vue.api.entity.Blog;
import com.vue.api.dao.BlogMapper;
import com.vue.api.service.BlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private BlogMapper blogMapper;

    @Override
    public IPage<Blog> selectListPage(Integer currentPage, Integer size) {
        LambdaQueryWrapper<Blog> lambdaQueryWrapper = new LambdaQueryWrapper<Blog>()
                .orderByDesc(Blog::getCreated);

        Page<Blog> page = new Page<>(currentPage, size);
        IPage<Blog> iPage = blogMapper.selectPage(page, lambdaQueryWrapper);
        return iPage;
    }
}
