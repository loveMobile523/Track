package offline.mapbox.trackerapp.model;

public class DBIcon {
    private long id;
    private String iconId;
    private String name;
    private String path;
    private String description;
    private String updated;         // 1: from Remote DB,    0: to Remote DB

    public DBIcon() {}

    public DBIcon(String iconId, String name, String path, String description, String updated) {
        this.iconId = iconId;
        this.name = name;
        this.path = path;
        this.description = description;
        this.updated = updated;
    }

    public DBIcon(long id, String iconId, String name, String path, String description, String updated) {
        this.id = id;
        this.iconId = iconId;
        this.name = name;
        this.path = path;
        this.description = description;
        this.updated = updated;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setIconId(String iconId) {
        this.iconId = iconId;
    }

    public String getIconId() {
        return iconId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getUpdated() {
        return updated;
    }
}

