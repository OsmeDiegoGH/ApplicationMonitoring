package org.monitoring.entities;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.monitoring.helpers.RESTRequestHelper.HTTP_REQUEST_HEADER_NAME;
import org.monitoring.helpers.RESTRequestHelper.HTTP_REQUEST_HEADER_VALUE;
import org.monitoring.helpers.RESTRequestHelper.HTTP_REQUEST_METHOD;

public class Schedule extends BaseMongoEntity {
    private String name;
    private boolean isActive;
    private Date initDate;
    private Date endDate;
    private int frecuency;//minutes
    private String requestUrl;
    private HTTP_REQUEST_METHOD requestMethod;
    private HashMap<HTTP_REQUEST_HEADER_NAME, HTTP_REQUEST_HEADER_VALUE> requestHeaders;
    private HashMap<String, String> requestBodyParameters;
    private String expectedResult;
    private List<String> emailAlertList;

    public Schedule(){};
    
    public Schedule(String name, boolean isActive, Date initDate, Date endDate, int frecuency, String requestUrl, HTTP_REQUEST_METHOD requestMethod, HashMap<HTTP_REQUEST_HEADER_NAME, HTTP_REQUEST_HEADER_VALUE> requestHeaders, HashMap<String, String> requestBodyParameters, String expectedResult, List<String> emailAlertList) {
        this.name = name;
        this.isActive = isActive;
        this.initDate = initDate;
        this.endDate = endDate;
        this.frecuency = frecuency;
        this.requestUrl = requestUrl;
        this.requestMethod = requestMethod;
        this.requestHeaders = requestHeaders;
        this.requestBodyParameters = requestBodyParameters;
        this.expectedResult = expectedResult;
        this.emailAlertList = emailAlertList;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public Date getInitDate() {
        return initDate;
    }

    public void setInitDate(Date initDate) {
        this.initDate = initDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getFrecuency() {
        return frecuency;
    }

    public void setFrecuency(int frecuency) {
        this.frecuency = frecuency;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public HTTP_REQUEST_METHOD getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(HTTP_REQUEST_METHOD requestMethod) {
        this.requestMethod = requestMethod;
    }

    public HashMap<HTTP_REQUEST_HEADER_NAME, HTTP_REQUEST_HEADER_VALUE> getRequestHeaders() {
        return requestHeaders;
    }

    public void setRequestHeaders(HashMap<HTTP_REQUEST_HEADER_NAME, HTTP_REQUEST_HEADER_VALUE> requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    public HashMap<String, String> getRequestBodyParameters() {
        return requestBodyParameters;
    }

    public void setRequestBodyParameters(HashMap<String, String> requestBodyParameters) {
        this.requestBodyParameters = requestBodyParameters;
    }

    public String getExpectedResult() {
        return expectedResult;
    }

    public void setExpectedResult(String expectedResult) {
        this.expectedResult = expectedResult;
    }
    
    public List<String> getEmailAlertList() {
        return emailAlertList;
    }

    public void setEmailAlertList(List<String> emailAlertList) {
        this.emailAlertList = emailAlertList;
    }
}
