package com.ibm.openpages.ext.interfaces.common.exception;


import org.springframework.web.client.RestClientException;

public class ServiceCallException extends RestClientException {

	public ServiceCallException(String msg) {
		super(msg);
	}

	public ServiceCallException(String msg, Throwable ex) {
		super(msg, ex);
	}
}
