package com.vue.api.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vue.api.constant.Result;
import com.vue.api.entity.Blog;
import com.vue.api.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @ResponseBody
    @GetMapping("/list")
    public Result<List> list(@RequestParam(defaultValue = "1") Integer currentPage) {
        Result<List> blogResult = new Result<>();
        Integer size = 5;   //
        IPage<Blog> iPage = blogService.selectListPage(currentPage, size);
        blogResult.setResult(iPage.getRecords());
        blogResult.success("查询成功！");
        return blogResult;
    }


}
