package model;

public class UserData {
    private String username;
    private String pwd;
    private String email;

    public UserData(String username, String pwd, String email) {
        this.username = username;
        this.pwd = pwd;
        this.email = email;
    }

    public String getUser() {
        return username;
    }

    public String getPwd() {
        return pwd;
    }

    public String getEmail() {
        return email;
    }

    public void setUser(String user) {
        this.username = user;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}