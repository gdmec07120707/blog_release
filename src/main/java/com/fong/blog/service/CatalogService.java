package com.fong.blog.service;

import com.fong.blog.domain.Catalog;
import com.fong.blog.domain.User;

import java.util.List;

/**
 * Catalog 服务类
 */
public interface CatalogService {

    /**
     * 保存catelog
     * @param catalog
     * @return
     */
    Catalog saveCatalog(Catalog catalog);

    /**
     * 删除
     * @param id
     */
    void removeCatalog(Long id);

    /**
     * 查询
     * @param id
     * @return
     */
    Catalog getCatalogById(Long id);

    /**
     * 列表
     * @param user
     * @return
     */
    List<Catalog> listCatalogs(User user);
}
