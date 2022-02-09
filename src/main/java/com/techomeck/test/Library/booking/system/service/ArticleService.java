package com.techomeck.test.Library.booking.system.service;

import java.util.List;

import com.techomeck.test.Library.booking.system.entity.Article;

public interface ArticleService {

    List<Article> getArticles();

    void addArticle(Article article);

    boolean deleteArticle(int id);

    Article getArticle(int id);

}
