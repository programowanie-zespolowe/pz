package sample.Structs;

public class Building {
    private int IdBuilding;
    private int IdUser;
    private String NameBuilding;
    private byte[] ImageBuilding;
    private double Scale;

    public double getScale() {
        return Scale;
    }

    public void setScale(double scale) {
        Scale = scale;
    }

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
