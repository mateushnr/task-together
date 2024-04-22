package Model;

public class Group {
    private int idGroup;
    private String name;
    private String description;
    private String type;

    public Group() {}

    public Group(int idGroup, String name, String description) {
        this.idGroup = idGroup;
        this.name = name;
        this.description = description;
    }

    public int getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(int idGroup) {
        this.idGroup = idGroup;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
