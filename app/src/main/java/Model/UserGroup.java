package Model;

public class UserGroup {
    private int idUser;
    private int idGroup;
    private boolean statusAdmin;
    private String statusParticipation;
    private String groupName;
    private String userName;
    private String userPhone;

    public UserGroup(int idUser, int idGroup, boolean statusAdmin, String statusParticipation, String groupName, String userName, String userPhone) {
        this.idUser = idUser;
        this.idGroup = idGroup;
        this.statusAdmin = statusAdmin;
        this.statusParticipation = statusParticipation;
        this.groupName = groupName;
        this.userName = userName;
        this.userPhone = userPhone;
    }

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

    public boolean isStatusAdmin() {
        return statusAdmin;
    }

    public void setStatusAdmin(boolean statusAdmin) {
        this.statusAdmin = statusAdmin;
    }

    public String getStatusParticipation() {
        return statusParticipation;
    }

    public void setStatusParticipation(String statusParticipation) {
        this.statusParticipation = statusParticipation;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }
}
