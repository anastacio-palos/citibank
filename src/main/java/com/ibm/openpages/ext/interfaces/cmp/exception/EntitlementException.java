package com.ibm.openpages.ext.interfaces.cmp.exception;



public class EntitlementException extends RuntimeException{


	private static final long serialVersionUID = -8063775793796718857L;

	public EntitlementException() {

	}

	public EntitlementException(String message) {
		super(message);

	}

	public EntitlementException(Throwable cause) {
		super(cause);

	}

	public EntitlementException(String message, Throwable cause) {
		super(message, cause);

	}

	public EntitlementException(String message, Throwable cause, boolean enableSuppression,
							 boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);

	}
	

}
