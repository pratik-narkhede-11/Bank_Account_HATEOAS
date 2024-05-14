package com.pratik;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.pratik.dao.AccountRepository;
import com.pratik.model.Account;

@DataJpaTest
@Rollback(false)
public class AccountRepoTest 
{
	@Autowired 
	private AccountRepository repo;
	
	@Test
	public void testAddAccount()
	{
		Account account = new Account("123456789",100);
		Account savedAccount = repo.save(account);
		
		assertThat(savedAccount).isNotNull();
		assertThat(savedAccount.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testDepositAmount()
	{
		Account account = new Account("123456789",100);
		Account savedAccount = repo.save(account);
		
		repo.updateBalance(150, savedAccount.getId());
		
		Account updatedAccount = repo.findById(savedAccount.getId()).get();
		assertThat(updatedAccount.getBalance()).isEqualTo(150);
	}
}
