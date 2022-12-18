package com.socio.Model;

import com.google.gson.annotations.SerializedName;

public class MyResponse {
     @SerializedName("success")
            boolean success;
            @SerializedName("message")
                    String message;

                    String getMessage() {
                    return message;
                    }

                    boolean getSuccess() {
                    return success;
                    }

}
