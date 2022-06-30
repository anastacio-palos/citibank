package com.ibm.openpages.ext.notification;

import com.ibm.openpages.api.configuration.IConfigProperties;
import com.ibm.openpages.api.service.IConfigurationService;
import com.ibm.openpages.api.service.local.LocalServiceFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

public class EmailConfiguration {


    private String host ;
    private String from ;
    private String to;
    private String cc;

    private String HOST_SETTING = "/OpenPages/Applications/Common/Email/Mail Server";
    private String FROM_SETTING = "/OpenPages/Applications/Common/Email/Mail From Address";
    private String TO_SETTING = "/OpenPages/Custom Deliverables/Interfaces/DSMT Interface/Email/To List";
    private String CC_SETTING = "/OpenPages/Custom Deliverables/Interfaces/DSMT Interface/Email/CC List";


    private  JavaMailSenderImpl mailSender = null;

    private static EmailConfiguration emailConfiguration = null;


    private IConfigProperties configProperties;

    public static EmailConfiguration getInstance(LocalServiceFactory sf){

        if(emailConfiguration == null){
            emailConfiguration = new EmailConfiguration(sf);
        }

        return emailConfiguration;
    }

    private EmailConfiguration(LocalServiceFactory serviceFactory){
        IConfigurationService configurationService = serviceFactory.createConfigurationService();
        this.configProperties = configurationService.getConfigProperties();
        this.host = configProperties.getProperty(HOST_SETTING);
        this.from = configProperties.getProperty(FROM_SETTING);
        this.to = configProperties.getProperty(TO_SETTING);
        this.cc = configProperties.getProperty(CC_SETTING);

        mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);

        //        mailSender.setUsername("my.gmail@gmail.com");
        //        mailSender.setPassword("password");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.debug", "true");







    }

    public String getHost() {
        return host;
    }

    public void setHost(final String host) {
        this.host = host;
    }


    public String getFrom() {
        return from;
    }

    public void setFrom(final String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(final String to) {
        this.to = to;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(final String cc) {
        this.cc = cc;
    }

    @Override
    public String toString() {
        return "EmailConfiguration{" +
               "host='" + host + '\'' +
               ", from='" + from + '\'' +
               ", to='" + to + '\'' +
               ", cc='" + cc + '\'' +
               '}';
    }

    public JavaMailSenderImpl getMailSender() {
        return mailSender;
    }


}
