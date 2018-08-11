package spero.shariq.com.myop_restretro1;


import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static spero.shariq.com.myop_restretro1.ImagePicker.REQUEST_PERMISSION;


public class AddMeds extends AppCompatActivity {
    ImageView imageView;
    LinearLayout MedicationFreqLayout;
    Spinner Spinner;
    Button uploadtoserverbtn;
    Bitmap bitmap;
    String imagepath;
//
    ApiInterface apiInterface;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meds);
        imageView = findViewById(R.id.imageview);
        uploadtoserverbtn = findViewById(R.id.uploadtoserverbtn);
        MedicationFreqLayout = findViewById(R.id.MedicationFreqLayout);
        Spinner = findViewById(R.id.Spinner);

        progressDialog = new ProgressDialog(this);

        uploadtoserverbtn.setEnabled(false);
        MedicationFreqLayout.setVisibility(View.GONE);
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION);
        }


    }
//helper methods for Image upload

    void UploadTheImage()
    {
        progressDialog.setMessage("UPLOADING IMAGE...PLEASE WAIT");
        progressDialog.show();
        File file = new File(imagepath);
        final RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image",Spinner.getSelectedItem().toString()+".jpg",requestBody);

        Call<List<MedImageModel>> call = apiInterface.uploadImage("TOKEN "+login.TOKEN,body);
        call.enqueue(new Callback<List<MedImageModel>>() {
            @Override
            public void onResponse(Call<List<MedImageModel>> call, Response<List<MedImageModel>> response) {
                if(response.isSuccessful())
                {

                    progressDialog.dismiss();
                    Toast.makeText(AddMeds.this,"image uploaded!",Toast.LENGTH_LONG).show();
                    uploadtoserverbtn.setEnabled(false);
                    imageView.setImageResource(R.drawable.searchgallery);

                    Spinner.setSelection(0);
                    MedicationFreqLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<MedImageModel>> call, Throwable t) {
                Toast.makeText(AddMeds.this,"Noooooooooo!",Toast.LENGTH_LONG).show();
            }
        });
    }


    public void uploadbtnClick(View view) {
        UploadTheImage();
    }

    //helper methods for Image Select
    void SelectTheImage()
    {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,0);
        uploadtoserverbtn.setEnabled(true);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK)
        {
            if(data == null)
            {
                Toast.makeText(AddMeds.this,"data null",Toast.LENGTH_LONG).show();
                return;
            }

            Uri uri = data.getData();
            try {
                //img.setEnabled(true);
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);


                imageView.setImageBitmap(bitmap);
                MedicationFreqLayout.setVisibility(View.VISIBLE);

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



    public void imgclick(View view) {
        SelectTheImage();
    }


    public void CancelClick(View view) {
        this.finish();

    }
}
