package sample.WebService;

public class LoginStruct {
    String name;
    String passowrd;

    public LoginStruct() {
        this.name = "";
        this.passowrd = "";
    }

    public LoginStruct(String name, String passowrd) {
        this.name = name;
        this.passowrd = passowrd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassowrd() {
        return passowrd;
    }

    public void setPassowrd(String passowrd) {
        this.passowrd = passowrd;
    }
}
