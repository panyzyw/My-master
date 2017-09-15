package com.yongyida.robot.voice.master.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyRegularUtils {
	public static boolean myMatch(String[] regexs , String s){
		if(regexs == null || s == null){
			return false;
		}
		 Pattern pattern;
		for(String regex : regexs ){
			  pattern = Pattern.compile(regex);
			  Matcher matcher = pattern.matcher(s);
			  if(matcher.matches())
				  return true;
		}		
		return false;		
	}
}
