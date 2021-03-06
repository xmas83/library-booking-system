package com.techomeck.test.Library.booking.system.service;

import java.util.List;

import com.techomeck.test.Library.booking.system.entity.UserArticleTransaction;
import org.springframework.http.ResponseEntity;

public interface TransactionService {

    ResponseEntity<String> borrowArticles(int id, List<String> articleTitles);

    ResponseEntity<String> returnArticles(int id, List<String> articleTitles);

    List<UserArticleTransaction> getTransactionsExpiredDateOfReturn();

}
