package com.sci.services.account.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sci.services.account.model.Status;
import com.sci.services.account.model.WebDepositPojo;
import com.sci.services.account.model.WebTransanactionPojo;
import com.sci.services.account.repository.DepositService;
import com.sci.services.account.model.TransactionPojo;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class DepositController {

	private static final Logger LOGGER = LoggerFactory.getLogger(DepositController.class);
	
	@Autowired
	private DepositService depositService;

    
	@PostMapping
	public Mono<Status> saveDeposit(@RequestBody WebDepositPojo webDepositPojo) {
		LOGGER.info("create: {}", webDepositPojo);
		return depositService.saveDeposit(webDepositPojo);
				
	}
	
	@GetMapping("/getbankstatement")
	public Flux<TransactionPojo> getBankStatementByDates(@RequestParam String accountNum, 
			@RequestParam String fromDate, @RequestParam String toDate) {
		LOGGER.info("getBankStatementByDates()");
		return depositService.getBankStatementByDates(accountNum, fromDate, toDate);
	}
		
	@GetMapping("/transactions")
	public Flux<WebTransanactionPojo> findByAccoutNum(@RequestParam String  accountNumber) {
		LOGGER.info("AccountNumber: {}", accountNumber);
		return depositService.findByAccountNumber(accountNumber);
	}
	
	
}
