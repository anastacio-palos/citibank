package com.ibm.openpages.ext.interfaces.common.util;

import com.ibm.openpages.ext.interfaces.common.exception.ServiceCallException;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Calendar;
import java.util.Map;

/**
 * <p>
 * This utility contains the methods used to call http requests.
 * </p>
 *
 * @author Luis Castillo <BR>
 * email : lacastil@mx1.ibm.com <BR>
 * company : IBM Consulting Services Mexico
 * @version 1.0
 * @custom.date : 12-01-2021
 */
@Component
public class RestClient {
    private final Logger logger;

    public RestClient(Logger logger) {
        this.logger = logger;
    }

    /**
     * <p>
     * invokes a get http operation
     * </p>
     *
     * @param url full url address
     * @return {@link ResponseEntity} with the response of the call
     */
    public ResponseEntity<String> getInvocation(String url) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForEntity(url, String.class);
    }

    /**
     * <p>
     * invokes a post http operation using body and headers
     * </p>
     *
     * @param body    of the request in plain format
     * @param headers {@link HttpHeaders} of the request
     * @param url     full url address
     * @param timeout
     * @return {@link ResponseEntity} with the response of the call
     */
    public ResponseEntity<String> postInvocation(String body, HttpHeaders headers, String url, int timeout) {
        ResponseEntity<String> response;
        Long startTime = Calendar.getInstance().getTimeInMillis();
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectTimeout(timeout);
        httpRequestFactory.setReadTimeout(timeout);
        RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        response = restTemplate.postForEntity(url, entity, String.class);
        if (!response.getStatusCode().is2xxSuccessful())
            throw new ServiceCallException("The service call returned the Error Code: " + response.getStatusCode().toString());
        logger.info("POST Request time = " + (Calendar.getInstance().getTimeInMillis() - startTime) + " milliseconds");
        return response;
    }

    /**
     * <p>
     * invokes a post http operation using body and headers map.
     * </p>
     *
     * @param body      of the request in plain format
     * @param headerMap {@link Map} with the headers of the request
     * @param url       full url address
     * @param timeout
     * @return
     */
    public ResponseEntity<String> postInvocation(String body, Map<String, String> headerMap,
                                                 String mediaType, String url, int timeout) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAll(headerMap);
        headers.setContentType(MediaType.parseMediaType(mediaType));
        return postInvocation(body, headers, url, timeout);
    }

    /**
     * <p>
     * this method helps to build an URI url.
     * </p>
     *
     * @param serverHostProtocol defines the protocol http or https
     * @param serverHostName     contains the hostname
     * @param serverPort         contains the port
     * @param restURI            contains the URI path
     * @return the full URI url built
     */
    public String buildRestURIUrl(String serverHostProtocol, String serverHostName, String serverPort, String restURI) {
        return serverHostProtocol + "://" + serverHostName + ":" + serverPort + restURI;
    }

}
