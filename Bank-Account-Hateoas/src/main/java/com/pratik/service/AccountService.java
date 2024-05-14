package com.pratik.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pratik.dao.AccountRepository;
import com.pratik.exceptions.AccountNotFoundException;
import com.pratik.model.Account;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AccountService 
{
	private AccountRepository repo;
	
	public AccountService(AccountRepository repo)
	{
		this.repo  = repo;
	}
	
	public List<Account> listAll()
	{
		return repo.findAll();
	}
	
	public Account get(Integer id)
	{
		return repo.findById(id).get();
	}
	
	public Account save(Account account)
	{
		return repo.save(account);
	}
	
	public Account deposit(float amount, int id)
	{
		repo.updateBalance(amount, id);
		return repo.findById(id).get();
	}
	
	public Account withdraw(float amount, int id)
	{
		repo.updateBalance(-amount, id);
		return repo.findById(id).get();
	}
	
	public void delete(int id) throws AccountNotFoundException 
	{
		if(!repo.existsById(id))
		{
			throw new AccountNotFoundException();
		}
		repo.deleteById(id);
	}
}
