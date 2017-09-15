package com.zccl.ruiqianqi.brain.system;
import com.iflytek.cloud.SpeechError;
public interface ISynthesizerCallback {
    void OnBegin();
    void OnPause();
    void OnResume();
    void OnComplete(SpeechError error);
}
