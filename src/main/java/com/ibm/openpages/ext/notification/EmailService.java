package com.ibm.openpages.ext.notification;

import com.ibm.openpages.api.service.local.LocalServiceFactory;
import com.ibm.openpages.ext.constant.DSMTConstant;
import com.ibm.openpages.ext.tss.service.ILoggerUtil;
import org.apache.commons.logging.Log;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class EmailService {

    private Log log;

    private LocalServiceFactory localServiceFactory;

    public EmailService(LocalServiceFactory localServiceFactory, ILoggerUtil iLoggerUtil){
        this.localServiceFactory = localServiceFactory;
        this.log = iLoggerUtil.getExtLogger(DSMTConstant.DSMT_INTERFACE_LOGGER);

    }


    public void sendEmail(String subject, String text, String filePath) throws Exception{

        log.info(String.format("Sending email subject=%s, text=%s, filePath=%s", subject, text, filePath));

        EmailConfiguration config = EmailConfiguration.getInstance(localServiceFactory);

        log.info("Email Config="+  config);

        JavaMailSender emailSender = config.getMailSender();
        MimeMessage message = emailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(config.getFrom());
        helper.setTo(config.getTo().split(","));
        if(config.getCc() != null)
            helper.setCc(config.getCc().split(","));
        helper.setSubject(subject);
        helper.setText(text, true);

        if(filePath != null && new File(filePath).exists())
            helper.addAttachment("DSMT Interface Job Report.zip", new File(filePath));

        emailSender.send(message);
    }

    public void sendEmail(String subject, String text, Set<String> additionalTo, Set<String> additionalCc, String filePath, String fileName){
        log.info(String.format("Sending email subject=%s, text=%s, filePath=%s", subject, text, filePath));
        EmailConfiguration config = EmailConfiguration.getInstance(localServiceFactory);
        log.info("Email Config=" + config);
        JavaMailSender emailSender = config.getMailSender();
        MimeMessage message = emailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(config.getFrom());

            if ((config.getTo() != null && !config.getTo().isEmpty()) || (additionalTo != null && !additionalTo.isEmpty())) {
                Set<String> toList = new HashSet<>();
                if (config.getTo() != null && !config.getTo().isEmpty())
                    toList.addAll(Arrays.asList(config.getTo().split(",")));
                if (additionalTo != null && !additionalTo.isEmpty())
                    toList.addAll(additionalTo);
                log.info("to:" + toList);
                helper.setTo(toList.toArray(new String[0]));
            }
            if ((config.getCc() != null && !config.getCc().isEmpty()) || (additionalCc != null && !additionalCc.isEmpty())) {
                Set<String> ccList = new HashSet<>();
                if (config.getCc() != null && !config.getCc().isEmpty())
                    ccList.addAll(Arrays.asList(config.getCc().trim().split(",")));
                if (additionalCc != null && !additionalCc.isEmpty())
                    ccList.addAll(additionalCc);
                log.info("cc:" + ccList);
                helper.setCc(ccList.toArray(new String[0]));
            }

            helper.setSubject(subject);
            helper.setText(text, true);

            if (filePath != null && new File(filePath).exists())
                helper.addAttachment(fileName, new File(filePath));

            emailSender.send(message);
        } catch (Exception e){
            log.error("An error has occurred while sending the email.",e);
        }
    }

}
