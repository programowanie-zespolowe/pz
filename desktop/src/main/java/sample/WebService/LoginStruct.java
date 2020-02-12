package sample.WebService;

public class LoginStruct {
    String name;
    String password;

    public LoginStruct() {
        this.name = "";
        this.password = "";
    }

    public LoginStruct(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
