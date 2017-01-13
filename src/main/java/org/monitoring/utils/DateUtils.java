package org.monitoring.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    
    public static DateUtils INSTANCE = new DateUtils();
    private final String DEFAULT_DATE_FORMAT = "dd/MM/yy";
    
    private DateUtils(){}
    
    public String toString(Date date){
        return toString(date, this.DEFAULT_DATE_FORMAT);
    }
    
    public String toString(Date date, String dateFormat){
        return new SimpleDateFormat(dateFormat).format(date);
    }
    
    public Date toDate(String strDate){
        return new Date(strDate);
    }
    
}
