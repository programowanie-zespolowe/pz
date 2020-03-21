package com.example.programowaniezespolowe.Data;

public class Group {
    private int idBuilding;
    private int idGroup;
    private String imageGroup;
    private String nameGroup;

    public Group(String imageGroup, String nameGroup) {
        this.imageGroup = imageGroup;
        this.nameGroup = nameGroup;
    }

    public int getIdBuilding() {
        return idBuilding;
    }

    public void setIdBuilding(int idBuilding) {
        this.idBuilding = idBuilding;
    }

    public int getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(int idGroup) {
        this.idGroup = idGroup;
    }

    public String getNameGroup() {
        return nameGroup;
    }

    public void setNameGroup(String nameGroup) {
        this.nameGroup = nameGroup;
    }


    public String getImageGroup() {
        return imageGroup;
    }

    public void setImageGroup(String imageGroup) {
        this.imageGroup = imageGroup;
    }
}
