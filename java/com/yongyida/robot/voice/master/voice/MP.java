package com.yongyida.robot.voice.master.voice;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.yongyida.robot.voice.master.application.MasterApplication;
import com.yongyida.robot.voice.master.runnable.BaseRunnable;
import com.yongyida.robot.voice.master.utils.MyExceptionUtils;
import com.yongyida.robot.voice.master.utils.MyLog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MP extends BaseVoice {

    boolean mResourceIsReading = false;

    public String mVoiceUri;

    private MediaPlayer mMediaPlayer;
    private boolean isAssetsResource;
    private AssetFileDescriptor fd;

    MyOnStartListener mOnStartListener;
    MyOnStopListener mOnStopListener;

    public MP(String uri, int voicer, int interrupt_state, int priority, boolean isEndSendRecycle, final MyOnPreparedListener myOnPreparedListener,
              final MyOnCompletionListener myOnCompletionListener) {
        super(interrupt_state, voicer, priority, isEndSendRecycle);

        mVoiceUri = uri;

        init();
        mMediaPlayer.setOnPreparedListener(new OnPreparedListener() {
            public void onPrepared(MediaPlayer mp) {
                if (myOnPreparedListener != null) {
                    myOnPreparedListener.onPrepared(mp);
                }
                MyLog.i("MP", "mp onPrepared");
                mResourceIsReading = true;
                MyLog.i("MP", "mResourceIsReading = true");
            }
        });

        mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                if (myOnCompletionListener != null) {
                    myOnCompletionListener.onCompletion(mp);
                }

                Log.d("tempTAG", "MP => onCompletion");

                sendLedBroad(false);

                MyLog.i("MP", "mp onCompleted");
                if (mMediaPlayer != null) {
                    mMediaPlayer.release();
                    mMediaPlayer = null;
                }
                VoiceQueue.getInstance().popAndRun();
            }
        });

    }

    public MP(String uri, int voicer, int interrupt_state, int priority, boolean isEndSendRecycle, final MyOnPreparedListener myOnPreparedListener,
              final MyOnCompletionListener myOnCompletionListener,
              final MyOnStartListener onStartListener,
              final MyOnStopListener onStopListener) {
        this(uri, voicer, interrupt_state, priority, isEndSendRecycle, myOnPreparedListener, myOnCompletionListener);

        mOnStartListener = onStartListener;
        mOnStopListener = onStopListener;
    }

    private void init() {
        mResourceIsReading = false;
        MyLog.i("MP", "mResourceIsReading = false");
        if (mMediaPlayer == null)
            mMediaPlayer = new MediaPlayer();
        else
            mMediaPlayer.reset();

        try {
            MyLog.i("MP", "mVoiceUri = " + mVoiceUri);

            if (mVoiceUri.contains("http")) { //
                //String uri = doCache(mVoiceUri);

                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mMediaPlayer.setDataSource(mVoiceUri);
                mMediaPlayer.prepareAsync();
                return;
            }

            //assets里面的资源文件
            MyLog.i("MP", "assets resource");
            isAssetsResource = true;
            //AssetFileDescriptor fd = MasterApplication.mAppContext.getAssets().openFd(mVoiceUri);
            fd = MasterApplication.mAppContext.getAssets().openFd(mVoiceUri);
            mMediaPlayer.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getDeclaredLength());

            //mMediaPlayer.prepareAsync();
            mMediaPlayer.prepare();
        } catch (IllegalArgumentException e) {
            MyLog.e("MP", MyExceptionUtils.getStringThrowable(e));
        } catch (IllegalStateException e) {
            MyLog.e("MP", MyExceptionUtils.getStringThrowable(e));
        } catch (IOException e) {
            MyLog.e("MP", MyExceptionUtils.getStringThrowable(e));
        }
    }

//	private void init(){
//		mResourceIsReading = false;
//		if( mMediaPlayer == null )
//			mMediaPlayer = new MediaPlayer();			
//		else		
//			mMediaPlayer.reset();
//
//		try {
//			if( mVoiceUri.contains("http") ){ //
//				
//				String uri = doCache(mVoiceUri);
//				if( uri == null ){
//					MyLog.i("MP", "mp init : cache not exist");
//					mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//					mMediaPlayer.setDataSource(mVoiceUri);
//					mMediaPlayer.prepareAsync();
//				}else{
//					MyLog.i("MP", "mp init : cache  exist");
//					mMediaPlayer.setDataSource(uri);
//					mMediaPlayer.prepare();
//				}
//			}else{//assets里面的资源文件		
//				MyLog.i("MP", "assets resource");
//				AssetFileDescriptor fd = MasterApplication.mAppContext.getAssets().openFd(mVoiceUri);
//				mMediaPlayer.setDataSource(fd.getFileDescriptor(),fd.getStartOffset(),fd.getDeclaredLength());
//				mMediaPlayer.prepare();			
//			}
//		} catch (IllegalArgumentException e) {				
//			MyLog.e("MP", MyExceptionUtils.getStringThrowable(e));
//		} catch (IllegalStateException e) {		
//			MyLog.e("MP", MyExceptionUtils.getStringThrowable(e));
//		} catch (IOException e) {		
//			MyLog.e("MP", MyExceptionUtils.getStringThrowable(e));
//		}	
//	}

    public void start() {
        MyLog.i("MP", "start()");
		//默认正在说话时会闪灯（挚康的不要闪）
        if(MasterApplication.isNeedSpekLed()){
            sendLedBroad(true);
        }

//		if(getVoicer() == MP_INTENT_RESOURCE){
        if (isAssetsResource) {
            if (mOnStartListener != null) {
                mOnStartListener.onStart();
            }
            mMediaPlayer.start();
        } else {
            new Thread() {
                @Override
                public void run() {
                    int count = 0;
                    while (!mResourceIsReading) {
                        count++;
                        if (count == 20)
                            break;
                        try {
                            Thread.sleep(400);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (count < 20) {
                        if (mOnStartListener != null) {
                            mOnStartListener.onStart();
                        }
                        mMediaPlayer.start();
                    } else {
                        MyLog.e("MP", "mp error onCompleted : 缓冲音频资源超时,可能原因:1网络异常,2资源异常  url = " + mVoiceUri);

                        if (mMediaPlayer != null) {
                            mMediaPlayer.release();
                            mMediaPlayer = null;
                        }
                        // VoiceQueue.getInstance().popAndRun();
                    }
                    super.run();
                }
            }.start();
        }

    }
//	else{
//			mMediaPlayer.start();
//		}		
//	}

    public void reStart() {
        if (getInterrupt_state() == BaseRunnable.INTERRUPT_STATE_PAUSE) {
            start();
        } else if (getInterrupt_state() == BaseRunnable.INTERRUPT_STATE_RESET) {
            init();
            start();
        }
    }

    public void pause() {
        mMediaPlayer.pause();
        setIsInterrupted(true);
    }

    public void stop() {
        if (mOnStopListener != null) {
            mOnStopListener.onStop();
        }
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
        }
    }

    private void sendLedBroad(boolean onOff) {
        Intent intentSpeakOpen = new Intent("com.yongyida.robot.change.BREATH_LED");
        intentSpeakOpen.putExtra("on_off", onOff);
        intentSpeakOpen.putExtra("place", 3);
        intentSpeakOpen.putExtra("colour", 3);
        intentSpeakOpen.putExtra("frequency", 2);
        intentSpeakOpen.putExtra("Permanent", "speak");
        intentSpeakOpen.putExtra("priority", 6);
        intentSpeakOpen.putExtra("package", MasterApplication.mAppContext.getPackageName());
        MasterApplication.mAppContext.sendBroadcast(intentSpeakOpen);
    }

    public boolean isRunning() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.isPlaying();
        }
        return false;
    }

    public static class MyOnPreparedListener {
        public void onPrepared(MediaPlayer mp) {
        }

        ;
    }

    public static class MyOnCompletionListener {
        public void onCompletion(MediaPlayer mp) {
        }

        ;
    }

    public static class MyOnStartListener {
        public void onStart() {
        }

        ;
    }

    public static class MyOnStopListener {
        public void onStop() {
        }

        ;
    }

    public static class MyOnPauseListener {
        public void onPause() {
        }

        ;
    }

    /**
     * @param URL
     * @return 缓存文件存在, 就返回该文件的绝对路径, 不存在就返回null
     */
    public String doCache(String URL) {
        try {
            File cacheFile = new File(MasterApplication.mAppContext.getCacheDir().getAbsolutePath() + URL.substring(URL.lastIndexOf("/")));
            if (cacheFile.exists()) {
                MyLog.i("MP", "MusicService doCache :  file exist");
                return cacheFile.getAbsolutePath();
            }
            //play(URL);
            MyLog.i("MP", "MusicService doCache : file non-exist,start downloading");
            HttpUtils mHttpUtils = new HttpUtils();
            mHttpUtils.download(URL, cacheFile.getAbsolutePath(), false, new RequestCallBack<File>() {

                @Override
                public void onFailure(HttpException arg0, String arg1) {
                    MyLog.e("MP", "MusicService doCache : " + arg1);
                }

                @Override
                public void onSuccess(ResponseInfo<File> arg0) {
                    MyLog.i("MP", "MusicService doCache : download successful");
                }

                @Override
                public void onLoading(long total, long current, boolean isUploading) {

                }
            });
        } catch (Throwable e) {
            MyLog.e("MP", MyExceptionUtils.getStringThrowable(e));
        }
        return null;
    }

    //----------------------------下面是判断 删除缓存文件的方法,后面放到 闲暇时间调用--------------------------------------------
    public void deleteOldFile() {
        try {
            ArrayList<Long> modifiedTime = new ArrayList<Long>();
            Map<Long, File> mapFile = new HashMap<Long, File>();
            File files[] = MasterApplication.mAppContext.getCacheDir().listFiles();
            for (int i = 0; i < files.length; i++) {
                modifiedTime.add(files[i].lastModified());
                mapFile.put(files[i].lastModified(), files[i]);
            }
            Collections.sort(modifiedTime);
            File file = mapFile.get(modifiedTime.get(0));
            boolean delete = file.delete();
            MyLog.i("MP", "MusicService delete : delete status:" + delete);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void manageFileSize() {
        try {
            measureTotalSize(MasterApplication.mAppContext.getCacheDir());
            long size = totalSize / (1024 * 1024);
            totalSize = 0;
            MyLog.i("MP", "cache size:" + size + ",time:" + new Date().getTime());
            if (size > 100) {
                MyLog.i("MP", "file size is greater than the set,start deleting old file");
                deleteOldFile();
                this.manageFileSize();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private long totalSize = 0;

    public void measureTotalSize(File file) {
        try {
            if (file.isFile()) {
                totalSize += file.length();
            } else {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    this.measureTotalSize(files[i]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
