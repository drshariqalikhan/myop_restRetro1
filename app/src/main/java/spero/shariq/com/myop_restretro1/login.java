package spero.shariq.com.myop_restretro1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class login extends AppCompatActivity {
    EditText editTextUsername, editTextPassword;

    public static String TOKEN;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        sharedPreferences = getApplicationContext().getSharedPreferences("mypref", 0);
        editor = sharedPreferences.edit();

    }

    public void loginClik(View view) {
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("username",editTextUsername.getText().toString());
        queryParams.put("password",editTextPassword.getText().toString());

        Call<LoginData> call = apiInterface.loginwith(queryParams);
        call.enqueue(new Callback<LoginData>() {
            @Override
            public void onResponse(Call<LoginData> call, Response<LoginData> response) {
            try {
                editor.putString("token", response.body().getToken());
                editor.commit();

                TOKEN = sharedPreferences.getString("token", "xxx");
//                Toast.makeText(login.this, TOKEN, Toast.LENGTH_LONG).show();
                if (response.isSuccessful()) {
                    startActivity(new Intent(login.this, MainActivity.class));
                } else {

                }
            }catch (Exception e){

                Toast.makeText(login.this, ""+"Incorrect USERNAME or PASSWORD", Toast.LENGTH_LONG).show();
            }
            }

            @Override
            public void onFailure(Call<LoginData> call, Throwable t) {


            }
        });

    }
}
