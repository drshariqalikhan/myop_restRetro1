package spero.shariq.com.myop_restretro1;

import com.google.gson.annotations.SerializedName;

/**
 * Created by drsha on 4/8/2018.
 */

public class LoginData {
    @SerializedName("token")
    String token;

    public String getToken() {
        return token;
    }
}
