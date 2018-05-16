package com.fong.blog.controller;

import com.fong.blog.domain.User;
import com.fong.blog.service.UserService;
import com.fong.blog.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/u")
public class UserspaceController {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Value("${file.server.url}")
    private String fileServerUrl;

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
}
