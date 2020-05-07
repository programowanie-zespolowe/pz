package com.example.programowaniezespolowe.Data;

import java.sql.Date;

public class RekordTime {
    private int idRecord;
    private int idOutdoorGame;
    private String mac;
    private String name;
    private Date startDate;
    private Date endDate;
    private long time;

    public int getIdRecord() {
        return idRecord;
    }

    public void setIdRecord(int idRecord) {
        this.idRecord = idRecord;
    }

    public int getIdOutdoorGame() {
        return idOutdoorGame;
    }

    public void setIdOutdoorGame(int idOutdoorGame) {
        this.idOutdoorGame = idOutdoorGame;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
