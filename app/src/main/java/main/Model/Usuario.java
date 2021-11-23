package main.Model;

public class Usuario {
    private String name;
    private String userName;
    private String email;
    private Integer stories;

    public Usuario(String name, String userName, String email) {
        this.name = name;
        this.userName = userName;
        this.email = email;
    }
    public Usuario(String name, String userName, String email, Integer stories) {
        this.name = name;
        this.userName = userName;
        this.email = email;
        this.stories = stories;
    }

    public void add(){
        this.stories = this.stories+1;
    }

    public Integer getStories() {
        return stories;
    }

    public void setStories(Integer stories) {
        this.stories = stories;
    }

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

    @Override
    public String toString() {
        return "Usuario{" +
                "name='" + name + '\'' +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", stories=" + stories +
                '}';
    }

    public Usuario() {
    }
}
