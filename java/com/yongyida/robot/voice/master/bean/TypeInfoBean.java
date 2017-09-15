package com.yongyida.robot.voice.master.bean;

public class TypeInfoBean {

	private String mService;
    /**
     * ������ֵ
     */
    private int mRc;
    /**
     * ������
     */
    private String mOperation;
    /**
     * �ı�
     */
    private String mText;

    public String getmService() {
        return mService;
    }

    public void setmService(String mService) {
        this.mService = mService;
    }

    public int getmRc() {
        return mRc;
    }

    public void setmRc(int mRc) {
        this.mRc = mRc;
    }

    public String getmOperation() {
        return mOperation;
    }

    public void setmOperation(String mOperation) {
        this.mOperation = mOperation;
    }

    public String getmText() {
        return mText;
    }

    public void setmText(String mText) {
        this.mText = mText;
    }
}