package offline.mapbox.trackerapp.model;

public class MessageLvModel {
    private String username;

    public MessageLvModel() {
        this.username = "";
    }

    public MessageLvModel(String username) {
        this.username = username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}

