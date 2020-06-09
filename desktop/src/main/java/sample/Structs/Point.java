package sample.Structs;

public class Point {
    public int IdPoint;
    public int IdImage;
    public double X;
    public double Y;
    public int IdPointType;
    public byte[] ImagePoint;
    public boolean OnOffDirection;
    public double Direction;

    public boolean isOnOffDirection() {
        return OnOffDirection;
    }

    public void setOnOffDirection(boolean onOffDirection) {
        OnOffDirection = onOffDirection;
    }

    public double getDirection() {
        return Direction;
    }

    public void setDirection(double direction) {
        Direction = direction;
    }

    public int getIdPoint() {
        return IdPoint;
    }

    public void setIdPoint(int idPoint) {
        IdPoint = idPoint;
    }

    public int getIdImage() {
        return IdImage;
    }

    public void setIdImage(int idImage) {
        IdImage = idImage;
    }

    public double getX() {
        return X;
    }

    public void setX(double x) {
        X = x;
    }

    public double getY() {
        return Y;
    }

    public void setY(double y) {
        Y = y;
    }

    public int getIdPointType() {
        return IdPointType;
    }

    public void setIdPointType(int idPointType) {
        IdPointType = idPointType;
    }

    public byte[] getImagePoint() {
        return ImagePoint;
    }

    public void setImagePoint(byte[] imagePoint) {
        ImagePoint = imagePoint;
    }
}
