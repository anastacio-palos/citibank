package com.ibm.openpages.ext.batch.omu.exception;



public class EmployeesException extends RuntimeException{


	private static final long serialVersionUID = -8063775793796718857L;

	public EmployeesException() {

	}

	public EmployeesException(String message) {
		super(message);

	}

	public EmployeesException(Throwable cause) {
		super(cause);

	}

	public EmployeesException(String message, Throwable cause) {
		super(message, cause);

	}

	public EmployeesException(String message, Throwable cause, boolean enableSuppression,
                              boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);

	}
	

}
