package com.ibm.openpages.ext.interfaces.icaps.exception;



public class IssueException extends RuntimeException{


	private static final long serialVersionUID = -8063775793796718857L;

	public IssueException(Throwable originalCause) {
		super(originalCause);

	}

	public IssueException(String message, Throwable originalCause) {
		super(message, originalCause);

	}
	

}
