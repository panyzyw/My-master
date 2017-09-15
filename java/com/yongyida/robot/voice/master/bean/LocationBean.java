package com.yongyida.robot.voice.master.bean;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LocationBean {

	@SerializedName("lng")
    @Expose
    private Double lng;
    @SerializedName("lat")
    @Expose
    private Double lat;

    /**
     *
     * @return
     *     The lng
     */
    public Double getLng() {
        return lng;
    }

    /**
     *
     * @param lng
     *     The lng
     */
    public void setLng(Double lng) {
        this.lng = lng;
    }

    /**
     *
     * @return
     *     The lat
     */
    public Double getLat() {
        return lat;
    }

    /**
     *
     * @param lat
     *     The lat
     */
    public void setLat(Double lat) {
        this.lat = lat;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(lng).append(lat).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof LocationBean) == false) {
            return false;
        }
        LocationBean rhs = ((LocationBean) other);
        return new EqualsBuilder().append(lng, rhs.lng).append(lat, rhs.lat).isEquals();
    }
}