package com.yongyida.robot.voice.master.bean;

public class StepBean {

	private String instruction;

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    @Override
    public String toString() {
        return "Step [instruction=" + instruction + "]";
    }

    public StepBean(String instruction) {
        super();
        this.instruction = instruction;
    }
}
