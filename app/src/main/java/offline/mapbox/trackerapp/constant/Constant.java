package offline.mapbox.trackerapp.constant;

public class Constant {
    public static final String MyShared = "MyShared";
    public static String firstRun = "firstRun";
    public static String Status = "Status";
    public static String LOGIN = "LOGIN";
    public static String LOGOUT = "LOGOUT";

    public static String UserId;
    public static String UserName;
    public static String UserType;
    public static String UserTypeAgent = "agent";
    public static String UserTypeWorker = "worker";

    //
    public static String Need = "Need";
    public static String NoNeed = "NoNeed";

    // position
    public static String Latitude;
    public static String Longitude;


    // camera zoom level
    public static int CameraZoomLevel = 12;

    // request permission tag
    public static final int Permission_ACCESS_FINE_LOCATION = 2;
    public static final int Permission_Camera = 5;
    public static final int Permission_Write_external_storage = 7;
    public static final int Permission_Read_external_storage = 8;

    public static final int REQUEST_TAKE_PHOTO = 11;
    public static final int REQUEST_SELECT_PHOTO = 12;

    // current selected view name
    public static final String Fields = "Fields";
    public static final String PreviewMarkers = "PreviewMarkers";
    public static final String DetailPreviewMarkers = "DetailPreviewMarkers";
    public static final String Message = "Message";
    public static final String NewMessage = "NewMessage";

    // file path
    public static final String IMAGE_UPLOADING = "UploadingImage";
    public static final String IMAGE_CARD_FOLDER = "CardImage";
    public static final String TRACKER_FOLDER = "Tracker";


    // max number for uploading images
    public static final int MAX_NUMBER_UPLOADING_IMAGES = 5;
    public static int currentCNTofImagesForUp;
    public static String[] imagePaths = new String[5];
    public static String selectedImagePath;

    // number of custom fields
    public static final int MAX_NUMBER_CUSTOM_FIELDS = 15;
    public static int cntOfCustomFields;


    // path
    public static final String IconBasePath = "http://maproots.com/dev/road/admin_side/";
}

