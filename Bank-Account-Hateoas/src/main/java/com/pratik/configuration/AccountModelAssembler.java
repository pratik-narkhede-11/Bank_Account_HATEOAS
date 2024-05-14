package com.pratik.configuration;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import com.pratik.controller.AccountController;
import com.pratik.model.Account;

@Configuration
public class AccountModelAssembler implements RepresentationModelAssembler<Account, EntityModel<Account>>
{
	@Override
	public EntityModel<Account> toModel(Account account)
	{
		EntityModel<Account> accountModel = EntityModel.of(account);
		accountModel.add(linkTo(methodOn(AccountController.class).getOne(account.getId())).withSelfRel());
		accountModel.add(linkTo(methodOn(AccountController.class).deposit(null, account.getId())).withRel("deposit"));
		accountModel.add(linkTo(methodOn(AccountController.class).withdraw(null, account.getId())).withRel("withdraw"));
		accountModel.add(linkTo(methodOn(AccountController.class).listAll()).withRel(/*"Collection"*/ IanaLinkRelations.COLLECTION));
		return accountModel;
	}
}
