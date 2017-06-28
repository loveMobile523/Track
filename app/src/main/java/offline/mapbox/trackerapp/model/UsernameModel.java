package offline.mapbox.trackerapp.model;

public class UsernameModel {
    private String username;

    public UsernameModel() {
        this.username = "";
    }

    public UsernameModel(String username) {
        this.username = username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
