package org.hyperledger.fabric.example;

import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;

/**
 * Created by cadmin on 12/10/16.
 */

public class RainfallInsurance implements Serializable{
    private String insuranceID;
    private String state;
    private int rainThreshold; // threshold in cm for the period
    private float insuredAmount, premiumAmount;
    private boolean paidOut, lapsed;

// TODO HW - Try adding Start Date and End Date to the insurance
//    private Date startDate, endDate;

    public RainfallInsurance(String insuranceID, String state, int rainThreshold, float insuredAmount) {
        this.insuranceID = insuranceID;
        this.state = state;
        this.rainThreshold = rainThreshold;
        this.insuredAmount = insuredAmount;
        this.premiumAmount = insuredAmount * 0.005f;
        this.paidOut = false;
        this.lapsed = false;
    }
    @Override
    public String toString() {
        return "RainfallInsurance{" +
                "insuranceID='" + insuranceID + '\'' +
                ", state='" + state + '\'' +
                ", rainThreshold=" + rainThreshold +
                ", insuredAmount=" + insuredAmount +
                ", premiumAmount=" + premiumAmount +
                ", paidOut=" + paidOut +
                ", lapsed=" + lapsed +
                '}';
    }

    public void execute(int recordedRain){
        if ( !lapsed && rainThreshold > recordedRain){
            this.paidOut = true;
        }
        else{
            this.lapsed = true;
        }
   }

    public byte[] toByteArr(){
        byte[] data = SerializationUtils.serialize(this);
        return data;//byte[] this;
    }

    public static RainfallInsurance toPojo(byte[] byteArr){
        return (RainfallInsurance) SerializationUtils.deserialize(byteArr);
    }
}
