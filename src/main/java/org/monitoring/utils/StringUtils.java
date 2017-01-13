package org.monitoring.utils;

public class StringUtils {
    
    public static StringUtils INSTANCE = new StringUtils();
    
    private StringUtils(){
    }
    
    public boolean isNullOrEmpty(String str)
    {
       return str == null || str.isEmpty();
    }
}
