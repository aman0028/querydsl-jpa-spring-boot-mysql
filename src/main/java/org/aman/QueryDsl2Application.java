package org.aman;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
public class QueryDsl2Application {

	public static void main(String[] args) {
		SpringApplication.run(QueryDsl2Application.class, args);
	}
}
//@EnableTransactionManagement