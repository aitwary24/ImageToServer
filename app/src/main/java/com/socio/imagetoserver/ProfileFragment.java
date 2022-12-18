package com.socio.imagetoserver;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.content.CursorLoader;

import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.socio.Model.MyResponse;
import com.socio.Service.ApiInterface;
import com.socio.utils.Constants;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment {

ImageView upload;
Button uploadbtn;
    //the image URI
    Uri selectedImage ;
 ProgressBar progressDialog;
 int a=0;
 TextView textView;

 Handler handler=new Handler();
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       uploadbtn= view.findViewById(R.id.buttonUploadImage);
       upload =view.findViewById(R.id.imageView);
       progressDialog=view.findViewById(R.id.progress_bar);
        textView=view.findViewById(R.id.textView);
        upload.setImageURI(selectedImage);

       uploadbtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                    //chooseImage(getActivity());
           uploadFile(selectedImage);
           setProgressDialog();

           }
       });
       upload.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                chooseImage(getActivity());
                //upload.setImageURI();
               //uploadFile(selectedImage);
               //upload.setImageURI(selectedImage);
           }
       });
    }
    private void chooseImage(Context context){

        Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        takePicture.putExtra("android.intent.extras.CAMERA_FACING", 1);
        //takePicture.setType("image/*");
        startActivityForResult(takePicture, 123);
        //upload.setImageURI(selectedImage);
       // selectedImage=takePicture.getData();
        upload.setImageURI(selectedImage);
        selectedImage=Uri.fromFile(Environment.getExternalStorageDirectory());


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {


        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK ) {

//            selectedImage = data.getData();
//            uploadFile(selectedImage);
//            upload.setImageURI(selectedImage);

          //selectedImage = data.getData();                                                         // Get the image file URI
//          upload.setImageURI(selectedImage);
          Bitmap photo = (Bitmap) data.getExtras().get("data");
            upload.setImageBitmap(photo);

            Bundle extras = data.getExtras();
            photo = (Bitmap) extras.get("data");
            // imageBitmap = (Bitmap) data.getExtras().get("data");
            upload.setImageBitmap(photo);
            selectedImage=data.getData();
        }
    }
    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this.getActivity(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
       // assert cursor != null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    private void uploadFile(Uri fileUri) {

        //creating a file
       File file = new File(String.valueOf((fileUri)));
        //File imageFile = new File(uri);                                                          // Create a file using the absolute path of the image

        RequestBody requestFile = RequestBody.create(MediaType.parse(String.valueOf(fileUri)), file);
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
        Call<MyResponse> call = api.uploadImage(requestFile, descBody);

        //peforming calls
        call.enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                // assert response.body() != null;
                // if (!response.body().error) {
                Log.v(response.message(),"File is uploading ");
                Toast.makeText(getActivity(), response.message(), Toast.LENGTH_LONG).show();
                Toast.makeText(getActivity(), "File Uploaded Successfully...", Toast.LENGTH_LONG).show();
                upload.setImageURI(selectedImage);
                uploadFile(selectedImage);
                // }
                //else {
                // }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                Toast.makeText(getActivity(), "Some error occurred...", Toast.LENGTH_LONG).show();


            }
        });
    }
 void setProgressDialog(){
     a = progressDialog.getProgress();
     new Thread(new Runnable() {
         public void run() {
             while (a < 100) {
                 a += 1;
                 handler.post(new Runnable() {
                     public void run() {
                         progressDialog.setProgress(a);
                         textView.setText(a + "/" + progressDialog.getMax());
                         if (a == 100){
                             textView.setText(" Your Image  has been Uploaded");
                         uploadFile(selectedImage);}
                         else
                             textView.setText("Please Try again");
                     }
                 });

                 try {
                     // Sleep for 50 ms to show progress you can change it as well.
                     Thread.sleep(50);
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }
             }
         }
     }).start();
 }

}