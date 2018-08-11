package spero.shariq.com.myop_restretro1;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static spero.shariq.com.myop_restretro1.ImagePicker.REQUEST_PERMISSION;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ApiInterface apiInterface;
    ConnectivityManager conMgr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getApplicationContext().getSharedPreferences("mypref", 0);
        editor = sharedPreferences.edit();
        login.TOKEN = sharedPreferences.getString("token","xxx");
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);


        //
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) !=
                    PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET},
                        REQUEST_PERMISSION);



                if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                        || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    // notify user you are online

                        IsLoggedIn(apiInterface, login.TOKEN);


                } else if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED
                        || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED) {

                    // notify user you are not online
                    Toast.makeText(MainActivity.this,"user you are not online",Toast.LENGTH_LONG).show();
                }
            }
            else
            {
                ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

                if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                        || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    // notify user you are online
                    IsLoggedIn(apiInterface, login.TOKEN);

                } else if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED
                        || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED) {

                    // notify user you are not online
                    Toast.makeText(MainActivity.this,"user you are not online",Toast.LENGTH_LONG).show();

                }
            }

    }

    void IsLoggedIn (final ApiInterface apiInterface, final String Token)
    {




            Call<List<JourneyData>> call =  apiInterface.getData("TOKEN "+Token);


            call.enqueue(new Callback<List<JourneyData>>() {
                @Override
                public void onResponse(Call<List<JourneyData>> call, Response<List<JourneyData>> response)
                {



                    if(response.isSuccessful())
                    {
                        try{
                            init_vars(response);

                        }catch (Exception e){}
                        startActivity(new Intent(MainActivity.this, TiledDashboard.class));

                    }else {


//                       Toast.makeText(MainActivity.this,"please log in...",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(MainActivity.this, login.class));
                    }




                }

                @Override
                public void onFailure(Call<List<JourneyData>> call, Throwable t) {
                    Toast.makeText(MainActivity.this,"fail",Toast.LENGTH_LONG).show();

                }
            });







    }
    @Nullable
    void init_vars( Response<List<JourneyData>> response)
    {
        for(int i = 0; i < response.body().size(); i++)
        {
            //set values
            editor.putString("Journey_point", response.body().get(i).getJourney_point());
            editor.putString("Op_name", response.body().get(i).getOp_name());

        }
        editor.commit();

    }

    @Override
    protected void onResume() {
        super.onResume();
        IsLoggedIn(apiInterface, login.TOKEN);
    }
}
