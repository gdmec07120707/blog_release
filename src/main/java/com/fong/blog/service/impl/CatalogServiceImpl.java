package com.fong.blog.service.impl;

import com.fong.blog.domain.Catalog;
import com.fong.blog.domain.User;
import com.fong.blog.repository.CatalogRepository;
import com.fong.blog.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 分类实现类
 */
@Service
public class CatalogServiceImpl implements CatalogService {

    @Autowired
    private CatalogRepository catalogRepository;

    @Override
    public Catalog saveCatalog(Catalog catalog) {
        //保存前判断是否有重复分类
        List<Catalog> list = catalogRepository.findByUserAndName(catalog.getUser(),catalog.getName());
        if(list!=null&&list.size()>0){
            throw new IllegalArgumentException("该分类已经存在了");
        }
        return catalogRepository.save(catalog);
    }

    @Override
    public void removeCatalog(Long id) {
        catalogRepository.delete(id);
    }

    @Override
    public Catalog getCatalogById(Long id) {
        return catalogRepository.findOne(id);
    }

    @Override
    public List<Catalog> listCatalogs(User user) {
        return catalogRepository.findByUser(user);
    }
}
