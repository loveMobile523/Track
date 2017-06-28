package offline.mapbox.trackerapp.model;

public class DBUser {
    private long id;
    private String userId;
    private String username;
    private String user_type;
    private String updated;         // 1: from Remote DB,    0: to Remote DB

    public DBUser() {}

    public DBUser(String userId, String username, String user_type, String updated) {
        this.userId = userId;
        this.username = username;
        this.user_type = user_type;
        this.updated = updated;
    }

    public DBUser(long id, String userId, String username, String user_type, String updated) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.user_type = user_type;
        this.updated = updated;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getUpdated() {
        return updated;
    }
}
