package com.fong.blog.controller;

import com.fong.blog.domain.User;
import com.fong.blog.domain.es.EsBlog;
import com.fong.blog.service.EsBlogService;
import com.fong.blog.vo.TagVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/blogs")
public class BlogController {

    @Autowired
    private EsBlogService esBlogService;

    @GetMapping
    public String listBlogs(@RequestParam(value = "order",required = false,defaultValue = "new") String order,
                            @RequestParam(value = "keyword",required = false,defaultValue = "") String keyword,
                            @RequestParam(value = "async",required = false) boolean async,
                            @RequestParam(value="pageIndex",required=false,defaultValue="0") int pageIndex,
                            @RequestParam(value="pageSize",required=false,defaultValue="10") int pageSize,
                            Model model){
        Page<EsBlog> page = null;
        List<EsBlog> list = null;
        boolean isEmpty = true;

        try{
            if(order.equals("hot")){
                Sort sort = new Sort(Sort.Direction.DESC,"readSize","commentSize","voteSize","createTime");
                Pageable pageable = new PageRequest(pageIndex,pageSize,sort);//,sort
                page = esBlogService.listHotestEsBlogs(keyword,pageable);
            }else if(order.equals("new")){
                Sort sort = new Sort(Sort.Direction.DESC,"createTime");
                Pageable pageable = new PageRequest(pageIndex,pageSize,sort);//,sort
                page = esBlogService.listNewestEsBlogs(keyword,pageable);
            }
            isEmpty = false;
        }catch (Exception e){
            e.printStackTrace();
            Pageable pageable = new PageRequest(pageIndex,pageSize);
            page = esBlogService.listEsBlogs(pageable);
        }

        list = page.getContent();

        model.addAttribute("order", order);
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", page);
        model.addAttribute("blogList", list);


        //首次访问页面的时候才加载
        if(!async&&!isEmpty){
            //最新5条
            List<EsBlog> newest = esBlogService.listTop5NewestEsBlog();
            model.addAttribute("newest", newest);
            //最热5条
            List<EsBlog> hotest = esBlogService.listTop5HotestEsBlog();
            model.addAttribute("hotest", hotest);
            //热门标签
            List<TagVO> tags = esBlogService.listTop30Tags();
            model.addAttribute("tags", tags);
            //热门用户
            List<User> users = esBlogService.listTop12User();
            model.addAttribute("users", users);
        }


        return (async==true?"/index :: #mainContainerRepleace":"/index");
    }

    @GetMapping("/newest")
    public String listNewestEsBlogs(Model model) {
        List<EsBlog> newest = esBlogService.listTop5NewestEsBlog();
        model.addAttribute("newest", newest);
        return "newest";
    }

    @GetMapping("/hotest")
    public String listHotestEsBlogs(Model model) {
        List<EsBlog> hotest = esBlogService.listTop5HotestEsBlog();
        model.addAttribute("hotest", hotest);
        return "hotest";
    }
}
