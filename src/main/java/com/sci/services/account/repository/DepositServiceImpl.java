package com.sci.services.account.repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.sci.services.account.constants.DepositQueryConstants;
import com.sci.services.account.constants.DepositServiceConstants;
import com.sci.services.account.model.Account;
import com.sci.services.account.model.Status;
import com.sci.services.account.model.TransactionPojo;
import com.sci.services.account.model.WebDepositPojo;
import com.sci.services.account.model.WebTransanactionPojo;
import com.sci.services.util.DatabaseUtil;
import com.sci.services.util.DepositServiceUtil;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class DepositServiceImpl implements DepositService {
	private static DepositServiceUtil util = DepositServiceUtil.getInstance();


	@Override
	public Mono<Status> saveDeposit(WebDepositPojo webDepositPojo) {
		Mono<WebDepositPojo> accountDetails = accountdetails(webDepositPojo);
		System.out.println("account det"+accountDetails);
		Mono<TransactionPojo> transactionPojo = addTransactionInfo(accountDetails);
		Mono<Account> AccountPojo = getAccountInfo(transactionPojo);
		return updateAccountMono(AccountPojo);
	}

	private Mono<TransactionPojo> addTransactionInfo(Mono<WebDepositPojo> monoUser) {
		Mono<TransactionPojo> newMonoUser = monoUser.flatMap(newUser -> {
			Flowable<Integer> transactionId = DatabaseUtil.getInstance().getDatabase().update(DepositQueryConstants.DEPOSIT_BALANCE_BY_ACCOUNT_NUMBER)
					.parameters("D", newUser.getBalance(), "Y", new Date(), newUser.getAccountId()).counts();
			Flowable<TransactionPojo> webDepositPojo = DatabaseUtil.getInstance().getDatabase().select(DepositQueryConstants.USER_DEPOSIT_CREATE).parameterStream(transactionId)
					.get(rs -> {
						return prepareTransactionPojo(newUser, rs);
					});
			return Mono.from(webDepositPojo);
		});
		return newMonoUser;
	}

	/**
	 * @param monoUser
	 * @return
	 */
	private Mono<Account> getAccountInfo(Mono<TransactionPojo> monoUser) {
		return monoUser.flatMap(newUser -> {
			Flowable<Account> accountPojo = DatabaseUtil.getInstance().getDatabase().select(DepositQueryConstants.GET_ACCOUNT_DETAILS).parameter(newUser.getAccountId()).get(rs -> {
				return prepareAccountPojo(newUser, rs);
			});
			return Mono.from(accountPojo);
		});
	}

	private Mono<Status> updateAccountMono(Mono<Account> webDepositPojo) {
		return webDepositPojo.flatMap(newUser -> {

			Flowable<Integer> updatedCount = DatabaseUtil.getInstance().getDatabase().select(DepositQueryConstants.ACCOUNT_BALANCE).parameter(newUser.getAccountId())
					.getAs(Integer.class).flatMap(account -> DatabaseUtil.getInstance().getDatabase().update(DepositQueryConstants.ACCOUNT_BALANCE_BY_ID).parameters(newUser.getBalance(),
							newUser.getAccountType(), new Date(), newUser.getAccountId()).counts());
			Flowable<Status> result = updatedCount.map(new Function<Integer, Status>() {
				@Override
				public Status apply(Integer count) throws Exception {
					Status status = null;
					if (count > 0) {
						status = util.prepareStatus("00", "Success");
					} else {
						status = util.prepareStatus("99", "Failed");
					}
					return status;
				}
			});
			return Mono.from(result);
		});
	}

	public Mono<WebDepositPojo> accountdetails(WebDepositPojo user) {
		Flowable<WebDepositPojo> employeeFlowable = DatabaseUtil.getInstance().getDatabase().select(DepositQueryConstants.UPDATE_ACCOUNT_BALANCE).parameters(user.getAccountNum()).get(rs -> {
			WebDepositPojo accountManagement = new WebDepositPojo();
			accountManagement.setAccountId(rs.getInt(DepositServiceConstants.ACCOUNT_ID));
			accountManagement.setCustomerId(rs.getInt(DepositServiceConstants.CUSTOMER_ID));
			accountManagement.setBalance(user.getBalance()/* (rs.getBigDecimal("balance")) */);
			accountManagement.setAccountNum(rs.getString(DepositServiceConstants.ACCOUNT_NUMBER));
			accountManagement.setAccountType(rs.getString(DepositServiceConstants.ACCOUNT_TYPE));
			accountManagement.setActiveFlag(rs.getString(DepositServiceConstants.ACTIVE_FLAG));
			return accountManagement;
		});
		return Mono.from(employeeFlowable);
	}

	private Account prepareAccountPojo(TransactionPojo newUser, ResultSet rs) throws SQLException {
		Account accountPojo = new Account();
		accountPojo.setActiveFlag(newUser.getActiveFlag());
		accountPojo.setAccountType(newUser.getTransactionType());
		accountPojo.setAccountId(newUser.getAccountId());
		BigDecimal balance = new BigDecimal(rs.getDouble("balance")).add(newUser.getDepositAmount());
		accountPojo.setBalance(balance);
		return accountPojo;
	}

	private TransactionPojo prepareTransactionPojo(WebDepositPojo newUser, ResultSet rs) throws SQLException {
		TransactionPojo transactionPojo = new TransactionPojo();
		transactionPojo.setDepositAmount(newUser.getBalance());
		transactionPojo.setActiveFlag(newUser.getActiveFlag());
		transactionPojo.setTransactionType(newUser.getAccountType());
		transactionPojo.setAccountId(newUser.getAccountId());

		return transactionPojo;
	}


	@Override
	public Flux<TransactionPojo> getBankStatementByDates(String accountNum, String fromDate, String toDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date endDate = null;
		
		try{
			Calendar c2 = Calendar.getInstance();
			if(toDate != null && !"".equalsIgnoreCase(toDate)){
			  c2.setTime(sdf.parse(toDate));
			  c2.add(Calendar.DAY_OF_MONTH, 1); 
			  endDate = c2.getTime();  
			}
		}catch(Exception e){
			//TODO
		}	
		
		
		Flowable<TransactionPojo> userFlowable = DatabaseUtil.getInstance().getDatabase().select(DepositQueryConstants.GET_SQL).parameters(accountNum, fromDate, endDate).get(rs -> {
			
			TransactionPojo transactionPojo = new TransactionPojo();
			transactionPojo.setAccountId(rs.getInt(DepositServiceConstants.ACCOUNT_ID));
			transactionPojo.setActiveFlag(rs.getString(DepositServiceConstants.ACTIVE_FLAG));
			transactionPojo.setDepositAmount(rs.getBigDecimal(DepositServiceConstants.DEPOSIT_AMOUNT));
			transactionPojo.setFromAccountId(rs.getInt(DepositServiceConstants.FROM_ACCOUNT));
			transactionPojo.setTransactionDateTime(rs.getDate(DepositServiceConstants.TRANSACT_DATE_AND_TIME));
			transactionPojo.setTransactionId(rs.getInt(DepositServiceConstants.TRANSACTION_ID));
			transactionPojo.setTransactionType(rs.getString(DepositServiceConstants.TRANSACTION_TYPE));
			transactionPojo.setWithdrawAmount(rs.getBigDecimal(DepositServiceConstants.WITHDRAW_AMOUNT));
			transactionPojo.setAccountNum(rs.getString(DepositServiceConstants.ACCOUNT_NUMBER));
			return transactionPojo;
		});
		return Flux.from(userFlowable);
	}
			
	private TransactionPojo getTransactionPojo(ResultSet rs) throws SQLException{
		TransactionPojo transactionPojo = new TransactionPojo();
		
		transactionPojo.setAccountId(rs.getInt(DepositServiceConstants.ACCOUNT_ID));
		transactionPojo.setActiveFlag(rs.getString(DepositServiceConstants.ACTIVE_FLAG));
		transactionPojo.setDepositAmount(rs.getBigDecimal(DepositServiceConstants.DEPOSIT_AMOUNT));
		transactionPojo.setFromAccountId(rs.getInt(DepositServiceConstants.FROM_ACCOUNT));
		transactionPojo.setTransactionDateTime(rs.getDate(DepositServiceConstants.TRANSACT_DATE_AND_TIME));
		transactionPojo.setTransactionId(rs.getInt(DepositServiceConstants.TRANSACTION_ID));
		transactionPojo.setTransactionType(rs.getString(DepositServiceConstants.TRANSACTION_TYPE));
		transactionPojo.setWithdrawAmount(rs.getBigDecimal(DepositServiceConstants.WITHDRAW_AMOUNT));
		
		return transactionPojo;
	}
	
	
	@Override
	public Flux<WebTransanactionPojo> findByAccountNumber(String accountNumber) {
		Flowable<WebTransanactionPojo> employeeFlowable = DatabaseUtil.getInstance().getDatabase().select(DepositQueryConstants.SQL).parameter(accountNumber).get(rs -> {
			WebTransanactionPojo webTransanaction = new WebTransanactionPojo();
			webTransanaction.setTransactionType(rs.getString(DepositServiceConstants.TRANSACTION_TYPE));
			webTransanaction.setDepositAmount(rs.getBigDecimal(DepositServiceConstants.DEPOSIT_AMOUNT));
			webTransanaction.setWithdrawAmount(rs.getBigDecimal(DepositServiceConstants.WITHDRAW_AMOUNT));
			webTransanaction.setTransactionDateTime(rs.getDate(DepositServiceConstants.TRANSACT_DATE_AND_TIME));
			return webTransanaction;
		});
		return Flux.from(employeeFlowable);
	}
}
