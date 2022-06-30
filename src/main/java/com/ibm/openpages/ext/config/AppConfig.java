package com.ibm.openpages.ext.config;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;


@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() throws Exception{

        SSLContext sslContext = SSLContext.getInstance("TLS");

        TrustManager[] trustManagers = new TrustManager[]{
            new X509TrustManager() {
                @Override
                public void checkClientTrusted(final X509Certificate[] x509Certificates, final String s) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(final X509Certificate[] x509Certificates, final String s) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }
        };

        sslContext.init(null, trustManagers, new SecureRandom());

        HttpClient client =
            HttpClientBuilder.create().setSSLHostnameVerifier((a, b) -> true).setSSLContext(sslContext).build();

        return new RestTemplate(new HttpComponentsClientHttpRequestFactory(client));
    }
}
