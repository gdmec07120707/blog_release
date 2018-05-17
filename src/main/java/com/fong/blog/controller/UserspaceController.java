package com.fong.blog.controller;

import com.fong.blog.domain.Blog;
import com.fong.blog.domain.User;
import com.fong.blog.service.BlogService;
import com.fong.blog.service.UserService;
import com.fong.blog.util.ConstraintViolationExceptionHandler;
import com.fong.blog.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ConstraintViolationException;
import java.util.List;

@Controller
@RequestMapping("/u")
public class UserspaceController {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private BlogService blogService;

    @Value("${file.server.url}")
    private String fileServerUrl;



    /**
     * 获取个人设置页面
     * @param username
     * @param model
     * @return
     */
    @GetMapping("/{username}/profile")
    @PreAuthorize("authentication.name.equals(#username)")   //判断是否是当前用户
    public ModelAndView profile(@PathVariable("username") String username,Model model){
        User user = (User) userDetailsService.loadUserByUsername(username);
        model.addAttribute("user",user);
        model.addAttribute("fileServerUrl",fileServerUrl);
        return new ModelAndView("/userspace/profile","userModel",model);
    }

    @PostMapping("/{username}/profile")
    @PreAuthorize("authentication.name.equals(#username)")   //判断是否是当前用户
    public String savaProfile(@PathVariable("username") String username,User user){
        User originaUser = userService.getUserById(user.getId());
        originaUser.setEmail(user.getEmail());
        originaUser.setName(user.getName());

        //判断密码是否做了变更
        String rawPassword = originaUser.getPassword();
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodePassword = encoder.encode(user.getPassword());
        boolean isMatch = encoder.matches(rawPassword,encodePassword);
        if(!isMatch){
            originaUser.setEncodePassword(user.getPassword());
        }
        userService.saveOrUpdateUser(originaUser);

        return "redirect:/u/"+username+"/profile";
    }

    /**
     * 获取编辑头像的界面
     * @param username
     * @param model
     * @return
     */
    @GetMapping("/{username}/avatar")
    @PreAuthorize("authentication.name.equals(#username)")   //判断是否是当前用户
    public ModelAndView avatar(@PathVariable("username") String username,Model model){
        User user = (User) userDetailsService.loadUserByUsername(username);
        model.addAttribute("user",user);
        return new ModelAndView("/userspace/avatar","userModel",model);
    }

    @PostMapping("/{username}/avatar")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> saveAvatar(@PathVariable("username") String username, @RequestBody User user){
        String avatarUrl = user.getAvatar();

        User originalUser = userService.getUserById(user.getId());
        originalUser.setAvatar(avatarUrl);
        userService.saveOrUpdateUser(originalUser);
        return ResponseEntity.ok().body(new Response(true, "处理成功", avatarUrl));
    }


    @GetMapping("/{username}")
    public String userSpace(@PathVariable("username") String username,Model model) {
        User user  = (User) userDetailsService.loadUserByUsername(username);
        model.addAttribute("user",user);
        return "redirect:/u/"+username+"/blogs";
    }

    @GetMapping("/{username}/blogs")
    public String listBlogsByOrder(@PathVariable("username") String username,
                                   @RequestParam(value = "order", required = false, defaultValue = "new") String order,
                                   @RequestParam(value = "category", required = false) Long category,
                                   @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                                   @RequestParam(value="async",required=false) boolean async,
                                   @RequestParam(value="pageIndex",required=false,defaultValue="0") int pageIndex,
                                   @RequestParam(value="pageSize",required=false,defaultValue="10") int pageSize,
                                   Model model) {

        User user  = (User) userDetailsService.loadUserByUsername(username);
        model.addAttribute("user", user);

        if(category !=null){
            System.out.println("category:"+category);
            System.out.println("selflink:"+"redirect:/u/"+username+"/blogs?category="+category);
            return "/u";
        }

        Page<Blog> page = null;
        if(order.equals("hot")){
            Sort sort = new Sort(Sort.Direction.DESC,"reading","comments","likes");
            Pageable pageable = new PageRequest(pageIndex,pageSize,sort);
            page = blogService.listBlogsByTitleLikeAndSort(user,keyword,pageable);
        }
        if(order.equals("new")){
            Pageable pageable = new PageRequest(pageIndex, pageSize);
            page = blogService.listBlogsByTitleLike(user, keyword, pageable);
        }

        List<Blog> list = page.getContent();

        model.addAttribute("order",order);
        model.addAttribute("page",page);
        model.addAttribute("blogList",list);

        return (async == true?"/userspace/u :: #mainContainerRepleace":"/userspace/u");
    }

    /**
     * 获取博客展示页面
     * @param username
     * @param id
     * @return
     */
    @GetMapping("/{username}/blogs/{id}")
    public String listBlogsByOrder(
            @PathVariable("username") String username,
            @PathVariable("id") Long id,Model model) {
        //每次阅读数量+1
        blogService.readingIncrease(id);

        boolean isBlogOwner = false;
        if(SecurityContextHolder.getContext().getAuthentication()!=null&&
                SecurityContextHolder.getContext().getAuthentication().isAuthenticated()){
            User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(principal!=null&&username.equals(principal.getUsername())){
                isBlogOwner = true;
            }
        }

        model.addAttribute("isBlogOwner",isBlogOwner);
        model.addAttribute("blogModel",blogService.getBlogById(id));
        System.out.println("listBlogsByOrder:id======"+id+username );
        return "/userspace/blog";
    }

    /**
     * 删除博客
     * @param username
     * @param id
     * @return
     */
    @DeleteMapping
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> deleteBlog(@PathVariable("username") String username,@PathVariable("id")Long id){
        try {
            blogService.removeBlog(id);
        }catch (Exception e){
            return ResponseEntity.ok().body(new Response(false,e.getMessage()));
        }
        String redirect = "/u/"+username+"/blogs";
        return ResponseEntity.ok().body(new Response(true,"处理成功",redirect));
    }

    /**
     * 获取博客新增页面
     * @param model
     * @return
     */
    @GetMapping("/{username}/blogs/edit")
    public ModelAndView createBlog(Model model) {
        model.addAttribute("blog",new Blog(null,null,null));
        return new ModelAndView("/userspace/blogedit","blogModel",model);
    }

    /**
     * 获取博客编辑页面
     * @param model
     * @return
     */
    @GetMapping("/{username}/blogs/edit/{id}")
    public ModelAndView editBlog(@PathVariable("username") String username,@PathVariable("id") Long id,Model model) {
        model.addAttribute("blog",blogService.getBlogById(id));
        return new ModelAndView("/userspace/blogedit","blogModel",model);
    }

    /**
     * 保存博客
     * @param username
     * @param blog
     * @return
     */
    @PostMapping("/{username}/blogs/edit")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> saveBlog(@PathVariable("username") String username, @RequestBody Blog blog) {
        User user = (User) userDetailsService.loadUserByUsername(username);
        blog.setUser(user);
        try {
            blog =  blogService.saveBlog(blog);
        } catch (ConstraintViolationException e)  {
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        String redirectUrl = "/u/"+username+"/blogs/"+blog.getId();
        return ResponseEntity.ok().body(new Response(true, "处理成功", redirectUrl));
    }

}
