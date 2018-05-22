package com.fong.blog.service.impl;

import com.fong.blog.domain.Comment;
import com.fong.blog.repository.CommentRepository;
import com.fong.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public Comment getCommentById(Long id) {
        return commentRepository.findOne(id);
    }

    @Override
    @Transactional
    public void removeComment(Long id) {
        commentRepository.delete(id);
    }
}
