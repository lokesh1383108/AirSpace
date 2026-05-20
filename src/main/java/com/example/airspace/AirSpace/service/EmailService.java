package com.example.airspace.AirSpace.service;


import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import java.util.Map;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.example.airspace.AirSpace.constants.emailTemplates;
//import java.util.logging.Logger;

@Service
public class EmailService {

    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;


    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    
    private final Logger logger =  LoggerFactory.getLogger(EmailService.class);
    @Value("${spring.mail.username}") private String sender;

    public boolean sendEmail(String Recipient, String content) {
        // Code to send an email with the OTP. Integrate with an email API.
    try{
        MimeMessage Message = javaMailSender.createMimeMessage();
        Message.setFrom(sender + "AirSpace");
        Message.setRecipients(MimeMessage.RecipientType.TO,Recipient);
        Message.setSubject("Email from AirSpace");
        Message.setText("Your Verification OTP is "+content);


        // Sending the mail
        javaMailSender.send(Message);
        logger.info("OTP Email sent: "+Recipient);
        return true;
    }// Catch block to handle the exceptions
    catch (Exception e) {
        logger.error("Exception Happened while sending Mail: "+e.getMessage()+e);
        System.out.println( "Error while Sending Mail"+e);

        return false;
    }
    }

     public boolean sendTemplatedEmail(String recipient, String subject,
                                     emailTemplates template, Map<String, Object> variables) {
        try {
             
            
            // validate the recipent and subject not null
            validateEmailParameters(recipient, subject);
            
            // building the template with variables to send on mail
            String emailContent = generateEmailFromTemplate(template, variables);
            
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(sender, "AirSpace");
            helper.setTo(recipient);
            helper.setSubject(subject);
            helper.setText(emailContent, true); // true = HTML content
            
            javaMailSender.send(message);
            logger.info("Email sent successfully to: {} | Template: {}", recipient, template);
            return true;
            
        } catch (Exception e) {
            logger.error("Failed to send email to: {} | Error: {}", recipient, e.getMessage());
            return false;
        }
    }

    private void validateEmailParameters(String recipient, String subject) {
        if (!StringUtils.hasText(recipient)) {
            throw new IllegalArgumentException("Recipient email cannot be empty");
        }
        if (!StringUtils.hasText(subject)) {
            throw new IllegalArgumentException("Email subject cannot be empty");
        }
        if (!isValidEmail(recipient)) {
            throw new IllegalArgumentException("Invalid email format: " + recipient);
        }
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    public String generateEmailFromTemplate(emailTemplates templateName, Map<String, Object> variables) {
        try {
            Context context = new Context();
            context.setVariables(variables);
            System.out.println(templateName.getTemplateFile());
            return templateEngine.process("email/" + templateName.getTemplateFile(), context);
        } catch (Exception e) {
            System.out.println("Error occured while processing Otp template.");
            logger.error("Email failed: ");
            // Fallback to simple template if Thymeleaf fails
            return "generateFallbackTemplate(templateName, variables);";

        }

  }


}
