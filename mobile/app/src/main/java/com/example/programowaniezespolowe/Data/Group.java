package com.example.programowaniezespolowe.Data;

public class Group {
    private int idBuilding;
    private int idGroup;
//    private byte[] imageGroup;
    private String imageGroup;
    private String nameGroup;

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

//    public byte[] getImageGroup() {
//        return imageGroup;
//    }
//
//    public void setImageGroup(byte[] imageGroup) {
//        this.imageGroup = imageGroup;
//    }

    public String getImageGroup() {
        return imageGroup;
    }

    public void setImageGroup(String imageGroup) {
        this.imageGroup = imageGroup;
    }
}
