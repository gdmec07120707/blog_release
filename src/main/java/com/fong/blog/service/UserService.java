package com.fong.blog.service;

import com.fong.blog.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    User saveOrUpdateUser(User User);

    User registerUser(User User);

    void removeUser(Long id);

    User getUserById(Long id);

    Page<User> listUserByNameLike(String name, Pageable pageable);


}
