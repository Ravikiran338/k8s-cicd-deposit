/**
 * 
 */
package com.sci.services.account.repository;

import com.sci.services.account.model.Status;
import com.sci.services.account.model.TransactionPojo;
import com.sci.services.account.model.WebDepositPojo;
import com.sci.services.account.model.WebTransanactionPojo;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author mn259
 *
 */
public interface DepositService {
	public Mono<Status> saveDeposit(WebDepositPojo webDepositPojo);
	
	public Flux<TransactionPojo> getBankStatementByDates(String accountNum, String fromDate, String toDate);
	public Flux<WebTransanactionPojo> findByAccountNumber(String  accountNumber);
}
