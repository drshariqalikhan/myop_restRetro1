package spero.shariq.com.myop_restretro1;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by drsha on 4/8/2018.
 */

public class ApiClient {

    public static final String BASE_URL = "https://myop.pythonanywhere.com";
    public static Retrofit retrofit = null;

    public static Retrofit getApiClient()
    {
        if(retrofit == null)
        {
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        }
        return retrofit;
    }
}
