package com.sci.services.account.model;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


public class Account implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int accountId;
	private String accountNum;
	private String accountType;
	private BigDecimal balance;
	private String activeFlag;
	private int customerId;
	private Date accountCreatedDatetime;
	
	
	
	public Account() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	public Account(int accountId, int customerId) {
		super();
		this.accountId = accountId;
		this.customerId = customerId;
	}



	public Account(int accountId, String accountNum, String accountType, BigDecimal balance,
			String activeFlag, int customerId, Date accountCreatedDatetime) {
		super();
		this.accountId = accountId;
		this.accountNum = accountNum;
		this.accountType = accountType;
		this.balance = balance;
		this.activeFlag = activeFlag;
		this.customerId = customerId;
		this.accountCreatedDatetime = accountCreatedDatetime;
	}



	public int getAccountId() {
		return accountId;
	}



	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}



	public String getAccountNum() {
		return accountNum;
	}



	public void setAccountNum(String accountNum) {
		this.accountNum = accountNum;
	}



	public String getAccountType() {
		return accountType;
	}



	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}



	public BigDecimal getBalance() {
		return balance;
	}



	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}



	public String getActiveFlag() {
		return activeFlag;
	}



	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}



	public int getCustomerId() {
		return customerId;
	}



	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}



	public Date getAccountCreatedDatetime() {
		return accountCreatedDatetime;
	}



	public void setAccountCreatedDatetime(Date accountCreatedDatetime) {
		this.accountCreatedDatetime = accountCreatedDatetime;
	}






	
}
