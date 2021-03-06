package me.breakblog.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import me.breakblog.entity.*;
import me.breakblog.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
public class BlogController {

    private LinkService linkService;
    private CategoryService categoryService;
    private PostService postService;
    private AdminService adminService;
    private CommentService commentService;

    @ModelAttribute
    public void baseModel(Model model) {
        List<Link> links = linkService.getList();
        List<Category> categories = categoryService.getList();
        Admin admin = adminService.getById(1);
        model.addAttribute("links", links);
        model.addAttribute("categories", categories);
        model.addAttribute("blogTitle", admin.getBlogTitle());
        model.addAttribute("blogSubtitle", admin.getBlogSubtitle());
        model.addAttribute("blogAuthor", admin.getName());
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size, Model model) {
        Page postPage = postService.getPage(page, size);
        model.addAttribute("page", postPage);
        return "blog/index";
    }

    @RequestMapping(value = "/about", method = RequestMethod.GET)
    public String about(Model model) {
        Admin admin = adminService.getById(1);
        model.addAttribute("about", admin.getAbout());
        return "blog/about";
    }

    @RequestMapping(value = "/post/{id}", method = RequestMethod.GET)
    public String post(@PathVariable("id") int id, Model model,
                       @RequestParam(name = "reply", required = false) Integer rid,
                       @RequestParam(name = "author", required = false) String author,
                       @RequestParam(name = "page", defaultValue = "1") int page,
                       @RequestParam(name = "size", defaultValue = "10") int size) {
        postService.updatePageView(id);
        Post post = postService.getPostById(id);
        Page commentPage = commentService.getPageByPostId(id, page, size);

        if (rid != null && author != null) {
            model.addAttribute("replyId", rid);
            model.addAttribute("replyAuthor", author);
        }
        model.addAttribute("post", post);
        model.addAttribute("page", commentPage);
        return "blog/post";
    }

    @RequestMapping(value = "/category/{id}", method = RequestMethod.GET)
    public String category(
            @PathVariable("id") int id,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size, Model model) {
        Page postPage = postService.getPageByCategoryId(id, page, size);
        Category categoryById = categoryService.getById(id);
        model.addAttribute("page", postPage);
        model.addAttribute("categoryById", categoryById);
        return "blog/category";
    }

    @RequestMapping(value = "/demo", method = RequestMethod.GET)
    public String demo() {
        return "blog/demo";
    }
}

