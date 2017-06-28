package offline.mapbox.trackerapp.model;

public class ImageRecyclerModel {
    private String imageURL;

    public ImageRecyclerModel() {
        this.imageURL = "";
    }

    public ImageRecyclerModel(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getImageURL() {
        return imageURL;
    }
}
