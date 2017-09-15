package com.yongyida.robot.voice.master.Container;

import java.util.HashMap;

public class ServiceMap {
	
	private static ServiceMap mInstance = new ServiceMap();
	
	private  HashMap<String , Class<?>> mHashMap;
	
	private ServiceMap(){
		mHashMap = new HashMap<String, Class<?>>();
	}
	
	public static ServiceMap getInstance(){ 
		return mInstance;
	}
	
	public void register( String serverName, Class<?> serverClass){
		if(!mHashMap.containsKey(serverName));
			mHashMap.put(serverName, serverClass);	
	}
	
	public Class<?> getServerClass(String serverName){
		return mHashMap.get(serverName);
	}
}
