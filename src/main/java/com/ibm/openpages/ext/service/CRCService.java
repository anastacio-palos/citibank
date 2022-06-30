package com.ibm.openpages.ext.service;

import com.ibm.openpages.ext.constant.CRCConstants;
import com.ibm.openpages.ext.tss.service.ILoggerUtil;
import org.apache.commons.logging.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class CRCService {

    private Log log;
    private RestTemplate restTemplate;



    public CRCService(RestTemplate restTemplate,ILoggerUtil loggerUtil){

        this.log = loggerUtil.getExtLogger(CRCConstants.CRC_LOG_FILE);

        this.restTemplate = restTemplate;

    }


    public Object invoke(String url, Class responseType){

        log.info("Invoking url="+ url);

        ResponseEntity response =
           null; Optional.ofNullable(responseType).map(v ->  restTemplate.getForEntity(url, responseType)).orElseGet(() -> restTemplate.getForEntity(url, String.class));

        log.info("Response = " + response);
        log.info("Response code = " + response.getStatusCode());

        if(response.getStatusCode().is2xxSuccessful()){

            log.info("Success response="+ response.getBody());
            return response.getBody();

        }else{

            throw new RuntimeException(String.format("Error Invoking CRC service code =%s message=%s",
                response.getStatusCode().toString(), response.getBody()));
        }

    }


}
