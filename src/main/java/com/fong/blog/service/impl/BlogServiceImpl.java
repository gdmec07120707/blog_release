package com.fong.blog.service.impl;

import com.fong.blog.domain.Blog;
import com.fong.blog.domain.Comment;
import com.fong.blog.domain.User;
import com.fong.blog.repository.BlogRepository;
import com.fong.blog.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Override
    public Blog saveBlog(Blog blog) {
        return blogRepository.save(blog);
    }

    @Override
    public void removeBlog(Long id) {
        blogRepository.delete(id);
    }

    @Override
    public Blog updateBlog(Blog blog) {
        return blogRepository.save(blog);
    }

    @Override
    public Blog getBlogById(Long id) {
        return blogRepository.findOne(id);
    }

    @Override
    public Page<Blog> listBlogsByTitleLike(User user, String title, Pageable pageable) {
        title = "%"+title+"%";
        Page<Blog> blogs =blogRepository.findByUserAndTitleLikeOrderByCreateTimeDesc(user,title,pageable);
        return blogs;
    }

    @Override
    public Page<Blog> listBlogsByTitleLikeAndSort(User user, String title, Pageable pageable) {
        title = "%" + title + "%";
        Page<Blog> blogs =blogRepository.findByUserAndTitleLike(user,title,pageable);
        return blogs;
    }

    @Override
    public void readingIncrease(Long id) {
        Blog blog = blogRepository.findOne(id);
        blog.setReadSize(blog.getReadSize()+1);
        blogRepository.save(blog);
    }

    @Override
    public Blog createComment(Long id, String commentContent) {
        Blog originalBlog = blogRepository.findOne(id);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        originalBlog.setUser(user);
        Comment comment = new Comment(user,commentContent);
        originalBlog.addComment(comment);
        return blogRepository.save(originalBlog);
    }

    @Override
    public void removeComment(Long blogId, Long commentId) {
        Blog originalBlog =  blogRepository.findOne(blogId);
        originalBlog.removeComment(commentId);
        blogRepository.save(originalBlog);
    }
}
