package com.ibm.openpages.ext.interfaces.cmp.util;

import com.ibm.openpages.api.logging.LoggerFactory;
import com.ibm.openpages.ext.interfaces.cmp.bean.EntitlementResponseBean;
import com.ibm.openpages.ext.interfaces.cmp.exception.EntitlementException;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class EntitlementExceptionHandler {
	
	//Add an exception handler for NotFoundException

	private Logger logger = LoggerFactory.getLoggerFactory().getSimpleLogger(EntitlementExceptionHandler.class);

	
	@ExceptionHandler 
	public ResponseEntity<EntitlementResponseBean> handleException(EntitlementException exc) {
		
		//create CustomerErrorResponse

		EntitlementResponseBean error = new EntitlementResponseBean();
		error.setStatus("Error");
		error.getComments().append(exc.getMessage());
		
		
		//return ResponseEntity 
		
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}
	
	// Add another exception handler ... to catch any exception (catch all)
	
	@ExceptionHandler 
	public ResponseEntity<EntitlementResponseBean> handleException(Exception exc) {
		
		//create ErrorResponse Bad request

		logger.error("error Bad Request ",exc);
		EntitlementResponseBean error = new EntitlementResponseBean();
		error.setStatus("Error");
		error.getComments().append(exc.getMessage());
		
		
		//return ResponseEntity 
		
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
	

}
