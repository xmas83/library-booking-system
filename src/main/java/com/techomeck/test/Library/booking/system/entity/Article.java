package com.techomeck.test.Library.booking.system.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Data;

@Table
@Entity
@Data
public class Article {

    @Id
    @SequenceGenerator(name = "articleSeq", sequenceName = "articleSeq", initialValue = 100)
    @GeneratedValue(generator = "articleSeq")
    @Hidden
    private Integer id;

    @Column(unique = true)
    private String title;

    @Column
    private String type;

    @Column
    private String author;

}
