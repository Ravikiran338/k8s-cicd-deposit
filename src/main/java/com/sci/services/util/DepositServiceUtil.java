/**
 * 
 */
package com.sci.services.util;

import com.sci.services.account.model.Status;

/**
 * @author mn259
 *
 */
public class DepositServiceUtil {
	
	private static DepositServiceUtil userServiceUtil = null;

	private DepositServiceUtil() {

	}
	public static DepositServiceUtil getInstance() {
		if (userServiceUtil == null){
			userServiceUtil = new DepositServiceUtil();
		}
		return userServiceUtil;
	}
	
	/**
	 * @param statusCode
	 * @param statusDesc
	 */
	public Status prepareStatus(String statusCode,String statusDesc) {
		Status status = new Status();
		status.setStatusCode(statusCode);
		status.setStatusDesc(statusDesc);
		return status;
	} 
}
