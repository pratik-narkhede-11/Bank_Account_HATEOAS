package com.pratik.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.pratik.model.Account;

public interface AccountRepository extends JpaRepository<Account, Integer> 
{
	@Modifying
	@Query("UPDATE Account a SET a.balance = a.balance + ?1 WHERE a.id = ?2")
	public void updateBalance(float amount, int id);
}
