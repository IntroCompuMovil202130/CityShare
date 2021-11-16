package main.Model;

public class Usuario {
    private String name;
    private String userName;
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Usuario(String name, String userName, String email) {
        this.name = name;
        this.userName = userName;
        this.email = email;
    }

    public Usuario() {
    }
}
