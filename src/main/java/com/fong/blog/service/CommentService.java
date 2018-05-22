package com.fong.blog.service;

import com.fong.blog.domain.Blog;
import com.fong.blog.domain.Comment;

public interface CommentService {

    /**
     * 根据id获取commment
     * @param id
     * @return
     */
     Comment getCommentById(Long id);


    /**
     * 删除评论
     * @param id
     */
    void removeComment(Long id);

}
