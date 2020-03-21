package com.example.programowaniezespolowe.Data;

public class PointPath {
    private static PointPath instance;
    private int previousPoint;
    private int currentPoint;
    private int nextPoint;

    public static PointPath getInstance(){
        if(instance == null){
            instance = new PointPath();
        }
        return instance;
    }

    public int getPreviousPoint() {
        return previousPoint;
    }

    public void setPreviousPoint(int previousPoint) {
        this.previousPoint = previousPoint;
    }

    public int getCurrentPoint() {
        return currentPoint;
    }

    public void setCurrentPoint(int currentPoint) {
        if(previousPoint == 0){
            this.previousPoint = currentPoint;
        }else{
            this.previousPoint = this.currentPoint;
        }
        this.currentPoint = currentPoint;
    }

    public int getNextPoint() {
        return nextPoint;
    }

    public void setNextPoint(int nextPoint) {
        this.nextPoint = nextPoint;
    }
}
