package com.yongyida.robot.voice.master.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

public class MyExceptionUtils {
	public static String getStringThrowable(Throwable ex){
		if(ex == null){
			return "";
		}
        StringWriter sw = new StringWriter();  
        PrintWriter pw = new PrintWriter(sw, true);  
        ex.printStackTrace(pw);
        return sw.getBuffer().toString();        
	}
}
