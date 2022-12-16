package com.socio.Service;


import com.socio.Model.MyResponse;



import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;


public interface ApiInterface {
        @Multipart
        @POST("/content")
        Call<ResponseBody> postImage(@Part MultipartBody.Part image, @Part("name") RequestBody name);

        @Multipart
        @POST("/content")
        Call<ResponseBody> uploadFile(
                @Part("description") RequestBody description,
                @Part MultipartBody.Part file);

        @PUT("/content")
        Call<ResponseBody> getStatus(@Field("image") String capturedImage);

//        @Multipart
//        @POST("/upload_multi_files/fileUpload.php")
//        Call<UploadObject> uploadSingleFile(@Part MultipartBody.Part file, @Part("name") RequestBody name);

       @Multipart @POST("/content.php")
       // Call<ResponseBody> uploadMultiFile(@Body RequestBody file);
        Call<MyResponse> uploadImage(@Part("image\"; filename=\"myfile.jpg\" ") RequestBody file, @Part("desc") RequestBody desc);

}



