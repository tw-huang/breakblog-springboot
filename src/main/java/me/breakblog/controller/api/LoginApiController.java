package me.breakblog.controller.api;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import me.breakblog.dto.LoginDTO;
import me.breakblog.entity.Admin;
import me.breakblog.service.AdminService;
import me.breakblog.util.JwtUtil;
import me.breakblog.util.Result;
import me.breakblog.vo.MenuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: tw.huang
 * @DateTime: 2020-08-02 3:01
 * @Description: TODO
 */
@RestController
@RequestMapping("/api")
public class LoginApiController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/login")
    public Result login(@RequestBody @Validated LoginDTO loginDTO) {
        Map<String, Object> map = new HashMap<>();
        QueryWrapper<Admin> qw = new QueryWrapper<>();
        qw.eq("username", loginDTO.getUsername()).eq("password", loginDTO.getPassword());
        Admin admin = adminService.getOne(qw);
        if (admin != null) {
            String token = JwtUtil.createToken(admin);
            map.put("token", token);
            return Result.success(map, "登录成功");
        }
        return Result.failure("用户名或密码不正确");
    }

    @GetMapping("/currentUser")
    public Result currentUser(HttpServletRequest request) {
        String userName = JwtUtil.getUserName(request);
        Admin admin = adminService.getAdminByUsername(userName);
        return Objects.isNull(admin) ? Result.failure("获取当前用户失败") : Result.success(admin, "当前用户");
    }

    @GetMapping("/logout")
    public Result logout(HttpServletRequest request) {
        return Result.success();
    }

    @GetMapping("/menus")
    public Result menu() {
        //模拟菜单数据
        JSONArray jsonArray = new JSONArray();

        MenuVo category = new MenuVo();
        category.setId(1);
        category.setName("分类管理");
        category.setPath("category");
        category.setIcon("el-icon-collection");
        MenuVo categories = new MenuVo();
        categories.setId(101);
        categories.setName("分类列表");
        categories.setPath("categories");
        categories.setIcon("el-icon-arrow-right");
        ArrayList<MenuVo> categoryList = new ArrayList<>();
        categoryList.add(categories);
        category.setChildren(categoryList);

        MenuVo post = new MenuVo();
        post.setId(2);
        post.setName("文章管理");
        post.setPath("post");
        post.setIcon("el-icon-notebook-2");
        MenuVo posts = new MenuVo();
        posts.setId(201);
        posts.setName("文章列表");
        posts.setPath("posts");
        posts.setIcon("el-icon-arrow-right");
        MenuVo newPost = new MenuVo();
        newPost.setId(202);
        newPost.setName("编写文章");
        newPost.setPath("post");
        newPost.setIcon("el-icon-arrow-right");
        ArrayList<MenuVo> postList = new ArrayList<>();
        postList.add(posts);
        postList.add(newPost);
        post.setChildren(postList);

        MenuVo comment = new MenuVo();
        comment.setId(3);
        comment.setName("评论管理");
        comment.setPath("comment");
        comment.setIcon("el-icon-chat-line-square");
        MenuVo comments = new MenuVo();
        comments.setId(301);
        comments.setName("评论列表");
        comments.setPath("comments");
        comments.setIcon("el-icon-arrow-right");
        ArrayList<MenuVo> commentList = new ArrayList<>();
        commentList.add(comments);
        comment.setChildren(commentList);

        MenuVo link = new MenuVo();
        link.setId(4);
        link.setName("友链管理");
        link.setPath("link");
        link.setIcon("el-icon-link");
        MenuVo links = new MenuVo();
        links.setId(401);
        links.setName("友链列表");
        links.setPath("links");
        links.setIcon("el-icon-arrow-right");
        ArrayList<MenuVo> linkList = new ArrayList<>();
        linkList.add(links);
        link.setChildren(linkList);

        MenuVo report = new MenuVo();
        report.setId(5);
        report.setName("数据统计");
        report.setPath("report");
        report.setIcon("el-icon-data-analysis");
        MenuVo reports = new MenuVo();
        reports.setId(501);
        reports.setName("数据报告");
        reports.setPath("reports");
        reports.setIcon("el-icon-arrow-right");
        ArrayList<MenuVo> reportList = new ArrayList<>();
        reportList.add(reports);
        report.setChildren(reportList);

        jsonArray.add(category);
        jsonArray.add(post);
        jsonArray.add(comment);
        jsonArray.add(link);
        jsonArray.add(report);

        return Result.success(jsonArray, "用户菜单");

    }


}
