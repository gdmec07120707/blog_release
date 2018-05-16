package com.fong.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/blog")
public class BlogController {

    @RequestMapping
    public String listBlogs(@RequestParam(value = "order",required = false,defaultValue = "new") String order,
                            @RequestParam(value = "tag",required = false)Long tag){
        System.out.println("order:"+order+";tag:"+tag);
        return "redirect:/index?order="+order+"&tag="+tag;
    }
}
