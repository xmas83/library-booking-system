package com.techomeck.test.Library.booking.system.repository;

import com.techomeck.test.Library.booking.system.entity.Article;
import com.techomeck.test.Library.booking.system.entity.User;
import com.techomeck.test.Library.booking.system.entity.UserArticleTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserArticleTransactionRepository extends JpaRepository<UserArticleTransaction, Integer> {

    List<UserArticleTransaction> findByUser(User user);

    @Query(value = "Select u from UserArticleTransaction u where u.returnedOn IS NULL and u.user = :user and u.article = :article")
    UserArticleTransaction getItem(@Param("user") User user, @Param("article") Article article);

    @Query("Select u from UserArticleTransaction u where u.returnedOn IS NULL")
    List<UserArticleTransaction> findArticlesNotReturned();

}
