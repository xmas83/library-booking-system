package com.techomeck.test.Library.booking.system.service.impl;

import com.techomeck.test.Library.booking.system.ArticleType;
import com.techomeck.test.Library.booking.system.entity.Article;
import com.techomeck.test.Library.booking.system.entity.User;
import com.techomeck.test.Library.booking.system.entity.UserArticleTransaction;
import com.techomeck.test.Library.booking.system.repository.ArticleRepository;
import com.techomeck.test.Library.booking.system.repository.UserArticleTransactionRepository;
import com.techomeck.test.Library.booking.system.repository.UserRepository;
import com.techomeck.test.Library.booking.system.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    private UserArticleTransactionRepository transactionRepo;
    private UserRepository userRepo;
    private ArticleRepository articleRepo;


    @Autowired
    public TransactionServiceImpl(UserArticleTransactionRepository transactionRepo, UserRepository userRepo, ArticleRepository articleRepo) {
        this.transactionRepo = transactionRepo;
        this.userRepo = userRepo;
        this.articleRepo = articleRepo;
    }


    @Override
    @Transactional
    public ResponseEntity<String> borrowArticles(int userId, List<String> articleTitles) {
        if (articleTitles.size() > 5) {
            return new ResponseEntity<String>("Your borrowable limit at a time cannot be more than 5 ",
                    HttpStatus.BAD_REQUEST);
        }
        Optional<User> user = userRepo.findById(userId);
        if (user.isEmpty()) {
            return new ResponseEntity<String>("User ID does not exists", HttpStatus.BAD_REQUEST);
        }
        List<Article> articlesOnHand = new ArrayList<Article>();
        List<UserArticleTransaction> transactions = transactionRepo.findByUser(user.get());
        if (!transactions.isEmpty()) {
            long onHandCount = transactions.stream().filter(item -> item.getReturnedOn() == null).count();
            if (onHandCount == 7 || (onHandCount + articleTitles.size()) > 7) {
                return new ResponseEntity<String>(
                        "Your borrowable limit will exceed with this request. "
                                + "Please make sure your requested count with article on-hand is not exceeding 7",
                        HttpStatus.BAD_REQUEST);
            }
            transactions.forEach(transaction -> {
                if (transaction.getReturnedOn() == null) {
                    articlesOnHand.add(transaction.getArticle());
                }
            });
        }
        List<Article> requestedArticles = new ArrayList<Article>();
        for (String title : articleTitles) {
            Optional<Article> article = articleRepo.findByTitle(title);
            if (article.isEmpty()) {
                return new ResponseEntity<String>("Article '" + title + "' does not exists", HttpStatus.BAD_REQUEST);
            }
            requestedArticles.add(article.get());
        }
        long cnt1 = requestedArticles.stream().filter(article -> article.getType().equals(ArticleType.NEW.toString()))
                .count();
        long cnt2 = articlesOnHand.stream().filter(article -> article.getType().equals(ArticleType.NEW.toString()))
                .count();

        if ((cnt1 + cnt2) > 2) {
            return new ResponseEntity<String>("Your borrowable limit for NEW articles will exceed with this request. "
                    + "Please make sure your requested count with article on-hand of type NEW is not exceeding 2",
                    HttpStatus.BAD_REQUEST);
        }
        requestedArticles.forEach(article -> {
            UserArticleTransaction item = new UserArticleTransaction();
            item.setArticle(article);
            item.setUser(user.get());
            item.setBorrowedOn(LocalDateTime.now());
            transactionRepo.save(item);
        });
        return new ResponseEntity<String>("SUCCESS", HttpStatus.OK);
    }


    @Override
    @Transactional
    public ResponseEntity<String> returnArticles(int userId, List<String> articleTitles) {
        Optional<User> user = userRepo.findById(userId);
        if (user.isEmpty()) {
            return new ResponseEntity<String>("User ID does not exists", HttpStatus.BAD_REQUEST);
        }
        List<Article> requestedArticles = new ArrayList<Article>();
        for (String title : articleTitles) {
            Optional<Article> article = articleRepo.findByTitle(title);
            if (article.isEmpty()) {
                return new ResponseEntity<String>("Article '" + title + "' does not exists", HttpStatus.BAD_REQUEST);
            }
            requestedArticles.add(article.get());
        }
        requestedArticles.forEach(article -> {
            UserArticleTransaction item = transactionRepo.getItem(user.get(), article);
            if (item != null) {
                item.setReturnedOn(LocalDateTime.now());
                transactionRepo.save(item);
            }
        });
        return new ResponseEntity<String>("SUCCESS", HttpStatus.OK);
    }
}
