package offline.mapbox.trackerapp.model;

public class CategoryModel {
    //    private int ImageId;
    private String Title;

    public CategoryModel(String Title/*, int ImageId*/){

        this.Title = Title;
//        this.ImageId = ImageId;
    }

    public String getTitle(){

        return Title;
    }

    public void setTitle(String Title){

        this.Title = Title;
    }

//    public int getImageId(){
//
//        return ImageId;
//    }
//
//    public void setImageId(int ImageId){
//
//        this.ImageId = ImageId;
//    }

    @Override
    public String toString() {
        return Title ;
    }
}
