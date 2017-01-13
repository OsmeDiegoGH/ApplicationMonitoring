package org.monitoring.services;

import acertum.web.arquitectura.utilidades.Config;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EmailService {
    
    private final String EMAIL_SERVER_IP = Config.obtenerParametro("EMAIL_SERVER_IP");
    private final String EMAIL_PROVIDER = Config.obtenerParametro("EMAIL_PROVIDER");
    private final String EMAIL_SERVER_USER = Config.obtenerParametro("EMAIL_SERVER_USER");
    private final String EMAIL_SERVER_PWD = Config.obtenerParametro("EMAIL_SERVER_PWD");
    
    public void sendEmail(String emailFrom, String personal, String emailTo, String subject, String bodyMessage) throws MessagingException, UnsupportedEncodingException{
        HashMap<Message.RecipientType, String> recipients = new HashMap<>();
        recipients.put(Message.RecipientType.TO, emailTo);
        sendEmail(emailFrom, personal, recipients, subject, bodyMessage);
    }
    
    public void sendEmail(
            String emailFrom, 
            String personal, 
            HashMap<Message.RecipientType, String> recipients, 
            String subject,
            String bodyMessage
    ) throws MessagingException, UnsupportedEncodingException{
        sendEmail(emailFrom, personal, recipients, subject, bodyMessage, new ArrayList<MimeBodyPart>(), new ArrayList<MimeBodyPart>());
    }
    
    public void sendEmail(
            String emailFrom, 
            String personal, 
            HashMap<Message.RecipientType, String> recipients, 
            String subject,
            String bodyMessage,
            List<MimeBodyPart> images,
            List<MimeBodyPart> attachments
    ) throws MessagingException, UnsupportedEncodingException{
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText(bodyMessage, "utf-8", "html");        
        
        Properties properties = System.getProperties();
        properties.put("mail.smtp.ssl.trust", "10.63.200.79");
        properties.put("mail.smtp.port", "25");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        Session session = Session.getDefaultInstance(properties, null);
        
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(emailFrom, personal));
        message.setSubject(subject);
        
        MimeMultipart multipart;
        if (attachments.size() > 0) {
            //Tiene adjuntos, tengo que hacerlos visibles para su descargar
            multipart = new MimeMultipart();
        } else {
            //no tengo archivos adjuntos, oculto las imágenes adjuntas
            multipart = new MimeMultipart("related");
        }
        
        multipart.addBodyPart(messageBodyPart);
        
        for (MimeBodyPart image : images) {
            multipart.addBodyPart(image);
        }
        
        for (MimeBodyPart attachment : attachments) {
            multipart.addBodyPart(attachment);
        }
        message.setContent(multipart, "html");
        
        for(Map.Entry<Message.RecipientType, String> entry : recipients.entrySet()){
            message.setRecipient(entry.getKey(), new InternetAddress(entry.getValue()));
        }
        
        Transport transport = session.getTransport(EMAIL_PROVIDER);
        transport.connect(EMAIL_SERVER_IP, EMAIL_SERVER_USER, EMAIL_SERVER_PWD);
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }
    
}
