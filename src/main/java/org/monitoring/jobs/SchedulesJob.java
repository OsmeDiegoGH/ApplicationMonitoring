package org.monitoring.jobs;

import acertum.web.arquitectura.utilidades.Config;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import org.monitoring.entities.Schedule;
import org.monitoring.helpers.RESTRequestHelper;
import org.monitoring.helpers.RESTRequestHelper.HTTP_REQUEST_HEADER_NAME;
import org.monitoring.helpers.RESTRequestHelper.HTTP_REQUEST_HEADER_VALUE;
import org.monitoring.repositories.ScheduleRepository;
import org.monitoring.services.EmailService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class SchedulesJob implements Job{
    
    private final String DEFAULT_EMAIL_PERSONAL = Config.obtenerParametro("DEFAULT_EMAIL_PERSONAL");
    private final String DEFAULT_EMAIL_SUBJECT = Config.obtenerParametro("DEFAULT_EMAIL_SUBJECT");
    private final String EMAIL_FROM_WHEN_SCHEDULES_FAIL = Config.obtenerParametro("EMAIL_FROM_WHEN_SCHEDULES_FAIL");
    private final EmailService emailService;
    private final ScheduleRepository scheduleRepository;
    private final RESTRequestHelper restRequestHelper;
    
    public SchedulesJob(){
        this(new EmailService(), new ScheduleRepository(), new RESTRequestHelper());
    }
    
    public SchedulesJob(EmailService emailService, ScheduleRepository scheduleRepository, RESTRequestHelper restRequestHelper){
        this.emailService = emailService;
        this.scheduleRepository = scheduleRepository;
        this.restRequestHelper = restRequestHelper;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        List<Schedule> activeSchedules = scheduleRepository.findActiveSchedules();
        
        for(Schedule schedule : activeSchedules){
            HashMap<String, String> headers = new HashMap<>();
            for(Map.Entry<HTTP_REQUEST_HEADER_NAME, HTTP_REQUEST_HEADER_VALUE> entry: schedule.getRequestHeaders().entrySet()){
                headers.put(entry.getKey().getName(), entry.getValue().getName());
            }
            try {
                String resultRawRequest = restRequestHelper.doRequest(
                        schedule.getRequestUrl(),
                        schedule.getRequestMethod(),
                        headers,
                        schedule.getRequestBodyParameters()
                );
                if (!schedule.getExpectedResult().toLowerCase().equals("http_200") && !schedule.getExpectedResult().toLowerCase().equals(resultRawRequest.toLowerCase())) {
                    sendEmailAlert(schedule);
                }
            } catch (Exception ex) {
                sendEmailAlert(schedule);
            }
            //TODO: update last request triggered
        }
    }
    
    public void sendEmailAlert(Schedule schedule){
        try {
            String customEmailTemplate = getEmailAlertTemplate().replace("%service_name%", schedule.getName());
            customEmailTemplate = customEmailTemplate.replace("%service_url%", schedule.getRequestUrl());

            HashMap<Message.RecipientType, String> recipients = new HashMap<>();
            for(String emailAlert : schedule.getEmailAlertList()){
                recipients.put(Message.RecipientType.TO, emailAlert);
            }            
            emailService.sendEmail(
                    EMAIL_FROM_WHEN_SCHEDULES_FAIL,
                    DEFAULT_EMAIL_PERSONAL,
                    recipients,
                    DEFAULT_EMAIL_SUBJECT,
                    customEmailTemplate
            );
        } catch (MessagingException | UnsupportedEncodingException ex) {
            //TODO: Log exception
            String errorMessage = ex.toString();
        }
    }
    
    public String getEmailAlertTemplate(){
        String template = "";
        template += "<!DOCTYPE html>";
        template += "<html>";
        template += "<body>";
        template += "<p>Fallo al realizar petición al servicio <b>%service_name%</b> con url <b>%service_url%</b></p>";
        template += "</body>";
        template += "</html>";
        return template;
    }
    
}
