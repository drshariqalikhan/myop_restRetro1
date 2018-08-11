package spero.shariq.com.myop_restretro1;

import com.google.gson.annotations.SerializedName;

/**
 * Created by drsha on 4/8/2018.
 */

public class JourneyData {

    @SerializedName("op_name")
    private String Op_name;

    @SerializedName("journey_point")
    private String Journey_point;


    public String getOp_name() {
        return Op_name;
    }

    public String getJourney_point() {
        return Journey_point;
    }


}
