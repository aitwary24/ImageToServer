package com.socio.imagetoserver;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.loader.content.CursorLoader;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.socio.Model.MyResponse;
import com.socio.Service.ApiInterface;
import com.socio.utils.Constants;


import java.io.File;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class DashboardActivity extends AppCompatActivity {
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;
    public  static final  int REQUEST_CODE=1234;

    public static final String ALLOW_KEY = "ALLOWED";
    public static final String CAMERA_PREF = "camera_pref";

    String myuid;
    ActionBar actionBar;
    BottomNavigationView navigationView;
    DrawerLayout dLayout;
    String filepath=" ";
    ImageView imageView;
    Bundle bundle=new Bundle();

    private Uri uri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        uri = getIntent().getData();
        //setNavigationDrawer();

        actionBar = getSupportActionBar();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); // get the reference of Toolbar
        setSupportActionBar(toolbar); // Setting/replace toolbar as the ActionBar

        navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(selectedListener);

        // When we open the application first
        // time the fragment should be shown to the user
        // in this case it is home fragment
        HomeFragment fragment = new HomeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content, fragment, "");
        fragmentTransaction.commit();


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dLayout.openDrawer(Gravity.LEFT);
            }
        });
       // setNavigationDrawer();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener selectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {

                case R.id.nav_account:
                    //actionBar.setTitle("Home");

                    Intent homeintent=new Intent(DashboardActivity.this,MainActivity.class);
                    startActivity(homeintent);

                    HomeFragment fragment = new HomeFragment();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content, fragment, "");
                    fragmentTransaction.commit();
                  //  startActivity(new Intent(getApplicationContext(),HomeFragment.class));
                    overridePendingTransition(0,0);
                    return true;


                case R.id.nav_camera:
                    //actionBar.setTitle("Camera");
                    if(checkAndRequestPermissions(DashboardActivity.this)){
                        chooseImage(DashboardActivity.this);
                        //supportFinishAfterTransition();
                    }
                    Intent intent=new Intent(DashboardActivity.this,ImagePreviewActivity.class);
                    startActivity(intent);
                    CameraUploadFragment fragment1 = new CameraUploadFragment();
                    FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction1.replace(R.id.content, fragment1);
                    fragmentTransaction1.commit();
                    //supportFinishAfterTransition();
                    return true;


                case R.id.nav_profile:
                    //actionBar.setTitle("Profile");
                    ProfileFragment fragment2 = new ProfileFragment();
                    FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction2.replace(R.id.content, fragment2, "");
                    fragmentTransaction2.commit();
                    return true;


            }
            return false;
        }
    };

    private void setNavigationDrawer() {
        //dLayout = (DrawerLayout) findViewById(R.id.drawer_layout); // initiate a DrawerLayout
        NavigationView navView = (NavigationView) findViewById(R.id.toolbar_navigation); // initiate a Navigation View
// implement setNavigationItemSelectedListener event on NavigationView
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Fragment frag = null; // create a Fragment Object
                int itemId = menuItem.getItemId(); // get selected menu item's id
// check selected menu item's id and replace a Fragment Accordingly
                if (itemId == R.id.first) {
                    frag = new HomeFragment();
                } else if (itemId == R.id.second) {
                    frag = new CameraUploadFragment();
                } else if (itemId == R.id.third) {
                    frag = new ProfileFragment();
                }
// display a toast message with menu item's title
                Toast.makeText(getApplicationContext(), menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                if (frag != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.nav_frame, frag); // replace a Fragment with Frame Layout
                    transaction.commit(); // commit the changes
                    dLayout.closeDrawers(); // close the all open Drawer Views
                    return true;
                }
                return false;
            }
        });
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {


        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            //the image URI
            Uri selectedImage = data.getData();
            uploadFile(selectedImage);
            imageView.setImageURI(selectedImage);


        }
    }

    public static void saveToPreferences(Context context, String key, Boolean allowed) {
        SharedPreferences myPrefs = context.getSharedPreferences(CAMERA_PREF,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        prefsEditor.putBoolean(key, allowed);
        prefsEditor.commit();
    }



    // Handled permission Result
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS:
                if (ContextCompat.checkSelfPermission(DashboardActivity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(),
                            "FlagUp Requires Access to Camara.", Toast.LENGTH_SHORT)
                            .show();
                } else if (ContextCompat.checkSelfPermission(DashboardActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(),
                            "FlagUp Requires Access to Your Storage.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    chooseImage(DashboardActivity.this);
                }
                break;
        }
    }



    public static boolean checkAndRequestPermissions(final Activity context) {
        int WExtstorePermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int cameraPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (WExtstorePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded
                    .add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(context, listPermissionsNeeded
                            .toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    // function to let's the user to choose image from camera or gallery
    private void chooseImage(Context context){
        final CharSequence[] optionsMenu = {"Take Photo", "Choose from Gallery", "Exit" }; // create a menuOption Array
        // create a dialog for showing the optionsMenu
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // set the items in builder
        builder.setItems(optionsMenu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if(optionsMenu[i].equals("Take Photo")){
                    // Open the camera and get the photo
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    //takePicture.putExtra("android.intent.extras.CAMERA_FACING", 1);
                    startActivityForResult(takePicture, 0);
                    dialogInterface.dismiss();
                    processCameraImage();


                }
                else if(optionsMenu[i].equals("Choose from Gallery")){
                    // choose from  external storage
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);
                    dialogInterface.dismiss();
                    processGalleryImage();

                }
                else if (optionsMenu[i].equals("Exit")) {
                    dialogInterface.dismiss();
                }
                dialogInterface.dismiss();
            }
        });

        builder.show();

    }
    private void uploadFile(Uri fileUri) {

        //creating a file
        File file = new File(getRealPathFromURI(fileUri));

        //creating request body for file
        RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(fileUri)), file);
        RequestBody descBody = RequestBody.create(MediaType.parse("text/plain"), "My Image");

        //The gson builder
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();


        //creating retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        //creating our api
        ApiInterface api = retrofit.create(ApiInterface.class);

        //creating a call and calling the upload image method
        retrofit2.Call<MyResponse> call = api.uploadImage(requestFile, descBody);

        //peforming calls
        call.enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(retrofit2.Call<MyResponse> call, Response<MyResponse> response) {
                // assert response.body() != null;
                // if (!response.body().error) {
                Toast.makeText(getApplicationContext(), "File Uploaded Successfully...", Toast.LENGTH_LONG).show();
                    Intent intent =new Intent(DashboardActivity.this,ImagePreviewActivity.class);
                    startActivity(intent);
                // }
                //else {
                // Toast.makeText(getApplicationContext(), "Some error occurred...", Toast.LENGTH_LONG).show();
                // }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                Intent intent=new Intent(DashboardActivity.this,ImagePreviewActivity.class);
                startActivity(intent);
            }
        });
    }
    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        assert cursor != null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }
    //Use  this method to select image from Gallery
    private void processGalleryImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),
                REQUEST_CODE);
    }

    //Use  this method to click image from Camera
    private void processCameraImage() {
        Intent cameraIntent = new
                Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, REQUEST_CODE,bundle);

    }

}


