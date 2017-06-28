package offline.mapbox.trackerapp.model;

public class DBMarker {
    private long id;
    private String markerId;
    private String name;
    private String icon_name;
    private String icon_path;
    private String address;
    private String location_latlng;
    private String category;
    private String sub_category;
    private String description;
    private String custom_fields;
    private String drawing_type;
    private String fill_color;
    private String border_color;
    private String transparency;
    private String border_weight;
    private String bounds;
    private String budget;
    private String expence;
    private String date_decide_budget;
    private String image;
    private String status;
    private String damage_route;
    private String updated;         // 1: from Remote DB,    0: to Remote DB

    public DBMarker() {}

    public DBMarker(String location_latlng, String category, String sub_category, String description, String custom_fields, String drawing_type, String updated) {
        this.location_latlng = location_latlng;
        this.category = category;
        this.sub_category = sub_category;
        this.description = description;
        this.custom_fields = custom_fields;
        this.drawing_type = drawing_type;
        this.updated = updated;

        this.markerId = "";
        this.name = "";
        this.icon_name = "";
        this.icon_path = "";
        this.address = "";
        this.fill_color = "";
        this.border_color = "";
        this.transparency = "";
        this.border_weight = "";
        this.bounds = "";
        this.budget = "";
        this.expence = "";
        this.date_decide_budget = "";
        this.image = "";
        this.status = "";
        this.damage_route = "";
    }

    public DBMarker(long id, String location_latlng, String category, String sub_category, String description, String custom_fields, String drawing_type, String updated) {
        this.id = id;
        this.location_latlng = location_latlng;
        this.category = category;
        this.sub_category = sub_category;
        this.description = description;
        this.custom_fields = custom_fields;
        this.drawing_type = drawing_type;
        this.updated = updated;

        this.markerId = "";
        this.name = "";
        this.icon_name = "";
        this.icon_path = "";
        this.address = "";
        this.fill_color = "";
        this.border_color = "";
        this.transparency = "";
        this.border_weight = "";
        this.bounds = "";
        this.budget = "";
        this.expence = "";
        this.date_decide_budget = "";
        this.image = "";
        this.status = "";
        this.damage_route = "";
    }

    public DBMarker(String name, String icon_name, String icon_path,
                    String location_latlng, String category, String sub_category,
                    String description, String custom_fields, String drawing_type,
                    String expence, String status, String updated) {
        this.name = name;
        this.icon_name = icon_name;
        this.icon_path = icon_path;
        this.location_latlng = location_latlng;
        this.category = category;
        this.sub_category = sub_category;
        this.description = description;
        this.custom_fields = custom_fields;
        this.drawing_type = drawing_type;
        this.expence = expence;
        this.status = status;
        this.updated = updated;

        this.markerId = "";
        this.address = "";
        this.fill_color = "";
        this.border_color = "";
        this.transparency = "";
        this.border_weight = "";
        this.bounds = "";
        this.budget = "";
        this.date_decide_budget = "";
        this.image = "";
        this.damage_route = "";
    }

    public DBMarker(long id, String name, String icon_name, String icon_path,
                    String location_latlng, String category, String sub_category,
                    String description, String custom_fields, String drawing_type,
                    String expence, String status, String updated) {
        this.id = id;
        this.name = name;
        this.icon_name = icon_name;
        this.icon_path = icon_path;
        this.location_latlng = location_latlng;
        this.category = category;
        this.sub_category = sub_category;
        this.description = description;
        this.custom_fields = custom_fields;
        this.drawing_type = drawing_type;
        this.expence = expence;
        this.status = status;
        this.updated = updated;

        this.markerId = "";
        this.address = "";
        this.fill_color = "";
        this.border_color = "";
        this.transparency = "";
        this.border_weight = "";
        this.bounds = "";
        this.budget = "";
        this.date_decide_budget = "";
        this.image = "";
        this.damage_route = "";
    }

    public DBMarker(String name, String icon_name, String icon_path,
                    String location_latlng, String category, String sub_category,
                    String description, String custom_fields, String drawing_type,
                    String expence, String image, String status, String updated) {
        this.markerId = "";
        this.name = name;
        this.icon_name = icon_name;
        this.icon_path = icon_path;
        this.location_latlng = location_latlng;
        this.category = category;
        this.sub_category = sub_category;
        this.description = description;
        this.custom_fields = custom_fields;
        this.drawing_type = drawing_type;
        this.expence = expence;
        this.image = image;
        this.status = status;
        this.updated = updated;


        this.address = "";
        this.fill_color = "";
        this.border_color = "";
        this.transparency = "";
        this.border_weight = "";
        this.bounds = "";
        this.budget = "";
        this.date_decide_budget = "";
        this.damage_route = "";
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setMarkerId(String markerId) {
        this.markerId = markerId;
    }

    public String getMarkerId() {
        return markerId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setIcon_name(String icon_name) {
        this.icon_name = icon_name;
    }

    public String getIcon_name() {
        return icon_name;
    }

    public void setIcon_path(String icon_path) {
        this.icon_path = icon_path;
    }

    public String getIcon_path() {
        return icon_path;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setLocation_latlng(String location_latlng) {
        this.location_latlng = location_latlng;
    }

    public String getLocation_latlng() {
        return location_latlng;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setSub_category(String sub_category) {
        this.sub_category = sub_category;
    }

    public String getSub_category() {
        return sub_category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setCustom_fields(String custom_fields) {
        this.custom_fields = custom_fields;
    }

    public String getCustom_fields() {
        return custom_fields;
    }

    public void setDrawing_type(String drawing_type) {
        this.drawing_type = drawing_type;
    }

    public String getDrawing_type() {
        return drawing_type;
    }

    public void setFill_color(String fill_color) {
        this.fill_color = fill_color;
    }

    public String getFill_color() {
        return fill_color;
    }

    public void setBorder_color(String border_color) {
        this.border_color = border_color;
    }

    public String getBorder_color() {
        return border_color;
    }

    public void setTransparency(String transparency) {
        this.transparency = transparency;
    }

    public String getTransparency() {
        return transparency;
    }

    public void setBorder_weight(String border_weight) {
        this.border_weight = border_weight;
    }

    public String getBorder_weight() {
        return border_weight;
    }

    public void setBounds(String bounds) {
        this.bounds = bounds;
    }

    public String getBounds() {
        return bounds;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getBudget() {
        return budget;
    }

    public void setExpence(String expence) {
        this.expence = expence;
    }

    public String getExpence() {
        return expence;
    }

    public void setDate_decide_budget(String date_decide_budget) {
        this.date_decide_budget = date_decide_budget;
    }

    public String getDate_decide_budget() {
        return date_decide_budget;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setDamage_route(String damage_route) {
        this.damage_route = damage_route;
    }

    public String getDamage_route() {
        return damage_route;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getUpdated() {
        return updated;
    }
}
