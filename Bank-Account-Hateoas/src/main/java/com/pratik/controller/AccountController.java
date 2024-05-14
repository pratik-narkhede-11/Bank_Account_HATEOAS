package com.pratik.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pratik.configuration.AccountModelAssembler;
import com.pratik.exceptions.AccountNotFoundException;
import com.pratik.model.Account;
import com.pratik.service.AccountService;

@RestController
@RequestMapping("/api/accounts")
public class AccountController 
{
	private AccountService service;
	private AccountModelAssembler assembler;
	
	public AccountController(AccountService service, AccountModelAssembler assembler) {
		this.service = service;
		this.assembler = assembler;
	}

	@GetMapping
	public ResponseEntity<CollectionModel<EntityModel<Account>>> listAll()
	{
		List<Account> accounts = service.listAll();
		if(accounts.isEmpty())
			return ResponseEntity.noContent().build();
		
		List<EntityModel<Account>> acc = accounts.stream().map(assembler::toModel).collect(Collectors.toList());
		CollectionModel<EntityModel<Account>> model = CollectionModel.of(acc);
		model.add(linkTo(methodOn(AccountController.class).listAll()).withSelfRel());
		return new ResponseEntity<>(model, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<EntityModel<Account>> getOne(@PathVariable("id") int id)
	{
		try {
			Account account = service.get(id);
			EntityModel<Account> model = assembler.toModel(account);
//			account.add(linkTo(methodOn(AccountController.class).getOne(account.getId())).withSelfRel());
//			account.add(linkTo(methodOn(AccountController.class).deposit(null, account.getId())).withRel("deposit"));
//			account.add(linkTo(methodOn(AccountController.class).withdraw(null, account.getId())).withRel("withdraw"));
//			account.add(linkTo(methodOn(AccountController.class).listAll()).withRel(/*"Collection"*/ IanaLinkRelations.COLLECTION));
			return new ResponseEntity<>(model, HttpStatus.OK);
		} catch (NoSuchElementException e)
		{
			return ResponseEntity.notFound().build();
		}
		
	}
	
	@PostMapping
	public ResponseEntity<EntityModel<Account>> add(@RequestBody Account account)
	{
		Account savedAccount = service.save(account);
		EntityModel<Account> model = assembler.toModel(savedAccount);
		return ResponseEntity.created(linkTo(methodOn(AccountController.class).getOne(savedAccount.getId())).toUri()).body(model);
	}
	
	@PutMapping
	public ResponseEntity<EntityModel<Account>> replace(@RequestBody Account account)
	{
		Account updatedAccount = service.save(account);
		EntityModel<Account> model = assembler.toModel(updatedAccount);
		return new ResponseEntity<>(model, HttpStatus.OK);
	}
	
	@PatchMapping("/{id}/deposit")
	public ResponseEntity<EntityModel<Account>> deposit(@RequestBody Map<String, Float> data, @PathVariable("id") int id)
	{
		float amount = data.get("amount");
		Account updatedAccount = service.deposit(amount, id);
		EntityModel<Account> model = assembler.toModel(updatedAccount);
		return new ResponseEntity<>(model, HttpStatus.OK);
	}
	
	@PatchMapping("/{id}/withdraw")
	public ResponseEntity<EntityModel<Account>> withdraw(@RequestBody Map<String, Float> data, @PathVariable("id") int id)
	{
		float amount = data.get("amount");
		Account updatedAccount = service.withdraw(amount, id);
		EntityModel<Account> model = assembler.toModel(updatedAccount);
		return new ResponseEntity<>(model, HttpStatus.OK);
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<?> delete(@PathVariable("id") int id)
	{
		try {
			service.delete(id);
			return ResponseEntity.noContent().build();
		} catch (AccountNotFoundException e)
		{
			return ResponseEntity.notFound().build();
		}
	}
}
