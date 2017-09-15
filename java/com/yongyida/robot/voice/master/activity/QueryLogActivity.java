package com.yongyida.robot.voice.master.activity;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yongyida.robot.voice.master.constant.MyData;
import com.yongyida.robot.voice.master1.R;

public class QueryLogActivity extends Activity{

	protected static final int QUERY = 0;
	
	TextView tv_log;
	CheckBox cb_datetime;
	CheckBox cb_package;
	CheckBox cb_module;
	CheckBox cb_class;
	CheckBox cb_fun;
	CheckBox cb_info;
	CheckBox cb_tag;
	
	EditText et_datetime;
	EditText et_package;
	EditText et_module;
	EditText et_class;	
	EditText et_fun;
	EditText et_info;
	EditText et_tag;
	
	String filter_datetime;
	String filter_package;
	String filter_module;
	String filter_class;	
	String filter_fun;
	String filter_info;
	String filter_tag;
	
	Uri uri = Uri.parse("content://" + MyData.VOICE_CONTENT_PROVIDER_AUTHORITY + "/voice_log");
	StringBuffer result; 
	
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case QUERY:
				getQueryLogShow();
				break;
			}
		};
	};
	
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_querylog);
        
        init();
    }

	private void init() {
		tv_log = (TextView) findViewById(R.id.tv_log);
		cb_datetime = (CheckBox) findViewById(R.id.cb_datetime);
		cb_package = (CheckBox) findViewById(R.id.cb_package);
		cb_module = (CheckBox) findViewById(R.id.cb_module);
		cb_class = (CheckBox) findViewById(R.id.cb_class);
		cb_fun = (CheckBox) findViewById(R.id.cb_fun);
		cb_info = (CheckBox) findViewById(R.id.cb_info);
		cb_tag = (CheckBox) findViewById(R.id.cb_tag);
		
		et_datetime = (EditText)findViewById(R.id.et_datetime);
		et_package = (EditText)findViewById(R.id.et_package);
		et_module = (EditText)findViewById(R.id.et_module);
		et_class = (EditText)findViewById(R.id.et_class);	
		et_fun = (EditText)findViewById(R.id.et_fun);
		et_info = (EditText)findViewById(R.id.et_info);
		et_tag = (EditText)findViewById(R.id.et_tag);
	}

	public void onQueryLog(View v){	
		filter_datetime = et_datetime.getText().toString();
		filter_package = et_package.getText().toString();
		filter_module = et_module.getText().toString();
		filter_class = et_class.getText().toString();
		filter_fun = et_fun.getText().toString();
		filter_info = et_info.getText().toString();
		filter_tag = et_tag.getText().toString();
		
		ArrayList<String> list = new ArrayList<String>();		
		String selection = "datetime >= '" + filter_datetime + "'"; //过滤时间
		list.add("datetime");
		
		if(cb_package.isChecked()){
			if(!TextUtils.isEmpty(filter_package))
				selection += " and package_name like '%" + filter_package + "%'"; 
			list.add("package_name");
		}
		if(cb_module.isChecked()){
			if(!TextUtils.isEmpty(filter_module))
				selection += " and module_name like '%" + filter_module +"%'";
			list.add("module_name");
		}
		if(cb_class.isChecked()){
			if(!TextUtils.isEmpty(filter_class))
				selection += " and class_name like '%" + filter_class +"%'";
			list.add("class_name");
		}
		if(cb_fun.isChecked()){
			if(!TextUtils.isEmpty(filter_fun))
				selection += " and fun_name like '%" + filter_fun +"%'";
			list.add("fun_name");
		}
		
		if(!TextUtils.isEmpty(filter_info))
			selection += " and info like '%" + filter_info +"%'";
		list.add("info");

		if(cb_tag.isChecked()){
			if(!TextUtils.isEmpty(filter_tag))
				selection += " and tag like '%" + filter_tag +"%'";			
		}		
		
		if(list.size() <= 0)
			return ;
		
        String[] projection = new String[list.size()]; 
        for( int i = 0; i < list.size(); i++){
        	projection[i] = list.get(i);
        }
        
        ContentResolver resolver = QueryLogActivity.this.getContentResolver();
        Log.e("123", "selection = " + selection);
        Cursor cursor = resolver.query(uri, projection, selection, null, null);  
        if(cursor == null){
        	Log.e("123", "cursor == null");
        	return ;
        } 

        int infoIndex = cursor.getColumnIndex("info");  
        int tagIndex = cursor.getColumnIndex("tag");  
                
        cursor.moveToFirst();   
        result = null;
        result = new StringBuffer();

		while (!cursor.isAfterLast()) {
			result.append("[ ");
			for( int i = 0; i < cursor.getColumnCount(); i++ ){
				if(i == tagIndex){
					continue;
				}
				if( i == infoIndex ){
					result.append(" ] : ");
				}
				result.append(cursor.getString(i) + "  ");
			}
			
			result.append("\n");
			cursor.moveToNext();
		}  

		//显示到文本框
		handler.sendEmptyMessage(QUERY);
	}

	/**
	 * 从内容提供者读取数据,写到tv_log
	 */
	void getQueryLogShow(){
		tv_log.setText(result);
	}
	
	/**
	 * 导出日志
	 * @param v
	 */
	public void exportLog(View v){
		Context context = getApplicationContext();		
        try {          
        	String sdCard = Environment.getExternalStorageDirectory().toString();
            FileWriter writer = new FileWriter( sdCard + "/" + context.getPackageName() + ".txt", false);
            writer.write(result.toString());
            writer.close();
            Toast.makeText(getApplicationContext(), "导出完成", 1).show();
        } catch (IOException e) {
        	Log.e("123", "write exception");
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "异常", 1).show();
        }       
	}
	
	//清除
	public void clearText(View v){
		tv_log.setText("");
	}

	public void onClearLog(View v){
		tv_log.setText("");
        ContentResolver resolver = QueryLogActivity.this.getContentResolver();
        resolver.delete(uri, "1=1", null);
		Toast.makeText(this, "清空日志文件", 1).show();
	}
	
	public void quit(View v){
		finish();
	}

}
