package Model;

public class UserGroup {

    private int idUserGroup;
    private int idUser;
    private int idGroup;
    private int idWhoInvited;
    private String accessLevel;
    private String statusParticipation;

    public int getIdUserGroup() {
        return idUserGroup;
    }

    public void setIdUserGroup(int idUserGroup) {
        this.idUserGroup = idUserGroup;
    }

    public UserGroup() {}

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(int idGroup) {
        this.idGroup = idGroup;
    }

    public int getIdWhoInvited() {
        return idWhoInvited;
    }

    public void setIdWhoInvited(int idWhoInvited) {
        this.idWhoInvited = idWhoInvited;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
    }

    public String getStatusParticipation() {
        return statusParticipation;
    }

    public void setStatusParticipation(String statusParticipation) {
        this.statusParticipation = statusParticipation;
    }
}
