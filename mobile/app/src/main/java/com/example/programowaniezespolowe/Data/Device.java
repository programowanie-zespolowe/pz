package com.example.programowaniezespolowe.Data;


public class Device {
    private String name;
    private String macId;

    public Device(String name, String macId) {
        this.name = name;
        this.macId = macId;
    }

    public String getName() {
        return name;
    }

    public void setName() {
        this.name = name;
    }

    public String getMacId() {
        return macId;
    }

    public void setMacId(String macId) {
        this.macId = macId;
    }

}
