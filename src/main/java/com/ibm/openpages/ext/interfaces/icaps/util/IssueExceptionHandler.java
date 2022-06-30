package com.ibm.openpages.ext.interfaces.icaps.util;

import com.ibm.openpages.api.logging.LoggerFactory;
import com.ibm.openpages.ext.interfaces.icaps.bean.IssueResponseBean;
import com.ibm.openpages.ext.interfaces.icaps.exception.IssueException;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class IssueExceptionHandler {
	
	//Add an exception handler for NotFoundException

	private Logger logger = LoggerFactory.getLoggerFactory().getSimpleLogger(IssueExceptionHandler.class);

	
	@ExceptionHandler 
	public ResponseEntity<IssueResponseBean> handleException(IssueException exc) {
		
		//create CustomerErrorResponse

		IssueResponseBean error = new IssueResponseBean();
		error.setStatus("Error");
		error.getComments().append(exc.getMessage());
		
		
		//return ResponseEntity 
		
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}
	
	// Add another exception handler ... to catch any exception (catch all)
	
	@ExceptionHandler 
	public ResponseEntity<IssueResponseBean> handleException(Exception exc) {
		
		//create ErrorResponse Bad request

		logger.error("error Bad Request ",exc);
		IssueResponseBean error = new IssueResponseBean();
		error.setStatus("Error");
		error.getComments().append(exc.getMessage());
		
		
		//return ResponseEntity 
		
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
	

}
