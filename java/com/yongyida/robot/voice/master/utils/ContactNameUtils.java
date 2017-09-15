package com.yongyida.robot.voice.master.utils;

import java.util.ArrayList;

import com.yongyida.robot.voice.master.application.MasterApplication;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.text.TextUtils;

public class ContactNameUtils {
	/** 获取库Phone表字段 **/
	private static final String[] PHONES_PROJECTION = new String[] {
			Phone.DISPLAY_NAME, Phone.NUMBER, Photo.PHOTO_ID, Phone.CONTACT_ID };
	/** 联系人显示名称 **/
	private static final int PHONES_DISPLAY_NAME_INDEX = 0;

	/** 电话号码 **/
	private static final int PHONES_NUMBER_INDEX = 1;


	
	 //查询指定电话的联系人姓名，邮箱
    public static String testContactNameByNumber(String number) {
    	Context context = MasterApplication.mAppContext;
        String name = "";
        Uri uri = Uri.parse("content://com.android.contacts/data/phones/filter/" + number);
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(uri, new String[]{"display_name"}, null, null, null);
        if (cursor.moveToFirst()) {
            name = cursor.getString(0);
            MyLog.i("TelephoneReceiver", name);
        }else{
        	 
        	name = "";
        }
        cursor.close();
		return name;
    }
    
    /** 得到手机通讯录联系人信息拨打电话 **/
	public static String getPhoneContactsCallPhone(String name) {
		Context context = MasterApplication.mAppContext;
		ContentResolver resolver =context.getContentResolver();

		Cursor phoneCursor = resolver.query(Phone.CONTENT_URI,
				PHONES_PROJECTION, null, null, null);
		if (phoneCursor != null) {
			while (phoneCursor.moveToNext()) {

				// 得到手机号码
				String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);

				// 当手机号码为空的或者为空字段 跳过当前循环
				if (TextUtils.isEmpty(phoneNumber))
					continue;
				// 得到联系人名称
				String contactName = phoneCursor
						.getString(PHONES_DISPLAY_NAME_INDEX);

				if (name.equals(contactName)) {

					
					return phoneNumber;
					
				}
			}

		}
		return null;

	}

}
