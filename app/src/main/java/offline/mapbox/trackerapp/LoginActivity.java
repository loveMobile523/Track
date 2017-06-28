package offline.mapbox.trackerapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;

import java.sql.DriverManager;
import java.sql.SQLException;

import mehdi.sakout.fancybuttons.FancyButton;
import offline.mapbox.trackerapp.constant.Constant;

public class LoginActivity extends Activity {
    private SharedPreferences.Editor editor;
    private SharedPreferences prefs;

    private FancyButton btnLogin;

    private EditText etUsername;
    private EditText etPassword;

    public static String userName;
    public static String password;
    public static String userId;
    public static String userType;

    public static String needData;

    KProgressHUD kpHUD;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editor = getSharedPreferences(Constant.MyShared, MODE_PRIVATE).edit();
        prefs = getSharedPreferences(Constant.MyShared, MODE_PRIVATE);

        String status = prefs.getString(Constant.Status, Constant.LOGOUT);
        if(status.equals(Constant.LOGIN)) {
            needData = Constant.NoNeed;
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        initWidget();
        onClickButton();

        isWriteStoragePermissionGranted();
    }

    private void initWidget() {
        kpHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true)
//                .setLabel("Login")
                .setAnimationSpeed(1)
                .setDimAmount(0.5f);

        btnLogin = (FancyButton) findViewById(R.id.btnLogin);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
    }

    private void onClickButton() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = etUsername.getText().toString();
                password = etPassword.getText().toString();

                if(isNetworkAvailable()) {
                    loginBackground();
                } else {
                    Toast.makeText(LoginActivity.this, "Please check your Network Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG","Permission is granted");
                isReadStoragePermissionGranted();
            } else {

                Log.v("TAG","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constant.Permission_Write_external_storage);
            }
        }
    }

    public  void isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG","Permission is granted");
                isCameraPermissionGranted();
            } else {

                Log.v("TAG","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constant.Permission_Read_external_storage);
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG","Permission is granted");
        }
    }

    public  void isCameraPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG","Permission is granted");
                isFindLocationPermissionGranted();
            } else {

                Log.v("TAG","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, Constant.Permission_Camera);
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG","Permission is granted");
        }
    }

    public void isFindLocationPermissionGranted() {

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
                // TODO: Consider calling

            } else {
                Log.v("TAG","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constant.Permission_ACCESS_FINE_LOCATION);
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG","Permission is granted");
        }

    }

    // ---------------------------------------------------------------------------------------------
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    // ---------------------------------------------------------------------------------------------
    private void loginBackground() {
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
                    java.sql.Connection con = DriverManager.getConnection(MainActivity.url, MainActivity.user, MainActivity.pass);
                    java.sql.Statement st = con.createStatement();

                    // -----------------------------------------------------------------------------
                    java.sql.ResultSet rs = st.executeQuery("SELECT id, username, password, user_type FROM users");

                    while (rs.next()) {
                        String id = rs.getString("id");
                        String username = rs.getString("username");
                        String password = rs.getString("password");
                        String user_type = rs.getString("user_type");

                        if(username.equals(LoginActivity.userName) && password.equals(LoginActivity.password)) {
                            if(user_type.equals(Constant.UserTypeAgent) || user_type.equals(Constant.UserTypeWorker)) {
                                LoginActivity.userId = id;
                                LoginActivity.userType = user_type;
                                return "success";
                            } else {
                                return "Please, check user type.";
                            }
                        } else {

                        }
                    }

                    return "Please, check user name and password.";

                    // -----------------------------------------------------------------------------


                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return "fail";
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
                    editor.putString(Constant.Status, Constant.LOGIN);
                    editor.putString(Constant.UserId, LoginActivity.userId);
                    editor.putString(Constant.UserName, LoginActivity.userName);
                    editor.commit();
                    needData = Constant.Need;

                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else if (result.equals("fail")) {

                } else {
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                }
            }
        };
        _Task.execute((String[]) null);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Constant.Permission_Write_external_storage){

            if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Log.v("TAG","Permission: "+permissions[0]+ "was "+grantResults[0]);
                //resume tasks needing this permission
                isReadStoragePermissionGranted();
            } else if(grantResults[0]== PackageManager.PERMISSION_DENIED){
                // re send request
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constant.Permission_Write_external_storage);
            }
        }

        if(requestCode == Constant.Permission_Read_external_storage) {
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Log.v("TAG","Permission: "+permissions[0]+ "was "+grantResults[0]);
                //resume tasks needing this permission
                isCameraPermissionGranted();
            } else if(grantResults[0]== PackageManager.PERMISSION_DENIED){
                // re send request
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constant.Permission_Read_external_storage);
            }
        }

        if(requestCode ==  Constant.Permission_Camera) {
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Log.v("TAG","Permission: "+permissions[0]+ "was "+grantResults[0]);
                //resume tasks needing this permission
                isFindLocationPermissionGranted();
            } else if(grantResults[0]== PackageManager.PERMISSION_DENIED){
                // re send request
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, Constant.Permission_Camera);
            }
        }

        if(requestCode ==  Constant.Permission_ACCESS_FINE_LOCATION) {
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Log.v("TAG","Permission: "+permissions[0]+ "was "+grantResults[0]);
                //resume tasks needing this permission
            } else if(grantResults[0]== PackageManager.PERMISSION_DENIED){
                // re send request
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constant.Permission_ACCESS_FINE_LOCATION);
            }
        }
    }
}
