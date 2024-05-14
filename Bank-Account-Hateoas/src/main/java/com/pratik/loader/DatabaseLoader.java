package com.pratik.loader;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.pratik.dao.AccountRepository;
import com.pratik.model.Account;

@Configuration
public class DatabaseLoader 
{
	private AccountRepository repo;

	public DatabaseLoader(AccountRepository repo) {
		this.repo = repo;
	}
	
	@Bean
	public CommandLineRunner initDatabase()
	{
		return args -> {
			Account account1 = new Account("4561237890", 100);
			Account account2 = new Account("7411237890", 500);
			Account account3 = new Account("9691237890", 900);
			
			repo.saveAll(List.of(account1, account2, account3));
			System.out.println("Sample Database Loaded");
		};
	}
	
}
