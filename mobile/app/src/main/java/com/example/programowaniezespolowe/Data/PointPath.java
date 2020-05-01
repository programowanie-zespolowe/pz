package com.example.programowaniezespolowe.Data;

import com.example.programowaniezespolowe.Activity.ChooseActivity;

public class PointPath {
    private static PointPath instance;
    private int previousPoint;
    private int currentPoint;
    private int nextPoint;
    private int targetPoint;

    public static PointPath getInstance(){
        if(instance == null){
            instance = new PointPath();
        }
        return instance;
    }

    public static void setInstance(PointPath instance) {
        PointPath.instance = instance;
    }

    public int getPreviousPoint() {
        return instance.previousPoint;
    }

    public void setPreviousPoint(int previousPoint) {
        instance.previousPoint = previousPoint;
    }

    public int getCurrentPoint() {
        return instance.currentPoint;
    }

    public void setCurrentPoint(int currentPoint) {
        if(instance.previousPoint == 0){
            instance.previousPoint = -1;
        }else{
            if(ChooseActivity.getOption() == 1) {
                instance.previousPoint = this.currentPoint;
            }
        }
        instance.currentPoint = currentPoint;
    }

    public int getNextPoint() {
        return instance.nextPoint;
    }

    public void setNextPoint(int nextPoint) {
        instance.nextPoint = nextPoint;
    }

    public int getTargetPoint() {
        return instance.targetPoint;
    }

    public void setTargetPoint(int targetPoint) {
        instance.targetPoint = targetPoint;
    }
}
