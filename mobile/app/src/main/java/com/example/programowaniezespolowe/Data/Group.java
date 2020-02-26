package com.example.programowaniezespolowe.Data;

public class Group {
    private int idGroup;
    private String nameGroup;
    private byte[] imageGroup;
    private int idBuilding;

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

    public byte[] getImageGroup() {
        return imageGroup;
    }

    public void setImageGroup(byte[] imageGroup) {
        this.imageGroup = imageGroup;
    }
}
