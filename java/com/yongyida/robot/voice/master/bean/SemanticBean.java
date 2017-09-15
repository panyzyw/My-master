package com.yongyida.robot.voice.master.bean;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SemanticBean {

	@SerializedName("slots")
    @Expose
    private SlotsBean slots;

    /**
     *
     * @return
     *     The slots
     */
    public SlotsBean getSlots() {
        return slots;
    }

    /**
     *
     * @param slots
     *     The slots
     */
    public void setSlots(SlotsBean slots) {
        this.slots = slots;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(slots).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof SemanticBean) == false) {
            return false;
        }
        SemanticBean rhs = ((SemanticBean) other);
        return new EqualsBuilder().append(slots, rhs.slots).isEquals();
    }
}