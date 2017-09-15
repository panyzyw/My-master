package com.yongyida.robot.voice.master.bean;

public class AnswerInfoBean {

	private String mType;
    /**
     * 目的地
     */
    private String mCity;
    /**
     * 方式
     */
    private String mPoi;
    /**
     * 城市
     */
    private String mCityAddr;
    /**
     * 区域
     */
    private String mArea;
    /**
     * 区域名称
     */
    private String mAreaAddr;
    /**
     * 省
     */
    private String province ;
    /**
     * 名称
     */
    private String provinceAddr ;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getProvinceAddr() {
        return provinceAddr;
    }

    public void setProvinceAddr(String provinceAddr) {
        this.provinceAddr = provinceAddr;
    }

    public String getmArea() {
        return mArea;
    }

    public void setmArea(String mArea) {
        this.mArea = mArea;
    }

    public String getmAreaAddr() {
        return mAreaAddr;
    }

    public void setmAreaAddr(String mAreaAddr) {
        this.mAreaAddr = mAreaAddr;
    }

    public String getmType() {
        return mType;
    }

    public void setmType(String mType) {
        this.mType = mType;
    }

    public String getmCity() {
        return mCity;
    }

    public void setmCity(String mCity) {
        this.mCity = mCity;
    }

    public String getmPoi() {
        return mPoi;
    }

    public void setmPoi(String mPoi) {
        this.mPoi = mPoi;
    }

    public String getmCityAddr() {
        return mCityAddr;
    }

    public void setmCityAddr(String mCityAddr) {
        this.mCityAddr = mCityAddr;
    }
}