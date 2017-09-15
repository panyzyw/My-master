package com.yongyida.robot.voice.master.utils.runnableutils;

import android.content.Context;

import com.yongyida.robot.voice.master.constant.Constant;

public class SmarthomeUtils {
	
	/**
	 * 语音提示的内容
	 * @param device
	 * @param action
	 * @return
	 */
	public static String voiceTip(Context context, String device , String action ){
		if( device.equals("AIR_CONDITIONER") ){
			return acVoiceTip(context,action);
		}else if( device.equals("TV") ){
			return tvVoiceTip(action);
		}else if( device.equals("STB") ){	//机顶盒	
			return stbVoiceTip(action);
		}
		return Constant.not_find_device;
	}
	
	/**
	 * 获取适当的温度
	 * @param context
	 * @param currentTemp
	 * @return
	 */
	public static int getRightTemp( Context context,String currentTemp ){
		String minMax = context.getSharedPreferences("smarthome", Context.MODE_PRIVATE).getString("AIR_CONDITIONER", "17,30");
		String[] mm = minMax.split(",");
		int min = Integer.parseInt(mm[0]);
		int max = Integer.parseInt(mm[1]);
		int cur = Integer.parseInt(currentTemp);
		if( cur < min )
			cur = min;
		else if( cur > max )
			cur = max;
		return cur;
	}

	//电视提示
	public static String tvVoiceTip(String action ){
		String tipContent = Constant.know;
		if( action.equals("open") ){
			tipContent += Constant.openTV;
		}else if( action.equals("stop") ){
			tipContent += Constant.closeTV;
		}else if(action.equals("menu") ){
			tipContent += Constant.openMenu;
		}else if( action.equals("signalsource") ){
			tipContent += Constant.openSignal;
		}else if( action.equals("mute") ){
			tipContent += Constant.setTVMute;
		}else if( action.equals("up") ){
			tipContent = Constant.up;  // 上下左右 不要提示
		}else if( action.equals("down") ){
			tipContent =Constant.down;
		}else if( action.equals("left") ){
			tipContent =Constant.left;
		}else if( action.equals("right") ){
			tipContent =Constant.right;
		}else if( action.contains("turnup") ){
			//...
			String[] arr = action.split(":");
			if( arr.length == 2){
				tipContent += Constant.turnup + arr[1];
			}else{
				tipContent += Constant.turnup + 1;
			}					
		}else if(action.contains("turndown") ){
			//...
			String[] arr = action.split(":");
			if( arr.length == 2){
				tipContent += Constant.turndown + arr[1];
			}else{
				tipContent += Constant.turndown + 1;
			}		
		}else if( action.equals("homepage") ){
			tipContent += Constant.openHomepage;
		}else if( action.equals("back") ){
			tipContent += Constant.back;
		}else if(action.equals("twoback") ){
			tipContent += Constant.quit;
		}else if(action.equals("ok") ){
			tipContent += Constant.ok;
		}else if( action.equals("next") ){
			tipContent += Constant.changeNextChannel;
		}else if( action.equals("prev") ){
			tipContent += Constant.changePrevChannel;
		}else if( action.contains("channel") ){
			//...
			String[] arr = action.split(":");
			if( arr.length == 2){
				tipContent += Constant.TVSetChannel + arr[1] + Constant.channel;
			}else{
				tipContent += Constant.channelUnknow;
			}	
		}else if( action.contains("volumeup") ){
			//获取数值
			String[] arr = action.split(":");
			if( arr.length == 2){
				tipContent += Constant.turnupVolum +  arr[1];
			}else{
				tipContent += Constant.turnupVolum +  1;
			}	
		}else if( action.contains("volumedown") ){
			//...
			String[] arr = action.split(":");
			if( arr.length == 2){
				tipContent += Constant.turndownVolum +  arr[1];
			}else{
				tipContent += Constant.turndownVolum +  1;
			}					
		}				
		return tipContent;	
	}	
	
	//空调提示
	public static String acVoiceTip( Context context,String action ){
		String tipContent = Constant.know;
		if( action.equals("open") ){
			tipContent += Constant.openAircondition;
		}else if( action.equals("stop") ){
			tipContent += Constant.closeAircondition;
		}else if(action.equals("cool") ){
			tipContent += Constant.cool;
		}else if( action.equals("hot") ){
			tipContent += Constant.hot;
		}else if( action.equals("auto") ){
			tipContent += Constant.auto;
		}else if( action.equals("dry") ){
			tipContent += Constant.dry;
		}else if( action.equals("blowwind") ){
			tipContent += Constant.blowwind;
		}else if( action.equals("sendwind") ){ //自动风速或送风
			tipContent += Constant.windAuto;
		}else if( action.equals("strongwind") ){
			tipContent += Constant.strongWind;
		}else if( action.equals("midwind") ){
			tipContent += Constant.midWind;
		}else if( action.equals("lowwind") ){
			tipContent += Constant.lowWind;
		}else if(action.equals("windup") ){
			tipContent += Constant.turnUpWindSpeed;
		}else if( action.equals("winddown") ){
			tipContent += Constant.turnDownWindSpeed;
		}else if( action.equals("windvertical") ){
			tipContent += Constant.verticalWind;
		}else if(action.equals("windhorizontal") ){
			tipContent += Constant.horizontalWind;
		}else if( action.equals("windsweeper") ){
			tipContent += Constant.openWind;
		}else if( action.equals("stopwind") ){
			tipContent += Constant.stopWind;
		}else if( action.equals("query_degree") ){
			tipContent += Constant.queryTemperatrue;
		}else if( action.contains("tempset") ){
			//获取温度值
			String[] arr = action.split(":");
			if( arr.length == 2){
				tipContent += Constant.setTemperatrue +  SmarthomeUtils.getRightTemp(context,arr[1]) + Constant.temperatrue;
			}else{
				tipContent += Constant.temperatrueUnkonw;
			}		
		}else if( action.contains("tempup") ){
			//获取温度值
			String[] arr = action.split(":");
			
			if( arr.length == 2){
				tipContent += Constant.turnupTemperatrue +  arr[1] + Constant.temperatrue;
			}else{
				tipContent += Constant.turnupTemperatrue +  1 + Constant.temperatrue;
			}					
		}else if( action.contains("tempdown") ){
			//获取温度值
			String[] arr = action.split(":");
			if( arr.length == 2){
				tipContent += Constant.turndownTemperatrue +  arr[1] + Constant.temperatrue;
			}else{
				tipContent += Constant.turndownTemperatrue +  1 + Constant.temperatrue;
			}			
		}	
		return tipContent;		
	}
	
	public static String stbVoiceTip(String action){
		String tipContent = Constant.know;
		if( action.equals("open") ){
			tipContent += Constant.openSTB;
		}else if( action.equals("stop") ){
			tipContent += Constant.closeSTB;
		}
		return tipContent;
	}
	
	public static String StringtoNum( String num ){
		//将负 换为 -
		num = num.replace(Constant.negative, "-");		
		//如果字符串是 [-] 一十 / 十 / 二十 / 三十 / 四十 换为 [-] 10 20 30 40
		if( num.equals(Constant.ten_) || num.equals(Constant.negative_ten_) ){
			num = num.replace(Constant.ten_, "10");
		}else if(num.equals(Constant.ten) || num.equals(Constant.negative_ten)){
			num = num.replace(Constant.ten, "10");
		}else if(num.equals(Constant.twenty) || num.equals(Constant.negative_twenty)){
			num = num.replace(Constant.twenty, "20");
		}else if(num.equals(Constant.thirty) || num.equals(Constant.negative_thirty)){
			num = num.replace(Constant.thirty, "30");
		}else if(num.equals(Constant.forty) || num.equals(Constant.negative_forty)){
			num = num.replace(Constant.forty, "40");
		}		
		//将一十   十换为1
		num = num.replace(Constant.ten_, "1");
		num = num.replace(Constant.ten, "1");
		//将二十   换为 2
		num = num.replace(Constant.twenty, "2");
		//将三十  换为 3
		num = num.replace(Constant.thirty, "3");
		//将四十 换为 4
		num = num.replace(Constant.forty, "3");
				
		//将一到九换为1-9
		num = num.replace(Constant.one, "1");
		num = num.replace(Constant.two, "2");
		num = num.replace(Constant.three, "3");
		num = num.replace(Constant.four, "4");
		num = num.replace(Constant.five, "5");
		num = num.replace(Constant.six, "6");
		num = num.replace(Constant.seven, "7");
		num = num.replace(Constant.eight, "8");
		num = num.replace(Constant.nine, "9");
		
		//将两换为2
		num = num.replace(Constant.two_, "2");
		return num;
	}
	
	public static String TVStringtoNum( String num ){
		if( num.equals(Constant.one) ){
			num = "1";
		}else if( num.equals(Constant.two)  ||  num.equals(Constant.two_) ){
			num = "2";
		}else if( num.equals(Constant.three) ){
			num = "3";
		}else if( num.equals(Constant.four) ){
			num = "4";
		}else if( num.equals(Constant.five) ){
			num =  "5";
		}else if( num.equals(Constant.six) ){
			num = "6";
		}else if( num.equals(Constant.seven) ){
			num = "7";
		}else if( num.equals(Constant.eight) ){
			num = "8";
		}else if( num.equals(Constant.nine) ){
			num = "9";
		}else if( num.equals(Constant.ten) ){
			num = "10";
		}else if( num.equals(Constant.eleven) ){
			num = "11";
		}
		if( !isOkNum(num) ){
			num = "-1";
		}
		return num;		
	}
	
	//判断传进的来数字字符串是否正确  ,如 "12" 正确 ,"一" 不正确
	public static boolean isOkNum(String num){
		for( int i = 0; i < num.length(); i++)
			if( !isNumChar( num.charAt(i) )   )
				return false;
		return true;		
	}
	
	//判断字符是不是数字相关
	public static boolean isNumChar(char ch){
		if(ch == '-' || ( ch >= '0' && ch <= '9' ))
			return true;		
		return false;
	}
	
	public static String tvHardLimitOperation(String action) {
		if( action.equals("max") ){
			return Constant.volumeMaxOperationTip;
		}else if( action.equals("min") ){
			return Constant.volumeMinOperationTip;
		}else{
			return Constant.volumeSetOperationTip;
		}
	}
}
