package com.ibm.openpages.ext.interfaces.icaps.exception.pushtoicaps;


import com.ibm.openpages.ext.interfaces.icaps.constant.IssueCommonConstants;

public class OPDataTransformException extends RuntimeException {


	private static final long serialVersionUID = -8063775793796718857L;

	public OPDataTransformException(String message, Throwable originalCause) {
		super(IssueCommonConstants.TRANSFORMING_DATA_ERROR_MESSAGE + ": " + message, originalCause);

	}

	

}
