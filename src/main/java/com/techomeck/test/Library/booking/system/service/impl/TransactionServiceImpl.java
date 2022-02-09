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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    private static final String INVALID_USER_ID = "User ID does not exists";

    private static final String INVALID_ARTICLE_TITLE = "One or more requested article(s) does not exists";

    private static final String EXCEEDING_NEW_ARTICLE_BORROWAL_LIMIT = "Your borrowal limit for NEW articles will exceed with this request. "
            + "Please make sure your requested count with article on-hand of type NEW is not exceeding 2";

    private static final String EXCEEDED_DATE_OF_RETURN = "Your already hold one or more book(s) which had exceeded the expected date of return. "
            + "So please return them to borrow further.";

    private static final String EXCEEDING_BORROWAL_LIMIT = "Your borrowal limit at a time cannot be more than 5 ";

    private static final String EXCEEDING_TOTAL_BORROWAL_LIMIT = "Your borrowal limit will exceed with this request. "
            + "Please make sure your requested count with article on-hand is not exceeding 7";


    private UserArticleTransactionRepository transactionRepo;
    private UserRepository userRepo;
    private ArticleRepository articleRepo;


    @Autowired
    public TransactionServiceImpl(UserArticleTransactionRepository transactionRepo, UserRepository userRepo, ArticleRepository articleRepo) {
        this.transactionRepo = transactionRepo;
        this.userRepo = userRepo;
        this.articleRepo = articleRepo;
    }

    /**
     * Records transactions when article is being borrowed
     */
    @Override
    @Transactional
    public ResponseEntity<String> borrowArticles(int userId, List<String> articleTitles) {
        // Validate if the user is borrowing more than 5 articles at the same time
        if (articleTitles.size() > 5) {
            return new ResponseEntity<String>(EXCEEDING_BORROWAL_LIMIT, HttpStatus.BAD_REQUEST);
        }
        // Validate if the given User ID is valid
        Optional<User> user = userRepo.findById(userId);
        if (user.isEmpty()) {
            return new ResponseEntity<String>(INVALID_USER_ID, HttpStatus.BAD_REQUEST);
        }
        // Validate if the user is not exceeding to hold more than 7 articles
        List<Article> articlesOnHand = new ArrayList<Article>();
        List<UserArticleTransaction> transactions = transactionRepo.findByUser(user.get());
        if (!transactions.isEmpty()) {
            long onHandCount = transactions.stream().filter(item -> item.getReturnedOn() == null).count();
            if (onHandCount == 7 || (onHandCount + articleTitles.size()) > 7) {
                return new ResponseEntity<String>(EXCEEDING_TOTAL_BORROWAL_LIMIT, HttpStatus.BAD_REQUEST);
            }
            for (UserArticleTransaction transaction : transactions) {
                if (transaction.getReturnedOn() == null) {
                    articlesOnHand.add(transaction.getArticle());

                    // Validate if the user is already holding one or more books beyond 30 days
                    if (transaction.getBorrowedOn().toLocalDate().isBefore(LocalDate.now().minusDays(29L))) {
                        return new ResponseEntity<String>(EXCEEDED_DATE_OF_RETURN, HttpStatus.BAD_REQUEST);
                    }
                }
            }

        }
        // Validate if the given Article ID is valid
        List<Article> requestedArticles = new ArrayList<Article>();
        for (String title : articleTitles) {
            Optional<Article> article = articleRepo.findByTitle(title);
            if (article.isEmpty()) {
                return new ResponseEntity<String>(INVALID_ARTICLE_TITLE, HttpStatus.BAD_REQUEST);
            }
            requestedArticles.add(article.get());
        }
        // Validate if the user ID is trying to borrow more than 2 articles of type NEW
        long cnt1 = requestedArticles.stream().filter(article -> article.getType().equals(ArticleType.NEW.toString()))
                .count();
        long cnt2 = articlesOnHand.stream().filter(article -> article.getType().equals(ArticleType.NEW.toString()))
                .count();

        if ((cnt1 + cnt2) > 2) {
            return new ResponseEntity<String>(EXCEEDING_NEW_ARTICLE_BORROWAL_LIMIT, HttpStatus.BAD_REQUEST);
        }

        // Save if all the validations are passed
        requestedArticles.forEach(article -> {
            UserArticleTransaction item = new UserArticleTransaction();
            item.setArticle(article);
            item.setUser(user.get());
            item.setBorrowedOn(LocalDateTime.now());
            transactionRepo.save(item);
        });

        return new ResponseEntity<String>("SUCCESS", HttpStatus.OK);
    }


    /**
     * Records transactions when article is being returned
     */
    @Override
    @Transactional
    public ResponseEntity<String> returnArticles(int userId, List<String> articleTitles) {

        // Validate if the given User ID is valid
        Optional<User> user = userRepo.findById(userId);
        if (user.isEmpty()) {
            return new ResponseEntity<String>(INVALID_USER_ID, HttpStatus.BAD_REQUEST);
        }

        // Validate if the given Article ID is valid
        List<Article> requestedArticles = new ArrayList<Article>();
        for (String title : articleTitles) {
            Optional<Article> article = articleRepo.findByTitle(title);
            if (article.isEmpty()) {
                return new ResponseEntity<String>(INVALID_ARTICLE_TITLE, HttpStatus.BAD_REQUEST);
            }
            requestedArticles.add(article.get());
        }

        // Save if all the validations are passed
        requestedArticles.forEach(article -> {
            UserArticleTransaction item = transactionRepo.getItem(user.get(), article);
            if (item != null) {
                item.setReturnedOn(LocalDateTime.now());
                transactionRepo.save(item);
            }
        });
        return new ResponseEntity<String>("SUCCESS", HttpStatus.OK);
    }

    /**
     * To get articles that were borrowed more than 30 days back
     */
    @Override
    public List<UserArticleTransaction> getTransactionsExpiredDateOfReturn() {
        LocalDate expectedDateOfReturn = LocalDate.now().minusDays(29);
        List<UserArticleTransaction> transactions = transactionRepo.findArticlesNotReturned();
        return transactions.stream()
                .filter(transaction -> transaction.getBorrowedOn().toLocalDate().isBefore(expectedDateOfReturn))
                .collect(Collectors.toList());
    }
}
