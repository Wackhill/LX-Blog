package com.shpakovskiy.lxblog.repository;

import com.shpakovskiy.lxblog.entity.Article;
import com.shpakovskiy.lxblog.entity.User;

import java.util.List;

public interface ArticleRepositoryInterface {

    List<Article> getPublicArticles();
    Article getById(int id);
    void addArticle(Article article, User author);
    void updateArticle(Article article);
    void removeArticle(Article article);
    boolean isUsersArticle(int articleId, User user);
    List<Article> getUsersArticles(User user);
}
