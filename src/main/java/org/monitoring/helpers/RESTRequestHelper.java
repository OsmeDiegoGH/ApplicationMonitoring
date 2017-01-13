package org.monitoring.helpers;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map.Entry;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.InetAddress; 
import java.net.NetworkInterface; 
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Map;
import org.monitoring.utils.StringUtils;


public class RESTRequestHelper {
    private final int CONNECTION_TIMEOUT = 1000;
    
    public static enum HTTP_REQUEST_METHOD{
        POST, GET
    }
    
    public static enum HTTP_REQUEST_HEADER_NAME{
        CONTENT_TYPE("Content-Type"),
        ACCEPT("Accept");
        
        private final String name;
        
        private HTTP_REQUEST_HEADER_NAME(String name){
            this.name = name;
        }
        
        public String getName(){
            return this.name;
        }
        
        public static HTTP_REQUEST_HEADER_NAME lookUp(String name){
            for (HTTP_REQUEST_HEADER_NAME enumElement : HTTP_REQUEST_HEADER_NAME.values()) {
                if(enumElement.name.toLowerCase().equals(name.toLowerCase())){
                    return enumElement;
                }
            }
            throw new EnumConstantNotPresentException(HTTP_REQUEST_HEADER_NAME.class, name);
        }
    }
    
    public static enum HTTP_REQUEST_HEADER_VALUE{
        FORM_DATA("multipart/form-data"),
        XXX_FORM_URLENCODED("application/x-www-form-urlencoded"),
        APPLICATION_JSON("application/json"),
        APPLICATION_XML("application/xml"),
        TEXT_PLAIN("text/plain"),
        TEXT_HTML("text/html"),
        TEXT_CSV("text/csv");
        
        private final String name;
        
        private HTTP_REQUEST_HEADER_VALUE(String name){
            this.name = name;
        }
        
        public String getName(){
            return this.name;
        }
        
        public static HTTP_REQUEST_HEADER_VALUE lookUp(String name){
            for (HTTP_REQUEST_HEADER_VALUE enumElement : HTTP_REQUEST_HEADER_VALUE.values()) {
                if(enumElement.name.toLowerCase().equals(name.toLowerCase())){
                    return enumElement;
                }
            }
            throw new EnumConstantNotPresentException(HTTP_REQUEST_HEADER_VALUE.class, name);
        }
    }
    
    public enum FORMAT_PARAMETERS{
        FORM_DATA, XML, JSON, CSV
    }
    
    public String doRequest(String url, HTTP_REQUEST_METHOD httpMethod, HashMap<String, String> headers, HashMap<String, String> params) throws Exception {
        String rawResponse = "";
        
        if( url.startsWith("https://") ){
            ignoreSSL();
        }
        
        FORMAT_PARAMETERS formatParameters = FORMAT_PARAMETERS.FORM_DATA;//default
        if(headers.containsKey("Content-Type")){
            String contentType = headers.get("Content-Type");
            if(contentType.equals(HTTP_REQUEST_HEADER_VALUE.APPLICATION_JSON.getName())){
                formatParameters = FORMAT_PARAMETERS.JSON;
            }else if(contentType.equals(HTTP_REQUEST_HEADER_VALUE.APPLICATION_XML.getName())){
                formatParameters = FORMAT_PARAMETERS.XML;
            }else if(contentType.equals(HTTP_REQUEST_HEADER_VALUE.TEXT_CSV.getName())){
                formatParameters = FORMAT_PARAMETERS.CSV;
            }else{
                formatParameters = FORMAT_PARAMETERS.FORM_DATA;
            }
        }
        String paramsParsed = formatParameters(params, formatParameters);
        
        
        if(httpMethod == HTTP_REQUEST_METHOD.GET && !StringUtils.INSTANCE.isNullOrEmpty(paramsParsed)){
            url = url + "?" + paramsParsed;
        }
        
        URL u = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        conn.setConnectTimeout(CONNECTION_TIMEOUT);
        conn.setRequestMethod(httpMethod.name());

        for (Entry<String, String> customHeader : headers.entrySet()) {
            conn.setRequestProperty(customHeader.getKey(), customHeader.getValue());
        }
        
        String clientIP = getClientIP();
        if(!StringUtils.INSTANCE.isNullOrEmpty(clientIP)){
            conn.setRequestProperty("X-IP-ORIGEN", clientIP);
        }

        if(httpMethod == HTTP_REQUEST_METHOD.POST){
            conn.setDoOutput(true);
            
            OutputStream os = conn.getOutputStream();
            OutputStreamWriter ow = new OutputStreamWriter(os);
            ow.write(paramsParsed);
            ow.flush();
            ow.close();
            os.close();
        }

        // Response
        InputStream is = conn.getInputStream();
        InputStreamReader ir = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(ir);

        String responseChunk;
        while ((responseChunk = br.readLine()) != null) {
            rawResponse = rawResponse + responseChunk;
        }

        br.close();
        ir.close();
        is.close();

        return rawResponse;
    }
    
    public void ignoreSSL() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            HostnameVerifier allHostsValid = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error [RESTRequestHelper@doRequest]: " + e.getMessage());
        } catch (KeyManagementException e) {
            System.err.println("Error [RESTRequestHelper@doRequest]: " + e.getMessage());
        }
    }
    
    private String getClientIP() { 
        String ipRequest = null; 
        
        try { 
            
            ipRequest = InetAddress.getLocalHost().getHostAddress(); 
            
            if ( ipRequest.startsWith("127.") ) { 
                /* Go harder! */ 
                
                Enumeration<NetworkInterface> nInterfaces = NetworkInterface.getNetworkInterfaces(); 
                
                while( nInterfaces.hasMoreElements() ) { 
                    
                    Enumeration<InetAddress> inetAddresses = nInterfaces.nextElement().getInetAddresses(); 
                    
                    while ( inetAddresses.hasMoreElements() ) { 
                        ipRequest = inetAddresses.nextElement().getHostAddress(); 
                        
                        if ( !ipRequest.startsWith("127.") ) 
                            return ipRequest; 
                    } 
                } 
            } 
        } catch (UnknownHostException e ) { 
            System.err.println( "[RESTServiceUtils::getClientIP()] Error: " + e.getMessage() ); 
        } catch (SocketException e) { 
            System.err.println( "[RESTServiceUtils::getClientIP()] Error: " + e.getMessage() ); 
        } 
        
        return ipRequest; 
    } 
    
    public String formatParameters(HashMap<String, String> parameters, FORMAT_PARAMETERS format) throws UnsupportedEncodingException{
        String formattedParameters = "";
        switch (format) {
            case XML:
                //TODO: implement format
                break;
            case FORM_DATA:
                //Parse params
                if(!parameters.isEmpty()){
                    for (Entry<String, String> entry : parameters.entrySet()) {
                        formattedParameters += entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), "UTF-8") + "&";
                    }
                    formattedParameters = formattedParameters.substring(0, formattedParameters.length() - 1);
                }
                break;
            case JSON:
                formattedParameters += "{";
                if(!parameters.isEmpty()){
                    for(Map.Entry<String, String> entry : parameters.entrySet()){
                        formattedParameters += "\"" + entry.getKey() + "\":\"" + entry.getValue() + "\",";
                    }
                    formattedParameters = formattedParameters.substring(0, formattedParameters.length() - 1);
                }
                formattedParameters += "}";
                break;
            case CSV:
                //TODO: implement format
                break;
        }
        return formattedParameters;
    }
}
