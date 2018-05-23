package com.fong.blog.service;

import com.fong.blog.domain.Blog;
import com.fong.blog.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BlogService {

    /**
     * 保存博客
     * @param blog
     * @return
     */
    Blog saveBlog(Blog blog);

    /**
     * 删除博客
     * @param id
     */
    void removeBlog(Long id);

    /**
     * 修改博客
     * @param blog
     * @return
     */
    Blog updateBlog(Blog blog);


    /**
     * 查询博客
     * @param id
     * @return
     */
    Blog getBlogById(Long id);

    /**
     * 根据用户名进行分页模糊查询（最新）
     * @param user
     * @return
     */
    Page<Blog> listBlogsByTitleLike(User user , String title, Pageable pageable);

    /**
     * 根据用户名进行分页模糊查询（最热）
     * @return
     */
    Page<Blog> listBlogsByTitleLikeAndSort(User suser, String title, Pageable pageable);


    /**
     * 阅读量递增
     * @param id
     */
    void readingIncrease(Long id);

    /**
     * 发表评论
     * @param id
     * @param commentContent
     * @return
     */
    Blog createComment(Long id, String commentContent);

    /***
     * 删除评论
     * @param blogId
     * @param commentId
     */
    void removeComment(Long blogId,Long commentId);


    /**
     * 点赞
     * @param blogId
     * @return
     */
    Blog createVote(Long blogId);

    /**
     * 取消点赞
     * @param blogId
     * @param voteId
     * @return
     */
    void removeVote(Long blogId, Long voteId);
}
