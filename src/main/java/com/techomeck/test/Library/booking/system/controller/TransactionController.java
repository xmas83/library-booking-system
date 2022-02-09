package com.techomeck.test.Library.booking.system.controller;

import com.techomeck.test.Library.booking.system.dto.UserArticleTransactionDto;
import com.techomeck.test.Library.booking.system.entity.UserArticleTransaction;
import com.techomeck.test.Library.booking.system.service.TransactionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TransactionController {

    private TransactionService transactionService;
    private static final ModelMapper modelMapper = new ModelMapper();


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

    @GetMapping("getTransactionsExpiredDateOfReturn")
    public List<UserArticleTransactionDto> getTransactionsExpiredDateOfReturn() {
        List<UserArticleTransaction> transactions = transactionService.getTransactionsExpiredDateOfReturn();
        if (!transactions.isEmpty()) {
            return transactions.stream().map(item -> modelMapper.map(item, UserArticleTransactionDto.class))
                    .collect(Collectors.toList());
        }
        return null;
    }
}
