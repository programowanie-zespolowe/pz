package com.example.programowaniezespolowe.Data;

public class PointDetail {
    public int IdPointDetails;
    public int IdPoint;
    public String namePoint;
    public int idGroup;
    public String imagePoint;

    public PointDetail(String namePoint, String imagePoint) {
        this.namePoint = namePoint;
        this.imagePoint = imagePoint;
    }

    public String getNamePoint() {
        return namePoint;
    }

    public void setNamePoint(String namePoint) {
        this.namePoint = namePoint;
    }

    public int getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(int idGroup) {
        this.idGroup = idGroup;
    }

    public int getIdPointDetails() {
        return IdPointDetails;
    }

    public void setIdPointDetails(int idPointDetails) {
        IdPointDetails = idPointDetails;
    }

    public int getIdPoint() {
        return IdPoint;
    }

    public void setIdPoint(int idPoint) {
        IdPoint = idPoint;
    }

    public String getImagePoint() {
        return imagePoint;
    }

    public void setImagePoint(String imagePoint) {
        this.imagePoint = imagePoint;
    }
}
