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
public class User {

    @Id
    @SequenceGenerator(name = "userSeq", sequenceName = "userSeq", initialValue = 100)
    @GeneratedValue(generator = "userSeq")
    @Hidden
    private Integer id;

    @Column
    private String firstName;

    @Column
    private String lastName;

}
