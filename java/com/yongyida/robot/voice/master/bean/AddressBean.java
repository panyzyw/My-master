package com.yongyida.robot.voice.master.bean;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddressBean {

	@SerializedName("semantic")
    @Expose
    private SemanticBean semantic;
    @SerializedName("rc")
    @Expose
    private Integer rc;
    @SerializedName("operation")
    @Expose
    private String operation;
    @SerializedName("service")
    @Expose
    private String service;
    @SerializedName("text")
    @Expose
    private String text;

    /**
     * @return The semantic
     */
    public SemanticBean getSemantic() {
        return semantic;
    }

    /**
     * @param semantic
     *            The semantic
     */
    public void setSemantic(SemanticBean semantic) {
        this.semantic = semantic;
    }

    /**
     * @return The rc
     */
    public Integer getRc() {
        return rc;
    }

    /**
     * @param rc
     *            The rc
     */
    public void setRc(Integer rc) {
        this.rc = rc;
    }

    /**
     * @return The operation
     */
    public String getOperation() {
        return operation;
    }

    /**
     * @param operation
     *            The operation
     */
    public void setOperation(String operation) {
        this.operation = operation;
    }

    /**
     * @return The service
     */
    public String getService() {
        return service;
    }

    /**
     * @param service
     *            The service
     */
    public void setService(String service) {
        this.service = service;
    }

    /**
     * @return The text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text
     *            The text
     */
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(semantic).append(rc).append(operation).append(service).append(text)
                .toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof AddressBean) == false) {
            return false;
        }
        AddressBean rhs = ((AddressBean) other);
        return new EqualsBuilder().append(semantic, rhs.semantic).append(rc, rhs.rc).append(operation, rhs.operation)
                .append(service, rhs.service).append(text, rhs.text).isEquals();
    }
}