package com.example.programowaniezespolowe.Data;

import java.util.Date;

public class OutdoorGameTime {
    private static OutdoorGameTime instance;
    private String macId;
    private Date start;
    private Date end;
    private String name;
    private int idOutdoorGame;

    public static OutdoorGameTime getInstance(){
        if(instance == null){
            instance = new OutdoorGameTime();
        }
        return instance;
    }

    public String getMacId() {
        return instance.macId;
    }

    public void setMacId(String macId) {
        instance.macId = macId;
    }

    public Date getStart() {
        return instance.start;
    }

    public void setStart(Date start) {
        instance.start = start;
    }

    public Date getEnd() {
        return instance.end;
    }

    public void setEnd(Date end) {
        instance.end = end;
    }

    public int getIdOutdoorGame() {
        return instance.idOutdoorGame;
    }

    public void setIdOutdoorGame(int idOutdoorGame) {
        instance.idOutdoorGame = idOutdoorGame;
    }

    public String getName() {
        return instance.name;
    }

    public void setName(String name) {
        instance.name = name;
    }
}
