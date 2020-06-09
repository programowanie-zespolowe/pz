package com.example.programowaniezespolowe.Data;

public class PointDetail {
    public int idPointDetails;
    public int idPoint;
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
        return idPointDetails;
    }

    public void setIdPointDetails(int idPointDetails) {
        idPointDetails = idPointDetails;
    }

    public int getIdPoint() {
        return idPoint;
    }

    public void setIdPoint(int idPoint) {
        idPoint = idPoint;
    }

    public String getImagePoint() {
        return imagePoint;
    }

    public void setImagePoint(String imagePoint) {
        this.imagePoint = imagePoint;
    }
}
