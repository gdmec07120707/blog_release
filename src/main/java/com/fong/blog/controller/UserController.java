package com.fong.blog.controller;

import com.fong.blog.domain.Authority;
import com.fong.blog.domain.User;
import com.fong.blog.repository.UserRepository;
import com.fong.blog.service.AuthorityService;
import com.fong.blog.service.UserService;
import com.fong.blog.util.ConstraintViolationExceptionHandler;
import com.fong.blog.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ConstraintDeclarationException;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
//@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class UserController {

    @Autowired
    private UserService userService;

    //权限（角色）管理
    @Autowired
    private AuthorityService authorityService;

    /**
     * 查询所有用户
     * @param model
     * @return
     */
    @GetMapping
    public ModelAndView list(@RequestParam(value = "async",required = false) boolean async,
            @RequestParam(value="pageIndex",required=false,defaultValue="0") int pageIndex,
            @RequestParam(value="pageSize",required=false,defaultValue="10") int pageSize,
            @RequestParam(value="name",required=false,defaultValue="") String name,
            Model model){

        Pageable pageable = new PageRequest(pageIndex,pageSize);
        Page<User> page = userService.listUserByNameLike(name,pageable);
        List<User> list = page.getContent();	// 当前所在页面数据列表

        model.addAttribute("page",page);
        model.addAttribute("userList",list);
        return new ModelAndView(async==true?"users/list :: #mainContainerRepleace":"users/list","userModel",model);//
    }


    @GetMapping("/add")
    public ModelAndView createForm(Model model){
        model.addAttribute("user", new User(null,null,null,null));
        System.out.println("------------------------add-------------------");
        return new ModelAndView("users/add","userModel",model);
    }

    @PostMapping
    public ResponseEntity<Response> create(User user,Long authorityId) {
        List<Authority> authorities = new ArrayList<>();
        authorities.add(authorityService.getAuthorityById(authorityId));
        user.setAuthorities(authorities);

        try{
            userService.saveOrUpdateUser(user);
        }catch (ConstraintViolationException e){
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        }
        return ResponseEntity.ok().body(new Response(true, "处理成功", user));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Response> delete(@PathVariable("id") Long id,Model model){
        try {
            userService.removeUser(id);
        }catch (Exception e){
            return  ResponseEntity.ok().body( new Response(false, e.getMessage()));
        }
        return  ResponseEntity.ok().body( new Response(true, "处理成功"));
    }

    /**
     * 获取修改用户的界面，及数据
     * @return
     */
    @GetMapping(value = "edit/{id}")
    public ModelAndView modifyForm(@PathVariable("id") Long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return new ModelAndView("users/edit", "userModel", model);
    }
}
