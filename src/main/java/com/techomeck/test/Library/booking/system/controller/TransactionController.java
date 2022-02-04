package com.techomeck.test.Library.booking.system.controller;

import com.techomeck.test.Library.booking.system.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TransactionController {

    private TransactionService transactionService;


    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("borrowArticle/{userId}")
    public ResponseEntity<String> borrowArticle(@PathVariable(name = "userId", required = true) int id,
                                                @RequestBody(required = true) List<String> articleTitles) {
        return transactionService.borrowArticles(id, articleTitles);
    }

    @PostMapping("returnArticle/{userId}")
    public ResponseEntity<String> returnArticle(@PathVariable(name = "userId", required = true) int id,
                                                @RequestBody(required = true) List<String> articleTitles) {
        return transactionService.returnArticles(id, articleTitles);
    }
}
