package main.Model;

public class Usuario {
    private String name;
    private String userName;
    private String email;
    private Integer stories;
    byte[] image;
    String code;
    private Double points;
    private Integer partidas;
    private Double promedio;

    public Usuario(String name, String userName, String email, String code) {
        this.name = name;
        this.userName = userName;
        this.email = email;
        this.code = code;
        this.points = 0d;
        this.partidas = 0;
        this.promedio = 0d;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }



    public Usuario(String name, String userName, String email) {
        this.name = name;
        this.userName = userName;
        this.email = email;
        this.points = 0d;
        this.partidas = 0;
        this.promedio = 0d;
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

    public Double getPoints() {
        return points;
    }

    public void setPoints(Double points) {
        this.points = points;
    }

    public Integer getPartidas() {
        return partidas;
    }

    public void setPartidas(Integer partidas) {
        this.partidas = partidas;
    }

    public Double getPromedio() {
        return promedio;
    }

    public void setPromedio(Double promedio) {
        this.promedio = promedio;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "name='" + name + '\'' +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", stories=" + stories +
                ", points=" + points +
                ", partidas=" + partidas +
                ", promedio=" + promedio +
                '}';
    }

    public void addPoints(String dist){
        Double distance = Double.valueOf(dist);

        Double points = 50000d - distance;

        this.points = this.points + points;
    }

    public void addPartidas(){
        this.partidas += 1;
    }
    
    public void recalcPromedio(){
        try {
            this.promedio = this.points / this.partidas;
        }catch (Exception e){}
    }

    public Usuario() {
    }
}
