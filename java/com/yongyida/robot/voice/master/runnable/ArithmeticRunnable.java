package com.yongyida.robot.voice.master.runnable;

import org.json.JSONObject;
import org.json.JSONTokener;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;

import com.iflytek.cloud.SpeechError;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.yongyida.robot.voice.master.application.MasterApplication;
import com.yongyida.robot.voice.master.bean.ArithmeticBean;
import com.yongyida.robot.voice.master.constant.Constant;
import com.yongyida.robot.voice.master.constant.MyIntent;
import com.yongyida.robot.voice.master.reciver.MyBroadcastReceiver;
import com.yongyida.robot.voice.master.recognizer.MySpeechRecognizer;
import com.yongyida.robot.voice.master.recognizer.MySpeechRecognizer.MyRecognizerListener;
import com.yongyida.robot.voice.master.utils.BeanUtils;
import com.yongyida.robot.voice.master.utils.Domain;
import com.yongyida.robot.voice.master.utils.MyExceptionUtils;
import com.yongyida.robot.voice.master.utils.MyLog;
import com.yongyida.robot.voice.master.utils.Utils;
import com.yongyida.robot.voice.master.voice.BaseVoice;
import com.yongyida.robot.voice.master.voice.TTS;
import com.yongyida.robot.voice.master.voice.TTS.MySynthesizerListener;
import com.yongyida.robot.voice.master.voice.VoiceQueue;

public class ArithmeticRunnable extends BaseRunnable {

	public static String mAnswer = "";
	private int count;//在服务器地址后面添加count参数，以解决重复请求同一个算术题
    private ArithmeticBean mArithmeticBean;
	public ArithmeticRunnable(String result) {
		mIsEndSendRecycle = false;
		mPriority = PRIORITY_OTHER;
		mInterrupt_state = INTERRUPT_STATE_STOP;

		mResult = result;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		MyLog.i("ArithmeticRunnable", "run()");
		ArithmeticBean arithmeticBean = BeanUtils.parseArithmeticJson(mResult,ArithmeticBean.class);
        mArithmeticBean = arithmeticBean;
		if (arithmeticBean == null) {
			MyLog.e("ArithmeticRunnable", "arithmeticBean == null");
			mIsEndSendRecycle = true;
			voiceTTS(Constant.comman_unkonw, new MySynthesizerListener());
			return;
		}

		registeReceiver();

		doArithmeticBean(arithmeticBean);
	}

	private void registeReceiver() {
		registeArithmeticQuestionCompleteReceiver();
		registeArithmeticAnswerCompleteReceiver();
	}

	private void registeArithmeticQuestionCompleteReceiver() {
		MyLog.i("ArithmeticRunnable", "registeQuestionReceiver");
		MyBroadcastReceiver receiver = new MyBroadcastReceiver(
				"ArithmeticQuestionCompleteReceiver") {
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub

				if (intent == null
						|| !intent.getAction().equals(
								MyIntent.ARITHMETIC_QUESTION_COMPLETE)) {
					return;
				}

				// 开启监听(监听回答)
				if (MySpeechRecognizer.getInstance().isSR()) {
					MySpeechRecognizer.getInstance().stopSR();
				}

				MySpeechRecognizer.getInstance(new MyRecognizerListener() {

					@Override
					public void onError(SpeechError error) {
						// TODO Auto-generated method stub
						MyLog.e("ArithmeticQuestionCompleteReceiver",
								"语音识别异常 , msg : " + error.getMessage()
										+ ",code : " + error.getErrorCode());
						super.onError(error);
					}

					@Override
					public void onResult(String results) {
						// TODO Auto-generated method stub

						String answerResult;
						if (results.contains(mAnswer)) {
							answerResult = "小朋友你真棒,回答正确,要再接再厉哦";
						} else {
							answerResult = "回答错误,正确答案是" + mAnswer
									+ " , ,  要加油哦,你行的";
						}

						voiceTTS(answerResult, new MySynthesizerListener() {
							@Override
							public void onCompleted(SpeechError error) {
								// TODO Auto-generated method stub

								if (error != null) {
									MyLog.e("ArithmeticRunnable",
											"说结果时 语音合成异常 , msg : "
													+ error.getMessage()
													+ ",code : "
													+ error.getErrorCode());
									return;
								}

								// 发送 回答完 广播
								Utils.sendBroadcast(MyIntent.ARITHMETIC_ANSWER_COMPLETE);

								super.onCompleted(error);
							}
						});

						super.onResult(results);
					}

				}).startSR();

				super.onReceive(context, intent);
			}
		};
		// 实例化过滤器并设置要过滤的广播
		IntentFilter intentFilter = new IntentFilter(
				MyIntent.ARITHMETIC_QUESTION_COMPLETE);
		registerBroadcast(receiver);

		// 注册到自己的广播队列(统一管理)
		MasterApplication.mAppContext.registerReceiver(receiver, intentFilter);
	}

	private void registeArithmeticAnswerCompleteReceiver() {
		MyLog.i("ArithmeticRunnable", "registeQuestionReceiver");
		MyBroadcastReceiver receiver = new MyBroadcastReceiver(
				"ArithmeticAnswerCompleteReceiver") {
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub

				if (intent == null
						|| !intent.getAction().equals(
								MyIntent.ARITHMETIC_ANSWER_COMPLETE)) {
					return;
				}

				requestArithmetic();

				super.onReceive(context, intent);
			}
		};
		// 实例化过滤器并设置要过滤的广播
		IntentFilter intentFilter = new IntentFilter(MyIntent.ARITHMETIC_ANSWER_COMPLETE);
		registerBroadcast(receiver);

		// 注册到自己的广播队列(统一管理)
		MasterApplication.mAppContext.registerReceiver(receiver, intentFilter);
	}

	private void doArithmeticBean(ArithmeticBean arithmeticBean) {
		requestArithmetic();
	}

	public void requestArithmetic() {
		try {
			HttpUtils httputils = new HttpUtils();
            count++;//在服务器地址后面添加count参数，以解决重复请求同一个算术题
			httputils.send(HttpMethod.GET, Domain.getArithmeticRequestURl()+"?count="+count,
					new RequestCallBack<String>() {
						@Override
						public void onFailure(HttpException arg0, String msg) {
							MyLog.e("ArithmeticRunnable", msg);
							voiceTTS("访问服务器资源失败");
						}

						@Override
						public void onSuccess(ResponseInfo<String> responseInfo) {
							MyLog.i("ArithmeticRunnable",
									"responseInfo result : "
											+ responseInfo.result);
							try {
								JSONTokener jsontokener = new JSONTokener(
										responseInfo.result);
								JSONObject jsonobject = new JSONObject(
										jsontokener);

								String ret = jsonobject.getString("ret");
								if (!ret.equals("0")) {
									voiceTTS("抱歉,没有找到算术学习资源");
									return;
								}
								String question = jsonobject
										.getString("question");
								String answer = jsonobject.getString("answer");
								String algorithm = jsonobject
										.getString("algorithm");
								if (question == null || answer == null
										|| algorithm == null) {
									voiceTTS("抱歉,算术学习资源异常");
									return;
								}
								mAnswer = answer;
								// 考虑到语音合成问题,替换加减乘除+-*/
								question = replaceSymbol(question, algorithm);
								if (TextUtils.isEmpty(question)) {
									voiceTTS("抱歉,算术学习资源异常");
									return;
								}
								voiceTTS("小朋友 : " + question + "等于多少?",
										new MySynthesizerListener() {
											public void onCompleted(
													SpeechError error) {
												if (error != null) {
													MyLog.e("ArithmeticRunnable",
															"说结果时 语音合成异常 , msg : "
																	+ error.getMessage()
																	+ ",code : "
																	+ error.getErrorCode());
													return;
												}
												// 发送问问题结束的广播
												Utils.sendBroadcast(MyIntent.ARITHMETIC_QUESTION_COMPLETE);
											};
										});
							} catch (Throwable e) {
								MyLog.e("NewsRunnable",
										MyExceptionUtils.getStringThrowable(e));
								voiceTTS("抱歉,没有找到新闻");
							}

						}
					});
		} catch (Throwable e) {
			MyLog.e("ArithmeticRunnable",
					MyExceptionUtils.getStringThrowable(e));
		}

	}

	protected String replaceSymbol(String question, String algorithm) {
		if (algorithm.equals("+")) {
			return question.replace(algorithm, "加");
		}

		if (algorithm.equals("-")) {
			return question.replace(algorithm, "减");
		}

		if (algorithm.equals("*")) {
			return question.replace(algorithm, "乘");
		}

		if (algorithm.equals("/")) {
			return question.replace(algorithm, "除");
		}

		MyLog.e("", "replaceSymbol 不是加减乘除运算");
		return null;
	}

	public void voiceTTS(String text,
			MySynthesizerListener mySynthesizerListener) {
		MyLog.i("ArithmeticRunnable", "voiceTTS");
		if (mySynthesizerListener == null)
			mySynthesizerListener = new MySynthesizerListener();
		TTS tts = new TTS(text, BaseVoice.TTS, mInterrupt_state, mPriority,
				mIsEndSendRecycle, mySynthesizerListener);
		VoiceQueue.getInstance().add(tts);
        Utils.collectInfoToServer(mArithmeticBean,text);
	}

	public void voiceTTS(String text) {
		voiceTTS(text, null);
	}
}
