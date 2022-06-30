package com.ibm.openpages.ext.interfaces.icaps.exception.pushtoicaps;


import com.ibm.openpages.ext.interfaces.icaps.constant.IssueCommonConstants;

public class ICapsErrUpdateException extends RuntimeException {


	private static final long serialVersionUID = -8063775793796718857L;

	public ICapsErrUpdateException(String message, Throwable originalCause) {
		super(IssueCommonConstants.ICAPS_ERR_UPDATE_ERROR_MESSAGE + ": " + message, originalCause);

	}


	

}
