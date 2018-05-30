package com.fong.blog.service;

import com.fong.blog.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    User saveOrUpdateUser(User User);

    User registerUser(User User);

    void removeUser(Long id);

    User getUserById(Long id);

    Page<User> listUserByNameLike(String name, Pageable pageable);

    /**
     * 根据名称查询列表
     * @param usernamelist
     * @return
     */
    List<User> listUsersByUsernames(List<String> usernamelist);
}
