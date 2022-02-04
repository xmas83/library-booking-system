package com.techomeck.test.Library.booking.system.service.impl;

import com.techomeck.test.Library.booking.system.entity.Article;
import com.techomeck.test.Library.booking.system.repository.ArticleRepository;
import com.techomeck.test.Library.booking.system.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleServiceImpl implements ArticleService {

    private ArticleRepository articleRepo;


    @Autowired
    public ArticleServiceImpl(ArticleRepository articleRepo) {
        this.articleRepo = articleRepo;
    }

    @Override
    public List<Article> getArticles() {
        return articleRepo.findAll();
    }


    @Override
    @Transactional
    public void addArticle(Article article) {
        articleRepo.save(article);
    }


    @Override
    public boolean deleteArticle(int id) {
        Optional<Article> article = articleRepo.findById(id);
        if (article.isPresent()) {
            articleRepo.delete(article.get());
            return true;
        }
        return false;
    }
}
