package com.example.programowaniezespolowe.Data;

public class NextPoint {
    private int idPoint;
    private int icon;
    private double angle;
    private double distance;
    private boolean eleveator;
    private boolean stairs;
    private int level;
    public int currentLevel;
    public int iconOnAnotherLevel;
    public double angleOnAnotherLevel;
    public double distanceOnAnotherLevel ;

    public int getIdPoint() {
        return idPoint;
    }

    public void setIdPoint(int idPoint) {
        this.idPoint = idPoint;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public boolean isEleveator() {
        return eleveator;
    }

    public void setEleveator(boolean eleveator) {
        this.eleveator = eleveator;
    }

    public boolean isStairs() {
        return stairs;
    }

    public void setStairs(boolean stairs) {
        this.stairs = stairs;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getIconOnAnotherLevel() {
        return iconOnAnotherLevel;
    }

    public void setIconOnAnotherLevel(int iconOnAnotherLevel) {
        iconOnAnotherLevel = iconOnAnotherLevel;
    }

    public double getAngleOnAnotherLevel() {
        return angleOnAnotherLevel;
    }

    public void setAngleOnAnotherLevel(double angleOnAnotherLevel) {
        angleOnAnotherLevel = angleOnAnotherLevel;
    }

    public double getDistanceOnAnotherLevel() {
        return distanceOnAnotherLevel;
    }

    public void setDistanceOnAnotherLevel(double distanceOnAnotherLevel) {
        distanceOnAnotherLevel = distanceOnAnotherLevel;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        currentLevel = currentLevel;
    }
}
