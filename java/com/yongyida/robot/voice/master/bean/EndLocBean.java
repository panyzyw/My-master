package com.yongyida.robot.voice.master.bean;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EndLocBean {

	@SerializedName("province")
    @Expose
    private String province;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("areaAddr")
    @Expose
    private String areaAddr;
    @SerializedName("provinceAddr")
    @Expose
    private String provinceAddr;
    @SerializedName("cityAddr")
    @Expose
    private String cityAddr;
    @SerializedName("poi")
    @Expose
    private String poi;
    @SerializedName("area")
    @Expose
    private String area;

    /**
     *
     * @return
     *     The province
     */
    public String getProvince() {
        return province;
    }

    /**
     *
     * @param province
     *     The province
     */
    public void setProvince(String province) {
        this.province = province;
    }

    /**
     *
     * @return
     *     The type
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @param type
     *     The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     * @return
     *     The city
     */
    public String getCity() {
        return city;
    }

    /**
     *
     * @param city
     *     The city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     *
     * @return
     *     The areaAddr
     */
    public String getAreaAddr() {
        return areaAddr;
    }

    /**
     *
     * @param areaAddr
     *     The areaAddr
     */
    public void setAreaAddr(String areaAddr) {
        this.areaAddr = areaAddr;
    }

    /**
     *
     * @return
     *     The provinceAddr
     */
    public String getProvinceAddr() {
        return provinceAddr;
    }

    /**
     *
     * @param provinceAddr
     *     The provinceAddr
     */
    public void setProvinceAddr(String provinceAddr) {
        this.provinceAddr = provinceAddr;
    }

    /**
     *
     * @return
     *     The cityAddr
     */
    public String getCityAddr() {
        return cityAddr;
    }

    /**
     *
     * @param cityAddr
     *     The cityAddr
     */
    public void setCityAddr(String cityAddr) {
        this.cityAddr = cityAddr;
    }

    /**
     *
     * @return
     *     The poi
     */
    public String getPoi() {
        return poi;
    }

    /**
     *
     * @param poi
     *     The poi
     */
    public void setPoi(String poi) {
        this.poi = poi;
    }

    /**
     *
     * @return
     *     The area
     */
    public String getArea() {
        return area;
    }

    /**
     *
     * @param area
     *     The area
     */
    public void setArea(String area) {
        this.area = area;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(province).append(type).append(city).append(areaAddr).append(provinceAddr).append(cityAddr).append(poi).append(area).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof EndLocBean) == false) {
            return false;
        }
        EndLocBean rhs = ((EndLocBean) other);
        return new EqualsBuilder().append(province, rhs.province).append(type, rhs.type).append(city, rhs.city).append(areaAddr, rhs.areaAddr).append(provinceAddr, rhs.provinceAddr).append(cityAddr, rhs.cityAddr).append(poi, rhs.poi).append(area, rhs.area).isEquals();
    }
}