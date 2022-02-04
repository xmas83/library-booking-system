package com.techomeck.test.Library.booking.system.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Data;

@Table
@Entity
@Data
public class UserArticleTransaction {

    @Id
    @SequenceGenerator(name = "transactionSeq", sequenceName = "transactionSeq", initialValue = 100)
    @GeneratedValue(generator = "transactionSeq")
    @Hidden
    private Integer id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Article article;

    @Column
    private LocalDateTime borrowedOn;

    @Column
    private LocalDateTime returnedOn;

}
