package Model;

public class User {
    private int idUser;
    private String name;
    private String email;
    private String password;
    private String phone;
    private String statusParticipation;

    public User() {}

    public User(int idUser, String name, String email, String password, String phone, String statusParticipation) {
        this.idUser = idUser;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.statusParticipation = statusParticipation;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatusParticipation() { return statusParticipation; }

    public void setStatusParticipation(String statusParticipation) { this.statusParticipation = statusParticipation; }
}
