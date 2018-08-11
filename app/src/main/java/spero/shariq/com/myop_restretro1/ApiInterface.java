package spero.shariq.com.myop_restretro1;

import android.content.SharedPreferences;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;


/**
 * Created by drsha on 4/8/2018.
 */

public interface ApiInterface {
//    @Headers("Authorization: Token b959d32cd0001f63b30e24da5d7ae40f89683c74")

    @POST("log/")
    Call<LoginData> loginwith(@Body Map<String, String> params);


    @POST("api/connect/")
    Call<List<JourneyData>> getData(@Header("Authorization") String authToken);


    @PUT("api/connect/")
    Call<JourneyData> putData(@Header("Authorization") String authToken,@Body Map<String, String> params);

    @Multipart
    @PUT("api/medpic/")
    Call<List<MedImageModel>> uploadImage(@Header("Authorization") String authToken,@Part MultipartBody.Part image);

    @POST("api/medpic/")
    Call<List<MedImageModel>> getImage(@Header("Authorization") String authToken);
//    @POST("api/medpic/")
//    Call<List<ResponseBody>> getImage(@Header("Authorization") String authToken);
}
