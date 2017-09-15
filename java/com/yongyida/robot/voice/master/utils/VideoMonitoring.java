package com.yongyida.robot.voice.master.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class VideoMonitoring {
	/**
	 * 查询机器人是否处于视频状态
	 * @return true, 正在视频中
	 *         false, 非 视频中
	 *         
	 */
	public static boolean isVideoing(Context context) {
		String value = null;
		Uri uri  = Uri.parse("content://com.yongyida.robot.video.provider/config");
		Cursor cursor = context.getContentResolver().query(uri, 
				null, 
                "name = ?", 
                new String[] { "videoing" },
                null);
		try {
			if (cursor.moveToFirst())
				value = cursor.getString(cursor.getColumnIndex("value"));
		}
		catch (Exception e) {
			Log.e(Tip.TAG, "queryVideoing error: " + e);
		}
		finally {
			if (cursor != null) {
				cursor.close();
				cursor = null;
			}
		}
		return (value != null && value.equals("true"));
	}

}
