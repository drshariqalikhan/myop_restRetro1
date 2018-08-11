package spero.shariq.com.myop_restretro1;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static spero.shariq.com.myop_restretro1.ImagePicker.REQUEST_PERMISSION;

public class Splash extends AppCompatActivity {
//    String urllogin = "http://myop.pythonanywhere.com/log/";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    ApiInterface apiInterface;
    TextView JPointTV,opNameTV;
    EditText JPointET,opNameET;
    Button Choosebtn, uploadbtn;
    ImageView img;
//    static final int IMG_REQUEST = 989;
//    int PICK_IMAGE_REQUEST = 1;
    Bitmap bitmap;
//    private String mImageUrl = "";
    String imagepath;
    ProgressBar progbar;

    ConnectivityManager conMgr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharedPreferences = getApplicationContext().getSharedPreferences("mypref", 0);
        editor = sharedPreferences.edit();
        login.TOKEN = sharedPreferences.getString("token", "xxx");
        opNameTV = findViewById(R.id.opNameTV);
        JPointTV = findViewById(R.id.JPointTV);
        opNameET = findViewById(R.id.opNameET);
        JPointET = findViewById(R.id.JPointET);
        uploadbtn = findViewById(R.id.uploadbtn);
        Choosebtn = findViewById(R.id.Choosebtn);
        img = (ImageView) findViewById(R.id.ImageView);
        progbar = findViewById(R.id.progbar);
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        opNameTV.setText(sharedPreferences.getString("Op_name", "xxx"));
        JPointTV.setText(sharedPreferences.getString("Journey_point", "xxx"));
//        IsLoggedIn(apiInterface, login.TOKEN);

    }

//get
    public void click(View view) {
//        login.TOKEN = "b959d32cd0001f63b30e24da5d7ae40f89683c74";

//        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<List<JourneyData>> call =  apiInterface.getData("TOKEN "+login.TOKEN);

        call.enqueue(new Callback<List<JourneyData>>() {
            @Override
            public void onResponse(Call<List<JourneyData>> call, Response<List<JourneyData>> response) {
                Toast.makeText(Splash.this,response.message(),Toast.LENGTH_LONG).show();

                for(int i = 0; i < response.body().size(); i++) {

                    JPointTV.setText(response.body().get(i).getJourney_point());
                    opNameTV.setText(response.body().get(i).getOp_name());
                }


        }

            @Override
            public void onFailure(Call<List<JourneyData>> call, Throwable t) {

//                startActivity(new Intent(Splash.this, login.class));
            }
        });
    }
//put text
    public void click2(View view) {

//        login.TOKEN = "b959d32cd0001f63b30e24da5d7ae40f89683c74";

//        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("journey_point",JPointET.getText().toString());
        queryParams.put("op_name",opNameET.getText().toString());

        Call<JourneyData> call = apiInterface.putData("TOKEN "+login.TOKEN,queryParams);
        call.enqueue(new Callback<JourneyData>() {
            @Override
            public void onResponse(Call<JourneyData> call, Response<JourneyData> response) {
                Toast.makeText(Splash.this,response.message().toString(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<JourneyData> call, Throwable t) {


            }
        });

    }
//browse pic
    public void click3(View view) {
        SelectTheImage();
    }
//upload pic
    public void click4(View view) {
        UploadTheImage();
        uploadbtn.setEnabled(true);

    }


    void IsLoggedIn (final ApiInterface apiInterface, final String Token)
    {
        try{

            Call<List<JourneyData>> call =  apiInterface.getData("TOKEN "+Token);

            call.enqueue(new Callback<List<JourneyData>>() {
                @Override
                public void onResponse(Call<List<JourneyData>> call, Response<List<JourneyData>> response) {

                    if(response.isSuccessful()){
                        Toast.makeText(Splash.this,response.message(),Toast.LENGTH_LONG).show();

                    }else {


//                        Toast.makeText(Splash.this,login.TOKEN,Toast.LENGTH_LONG).show();
                        startActivity(new Intent(Splash.this, login.class));
                    }


//                    for(int i = 0; i < response.body().size(); i++) {

//                        JPointTV.setText(response.body().get(i).getJourney_point());
//                        opNameTV.setText(response.body().get(i).getOp_name());
//                    }


                }

                @Override
                public void onFailure(Call<List<JourneyData>> call, Throwable t) {
                    Toast.makeText(Splash.this,"fail",Toast.LENGTH_LONG).show();

                }
            });


        }catch (Exception e){

        }



    }


    void SelectTheImage()
    {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,0);
        uploadbtn.setEnabled(true);

    }
    void UploadTheImage()
        {
        progbar.setVisibility(View.VISIBLE);
        File file = new File(imagepath);
        final RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image",file.getName(),requestBody);

        Call<List<MedImageModel>> call = apiInterface.uploadImage("TOKEN "+login.TOKEN,body);
       call.enqueue(new Callback<List<MedImageModel>>() {
           @Override
           public void onResponse(Call<List<MedImageModel>> call, Response<List<MedImageModel>> response) {
               if(response.isSuccessful())
               {
                   progbar.setVisibility(View.GONE);
                   Toast.makeText(Splash.this,"image uploaded!",Toast.LENGTH_LONG).show();
                   uploadbtn.setEnabled(false);
                   img.setImageBitmap(null);
               }
           }

           @Override
           public void onFailure(Call<List<MedImageModel>> call, Throwable t) {
               Toast.makeText(Splash.this,"Noooooooooo!",Toast.LENGTH_LONG).show();
           }
       });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK)
        {
            if(data == null)
            {
                Toast.makeText(Splash.this,"data null",Toast.LENGTH_LONG).show();
                return;
            }

            Uri uri = data.getData();
            try {
                //img.setEnabled(true);
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                img.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
            imagepath = getRealPathFromUri(uri);
        }

    }

  String getRealPathFromUri(Uri uri)
  {
      String[] projection = {MediaStore.Images.Media.DATA};
      CursorLoader loader = new CursorLoader(getApplicationContext(),uri,projection,null,null,null);
      Cursor cursor = loader.loadInBackground();
      int column_idx = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
      cursor.moveToFirst();
      String result = cursor.getString(column_idx);
      cursor.close();
      return result;

  }

    public void getImageServerClick(View view) {
        progbar.setVisibility(View.VISIBLE);

        Call<List<MedImageModel>> call = apiInterface.getImage("TOKEN "+login.TOKEN);
        call.enqueue(new Callback<List<MedImageModel>>() {
            @Override
            public void onResponse(Call<List<MedImageModel>> call, Response<List<MedImageModel>> response) {

                try{
                    for(int i = 0; i < response.body().size(); i++) {
                        byte[] decodedString = Base64.decode(response.body().get(i).getBase64_image(), Base64.DEFAULT);
                       // Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        Glide.with(getApplicationContext())
                                .load(decodedString)
                                .asBitmap()
                                .listener(new RequestListener<byte[], Bitmap>() {
                                    @Override
                                    public boolean onException(Exception e, byte[] model, Target<Bitmap> target, boolean isFirstResource) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Bitmap resource, byte[] model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {

                                        progbar.setVisibility(View.GONE);
                                        return false;
                                    }
                                })
                                .into(img);


                      //  img.setImageBitmap(decodedByte);
                    }
                }catch (Exception e){Toast.makeText(Splash.this,"btn_Done image",Toast.LENGTH_LONG).show();}

            }

            @Override
            public void onFailure(Call<List<MedImageModel>> call, Throwable t) {
                Toast.makeText(Splash.this,t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
}
