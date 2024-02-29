package model;

public class UserData {
    private final String username;
    private final String pwd;
    private final String email;

    public UserData(String username, String pwd, String email) {
        this.username = username;
        this.pwd = pwd;
        this.email = email;
    }

    public String getPwd() {
        return pwd;
    }
}