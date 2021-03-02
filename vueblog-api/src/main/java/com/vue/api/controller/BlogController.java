package com.vue.api.controller;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vue.api.constant.Result;
import com.vue.api.entity.Blog;
import com.vue.api.entity.User;
import com.vue.api.service.BlogService;
import com.vue.api.utils.CommonUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @ResponseBody
    @GetMapping("/list")
    public Result<List> list(@RequestParam(defaultValue = "1") Integer currentPage) {
        // User user = (User) SecurityUtils.getSubject().getPrincipal();

        Result<List> blogResult = new Result<>();
        Integer size = 5;   //
        IPage<Blog> iPage = blogService.selectListPage(currentPage, size);
        blogResult.setResult(iPage.getRecords());
        blogResult.success("查询成功！");
        return blogResult;
    }

    @ResponseBody
    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable(name = "id") Long id) {
        Blog blog = blogService.getById(id);
        if (CommonUtils.isEmpty(blog)) {
            return Result.error("该博客已被删除");
        }

        Result result =  Result.ok("查询成功");
        result.setResult(blog);
        return result;
    }

    @ResponseBody
    @PostMapping("/edit")
    public Result edit(@Validated @RequestBody Blog blog) {
        Blog temp = new Blog();
        temp.setCreated(LocalDateTime.now());
        temp.setStatus(0);



        BeanUtils.copyProperties(blog,temp, "id");

        blogService.saveOrUpdate(temp);
        return Result.ok("更新成功");
    }


}
