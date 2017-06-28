package offline.mapbox.trackerapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.Polygon;
import com.mapbox.mapboxsdk.annotations.PolygonOptions;
import com.mapbox.mapboxsdk.annotations.Polyline;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.location.LocationSource;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.offline.OfflineManager;
import com.mapbox.mapboxsdk.offline.OfflineRegion;
import com.mapbox.mapboxsdk.offline.OfflineRegionError;
import com.mapbox.mapboxsdk.offline.OfflineRegionStatus;
import com.mapbox.mapboxsdk.offline.OfflineTilePyramidRegionDefinition;
import com.mapbox.services.android.telemetry.location.LocationEngine;
import com.mapbox.services.android.telemetry.location.LocationEngineListener;
import com.mapbox.services.android.telemetry.permissions.PermissionsListener;
import com.mapbox.services.android.telemetry.permissions.PermissionsManager;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mehdi.sakout.fancybuttons.FancyButton;
import offline.mapbox.trackerapp.adapter.CategoryAdapter;
import offline.mapbox.trackerapp.adapter.IconAdapter;
import offline.mapbox.trackerapp.adapter.ImageRecyclerAdapter;
import offline.mapbox.trackerapp.adapter.UserAdapter;
import offline.mapbox.trackerapp.adapter.PreviewMarkerAdapter;
import offline.mapbox.trackerapp.adapter.SubCategoryAdapter;
import offline.mapbox.trackerapp.constant.Constant;
import offline.mapbox.trackerapp.database.DBCategoryTable;
import offline.mapbox.trackerapp.database.DBCustomFieldTable;
import offline.mapbox.trackerapp.database.DBIconTable;
import offline.mapbox.trackerapp.database.DBMarkerTable;
import offline.mapbox.trackerapp.database.DBSubCategoryTable;
import offline.mapbox.trackerapp.database.DBUserTable;
import offline.mapbox.trackerapp.model.DBCategory;
import offline.mapbox.trackerapp.model.DBCustomField;
import offline.mapbox.trackerapp.model.DBIcon;
import offline.mapbox.trackerapp.model.DBMarker;
import offline.mapbox.trackerapp.model.DBSubCategory;
import offline.mapbox.trackerapp.model.DBUser;
import offline.mapbox.trackerapp.model.ImageRecyclerModel;
import offline.mapbox.trackerapp.model.MessageLvModel;
import offline.mapbox.trackerapp.util.FileUtils;
import offline.mapbox.trackerapp.util.RecyclerOnItemClickListener;

public class MainActivity extends Activity  implements PermissionsListener{
    private SharedPreferences.Editor editor;
    private SharedPreferences prefs;

    private Button btnPreviewMarkers;
    private Button btnMessage;
    private Button btnSync;
    private Button btnLogout;
    private Button btnAdd;

    private RelativeLayout reLayGallery;
    private RelativeLayout reLayCamera;

    // ---------------------------------------------------------------------------------------------
    // middle part hidden show
    private String currentSelectedViewName;
    private LinearLayout liLayFields;
    private LinearLayout liLayPreviewMarkers;
    private RelativeLayout reLayMessage;
    private RelativeLayout reLayNewMessage;


    // ---------------------------------------------------------------------------------------------
    // fire base
//    private DatabaseReference mDatabase;
//    final FirebaseDatabase database = FirebaseDatabase.getInstance();
//    DatabaseReference ref = database.getReference("server/saving-data/fireblog");

    // ---------------------------------------------------------------------------------------------
    // map
//    MapView mMapView;
//    public static GoogleMap googleMap;

    // search
    private EditText etSearch;
    private ImageButton btnSearch;

    // marker, polyline, polygon
    private TextView tvMarker;
    private TextView tvPolyline;
    private TextView tvPolygon;

    private int selectedShape;
    public List<LatLng> listLatLngs;

    // ---------------------------------------------------------------------------------------------
    // category
    private Spinner spinnerCat;
    List<DBCategory> dbCategories;

    private Spinner spinnerSubCat;
    List<DBSubCategory> dbSubCategories;

    private Spinner spinnerIcon;
    List<DBIcon> dbIcons;
    public  static List<DBMarker> dbMarkers;
    public  static List<DBUser> dbUsers;

    private Spinner spinnerStatus;
    private EditText etName;
    private EditText etExpence;

    List<DBCustomField> dbCustomFields;

    private String selectedCategoryName = "";
    private String selectedSubCategoryName = "";
    private String selectedStatus = "";
    private String selectedIcon = "";
    private String selectedIconPath = "";

    private EditText[] etCustomFields = new EditText[Constant.MAX_NUMBER_CUSTOM_FIELDS];
    private int[] etCustomFieldResources = {R.id.etField1, R.id.etField2, R.id.etField3,
            R.id.etField4, R.id.etField5, R.id.etField6, R.id.etField7, R.id.etField8, R.id.etField9,
            R.id.etField10, R.id.etField11, R.id.etField12, R.id.etField13, R.id.etField14, R.id.etField15};

    private EditText etComment;
    // ---------------------------------------------------------------------------------------------
    // recycler view
    // category show recycler view
    private ArrayList<ImageRecyclerModel> imageRecyclerModelArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ImageRecyclerAdapter adapter;

    private ImageView imgSelected;

    // ---------------------------------------------------------------------------------------------
    // preview marker part
    private ImageButton btnBackPreviewMarker;

    private ListView lvPreviewMarkers;

    // ---------------------------------------------------------------------------------------------
    // detail preview marker part


    // ---------------------------------------------------------------------------------------------
    // message part
    private ImageButton btnBackMessage;
    private Button btnNewMessage;

    private ListView lvUser;
    public static String[] lvUsernames = new String[]{
            "User 1", "User 2", "User 3", "User 4", "User 5"
    };

    // ---------------------------------------------------------------------------------------------
    // remote db
    static final String url = "jdbc:mysql://115.124.123.25:3306/maproots_roads"; // jdbc:mysql://
    static final String user = "maproots_roadsus";
    static final String pass = "FrenchRoadMap1!";

//    static final String url = "jdbc:mysql://mysql.us.cloudlogin.co:3306/yoliza_roads"; // jdbc:mysql://
//    static final String user = "yoliza_roads";
//    static final String pass = "yoliz_Road1!";

    static String sqlStrStatic;

    KProgressHUD kpHUD;


    // ---------------------------------------------------------------------------------------------
    // Map Box
    private static final String TAG = "MainActivity";

    // JSON encoding/decoding
    public static final String JSON_CHARSET = "UTF-8";
    public static final String JSON_FIELD_REGION_NAME = "FIELD_REGION_NAME";

    // UI elements
    private com.mapbox.mapboxsdk.maps.MapView mapView;
    private MapboxMap map;
    private ProgressBar progressBar;
    private Button downloadButton;
    private Button listButton;

    private boolean isEndNotified;
    private int regionSelected;

    // Offline objects
    private OfflineManager offlineManager;
    private OfflineRegion offlineRegion;

    private LocationEngine locationEngine;
    private LocationEngineListener locationEngineListener;
    private PermissionsManager permissionsManager;

    private Location lastLocation;

    // ---------------------------------------------------------------------------------------------
    // image upload
    String baseRemotePath = "assets/img/markers/";
    String totalImagePath = "";
    String uploadFilePath = "";
    int serverResponseCode = 0;
    ProgressDialog dialog = null;

    String upLoadServerUri = "http://maproots.com/dev/road/admin_side/UploadToServer.php";     // first
//    String upLoadServerUri = "http://yoliza.com/roads/admin_side/UploadToServer.php";          // second
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        editor = getSharedPreferences(Constant.MyShared, MODE_PRIVATE).edit();
        prefs = getSharedPreferences(Constant.MyShared, MODE_PRIVATE);

        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.
        Mapbox.getInstance(this, getString(R.string.access_token));

        // This contains the MapView in XML and needs to be called after the account manager
        setContentView(R.layout.activity_main);

        // Get the location engine object for later use.
        locationEngine = LocationSource.getLocationEngine(this);
        locationEngine.activate();

        // Set up the MapView
        mapView = (com.mapbox.mapboxsdk.maps.MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new com.mapbox.mapboxsdk.maps.OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                map = mapboxMap;

                if (map != null) {
                    toggleGps(!map.isMyLocationEnabled());
                }
            }
        });



        // Assign progressBar for later use
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        // Set up the offlineManager
        offlineManager = OfflineManager.getInstance(this);

        // Bottom navigation bar button clicks are handled here.
        // Download offline button
        downloadButton = (Button) findViewById(R.id.download_button);
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadRegionDialog();
            }
        });

        // List offline regions
        listButton = (Button) findViewById(R.id.list_button);
        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadedRegionList();
            }
        });

        // -----------------------------------------------------------------------------------------
        initWidget();
        initSpinner();
        onClickBtn();
        onClickBackBtns();
        onClickSelectShape();
        onSpinnerItemSelected();
        onCaptureImage();

        resetVisibleMiddlePart();
        currentSelectedViewName = Constant.Fields;
        liLayFields.setVisibility(View.VISIBLE);

    }

    // ---------------------------------------------------------------------------------------------
    // Map Box Start

    private void toggleGps(boolean enableGps) {
        if (enableGps) {
            // Check if user has granted location permission
            permissionsManager = new PermissionsManager((PermissionsListener) MainActivity.this);
            if (!PermissionsManager.areLocationPermissionsGranted(this)) {
                permissionsManager.requestLocationPermissions(this);
            } else {
                enableLocation(true, true);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        enableLocation(true, false);
                    }
                },500);
            }
        } else {
            enableLocation(false, true);
        }
    }

    private void enableLocation(boolean enabled, final boolean judgeMove) {
        if (enabled) {
            // If we have the last location of the user, we can move the camera to that position.
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                // todo permission
                return;
            }

            lastLocation = locationEngine.getLastLocation();
            if (lastLocation != null && judgeMove) {
                Toast.makeText(getApplicationContext(), "Last Position: " + String.valueOf((new LatLng(lastLocation)).getLatitude()), Toast.LENGTH_SHORT).show();
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastLocation), Constant.CameraZoomLevel));
            }

            locationEngineListener = new LocationEngineListener() {
                @Override
                public void onConnected() {
                    // No action needed here.
                }

                @Override
                public void onLocationChanged(Location location) {
                    if (location != null) {
                        // Move the map camera to where the user location is and then remove the
                        // listener so the camera isn't constantly updating when the user location
                        // changes. When the user disables and then enables the location again, this
                        // listener is registered again and will adjust the camera once again.

                        drawShape(location);

                        editor.putString(Constant.Latitude, String.valueOf(location.getLatitude()));
                        editor.putString(Constant.Longitude, String.valueOf(location.getLongitude()));
                        editor.commit();

                        if(judgeMove) {
                            Toast.makeText(getApplicationContext(), "Current Position: " + String.valueOf(location.getLatitude()), Toast.LENGTH_SHORT).show();
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location), Constant.CameraZoomLevel));
                            locationEngine.removeLocationEngineListener(this);
                        }

//                        MarkerViewOptions markerViewOptions = new MarkerViewOptions()
//                                .position(new LatLng(location))
//                                /*.title("New Marker")
//                                .snippet("this is snippet")*/;
//
//                        map.addMarker(markerViewOptions);

//                        locationEngine.removeLocationEngineListener(this);
                    }
                }
            };
            locationEngine.addLocationEngineListener(locationEngineListener);
//            floatingActionButton.setImageResource(R.drawable.ic_location_disabled_24dp);
        } else {
//            floatingActionButton.setImageResource(R.drawable.ic_my_location_24dp);
        }
        // Enable or disable the location layer on the map
        map.setMyLocationEnabled(enabled);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, "This app needs location permissions in order to show its functionality.",
                Toast.LENGTH_LONG).show();
    }


    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            enableLocation(true, true);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    enableLocation(true, false);
                }
            },500);
        } else {
            Toast.makeText(this, "You didn't grant location permissions.",
                    Toast.LENGTH_LONG).show();
            finish();
        }
    }

    // Override Activity lifecycle methods
    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();

        adapter = new ImageRecyclerAdapter(MainActivity.this, imageRecyclerModelArrayList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();

//        if(permissionsManager == null) {
//            permissionsManager = new PermissionsManager(MainActivity.this);
//            if (!PermissionsManager.areLocationPermissionsGranted(this)) {
//                permissionsManager.requestLocationPermissions(this);
//            } else {
//                enableLocation(true);
//            }
//        }

//        enableLocation(true);


    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();

        // Ensure no memory leak occurs if we register the location listener but the call hasn't
        // been made yet.
        if (locationEngineListener != null) {
            locationEngine.removeLocationEngineListener(locationEngineListener);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    private void downloadRegionDialog() {
        // Set up download interaction. Display a dialog
        // when the user clicks download button and require
        // a user-provided region name
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        final EditText regionNameEdit = new EditText(MainActivity.this);
        regionNameEdit.setHint("Enter name");

        // Build the dialog box
        builder.setTitle("Name new region")
                .setView(regionNameEdit)
                .setMessage("Downloads the map region you currently are viewing")
                .setPositiveButton("Download", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String regionName = regionNameEdit.getText().toString();
                        // Require a region name to begin the download.
                        // If the user-provided string is empty, display
                        // a toast message and do not begin download.
                        if (regionName.length() == 0) {
                            Toast.makeText(MainActivity.this, "Region name cannot be empty.", Toast.LENGTH_SHORT).show();
                        } else {
                            // Begin download process
                            downloadRegion(regionName);
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        // Display the dialog
        builder.show();
    }

    private void downloadRegion(final String regionName) {
        // Define offline region parameters, including bounds,
        // min/max zoom, and metadata

        // Start the progressBar
        startProgress();

        // Create offline definition using the current
        // style and boundaries of visible map area
        String styleUrl = map.getStyleUrl();
        LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;
//        double minZoom = map.getCameraPosition().zoom;
        double minZoom = map.getMinZoomLevel();
        double maxZoom = map.getMaxZoomLevel();
        float pixelRatio = this.getResources().getDisplayMetrics().density;
        OfflineTilePyramidRegionDefinition definition = new OfflineTilePyramidRegionDefinition(
                styleUrl, bounds, minZoom, maxZoom, pixelRatio);

        // Build a JSONObject using the user-defined offline region title,
        // convert it into string, and use it to create a metadata variable.
        // The metadata varaible will later be passed to createOfflineRegion()
        byte[] metadata;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(JSON_FIELD_REGION_NAME, regionName);
            String json = jsonObject.toString();
            metadata = json.getBytes(JSON_CHARSET);
        } catch (Exception exception) {
            Log.e(TAG, "Failed to encode metadata: " + exception.getMessage());
            metadata = null;
        }

        // Create the offline region and launch the download
        offlineManager.createOfflineRegion(definition, metadata, new OfflineManager.CreateOfflineRegionCallback() {
            @Override
            public void onCreate(OfflineRegion offlineRegion) {
                Log.d(TAG, "Offline region created: " + regionName);
                MainActivity.this.offlineRegion = offlineRegion;
                launchDownload();
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, "Error: " + error);
            }
        });
    }

    private void launchDownload() {
        // Set up an observer to handle download progress and
        // notify the user when the region is finished downloading
        offlineRegion.setObserver(new OfflineRegion.OfflineRegionObserver() {
            @Override
            public void onStatusChanged(OfflineRegionStatus status) {
                // Compute a percentage
                double percentage = status.getRequiredResourceCount() >= 0
                        ? (100.0 * status.getCompletedResourceCount() / status.getRequiredResourceCount()) :
                        0.0;

                if (status.isComplete()) {
                    // Download complete
                    endProgress("Region downloaded successfully.");
                    return;
                } else if (status.isRequiredResourceCountPrecise()) {
                    // Switch to determinate state
                    setPercentage((int) Math.round(percentage));
                }

                // Log what is being currently downloaded
                Log.d(TAG, String.format("%s/%s resources; %s bytes downloaded.",
                        String.valueOf(status.getCompletedResourceCount()),
                        String.valueOf(status.getRequiredResourceCount()),
                        String.valueOf(status.getCompletedResourceSize())));
            }

            @Override
            public void onError(OfflineRegionError error) {
                Log.e(TAG, "onError reason: " + error.getReason());
                Log.e(TAG, "onError message: " + error.getMessage());
            }

            @Override
            public void mapboxTileCountLimitExceeded(long limit) {
                Log.e(TAG, "Mapbox tile count limit exceeded: " + limit);
            }
        });

        // Change the region state
        offlineRegion.setDownloadState(OfflineRegion.STATE_ACTIVE);
    }

    private void downloadedRegionList() {
        // Build a region list when the user clicks the list button

        // Reset the region selected int to 0
        regionSelected = 0;

        // Query the DB asynchronously
        offlineManager.listOfflineRegions(new OfflineManager.ListOfflineRegionsCallback() {
            @Override
            public void onList(final OfflineRegion[] offlineRegions) {
                // Check result. If no regions have been
                // downloaded yet, notify user and return
                if (offlineRegions == null || offlineRegions.length == 0) {
                    Toast.makeText(MainActivity.this, "You have no regions yet.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Add all of the region names to a list
                ArrayList<String> offlineRegionsNames = new ArrayList<>();
                for (OfflineRegion offlineRegion : offlineRegions) {
                    offlineRegionsNames.add(getRegionName(offlineRegion));
                }
                final CharSequence[] items = offlineRegionsNames.toArray(new CharSequence[offlineRegionsNames.size()]);

                // Build a dialog containing the list of regions
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("List")
                        .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Track which region the user selects
                                regionSelected = which;
                            }
                        })
                        .setPositiveButton("Navigate to", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                                Toast.makeText(MainActivity.this, items[regionSelected], Toast.LENGTH_LONG).show();

                                // Get the region bounds and zoom
                                LatLngBounds bounds = ((OfflineTilePyramidRegionDefinition)
                                        offlineRegions[regionSelected].getDefinition()).getBounds();
                                double regionZoom = ((OfflineTilePyramidRegionDefinition)
                                        offlineRegions[regionSelected].getDefinition()).getMinZoom();

                                // Create new camera position
                                CameraPosition cameraPosition = new CameraPosition.Builder()
                                        .target(bounds.getCenter())
                                        .zoom(Constant.CameraZoomLevel)
                                        .build();

                                // Move camera to new position
                                map.moveCamera(com.mapbox.mapboxsdk.camera.CameraUpdateFactory.newCameraPosition(cameraPosition));

                            }
                        })
                        .setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                // Make progressBar indeterminate and
                                // set it to visible to signal that
                                // the deletion process has begun
                                progressBar.setIndeterminate(true);
                                progressBar.setVisibility(View.VISIBLE);

                                // Begin the deletion process
                                offlineRegions[regionSelected].delete(new OfflineRegion.OfflineRegionDeleteCallback() {
                                    @Override
                                    public void onDelete() {
                                        // Once the region is deleted, remove the
                                        // progressBar and display a toast
                                        progressBar.setVisibility(View.INVISIBLE);
                                        progressBar.setIndeterminate(false);
                                        Toast.makeText(MainActivity.this, "Region deleted", Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onError(String error) {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        progressBar.setIndeterminate(false);
                                        Log.e(TAG, "Error: " + error);
                                    }
                                });
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                // When the user cancels, don't do anything.
                                // The dialog will automatically close
                            }
                        }).create();
                dialog.show();

            }

            @Override
            public void onError(String error) {
                Log.e(TAG, "Error: " + error);
            }
        });
    }

    private String getRegionName(OfflineRegion offlineRegion) {
        // Get the retion name from the offline region metadata
        String regionName;

        try {
            byte[] metadata = offlineRegion.getMetadata();
            String json = new String(metadata, JSON_CHARSET);
            JSONObject jsonObject = new JSONObject(json);
            regionName = jsonObject.getString(JSON_FIELD_REGION_NAME);
        } catch (Exception exception) {
            Log.e(TAG, "Failed to decode metadata: " + exception.getMessage());
            regionName = "Region " + offlineRegion.getID();
        }
        return regionName;
    }

    // Progress bar methods
    private void startProgress() {
        // Disable buttons
        downloadButton.setEnabled(false);
        listButton.setEnabled(false);

        // Start and show the progress bar
        isEndNotified = false;
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void setPercentage(final int percentage) {
        progressBar.setIndeterminate(false);
        progressBar.setProgress(percentage);
    }

    private void endProgress(final String message) {
        // Don't notify more than once
        if (isEndNotified) {
            return;
        }

        // Enable buttons
        downloadButton.setEnabled(true);
        listButton.setEnabled(true);

        // Stop and hide the progress bar
        isEndNotified = true;
        progressBar.setIndeterminate(false);
        progressBar.setVisibility(View.GONE);

        // Show a toast
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
    }
    // Map Box End
    // ---------------------------------------------------------------------------------------------

    private void resetCustomFields() {
        for(int i = 0; i < Constant.MAX_NUMBER_CUSTOM_FIELDS; i++) {
            etCustomFields[i].setVisibility(View.GONE);
        }
    }

    private void showCustomFields(List<DBCustomField> dbCustomFields) {
        Constant.cntOfCustomFields = dbCustomFields.size();

        for(int i = 0; i < Constant.cntOfCustomFields; i++) {
            etCustomFields[i].setVisibility(View.VISIBLE);

            DBCustomField dbCustomField = dbCustomFields.get(i);
            etCustomFields[i].setHint(dbCustomField.getField());
        }
    }

    private void initWidget() {
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
//        FirebaseDatabase.
//        mDatabase = FirebaseDatabase.getInstance().getReference();

        // todo consider shared
        Constant.currentCNTofImagesForUp = 0;

        kpHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true)
//                .setLabel("Login")
                .setAnimationSpeed(1)
                .setDimAmount(0.5f);

        // -----------------------------------------------------------------------------------------
        etComment = (EditText) findViewById(R.id.etComment);

        for(int i = 0; i < Constant.MAX_NUMBER_CUSTOM_FIELDS; i++) {
            etCustomFields[i] = (EditText) findViewById(etCustomFieldResources[i]);
        }

        resetCustomFields();

        // -----------------------------------------------------------------------------------------
        btnPreviewMarkers = (Button) findViewById(R.id.btnPreviewMarkers);
        btnMessage = (Button) findViewById(R.id.btnMessage);
        btnSync = (Button) findViewById(R.id.btnSync);
        btnLogout = (Button) findViewById(R.id.btnLogout);

        btnAdd = (Button) findViewById(R.id.btnAdd);

        reLayGallery = (RelativeLayout) findViewById(R.id.reLayGallery);
        reLayCamera = (RelativeLayout) findViewById(R.id.reLayCamera);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_horizontal);
        imgSelected = (ImageView) findViewById(R.id.imgSelected);

        etSearch = (EditText) findViewById(R.id.etSearch);
        btnSearch = (ImageButton) findViewById(R.id.btnSearch);

        // -----------------------------------------------------------------------------------------
        // shape select
        tvMarker = (TextView) findViewById(R.id.tvMarker);          // 1
        tvPolyline = (TextView) findViewById(R.id.tvPolyline);      // 2
        tvPolygon = (TextView) findViewById(R.id.tvPolygon);        // 3

        selectedShape = 1;
        listLatLngs = new ArrayList<LatLng>();

        // -----------------------------------------------------------------------------------------
        // middle part hidden show
        currentSelectedViewName = Constant.Fields;

        liLayFields = (LinearLayout) findViewById(R.id.liLayFields);
        liLayPreviewMarkers = (LinearLayout) findViewById(R.id.liLayPreviewMarkers);
        reLayMessage = (RelativeLayout) findViewById(R.id.reLayMessage);
        reLayNewMessage = (RelativeLayout) findViewById(R.id.reLayNewMessage);

        // -----------------------------------------------------------------------------------------
        // preview marker
        btnBackPreviewMarker = (ImageButton) findViewById(R.id.btnBackPreviewMarker);

        lvPreviewMarkers = (ListView) findViewById(R.id.lvPreviewMarkers);

        // -----------------------------------------------------------------------------------------
        // message
        btnBackMessage = (ImageButton) findViewById(R.id.btnBackMessage);

        btnNewMessage = (Button) findViewById(R.id.btnNewMessage);

        lvUser = (ListView) findViewById(R.id.lvUser);

        // -----------------------------------------------------------------------------------------
        // map
    }

    private void drawShape(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        if (listLatLngs.size() > 1000) listLatLngs.remove(2);
        listLatLngs.add(latLng);

        int len = listLatLngs.size();
        int i;
        map.clear();

        editor.putString(Constant.Latitude, String.valueOf(latLng.getLatitude()));
        editor.putString(Constant.Longitude, String.valueOf(latLng.getLongitude()));
        editor.commit();

        switch (selectedShape) {
            case 1:

                listLatLngs.clear();
                listLatLngs.add(latLng);
                map.addMarker(new MarkerOptions().position(latLng));
                break;
            case 2:
                PolylineOptions polylineOptions = new PolylineOptions();
                for (i = 0; i < len; i++) {
                    if(i == 0) {
                        map.addMarker(new MarkerOptions().position(listLatLngs.get(i)));
                    }
                    polylineOptions.add(listLatLngs.get(i));
                }
                polylineOptions.width(5);
                polylineOptions.color(Color.RED);
                Polyline line = map.addPolyline(polylineOptions);
                break;
            case 3:
                PolygonOptions polygonOptions = new PolygonOptions();
                for (i = 0; i < len; i++) {
                    if(i == 0) {
                        map.addMarker(new MarkerOptions().position(listLatLngs.get(i)));
                    }
                    polygonOptions.add(listLatLngs.get(i));
                }

                if (len > 0)
                    polygonOptions.add(listLatLngs.get(0));

                polygonOptions.strokeColor(Color.RED);
//            polygonOptions.fillColor(Color.BLUE);

                Polygon polygon = map.addPolygon(polygonOptions);
                break;
            default:
                break;
        }
    }

    // ---------------------------------------------------------------------------------------------
    private void initSpinner() {
        spinnerCat = (Spinner) findViewById(R.id.spinnerCat);
        spinnerSubCat = (Spinner) findViewById(R.id.spinnerSubCat);
        spinnerIcon = (Spinner) findViewById(R.id.spinnerIcon);
        spinnerStatus = (Spinner) findViewById(R.id.spinnerStatus);

        etName = (EditText) findViewById(R.id.etName);
        etExpence = (EditText) findViewById(R.id.etExpence);

        // -----------------------------------------------------------------------------------------
        LoginActivity.userName = prefs.getString(Constant.UserName, "");
        LoginActivity.userId = prefs.getString(Constant.UserId, "");

        if(LoginActivity.needData.equals(Constant.Need)) {
            getDataFromRemoteDB();
        } else {
            DBCategoryTable dbCategoryTable = new DBCategoryTable(MainActivity.this);
            dbCategories = dbCategoryTable.getAllCategories();

            DBUserTable dbUserTable = new DBUserTable(MainActivity.this);
            dbUsers = dbUserTable.getAllUsers();

            DBIconTable dbIconTable = new DBIconTable(MainActivity.this);
            dbIcons = dbIconTable.getAllIcons();

            DBMarkerTable dbMarkerTable = new DBMarkerTable(MainActivity.this);
            dbMarkers = dbMarkerTable.getAllMarkers();

            initSpinnerData();
        }
    }

    private void onSpinnerItemSelected() {
        spinnerCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(dbCategories != null ) {
                    if(dbCategories.size() > 0) {
                        DBCategory dbCategory = dbCategories.get(position);
                        selectedCategoryName = dbCategory.getCategory();
//                        selectedCategoryName = spinnerCat.getSelectedItem().toString();

                        getSubCategoryFromCategoryName(selectedCategoryName);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerSubCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(dbSubCategories != null) {
                    if(dbSubCategories.size() > 0) {
                        selectedSubCategoryName = dbSubCategories.get(position).getSub_category();

                        String selectedSubCategoryId = dbSubCategories.get(position).getSubCategoryID();

                        DBCustomFieldTable dbCustomFieldTable = new DBCustomFieldTable(MainActivity.this);
                        dbCustomFields = dbCustomFieldTable.getCustomFieldByCustomFieldSubCateId(selectedSubCategoryId);

                        resetCustomFields();
                        showCustomFields(dbCustomFields);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerIcon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(dbIcons != null) {
                    if(dbIcons.size() > 0) {
                        selectedIcon = dbIcons.get(position).getName();
                        selectedIconPath = Constant.IconBasePath + dbIcons.get(position).getPath();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    // reset selected shape
    private void resetSelectedShape() {
        tvMarker.setBackgroundResource(R.drawable.shape_line);
        tvPolyline.setBackgroundResource(R.drawable.shape_line);
        tvPolygon.setBackgroundResource(R.drawable.shape_line);
    }

    //
    private void onClickSelectShape() {
        tvMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetSelectedShape();
                tvMarker.setBackgroundResource(R.drawable.shape_curve);
                selectedShape = 1;
            }
        });

        tvPolyline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetSelectedShape();
                tvPolyline.setBackgroundResource(R.drawable.shape_curve);
                selectedShape = 2;
            }
        });

        tvPolygon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetSelectedShape();
                tvPolygon.setBackgroundResource(R.drawable.shape_curve);
                selectedShape = 3;
            }
        });
    }

    // reset middle view show hidden
    private void resetVisibleMiddlePart() {
        liLayFields.setVisibility(View.GONE);
        liLayPreviewMarkers.setVisibility(View.GONE);
        reLayMessage.setVisibility(View.GONE);
        reLayNewMessage.setVisibility(View.GONE);
    }

    private void onClickBackBtns() {
        btnBackPreviewMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetVisibleMiddlePart();

                currentSelectedViewName = Constant.Fields;
                liLayFields.setVisibility(View.VISIBLE);
            }
        });

        btnBackMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetVisibleMiddlePart();

                currentSelectedViewName = Constant.Fields;
                liLayFields.setVisibility(View.VISIBLE);
            }
        });
    }

    private void onClickBtn() {
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isNetworkAvailable()) {
                    String location = etSearch.getText().toString();
                    List<Address> addressList = null;

                    if (location != null || !location.equals("")) {
                        Geocoder geocoder = new Geocoder(MainActivity.this);
                        try {
                            addressList = geocoder.getFromLocationName(location, 1);

                        } catch (IOException e) {
                            Toast.makeText(MainActivity.this, "Please check your Network Connection", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }

                        if (addressList != null) {
                            if (addressList.size() > 0) {
                                Address address = addressList.get(0);
                                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
//                                googleMap.addMarker(new MarkerOptions().position(latLng)/*.title("Marker")*/);
//                                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                            } else {
                                Toast.makeText(MainActivity.this, "There is no search result", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "There is no search result", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Please check your Network Connection!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnPreviewMarkers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetVisibleMiddlePart();

                currentSelectedViewName = Constant.PreviewMarkers;
                liLayPreviewMarkers.setVisibility(View.VISIBLE);

                updatePreviewMarker();
            }
        });

        btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetVisibleMiddlePart();

                currentSelectedViewName = Constant.Message;
                reLayMessage.setVisibility(View.VISIBLE);
            }
        });

        btnNewMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                resetVisibleMiddlePart();
//
//                currentSelectedViewName = Constant.NewMessage;
//                reLayNewMessage.setVisibility(View.VISIBLE);

                startActivity(new Intent(MainActivity.this, NewMessageActivity.class));
            }
        });

        btnSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isNetworkAvailable()) {

                    DBMarkerTable dbMarkerTable = new DBMarkerTable(MainActivity.this);
                    List<DBMarker> dbMarkers = dbMarkerTable.getAllMarkers();

                    int len = dbMarkers.size();
                    DBMarker dbMarker;
                    if(len > 0) {
                        imageUploadingToServer();


                        final DatabaseReference markersRef = FirebaseDatabase.getInstance().getReference().child("markers");

                        if(listLatLngs.size() > 0) {
                            // prepare saving data to fire base
                            String key = markersRef.push().getKey();

                            String latitude = "latitude"; // String.valueOf(listLatLngs.get(0).latitude);
                            String longitude = "logitude"; // String.valueOf(listLatLngs.get(0).longitude);

                            SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyyHHmmss");
                            Date curDate = new Date(System.currentTimeMillis());
                            String dateTime = formatter.format(curDate);

                            // save data
                            Map<String, String> markers = new HashMap<String, String>();

                            markers.put("user_id", LoginActivity.userId);
                            markers.put("user_name", LoginActivity.userName);
                            markers.put("latitude", latitude);
                            markers.put("longitude", longitude);
                            markers.put("time", dateTime);

                            markersRef.child(key).setValue(markers);
                        }

//                    saveMarkerToLocalDB();

                        // save local DB marker data to Remote DB
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if(dialog != null) {
                                    dialog.dismiss();
                                }

                                saveDataToRemoteDB();
                            }
                        },2500);
                    } else {
                        Toast.makeText(getApplicationContext(), "No Marker", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please, check your network connection.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString(Constant.Status, Constant.LOGOUT);
                editor.commit();
                finish();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                locationEngine.removeLocationEngineListener(locationEngineListener);

                saveMarkerToLocalDB();

            }
        });

        // -----------------------------------------------------------------------------------------
        // recycler view
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnItemTouchListener(new RecyclerOnItemClickListener(MainActivity.this, new RecyclerOnItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // TODO ....
                ImageRecyclerModel imageRecyclerModel = imageRecyclerModelArrayList.get(position);
                String imageURL = imageRecyclerModel.getImageURL();

                Constant.selectedImagePath = imageURL;
                Bitmap imageBitmap = BitmapFactory.decodeFile(imageURL);

//                imgSelected.setImageResource(images[position]);
                imgSelected.setImageBitmap(imageBitmap);
            }
        }));

        imageRecyclerModelArrayList.clear();

//        for(int i = 0; i < MainActivity.images.length; i++) {
//            imageRecyclerModelArrayList.add(new ImageRecyclerModel(String.valueOf(i)));
//        }
        for(int i = 0; i < Constant.currentCNTofImagesForUp; i++) {
            imageRecyclerModelArrayList.add(new ImageRecyclerModel(Constant.imagePaths[i]));
        }
    }

    // ---------------------------------------------------------------------------------------------
    // image uploading before save data
    private void imageUploadingToServer() {


        DBMarkerTable dbMarkerTable = new DBMarkerTable(MainActivity.this);
        List<DBMarker> dbMarkers = dbMarkerTable.getAllMarkers();

        int len = dbMarkers.size();
        DBMarker dbMarker;
        if(len > 0) {

            for (int i = 0; i < len; i++ ) {
                dbMarker = dbMarkers.get(i);
                if(dbMarker.getUpdated().equals("0")) {
                    totalImagePath = "";

                    String totalImagePathLocal = dbMarker.getImage();
                    String[] separated = totalImagePathLocal.split(";");

                    int cntImages = separated.length;

                    for(int j = 0; j < cntImages; j++) {
                        uploadFilePath = separated[j].trim();

//                        Toast.makeText(getApplicationContext(), "uploadFilePath: " + uploadFilePath, Toast.LENGTH_SHORT).show();

                        if(!uploadFilePath.equals("")) {
                            final int[] status = new int[1];
                            final int finalI = i;
                            final int finalJ = j;
                            new Thread(new Runnable() {
                                public void run() {
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            if(finalI == 0 && finalJ == 0) {
                                                dialog = ProgressDialog.show(MainActivity.this, "", "Uploading image...", true);
                                            }
                                        }
                                    });

                                    status[0] = uploadFile(uploadFilePath);
                                }
                            }).start();

                            String imageName = uploadFilePath.substring(uploadFilePath.lastIndexOf("/") + 1);

                            totalImagePath = totalImagePath + baseRemotePath + imageName + ";";
                        }
                    }

                    dbMarker.setImage(totalImagePath);
                    dbMarkerTable.updateMarker(dbMarker);
                }
            }
        }
    }

    // ---------------------------------------------------------------------------------------------
    private void resetCustomFieldsAndImages() {
        etName.setText("");
        etExpence.setText("");

        etComment.setText("");

        // customFields
        int len = dbCustomFields.size();
        if(len > 0) {
            for(int i = 0; i < len; i++) {
                etCustomFields[i].setText("");
            }
        }

        // image path
        Constant.currentCNTofImagesForUp = 0;
        resetImageRecyclerView();
    }

    // ---------------------------------------------------------------------------------------------
    private void updatePreviewMarker() {

        DBMarkerTable dbMarkerTable = new DBMarkerTable(MainActivity.this);
        dbMarkers = dbMarkerTable.getAllMarkers();
        // ----------------------------------------------
        // marker
        lvPreviewMarkers.setAdapter(new PreviewMarkerAdapter(MainActivity.this, (ArrayList<DBMarker>) dbMarkers));
    }

    // ---------------------------------------------------------------------------------------------
    public int uploadFile(final String sourceFileUri) {


        String fileName = sourceFileUri;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.isFile()) {
            dialog.dismiss();

            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(MainActivity.this, "Image File not exist :" + sourceFileUri, Toast.LENGTH_SHORT).show();
                }
            });
            return 0;
        }
        else
        {
            try {
                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
//                dos.writeBytes("Content-Disposition: form-data; name="uploaded_file";filename=""
//                                + fileName + """ + lineEnd);
//
//                        dos.writeBytes(lineEnd);

                dos.writeBytes("Content-Disposition: form-data; name=uploaded_file;filename="+ fileName +  lineEnd);
                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                if(serverResponseCode == 200){

                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(MainActivity.this, "Image Upload Complete.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

                dialog.dismiss();
                ex.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(MainActivity.this, "MalformedURLException : check script url.",
                                Toast.LENGTH_SHORT).show();
                    }
                });

                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {

                dialog.dismiss();
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(MainActivity.this, "Got Exception : see logcat ",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e("Upload file to server Exception", "Exception : "
                        + e.getMessage(), e);
            }
            dialog.dismiss();

            return serverResponseCode;

        } // End else block
    }

    // ---------------------------------------------------------------------------------------------
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    // ---------------------------------------------------------------------------------------------
    private void saveMarkerToLocalDB() {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    Constant.Permission_ACCESS_FINE_LOCATION);
            return;
        }
//        googleMap.setMyLocationEnabled(false);

        // save marker data to local DB
        String name = "";
        String location_latlng = "";
        String descriptionStr = "";
        String customFieldsStr = "";
        String drawing_type = "";
        String status = "";
        String expence = "";

        name = etName.getText().toString();
        expence = etExpence.getText().toString();
        if(expence.equals("")) {
            expence = "0";
        }

        status = String.valueOf(spinnerStatus.getSelectedItem());
        String latitude = "";
        String longitude = "";

        int sizeOfLatLng = listLatLngs.size();

        if(listLatLngs.size() > 0) {
            // prepare saving data to local DB
            // --------------------
            // location_latlng

            for (int j = 0; j < sizeOfLatLng; j++ ) {
                latitude = String.valueOf(listLatLngs.get(0).getLatitude());
                longitude = String.valueOf(listLatLngs.get(0).getLongitude());

                location_latlng = location_latlng + latitude + ":" + longitude + ";";
            }


        } else {
            latitude = prefs.getString(Constant.Latitude, "0");
            longitude = prefs.getString(Constant.Longitude, "0");

            location_latlng = location_latlng + latitude + ":" + longitude + ";";
        }

        // --------------------
        // description
        descriptionStr = etComment.getText().toString();

        // --------------------
        // customFields
        int len = dbCustomFields.size();
        if(len > 0) {
            DBCustomField dbCustomField;
            for(int i = 0; i < len; i++) {
                dbCustomField = dbCustomFields.get(i);
                customFieldsStr = customFieldsStr + dbCustomField.getField() + " : ";
                customFieldsStr = customFieldsStr + etCustomFields[i].getText().toString() + " : ";
                customFieldsStr = customFieldsStr + dbCustomField.getType();

                customFieldsStr =customFieldsStr + ";";
            }
        }

        // ---------------------
        // image path
        String imagesPath = "";

        for(int i = 0; i < Constant.currentCNTofImagesForUp; i++) {
            imagesPath = imagesPath + Constant.imagePaths[i] + ";";
        }

        // --------------------
        // drawing_type
        switch (selectedShape) {
            case 1:
                drawing_type = "marker";
                break;
            case 2:
                drawing_type = "polyline";
                break;
            case 3:
                drawing_type = "polygon";
                break;
            default:
                break;
        }

        DBMarkerTable dbMarkerTable = new DBMarkerTable(MainActivity.this);
        DBMarker dbMarker = new DBMarker(name, selectedIcon, selectedIconPath,
                location_latlng, selectedCategoryName, selectedSubCategoryName,
                descriptionStr, customFieldsStr, drawing_type,
                expence, imagesPath, status, "0");

        dbMarkerTable.addMarker(dbMarker);
        Toast.makeText(getApplicationContext(), "Data save success to local DB", Toast.LENGTH_SHORT).show();
        listLatLngs.clear();
        map.clear();

        updatePreviewMarker();
        resetCustomFieldsAndImages();
    }

    // ---------------------------------------------------------------------------------------------
    private void saveDataToRemoteDB() {
        AsyncTask<String, String, String> _TaskSaveData = new AsyncTask<String, String, String>() {

            @Override
            protected void onPreExecute() {
                kpHUD.show();
            }

            @Override
            protected String doInBackground(String... arg0)
            {
                try {
                    // -----------------------------------------------------------------------------

                    DBMarkerTable dbMarkerTable = new DBMarkerTable(MainActivity.this);
                    List<DBMarker> dbMarkers = dbMarkerTable.getAllMarkers();

                    int len = dbMarkers.size();
                    DBMarker dbMarker;
                    if(len > 0) {
                        Class.forName("com.mysql.jdbc.Driver");
                        java.sql.Connection con = DriverManager.getConnection(url, user, pass);
                        java.sql.Statement st = con.createStatement();

                        String queryStr = "INSERT INTO markers(name, icon_name, icon_path, address, ";
                        queryStr = queryStr + "location_latlng, category, sub_category, description, custom_fields, drawing_type, ";
                        queryStr = queryStr + "fill_color, border_color, bounds, expence, image, status, damage_route) VALUES";
                        for (int i = 0; i < len; i++ ) {
                            dbMarker = dbMarkers.get(i);
                            if(dbMarker.getUpdated().equals("0")) {
                                queryStr = "INSERT INTO markers(name, icon_name, icon_path, address, ";
                                queryStr = queryStr + "location_latlng, category, sub_category, description, custom_fields, drawing_type, ";
                                queryStr = queryStr + "fill_color, border_color, bounds, expence, image, status, damage_route) VALUES";

                                queryStr = queryStr + "('";
                                queryStr = queryStr + dbMarker.getName() + "','";               // name
                                queryStr = queryStr + dbMarker.getIcon_name() + "','";          // icon_name
                                queryStr = queryStr + dbMarker.getIcon_path() + "','";          // icon_path
                                queryStr = queryStr + "','";                                    // address
                                queryStr = queryStr + dbMarker.getLocation_latlng() + "','";
                                queryStr = queryStr + dbMarker.getCategory() + "','";
                                queryStr = queryStr + dbMarker.getSub_category() + "','";
                                queryStr = queryStr + dbMarker.getDescription() + "','";
                                queryStr = queryStr + dbMarker.getCustom_fields() + "','";
                                queryStr = queryStr + dbMarker.getDrawing_type() + "','";
                                queryStr = queryStr + "','";                                    // fill_color
                                queryStr = queryStr + "','";                                    // border_color
                                queryStr = queryStr + "',";                                    // bounds
                                queryStr = queryStr + dbMarker.getExpence() + ",'";            // expence
                                queryStr = queryStr + dbMarker.getImage() + "','";                                    // image
                                queryStr = queryStr + dbMarker.getStatus() + "','";             // status
                                queryStr = queryStr + "')";                                     // damage_route

                                st.execute(queryStr);

                                dbMarker.setUpdated("1");
                                dbMarkerTable.updateMarker(dbMarker);
                                dbMarkerTable.deleteMarker(dbMarker);
                            }
                        }

                        sqlStrStatic = queryStr;

//                        st.execute(queryStr);

                        con.close();
                    } else {
                        return "NoData";
                    }

                } catch (SQLException e) {
                    e.printStackTrace();

                    Log.d("myapp========================================>  ", Log.getStackTraceString(e));
                    return "fail1";
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    return "fail";
                }
                return "success";
            }
            @Override
            protected void onProgressUpdate(String... values) {
                // TODO Auto-generated method stub
                super.onProgressUpdate(values);
                System.out.println("Progress : "  + values);
            }
            @Override
            protected void onPostExecute(String result) {
                kpHUD.dismiss();

                if(result.equals("success")) {
                    Toast.makeText(getApplicationContext(), "Data save success", Toast.LENGTH_SHORT).show();

                    updatePreviewMarker();
//                    DBMarkerTable dbMarkerTable = new DBMarkerTable(MainActivity.this);
//                    dbMarkerTable.dropTable();
                } else if(result.equals("NoData")) {
                    Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_SHORT).show();
                } else if(result.equals("fail1")) {
                    Toast.makeText(getApplicationContext(), "Data save fail : sql exception", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Data save fail", Toast.LENGTH_SHORT).show();
                }

//                Toast.makeText(getApplicationContext(), sqlStrStatic, Toast.LENGTH_LONG).show();
            }
        };
        _TaskSaveData.execute((String[]) null);
    }

    // ---------------------------------------------------------------------------------------------
    private void getDataFromRemoteDB() {
        AsyncTask<String, String, String> _Task = new AsyncTask<String, String, String>() {

            @Override
            protected void onPreExecute() {
                kpHUD.show();
            }

            @Override
            protected String doInBackground(String... arg0)
            {
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    java.sql.Connection con = DriverManager.getConnection(url, user, pass);
                    java.sql.Statement st = con.createStatement();

                    // -----------------------------------------------------------------------------
                    // get category info
                    java.sql.ResultSet rs = st.executeQuery("SELECT id, category FROM category");
                    DBCategoryTable dbCategoryTable = new DBCategoryTable(MainActivity.this);
                    dbCategoryTable.dropTable();

                    dbCategories = new ArrayList<DBCategory>();

                    while (rs.next()) {
                        String category= rs.getString("category");
                        String id = rs.getString("id");
                        dbCategories.add(new DBCategory(id, category, "1"));
                        dbCategoryTable.addCategory(new DBCategory(id, category, "1"));
                    }

                    // -----------------------------------------------------------------------------
                    // get icon info
                    java.sql.ResultSet rsIcon = st.executeQuery("SELECT id, name, path, description FROM icons");
                    DBIconTable dbIconTable = new DBIconTable(MainActivity.this);
                    dbIconTable.dropTable();

                    dbIcons = new ArrayList<DBIcon>();

                    while (rsIcon.next()) {
                        String id = rsIcon.getString("id");
                        String name= rsIcon.getString("name");
                        String path= rsIcon.getString("path");
                        String description= rsIcon.getString("description");
                        if(!name.equals("")){

                            dbIcons.add(new DBIcon(id, name, path, description, "1"));
                            dbIconTable.addIcon(new DBIcon(id, name, path, description, "1"));
                        }
                    }

                    // -----------------------------------------------------------------------------
                    // get user info
                    java.sql.ResultSet rsUser = st.executeQuery("SELECT id, username, user_type FROM users");
                    DBUserTable dbUserTable = new DBUserTable(MainActivity.this);
                    dbUserTable.dropTable();

                    dbUsers = new ArrayList<DBUser>();

                    while (rsUser.next()) {
                        String id = rsUser.getString("id");
                        String username = rsUser.getString("username");
                        String user_type = rsUser.getString("user_type");
                        if(!username.equals("") && !username.equals(LoginActivity.userName)){
                            dbUsers.add(new DBUser(id, username, user_type, "1"));
                            dbUserTable.addUser(new DBUser(id, username, user_type, "1"));
                        }
                    }

                    // -----------------------------------------------------------------------------
                    // get markers info
//                    java.sql.ResultSet rsMarker = st.executeQuery("SELECT id, name, icon_name, icon_path, " +
//                            "address, location_latlng, category, sub_category, description, custom_fields, " +
//                            "drawing_type, fill_color, border_color, transparency, border_weight, bounds, " +
//                            "budget, expence, date_decide_budget, image, status, damage_route FROM markers");
//
////                    java.sql.ResultSet rsMarker = st.executeQuery("SELECT id, name, description, custom_fields, image FROM markers");
//
//                    DBMarkerTable dbMarkerTable = new DBMarkerTable(MainActivity.this);
//                    dbMarkerTable.dropTable();
//
//                    dbMarkers = new ArrayList<DBMarker>();
//
//                    while (rsMarker.next()) {
//                        String id = rsMarker.getString("id");
//                        String name= rsMarker.getString("name");
//                        String icon_name= rsMarker.getString("icon_name");
//                        String icon_path= rsMarker.getString("icon_path");
//                        String address= rsMarker.getString("address");
//                        String location_latlng= rsMarker.getString("location_latlng");
//                        String category= rsMarker.getString("category");
//                        String sub_category= rsMarker.getString("sub_category");
//                        String description= rsMarker.getString("description");
//                        String custom_fields= rsMarker.getString("custom_fields");
//                        String drawing_type= rsMarker.getString("drawing_type");
//                        String fill_color= rsMarker.getString("fill_color");
//                        String border_color= rsMarker.getString("border_color");
//                        String transparency= rsMarker.getString("transparency");
//                        String border_weight= rsMarker.getString("border_weight");
//                        String bounds= rsMarker.getString("bounds");
//                        String budget= rsMarker.getString("budget");
//                        String expence= rsMarker.getString("expence");
////                        String date_decide_budget= rsMarker.getString("date_decide_budget");
//                        String image= rsMarker.getString("image");
//                        String status= rsMarker.getString("status");
////                        String damage_route= rsMarker.getString("damage_route");
//                        if(!name.equals("")){
//
//                            dbMarkers.add(new DBMarker(name, icon_name, icon_path, location_latlng,
//                                    category, sub_category, description, custom_fields, drawing_type,
//                                    expence, image, status, "1"));
//                            dbMarkerTable.addMarker(new DBMarker(name, icon_name, icon_path, location_latlng,
//                                    category, sub_category, description, custom_fields, drawing_type,
//                                    expence, image, status, "1"));
//                        }
//                    }

                    // -----------------------------------------------------------------------------
                    // get sub category info
                    java.sql.ResultSet rsSubCategory = st.executeQuery("SELECT id, category_id, category_name, sub_category FROM sub_category");
                    DBSubCategoryTable dbSubCategoryTable = new DBSubCategoryTable(MainActivity.this);
                    dbSubCategoryTable.dropTable();

                    while (rsSubCategory.next()) {
                        String id = rsSubCategory.getString("id");
                        String category_id = rsSubCategory.getString("category_id");
                        String category_name = rsSubCategory.getString("category_name");
                        String sub_category = rsSubCategory.getString("sub_category");

                        dbSubCategoryTable.addSubCategory(new DBSubCategory(id, category_id, category_name, sub_category, "1"));
                    }

                    // -----------------------------------------------------------------------------
                    java.sql.ResultSet rsCustomField = st.executeQuery("SELECT id, sub_category_id, field, type FROM custom_fields");
                    DBCustomFieldTable dbCustomFieldTable = new DBCustomFieldTable(MainActivity.this);
                    dbCustomFieldTable.dropTable();

                    while (rsCustomField.next()) {
                        String id = rsCustomField.getString("id");
                        String sub_category_id = rsCustomField.getString("sub_category_id");
                        String field = rsCustomField.getString("field");
                        String type = rsCustomField.getString("type");

                        dbCustomFieldTable.addCustomField(new DBCustomField(id, sub_category_id, field, type, "1"));
                    }

                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return null;
            }
            @Override
            protected void onProgressUpdate(String... values) {
                // TODO Auto-generated method stub
                super.onProgressUpdate(values);
                System.out.println("Progress : "  + values);
            }
            @Override
            protected void onPostExecute(String result) {
                kpHUD.dismiss();
                initSpinnerData();
            }
        };
        _Task.execute((String[]) null);
    }

    private void initSpinnerData() {
        CategoryAdapter adapter = new CategoryAdapter(MainActivity.this,
                R.layout.row_category_spinner, R.id.title, dbCategories);
        spinnerCat.setAdapter(adapter);

        // ----------------------------------------------
        IconAdapter adapterIcon = new IconAdapter(MainActivity.this,
                R.layout.row_icon_spinner, R.id.title, dbIcons);
        spinnerIcon.setAdapter(adapterIcon);

        // ----------------------------------------------
        // marker
//        lvPreviewMarkers.setAdapter(new PreviewMarkerAdapter(MainActivity.this, (ArrayList<DBMarker>) dbMarkers));

        // ----------------------------------------------
        // user
        lvUser.setAdapter(new UserAdapter(MainActivity.this, (ArrayList<DBUser>) dbUsers));

        // ----------------------------------------------
        // category
        DBCategoryTable dbCategoryTable = new DBCategoryTable(MainActivity.this);

        int len = dbCategoryTable.getAllCategories().size();
        if(len > 0) {
            DBCategory dbCategory = dbCategoryTable.getCategory(1);

            selectedCategoryName = dbCategory.getCategory(); /*String.valueOf(spinnerCat.getSelectedItem());*/

            // ----------------------------------------------
            getSubCategoryFromCategoryName(selectedCategoryName);
        }
    }

    private void getSubCategoryFromCategoryName(String categoryName) {
        DBSubCategoryTable dbSubCategoryTable = new DBSubCategoryTable(MainActivity.this);

        dbSubCategories = new ArrayList<DBSubCategory>();
        List<DBSubCategory> dbSubCategories1 = dbSubCategoryTable.getAllSubCategories();

        int len = dbSubCategories1.size();
        if(len > 0) {
            for(int i = 0; i < len; i++) {
                DBSubCategory dbSubCategory = dbSubCategories1.get(i);

                if(categoryName.equals(dbSubCategory.getCategory_name())) {
                    dbSubCategories.add(dbSubCategory);
                }
            }
        }

        SubCategoryAdapter adapterSub = new SubCategoryAdapter(MainActivity.this,
                R.layout.row_sub_category_spinner, R.id.title, dbSubCategories);
        spinnerSubCat.setAdapter(adapterSub);
    }

    // ---------------------------------------------------------------------------------------------
    private void onCaptureImage() {
        reLayCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean hasCamera = checkCameraHardware(MainActivity.this);
                if(hasCamera) {
                    dispatchTakePictureIntent();
                } else {
                    Toast.makeText(getApplicationContext(), "There is no camera!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        reLayGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , Constant.REQUEST_SELECT_PHOTO);//one can be replaced with any action code
            }
        });

    }

    /** Check if this device has a camera */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, Constant.REQUEST_TAKE_PHOTO);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constant.REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            String imagePath = FileUtils.saveBitmapToCameraWithDate(MainActivity.this, imageBitmap, true, null);

//            imgSelected.setImageBitmap(imageBitmap);

            if(Constant.currentCNTofImagesForUp < Constant.MAX_NUMBER_UPLOADING_IMAGES) {
                Constant.imagePaths[Constant.currentCNTofImagesForUp] = imagePath;
                Constant.currentCNTofImagesForUp++;
            }
        }

        if (requestCode == Constant.REQUEST_SELECT_PHOTO&& resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            String imagePath = FileUtils.getRealPathFromURI(this, selectedImage);
            Bitmap imageBitmap = BitmapFactory.decodeFile(imagePath);

            imagePath = FileUtils.saveBitmapToCameraWithDate(MainActivity.this, imageBitmap, true, null);

            if(Constant.currentCNTofImagesForUp < Constant.MAX_NUMBER_UPLOADING_IMAGES) {
                Constant.imagePaths[Constant.currentCNTofImagesForUp] = imagePath;
                Constant.currentCNTofImagesForUp++;
            }
//            imgSelected.setImageBitmap(imageBitmap);
        }

        resetImageRecyclerView();
    }

    private void resetImageRecyclerView () {
        imageRecyclerModelArrayList.clear();

        for(int i = 0; i < Constant.currentCNTofImagesForUp; i++) {
            imageRecyclerModelArrayList.add(new ImageRecyclerModel(Constant.imagePaths[i]));
        }

        adapter = new ImageRecyclerAdapter(MainActivity.this, imageRecyclerModelArrayList);
        recyclerView.setAdapter(adapter);
    }
}