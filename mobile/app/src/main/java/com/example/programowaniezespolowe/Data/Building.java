package com.example.programowaniezespolowe.Data;

public class Building {
    private int IdBuilding;
    private int IdUser;
    private String NameBuilding;
    private byte[] ImageBuilding;

    public int getIdBuilding() {
        return IdBuilding;
    }

    public void setIdBuilding(int idBuilding) {
        IdBuilding = idBuilding;
    }

    public int getIdUser() {
        return IdUser;
    }

    public void setIdUser(int idUser) {
        IdUser = idUser;
    }

    public String getNameBuilding() {
        return NameBuilding;
    }

    public void setNameBuilding(String nameBuilding) {
        NameBuilding = nameBuilding;
    }

    public byte[] getImageBuilding() {
        return ImageBuilding;
    }

    public void setImageBuilding(byte[] imageBuilding) {
        ImageBuilding = imageBuilding;
    }
}
