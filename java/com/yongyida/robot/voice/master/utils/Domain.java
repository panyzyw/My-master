package com.yongyida.robot.voice.master.utils;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.yongyida.robot.voice.master.application.MasterApplication;

public class Domain {

	public static String getServerHost(){		
		//Uri uri = Uri.parse("content://com.yongyida.robot.voice.master.httprequest/http_request");
		Uri uri = Uri.parse("content://com.yongyida.robot.voice.request/http_request");
		ContentResolver resolver = MasterApplication.mAppContext.getContentResolver();
		Cursor cursor = resolver.query(uri, null, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			//String http_server_host = cursor.getString(cursor.getColumnIndex("http_server_host"));
			String http_server_host = cursor.getString(cursor.getColumnIndex("httpRequest"));
			MyLog.i("Domain", "http_server_host = " + http_server_host);
			cursor.close();
			return http_server_host;
		}else{
			MyLog.e("Domain", "getServerHost fail and use default url");
			return "120.24.242.163/ftp";
		}
	}
	
	public static String getServerResourceHost(){
		//Uri uri = Uri.parse("content://com.yongyida.robot.voice.master.httprequest/http_request");
        Uri uri = Uri.parse("content://com.yongyida.robot.voice.request/http_request");
		ContentResolver resolver = MasterApplication.mAppContext.getContentResolver();
		Cursor cursor = resolver.query(uri, null, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			//String http_resouce_host = cursor.getString(cursor.getColumnIndex("http_resouce_host"));
			String http_resouce_host = cursor.getString(cursor.getColumnIndex("httpResource"));
			MyLog.i("Domain", "http_resouce_host = " + http_resouce_host);
			cursor.close();
			return http_resouce_host;
		}else{
			return "120.24.242.163/ftp/resource";
		}
	}

    public static String getRid(){
        Uri uri = Uri.parse("content://com.yongyida.robot.voice.request/http_request");
        ContentResolver resolver = MasterApplication.mAppContext.getContentResolver();
        Cursor cursor = resolver.query(uri, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String rid = cursor.getString(cursor.getColumnIndex("rid"));
            MyLog.i("Domain", "rid = " + rid);
            cursor.close();
            return rid;
        }else{
            MyLog.e("Domain", "getRid fail and use default 123456");
            return "123456";
        }
    }

	public static String getMusicRequestURl(){
		String url = "http://" + getServerHost() + "/music/query";
		MyLog.i("Domain", "url = " + url);
		return url;	
	}
	
	public static String getMusicResourceURl(){
		String url ="http://" + getServerResourceHost() + "/resource/music/";
		MyLog.i("Domain", "url = " + url);
		return url;	
	}
	
	public static String getJokeRequestURl(){
		String url = "http://" + getServerHost() + "/joke/query";
		MyLog.i("Domain", "url = " + url);
		return url;	
	}

	public static String getStoryRequestURl(){
		String url = "http://" + getServerHost() + "/story/query";
		MyLog.i("Domain", "url = " + url);
		return url;
	}
									 
	public static String getStoryResourceURl(){
		String url = "http://" + getServerResourceHost() + "/resource/story/";
		MyLog.i("Domain", "url = " + url);
		return url;
	}
	
	public static String getOperaRequestURl(){
		String url = "http://" + getServerHost() + "/chinese_opera/query";
		MyLog.i("Domain", "url = " + url);
		return url;
	}
	
	public static String getOperaResourceURl(){
		String url = "http://" + getServerResourceHost() + "/resource/chinese_opera/";
		MyLog.i("Domain", "url = " + url);
		return url;
	}
	
	public static String getSquaredanceRequestURl(){
		String url = "http://" + getServerResourceHost() + "/resource/square_dance.list";
		MyLog.i("Domain", "url = " + url);
		return url;
	}

	public static String getSquaredanceResourceURl(){
		String url = "http://" + getServerResourceHost() + "/resource/square_dance/";
		MyLog.i("Domain", "url = " + url);
		return url;
	}
	
	public static String getPoetryLearn(){
		String url = "http://" + getServerHost() + "/poetry/query";
		MyLog.i("Domain", "url = " + url);
		return url;
	}
	
	public static String getSinology(){
		String url = "http://" + getServerHost() + "/poetry/query";
		MyLog.i("Domain", "url = " + url);
		return url;
	}
	
	public static String getHealthResourceListURl(){
		String url = "http://" + getServerResourceHost() + "/resource/health.list";
		MyLog.i("Domain", "url = " + url);
		return url;
	}
	
	public static String getHealthResourceURl(){
		String url = "http://" + getServerResourceHost() + "/resource/health/";
		MyLog.i("Domain", "url = " + url);
		return url;
	}

	public static String getMoveInfoRequestURl(){
		String url = "http://" + getServerHost() + "/screen/query";
		MyLog.i("Domain", "url = " + url);
		return url;
	}
	
	public static String getNewsRequestURl(){
		String url = "http://" + getServerHost() + "/news/query";
		MyLog.i("Domain", "url = " + url);
		return url;
	}
	
	public static String getHabitResourceListURl(){
		String url = "http://" + getServerResourceHost() + "/resource/habit.list";
		MyLog.i("Domain", "url = " + url);
		return url;
	}
	
	public static String getHabitResourceURl(){
		String url = "http://" + getServerResourceHost() + "/resource/habit/";
		MyLog.i("Domain", "url = " + url);
		return url;
	}
	
	public static String getArithmeticRequestURl(){
		String url = "http://" + getServerHost() + "/arithmetic/question";
		MyLog.i("Domain", "url = " + url);
		return url;
	}
	
	public static String getTranslateBaseUrl(){
		// 翻译网络地址
		String httpUrl = "http://fanyi.youdao.com/openapi.do?keyfrom=yongyidajiqiren&key=1993764552&type=data&doctype=json&version=1.1&only=translate";		
		return httpUrl;
	}
	
	public static String getWeatherUrl(){
		String url = "http://" + getServerHost() + "/weather/query";
		MyLog.i("Domain", "url = " + url);
		return url;
	}
	
	public static String getCookRequestURl(){
		String url = "http://" + getServerHost() + "/menu/query";
		MyLog.i("Domain", "url = " + url);
		return url;	
	}

    public static String getStockRequestUrl(){
        String url = "http://" + getServerHost() + "/stock/query";
        MyLog.i("Domain", "url = " + url);
        return url;
    }
}
