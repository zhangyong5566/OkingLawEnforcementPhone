package com.zhang.okinglawenforcementphone.beans;

/**
 * Created by Administrator on 2018/4/8.
 */

public class GreenEvidenceSZOV {
    private int type;
    private GreenEvidence greenEvidence;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public GreenEvidence getGreenEvidence() {
        return greenEvidence;
    }

    public void setGreenEvidence(GreenEvidence greenEvidence) {
        this.greenEvidence = greenEvidence;
    }

    @Override
    public String toString() {
        return "GreenEvidenceSZOV{" +
                "type=" + type +
                ", greenEvidence=" + greenEvidence +
                '}';
    }
}
