package com.yongyida.robot.voice.master.bean;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SlotsBean {

	@SerializedName("distance")
    @Expose
    private String distance;
    @SerializedName("endLoc")
    @Expose
    private EndLocBean endLoc;
    @SerializedName("startLoc")
    @Expose
    private StartLocBean startLoc;

    /**
     *
     * @return
     *     The distance
     */
    public String getDistance() {
        return distance;
    }

    /**
     *
     * @param distance
     *     The distance
     */
    public void setDistance(String distance) {
        this.distance = distance;
    }

    /**
     *
     * @return
     *     The endLoc
     */
    public EndLocBean getEndLoc() {
        return endLoc;
    }

    /**
     *
     * @param endLoc
     *     The endLoc
     */
    public void setEndLoc(EndLocBean endLoc) {
        this.endLoc = endLoc;
    }

    /**
     *
     * @return
     *     The startLoc
     */
    public StartLocBean getStartLoc() {
        return startLoc;
    }

    /**
     *
     * @param startLoc
     *     The startLoc
     */
    public void setStartLoc(StartLocBean startLoc) {
        this.startLoc = startLoc;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(distance).append(endLoc).append(startLoc).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof SlotsBean) == false) {
            return false;
        }
        SlotsBean rhs = ((SlotsBean) other);
        return new EqualsBuilder().append(distance, rhs.distance).append(endLoc, rhs.endLoc).append(startLoc, rhs.startLoc).isEquals();
    }
}