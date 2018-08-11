package spero.shariq.com.myop_restretro1;

import com.google.gson.annotations.SerializedName;



/**
 * Created by drsha on 4/8/2018.
 */

public class MedImageModel
{

    @SerializedName("base64_image")
    String base64_image;

    public String getBase64_image() {
        return base64_image;
    }

//    @SerializedName("image")
//    String base64_image;
//
//    public String getBase64_image() {
//        return base64_image;
//    }
}
