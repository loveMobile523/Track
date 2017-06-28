package offline.mapbox.trackerapp.model;

public class DBCustomField {
    private long id;
    private String customFieldId;
    private String sub_category_id;
    private String field;
    private String type;
    private String updated;         // 1: from Remote DB,    0: to Remote DB

    public DBCustomField() {}

    public DBCustomField(String customFieldId, String sub_category_id, String field, String type, String updated) {
        this.customFieldId = customFieldId;
        this.sub_category_id = sub_category_id;
        this.field = field;
        this.type = type;
        this.updated = updated;
    }

    public DBCustomField(long id, String customFieldId, String sub_category_id, String field, String type, String updated) {
        this.id = id;
        this.customFieldId = customFieldId;
        this.sub_category_id = sub_category_id;
        this.field = field;
        this.type = type;
        this.updated = updated;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setCustomFieldId(String customFieldId) {
        this.customFieldId = customFieldId;
    }

    public String getCustomFieldId() {
        return customFieldId;
    }

    public void setSub_category_id(String sub_category_id) {
        this.sub_category_id = sub_category_id;
    }

    public String getSub_category_id() {
        return sub_category_id;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getUpdated() {
        return updated;
    }
}
