package me.breakblog.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.breakblog.dto.PageDTO;
import me.breakblog.entity.Comment;
import me.breakblog.entity.Post;
import me.breakblog.mapper.CommentMapper;
import me.breakblog.service.CommentService;

import me.breakblog.service.PostService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Resource
    private CommentMapper commentMapper;

    @Autowired
    private PostService postService;

    @Override
    public Page getPageByPostId(int id, int page, int size) {
        QueryWrapper<Comment> qw = new QueryWrapper<>();
        Page<Comment> commentPage = commentMapper.selectPage(new Page<>(page, size), qw.eq("post_id", id));
        for (Comment c : commentPage.getRecords()) {
            c.setComment(commentMapper.selectById(c.getRepliedId()));
        }
        return commentPage;
    }

    @Override
    public Map getPage(PageDTO pageDTO) {
        ArrayList<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        QueryWrapper<Comment> qw = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(pageDTO.getKeyword())) {
            qw.like("author", pageDTO.getKeyword()).or().like("body", pageDTO.getKeyword());
        }
        Page<Comment> page = commentMapper.selectPage(new Page<>(pageDTO.getPageNum(), pageDTO.getPageSize()), qw);
        for (Comment c : page.getRecords()) {
            HashMap<String, Object> hashMap = new HashMap<>();
            Comment comment = commentMapper.selectById(c.getRepliedId());
            Post post = postService.getById(c.getPostId());
            hashMap.put("id", c.getId());
            hashMap.put("author", c.getAuthor());
            hashMap.put("email", c.getEmail());
            hashMap.put("site", c.getSite());
            hashMap.put("postTitle", post.getTitle());
            hashMap.put("body", c.getBody());
            hashMap.put("reviewed", c.getReviewed() == 1);
            list.add(hashMap);
        }
        map.put("list", list);
        map.put("total", page.getTotal());
        map.put("size", page.getSize());
        return map;
    }
}
