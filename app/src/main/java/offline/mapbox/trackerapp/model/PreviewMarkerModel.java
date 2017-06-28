package offline.mapbox.trackerapp.model;

public class PreviewMarkerModel {
    private String content;

    public PreviewMarkerModel() {
        this.content = "";
    }

    public PreviewMarkerModel(String content) {
        this.content = content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
