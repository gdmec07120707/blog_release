package com.fong.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/u")
public class UserspaceController {

    @GetMapping("/{username}")
    public String userSpace(@PathVariable("username") String username) {
        return "u";
    }

    @GetMapping("/{username}/blogs")
    public String listBlogsByOrder(@PathVariable("username") String username,
                                   @RequestParam(value = "order", required = false, defaultValue = "new") String order,
                                   @RequestParam(value = "category", required = false, defaultValue = "new") Long category,
                                   @RequestParam(value = "keyword", required = false, defaultValue = "new") String keyword) {
        if(category !=null){
            System.out.println("category:"+category);
            System.out.println("selflink:"+"redirect:/u/"+username+"/blogs?category="+category);
        }else if (keyword!=null && keyword.isEmpty() == false){
            System.out.println("keyword:"+keyword);
            System.out.println("selflink:"+"redirect:/u/"+username+"/blogs?keyword="+keyword);
        }
        System.out.println("order:"+order);
        System.out.println("selflink:"+"redirect:/u/"+username+"/blogs?order="+order);
        return "/u";
    }

    @GetMapping("/{username}/blogs/{id}")
    public String listBlogsByOrder(
            @PathVariable("username") String username,
            @PathVariable("id") Long id) {
        System.out.println("listBlogsByOrder:id======"+id+username );
        return "/blog";
    }

    @GetMapping("/{username}/blogs/edit")
    public String editBlog() {
        return "/blogedit";
    }

}
