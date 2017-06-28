package offline.mapbox.trackerapp.model;

public class DBSubCategory {
    private long id;
    private String subCategoryID;
    private String category_id;
    private String category_name;
    private String sub_category;
    private String updated;         // 1: from Remote DB,    0: to Remote DB

    public DBSubCategory() {}

    public DBSubCategory(String subCategoryID, String category_id, String category_name, String sub_category, String updated) {
        this.subCategoryID = subCategoryID;
        this.category_id = category_id;
        this.category_name = category_name;
        this.sub_category = sub_category;
        this.updated = updated;
    }

    public DBSubCategory(long id, String subCategoryID, String category_id, String category_name, String sub_category, String updated) {
        this.id = id;
        this.subCategoryID = subCategoryID;
        this.category_id = category_id;
        this.category_name = category_name;
        this.sub_category = sub_category;
        this.updated = updated;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setSubCategoryID(String subCategoryID) {
        this.subCategoryID = subCategoryID;
    }

    public String getSubCategoryID() {
        return subCategoryID;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setSub_category(String sub_category) {
        this.sub_category = sub_category;
    }

    public String getSub_category() {
        return sub_category;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getUpdated() {
        return updated;
    }
}
