package com.example.programowaniezespolowe.Data;

public class BuildingLevel {
    int IdImage;
    int IdBuilding;
    byte[] PathImage;
    int BuildingLevel;
    double Scale;
    double NorthPointAngle;

    public int getIdImage() {
        return IdImage;
    }

    public void setIdImage(int idImage) {
        IdImage = idImage;
    }

    public int getIdBuilding() {
        return IdBuilding;
    }

    public void setIdBuilding(int idBuilding) {
        IdBuilding = idBuilding;
    }

    public byte[] getPathImage() {
        return PathImage;
    }

    public void setPathImage(byte[] pathImage) {
        PathImage = pathImage;
    }

    public int getBuildingLevel() {
        return BuildingLevel;
    }

    public void setBuildingLevel(int buildingLevel) {
        BuildingLevel = buildingLevel;
    }

    public double getScale() {
        return Scale;
    }

    public void setScale(double scale) {
        Scale = scale;
    }

    public double getNorthPointAngle() {
        return NorthPointAngle;
    }

    public void setNorthPointAngle(double northPointAngle) {
        NorthPointAngle = northPointAngle;
    }
}
