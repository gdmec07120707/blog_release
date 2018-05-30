package com.fong.blog.service;

import com.fong.blog.domain.User;
import com.fong.blog.domain.es.EsBlog;
import com.fong.blog.vo.TagVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EsBlogService {

    /**
     * 删除博客
     * @param id
     */
    void removeEsBlog(String id);

    /**
     * 更新
     * @param esBlog
     * @return
     */
    EsBlog updateEsBlog(EsBlog esBlog);

    /**
     * 根据id查找blog
     * @param blogId
     * @return
     */
    EsBlog getEsBlogByBlogId(long blogId);

    /**
     * 最新博客列表分页
     * @param keyword
     * @param pageable
     * @return
     */
    Page<EsBlog> listNewestEsBlogs(String keyword, Pageable pageable);

    /**
     * 最新博客列表分页
     * @param keyword
     * @param pageable
     * @return
     */
    Page<EsBlog> listHotestEsBlogs(String keyword, Pageable pageable);

    /**
     * 博客列表分页
     * @param pageable
     * @return
     */
    Page<EsBlog> listEsBlogs(Pageable pageable);

    /**
     * 最新前5
     * @return
     */
    List<EsBlog> listTop5NewestEsBlog();


    /**
     * 最热前5
     * @return
     */
    List<EsBlog> listTop5HotestEsBlog();


    /**
     * 最热前30标签
     * @return
     */
    List<TagVO> listTop30Tags();


    /**
     * 最热前12用户
     * @return
     */
    List<User> listTop12User();

}
