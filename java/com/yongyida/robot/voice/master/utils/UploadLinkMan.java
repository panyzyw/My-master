package com.yongyida.robot.voice.master.utils;

import android.content.Context;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.LexiconListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.util.ContactManager;
import com.iflytek.cloud.util.ContactManager.ContactListener;
import com.yongyida.robot.voice.master.application.MasterApplication;

public class UploadLinkMan {
	
	private static SpeechRecognizer mIat;
	//上传联系人
	public static void isUploadLinkMan(Context context){
		
		 mIat = SpeechRecognizer.createRecognizer(context,
				mInitListener);

		ContactManager mgr = ContactManager.createManager(context,
				mContactListener);

		mgr.asyncQueryAllContactsName();
		
	}
	
	/**
	 * 初始化监听器。
	 */
	public static InitListener mInitListener = new InitListener() {

		@Override
		public void onInit(int code) {
			MyLog.i("TelephoneRunnable", "SpeechRecognizer init() code = " + code);
			if (code != ErrorCode.SUCCESS) {
				// showTip("初始化失败，错误码："- + code);

			}
		}
	};

	/**
	 * 获取联系人监听器。
	 */
	private static ContactListener mContactListener = new ContactListener() {

		@Override
		public void onContactQueryFinish(String contactInfos, boolean changeFlag) {

			MyLog.i("TelephoneRunnable","enter  onContactQueryFinish");
			MyLog.i("TelephoneRunnable",contactInfos);

			
			MyLog.i("TelephoneRunnable",changeFlag + "");
			mIat.setParameter(SpeechConstant.ENGINE_TYPE,
					SpeechConstant.TYPE_CLOUD);
			mIat.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");
			mIat.updateLexicon("contact", contactInfos, mLexiconListener);

		}
	};

	/**
	 * 上传联系人/词表监听器。
	 */
	private static LexiconListener mLexiconListener = new LexiconListener() {

		@Override
		public void onLexiconUpdated(String lexiconId, SpeechError error) {
			if (error != null) {
				// showTip(error.toString());
				
				MyLog.i("TelephoneRunnable","enter onLexiconUpdated  error =" + error.toString());
			} else {
				// showTip("上传成功");
				
				MyLog.i("TelephoneRunnable","SUCCESS Upload");
			}
		}
	};

}
