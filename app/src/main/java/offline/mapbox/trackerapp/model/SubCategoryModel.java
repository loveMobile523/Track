package offline.mapbox.trackerapp.model;

public class SubCategoryModel {
    private String Title;

    public SubCategoryModel(String Title/*, int ImageId*/){

        this.Title = Title;
    }

    public String getTitle(){

        return Title;
    }

    public void setTitle(String Title){

        this.Title = Title;
    }

    @Override
    public String toString() {
        return Title ;
    }
}
