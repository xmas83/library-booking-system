package com.techomeck.test.Library.booking.system.dto;



import com.techomeck.test.Library.booking.system.entity.Article;
import com.techomeck.test.Library.booking.system.entity.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserArticleTransactionDto {

	private User user;

	private Article article;

	private LocalDateTime borrowedOn;

	private LocalDateTime returnedOn;


}
