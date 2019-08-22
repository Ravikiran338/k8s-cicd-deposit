/**
 * 
 */
package com.sci.services.account.constants;

/**
 * @author mn259
 *
 */
public class DepositQueryConstants {
	

	public static final String DEPOSIT_BALANCE_BY_ACCOUNT_NUMBER = "INSERT INTO transaction_tbl (transaction_type, deposit_amt, active_flag, transaction_datetime, account_id) VALUES (?, ?, ?,?,?)";
	public static final String USER_DEPOSIT_CREATE = "SELECT * FROM transaction_tbl e " + " WHERE transaction_id = ?";
	public static final String GET_ACCOUNT_DETAILS = "SELECT balance FROM accounts_tbl WHERE account_id = ?";
	public static final String ACCOUNT_BALANCE = "SELECT * FROM accounts_tbl e " + " WHERE account_id = ?";
	public static final String ACCOUNT_BALANCE_BY_ID = "UPDATE accounts_tbl set balance=?,account_type=?,account_created_datetime=? where account_id =?";

	public static final String UPDATE_ACCOUNT_BALANCE = "SELECT * FROM accounts_tbl where account_num = ?";
	
	public static final String GET_SQL ="SELECT a.account_num,b.transaction_id,b.transaction_type,b.deposit_amt,b.withdraw_amt,b.active_flag,b.transaction_datetime,b.account_id,b.from_account_id FROM transaction_tbl b\r\n" + 
			"JOIN accounts_tbl a ON a.account_id=b.account_id WHERE account_num =? and b.transaction_datetime BETWEEN ? AND ?";
	public static final String SQL = "SELECT transaction_type,deposit_amt,withdraw_amt,transaction_datetime FROM transaction_tbl a, accounts_tbl b \r\n"
			+ "WHERE a.account_id=b.account_id AND b.account_num=?  order BY transaction_id desc LIMIT 10";
	
	
}
