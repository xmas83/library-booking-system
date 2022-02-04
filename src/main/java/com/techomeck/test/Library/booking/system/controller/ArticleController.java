package com.techomeck.test.Library.booking.system.controller;

import com.techomeck.test.Library.booking.system.dto.ArticleDto;
import com.techomeck.test.Library.booking.system.entity.Article;
import com.techomeck.test.Library.booking.system.service.ArticleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ArticleController {

    private ArticleService articleService;
    private static final ModelMapper modelMapper = new ModelMapper();


    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }


    @GetMapping("/getArticles")
    public List<ArticleDto> getArticles() {
        List<Article> articles = articleService.getArticles();
        if (!articles.isEmpty()) {
            return articles.stream().map(item -> modelMapper.map(item, ArticleDto.class)).collect(Collectors.toList());
        }
        return null;
    }


    @PostMapping("/addArticle")
    public ResponseEntity<String> addArticle(@RequestBody ArticleDto articleDto) {
        articleService.addArticle(modelMapper.map(articleDto, Article.class));
        return new ResponseEntity<String>("Article Saved Successfully", HttpStatus.OK);
    }


    @DeleteMapping("deleteArticle/{id}")
    public ResponseEntity<String> deleteArticle(@PathVariable(value = "id") int id) {
        boolean response = articleService.deleteArticle(id);
        if (response) {
            return new ResponseEntity<String>("Deleted Successfully", HttpStatus.OK);
        }
        return new ResponseEntity<String>("Deletion failed. Please check ID", HttpStatus.BAD_REQUEST);
    }
}
