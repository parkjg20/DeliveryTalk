package com.dataflow.deliverytalk.Models;

public class ParcelInfo {
    String carrierId;
    String waybill;

    public ParcelInfo(){}

    public ParcelInfo(String carrierId, String waybill) {
        this.carrierId = carrierId;
        this.waybill = waybill;
    }

    public String getCarrierId() {
        return carrierId;
    }

    public void setCarrierId(String carrierId) {
        this.carrierId = carrierId;
    }

    public String getWaybill() {
        return waybill;
    }

    public void setWaybill(String waybill) {
        this.waybill = waybill;
    }
}