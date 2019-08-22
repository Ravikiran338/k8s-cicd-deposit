package com.sci.services.account.model;

import java.math.BigDecimal;

public class WebDepositPojo {
	
	
	private int accountId;
	private String accountNum;
	private String accountType;
	private String activeFlag;
	private int customerId;
	private BigDecimal balance;
	
	
	
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
	public String getActiveFlag() {
		return activeFlag;
	}
	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	
	
	
	
	
	
	/*"accountId": 4,
	"accountNum": "06597123459",
	"accountType": "C",
	"balance": 12234.49,
	"activeFlag": "Y",
	"customerId": 3*/

}
