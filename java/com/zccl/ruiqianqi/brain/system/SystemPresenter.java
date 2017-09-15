package com.zccl.ruiqianqi.brain.system;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import com.iflytek.cloud.SpeechError;

/**
 * Created by ruiqianqi on 2017/6/12 0012.
 */

public class SystemPresenter {

    // 类标志
    private static String TAG = SystemPresenter.class.getSimpleName();

    // 获得当前应用名称
    public static final int GET_CUR_PKG = 1;

    // 单例引用
    private static SystemPresenter instance;
    // 讲话服务
    private IMainService mainService;

    private Context mContext;

    /**
     * 连接远程服务
     */
    public ServiceConnection systemConn = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mainService = IMainService.Stub.asInterface(service);
            Log.e(TAG, "onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(TAG, "onServiceDisconnected");
            mainService = null;
        }

    };

    /**
     * 构造方法
     */
    private SystemPresenter(Context context){
        mContext = context;
        init();
    }

    /**
     * 用这个用话，instance不需要用volatile修饰
     *
     * @return
     */
    public static SystemPresenter getInstance(Context context) {
        if (instance == null) {
            synchronized (SystemPresenter.class) {
                SystemPresenter temp = instance;
                if (temp == null) {
                    temp = new SystemPresenter(context);
                    instance = temp;
                }
            }
        }
        return instance;
    }

    /**
     * 初始化
     */
    private void init(){
        bindSystemService();
    }

    /**********************************************************************************************/
    /**
     * 绑定服务，这个过程竟然要10多秒
     */
    public void bindSystemService(){
        if(null == mainService) {
            ComponentName componentName = new ComponentName("com.yongyida.robot.system", "com.zccl.ruiqianqi.brain.system.MainService");
            Intent intent = new Intent();
            intent.setComponent(componentName);
            mContext.bindService(intent, systemConn, Context.BIND_AUTO_CREATE);
        }
    }

    /**
     * 解绑服务
     */
    public void unbindSystemService(){
        if(null != mainService) {
            mContext.unbindService(systemConn);
        }
    }

    /**
     * 异步回调
     * @param cmd
     * @param msg
     * @param callback
     */
    public void sendCommand(int cmd, String msg, IMainCallback callback){
        bindSystemService();
        if(null != mainService){
            try {
                mainService.sendCommand(cmd, msg, callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 同步获取
     * @param cmd
     * @param msg
     * @return
     */
    public MainBean sendCommandSync(int cmd, String msg){
        bindSystemService();
        if(null != mainService){
            try {
                return mainService.sendCommandSync(cmd, msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 发音过程回调
     * @param words
     * @param tag
     * @param ISynthesizerCallback
     */
    public void startTTS(String words, String tag, final ISynthesizerCallback ISynthesizerCallback) {
        bindSystemService();
        try {
            mainService.startTTS(words, tag, new ITtsCallback.Stub() {
                @Override
                public void OnBegin() throws RemoteException {
                    if(null != ISynthesizerCallback){
                        ISynthesizerCallback.OnBegin();
                    }
                }

                @Override
                public void OnPause() throws RemoteException {
                    if(null != ISynthesizerCallback){
                        ISynthesizerCallback.OnPause();
                    }
                }

                @Override
                public void OnResume() throws RemoteException {
                    if(null != ISynthesizerCallback){
                        ISynthesizerCallback.OnResume();
                    }
                }

                @Override
                public void OnComplete(String error, String tag) throws RemoteException {
                    if(null != ISynthesizerCallback){
                        if(TextUtils.isEmpty(error)){
                            ISynthesizerCallback.OnComplete(null);
                        }else {
                            ISynthesizerCallback.OnComplete(new SpeechError(new Exception(error)));
                        }
                    }
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发音结束回调
     * @param words
     * @param runnable
     */
    public void startTTS(final String words, final Runnable runnable) {
        bindSystemService();
        if(null != mainService){
            try {
                mainService.startTTS(words, null, new ITtsCallback.Stub() {
                    @Override
                    public void OnBegin() throws RemoteException {

                    }

                    @Override
                    public void OnPause() throws RemoteException {

                    }

                    @Override
                    public void OnResume() throws RemoteException {

                    }

                    @Override
                    public void OnComplete(String error, String tag) throws RemoteException {
                        if(null != runnable){
                            runnable.run();
                        }
                    }
                });
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 暂停
     */
    public void pauseTTS()  {
        bindSystemService();
        if(null != mainService){
            try {
                mainService.pauseTTS();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 恢复
     */
    public void resumeTTS() {
        bindSystemService();
        if(null != mainService){
            try {
                mainService.resumeTTS();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 停止
     */
    public void stopTTS(String from) {
        bindSystemService();
        if(null != mainService){
            try {
                mainService.stopTTS(from);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 是否在发音
     * @return
     */
    public boolean isSpeaking() {
        if(null != mainService){
            try {
                return mainService.isSpeaking();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

}

