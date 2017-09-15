package com.yongyida.robot.voice.master.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JudgeNumberUtils {
	
//注明：当用户问：1234567。科大讯飞返回的是打电话的数据。所以为了反正用户后随机说这样的数字的时候。就得进行屏蔽
//	{
//		  "semantic": {
//		    "slots": {
//		      "code": "1234567"
//		    }
//		  }, 
//		  "rc": 0, 
//		  "operation": "CALL", 
//		  "service": "telephone", 
//		  "text": "1234567"
//		}
	 public static boolean isNumeric(String str){ 
		 
		 
		 Pattern pattern = Pattern.compile("[0-9]*|[一二三四五六七八九十]*|[壹贰叁肆伍陆柒捌玖拾]*");    
		   
         Matcher isNum = pattern.matcher(str);   
   
         if( !isNum.matches() ){   
   
             return false;    
  
         }    
  
        return true;    
      }   

	

}
