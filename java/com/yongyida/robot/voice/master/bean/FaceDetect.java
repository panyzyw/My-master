package com.yongyida.robot.voice.master.bean;

/**
 * Created by Administrator on 2016/12/29 0029.
 * by dean
 */

public class FaceDetect {
    public static final String TAG_FACE = "TAGFace";

    private static FaceDetect faceDetect;

    private String who;

    public static FaceDetect getInstance() {
        if (faceDetect == null) {
            faceDetect = new FaceDetect();
        }
        return faceDetect;
    }

    private FaceDetect() {
        this.who = "";
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }
}
