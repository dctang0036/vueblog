package com.vue.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vue.api.entity.Blog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dctang
 * @since 2021-01-29
 */
public interface BlogService extends IService<Blog> {

    /**
     * 分页查询
     * @param currentPage
     * @param size
     * @return
     */
    IPage<Blog> selectListPage(Integer currentPage, Integer size);

}
