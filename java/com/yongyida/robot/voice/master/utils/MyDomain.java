package com.yongyida.robot.voice.master.utils;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.widget.Toast;

import com.yongyida.robot.voice.master.application.MasterApplication;

public class MyDomain {
	
	private static MyDomain instance;
	
	public static MyDomain getInstance(){
		if(instance==null){
			instance = new MyDomain();
		}
		return instance;
	}
	
	private String http_server_host = "";
	private String http_resouce_host = "";
		
	private MyDomain(){
		Uri uri = Uri.parse("content://com.yongyida.robot.voice.master.httprequest/http_request");
		ContentResolver resolver = MasterApplication.mAppContext.getContentResolver();
		Cursor cursor = resolver.query(uri, null, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			// socket 端口号 String port_socket = cursor.getString(cursor.getColumnIndex("port_socket"));
			// socket 域名 String robot_tcp_host = cursor.getString(cursor.getColumnIndex("robot_tcp_host"));
			// http 域名
			http_server_host = cursor.getString(cursor.getColumnIndex("http_server_host"));
			// http 静态资源域名
			http_resouce_host = cursor.getString(cursor.getColumnIndex("http_resouce_host"));
			cursor.close();
		}else{ 	
			Toast.makeText(MasterApplication.mAppContext, "获取服务器地址失败 *_*",1).show();
		}
	}
	
	public String musicQuestUrl(){
		return "http://" + http_server_host + "/music/query";
	}
	
	public String musicResourceUrl(){
		return "http://" + http_resouce_host + "/resource/music/";
	}
	
	public String storyQuestUrl(){
		return "http://" + http_server_host + "/story/query";
	}
	
	public String storyResourceUrl(){
		return "http://" + http_resouce_host + "/resource/story/";
	}
}
