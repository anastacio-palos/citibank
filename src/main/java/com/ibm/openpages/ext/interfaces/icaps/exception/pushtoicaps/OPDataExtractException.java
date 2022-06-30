package com.ibm.openpages.ext.interfaces.icaps.exception.pushtoicaps;


import com.ibm.openpages.ext.interfaces.icaps.constant.IssueCommonConstants;
import com.ibm.openpages.ext.interfaces.icaps.exception.IssueException;

public class OPDataExtractException extends IssueException {


	private static final long serialVersionUID = -8063775793796718857L;

	public OPDataExtractException(String message, Throwable originalCause) {
		super(IssueCommonConstants.RETRIEVE_DATA_ERROR_MESSAGE + ": " + message, originalCause);

	}

	

}
