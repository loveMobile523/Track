package offline.mapbox.trackerapp.model;

public class DBCategory {
    private long id;
    private String categoryId;
    private String category;
    private String updated;         // 1: from Remote DB,    0: to Remote DB

    public DBCategory() {}

    public DBCategory(String categoryId, String category, String updated) {
        this.categoryId = categoryId;
        this.category = category;
        this.updated = updated;
    }

    public DBCategory(long id, String categoryId, String category, String updated) {
        this.id = id;
        this.categoryId = categoryId;
        this.category = category;
        this.updated = updated;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getUpdated() {
        return updated;
    }
}
