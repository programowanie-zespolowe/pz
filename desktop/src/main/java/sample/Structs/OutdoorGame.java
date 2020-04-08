package sample.Structs;

import java.util.Date;

public class OutdoorGame {
    int idOutdoorGame;
    int idBuilding;
    String nameGame;
    byte[] imageGame;
    Date startDateGame;
    Date endDateGame;
    Integer idFirstPoint;

    public int getIdOutdoorGame() {
        return idOutdoorGame;
    }

    public void setIdOutdoorGame(int idOutdoorGame) {
        this.idOutdoorGame = idOutdoorGame;
    }

    public int getIdBuilding() {
        return idBuilding;
    }

    public void setIdBuilding(int idBuilding) {
        this.idBuilding = idBuilding;
    }

    public String getNameGame() {
        return nameGame;
    }

    public void setNameGame(String nameGame) {
        this.nameGame = nameGame;
    }

    public byte[] getImageGame() {
        return imageGame;
    }

    public void setImageGame(byte[] imageGame) {
        this.imageGame = imageGame;
    }

    public Date getStartDateGame() {
        return startDateGame;
    }

    public void setStartDateGame(Date startDateGame) {
        this.startDateGame = startDateGame;
    }

    public Date getEndDateGame() {
        return endDateGame;
    }

    public void setEndDateGame(Date endDateGame) {
        this.endDateGame = endDateGame;
    }

    public Integer getIdFirstPoint() {
        return idFirstPoint;
    }

    public void setIdFirstPoint(Integer idFirstPoint) {
        this.idFirstPoint = idFirstPoint;
    }
}
