package com.example.programowaniezespolowe.Data;

public class Building {
    private int idBuilding;
    private int idUser;
//    private byte[] imageBuilding;
    private String imageBuilding;
    private String nameBuilding;

    public int getIdBuilding() {
        return idBuilding;
    }

    public void setIdBuilding(int idBuilding) {
        idBuilding = idBuilding;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        idUser = idUser;
    }

    public String getNameBuilding() {
        return nameBuilding;
    }

    public void setNameBuilding(String nameBuilding) {
        nameBuilding = nameBuilding;
    }

//    public byte[] getImageBuilding() {
//        return imageBuilding;
//    }
//
//    public void setImageBuilding(byte[] imageBuilding) {
//        this.imageBuilding = imageBuilding;
//    }

    public String getImageBuilding() {
        return imageBuilding;
    }

    public void setImageBuilding(String imageBuilding) {
        this.imageBuilding = imageBuilding;
    }
}
