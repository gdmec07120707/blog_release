package com.fong.blog.controller;

import com.fong.blog.domain.Blog;
import com.fong.blog.domain.Comment;
import com.fong.blog.domain.User;
import com.fong.blog.service.BlogService;
import com.fong.blog.service.CommentService;
import com.fong.blog.util.ConstraintViolationExceptionHandler;
import com.fong.blog.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.List;

@Controller
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private CommentService commentService;

    /**
     * 评论列表
     * @param blogId
     * @param model
     * @return
     */
    @GetMapping
    public String listComments(@RequestParam(value="blogId",required=true) Long blogId, Model model) {
        Blog blog = blogService.getBlogById(blogId);
        List<Comment> comments = blog.getComments();

        String commentOwner = "";
        if(SecurityContextHolder.getContext().getAuthentication()!=null&&
                SecurityContextHolder.getContext().getAuthentication().isAuthenticated()){
            User principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(principal!=null){
                commentOwner = principal.getUsername();
            }
        }

        model.addAttribute("commentOwner",commentOwner);
        model.addAttribute("comments",comments);
        return "/userspace/blog :: #mainContainerRepleace";
    }

    /**
     * 创建评论
     * @param blogId
     * @param commentContent
     * @return
     */
    @GetMapping("/add")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")  // 指定角色权限才能操作方法
    public ResponseEntity<Response> createComment(Long blogId, String commentContent) {
        try {
            blogService.createComment(blogId, commentContent);
        } catch (ConstraintViolationException e)  {
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }

        return ResponseEntity.ok().body(new Response(true, "处理成功", null));
    }

    /**
     * 删除评论
     * @param id
     * @param blogId
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteComments(@PathVariable("id")Long id,Long blogId){
        boolean isOwner = false;
        User user = commentService.getCommentById(id).getUser();

        //判断操作用户是否是博客的所有者&&SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")
        if(SecurityContextHolder.getContext().getAuthentication()!=null&&
                SecurityContextHolder.getContext().getAuthentication().isAuthenticated()){
            User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(principal!=null&&principal.getUsername().equals(user.getUsername())){
                isOwner = true;
            }
        }

        if(!isOwner){
            return ResponseEntity.ok().body(new Response(false,"您暂无操作权限"));
        }

        try {
            blogService.removeComment(blogId,id);
            commentService.removeComment(id);
        }catch (ConstraintViolationException e){
            return ResponseEntity.ok().body(new Response(false,ConstraintViolationExceptionHandler.getMessage(e)));
        }catch (Exception e){
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "处理成功", null));
    }

}
