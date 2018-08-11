package spero.shariq.com.myop_restretro1;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.System.console;

public class ImagePicker extends AppCompatActivity {

    public static final int REQUEST_IMAGE = 100;
    public static final int REQUEST_PERMISSION = 200;

    private ProgressBar mProgressBar;
    private Button button, btnUpload;
    TextView textView;
    Context mContext;
    String imageFilePath ="";
    File file;
    ImageView imageView;

    ApiInterface apiInterface;
    private static final String TAG = "upload";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_picker);
        textView = findViewById(R.id.textView);
        imageView = findViewById(R.id.imageview);
        mProgressBar = findViewById(R.id.progressBar);
        button = findViewById(R.id.btnChoose);
        btnUpload = findViewById(R.id.btnUpload);
        mContext = getApplicationContext();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION);
        }


    }

    public void TakePhotoclick(View view) {
        openCameraIntent();
    }
//
//    private void SnapUploadImage() {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(intent, REQ_CAMERA_IMAGE);
//    }
private void openCameraIntent() {
    Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    if (pictureIntent.resolveActivity(getPackageManager()) != null) {

        file = null;
        try {
            file = createImageFile();
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Uri photoUri = FileProvider.getUriForFile(this, getPackageName() +".provider", file);
        pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(pictureIntent, REQUEST_IMAGE);
    }
}

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Thanks for granting Permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
               // Toast.makeText(this,imageFilePath, Toast.LENGTH_LONG).show();
//                imageView.setImageURI(Uri.parse(imageFilePath));
                Glide.with(this).load(imageFilePath).into(imageView);

            }
            else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "You cancelled the operation", Toast.LENGTH_SHORT).show();
            }
        }
    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode,
//                                    Intent data) {
//        if (requestCode == REQUEST_IMAGE_CAPTURE ){
////            if (resultCode == Activity.RESULT_OK) {
//                Glide.with(this).load(imageFilePath).into(imageView);
////            }
////            else if(resultCode == Activity.RESULT_CANCELED) {
//                // User Cancelled the action
////            }
//        }
//
//
//    }
//    private void captureImage() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        // Ensure that there's a camera activity to handle the intent
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            // Create the File where the photo should go
//            File photofile = null;
//            try {
//                photofile = createImageFile();
//            } catch (IOException ex) {
//                // Error occurred while creating the File
//                ex.printStackTrace();
//            }
//            // Continue only if the File was successfully created
//            if (photofile != null) {
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
//                        Uri.fromFile(photofile));
//                startActivityForResult(takePictureIntent, REQ_CAMERA_IMAGE);
//            }
//        }
//    }
//
//    private File createImageFile() throws IOException {
//        // Create an image file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_PICTURES);
//
//        File image = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir      /* directory */
//        );
//
//        // Save a file: path for use with ACTION_VIEW intents
//        mCurrentPhotoPath = image.getAbsolutePath();
////        Log.e("Getpath", "Cool" + mCurrentPhotoPath);
//        return image;
//    }
//
//    private void setPic() {
//        // Get the dimensions of the View
//        int targetW = imageView.getWidth();
//        int targetH = imageView.getHeight();
//
//        // Get the dimensions of the bitmap
//        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//        bmOptions.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
//        int photoW = bmOptions.outWidth;
//        int photoH = bmOptions.outHeight;
//
//        // Determine how much to scale down the image
//        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
//
//        // Decode the image file into a Bitmap sized to fill the View
//        bmOptions.inJustDecodeBounds = false;
//        bmOptions.inSampleSize = scaleFactor;
//        bmOptions.inPurgeable = true;
//
//        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
//        imageView.setImageBitmap(bitmap);
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == REQ_CAMERA_IMAGE && resultCode == RESULT_OK)
//        {
//
//            Bitmap photo = (Bitmap) data.getExtras().get("data");
//            imageView.setImageBitmap(photo);
////
////            Uri tempUri = data.getData();
//////            Uri tempUri = data.getData();
//////            try {
//////                //img.setEnabled(true);
//////                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),tempUri);
//////                imageView.setImageBitmap(bitmap);
//////
//////            } catch (IOException e) {
//////                e.printStackTrace();
//////            }
////
////            // CALL THIS METHOD TO GET THE ACTUAL PATH
//////            File file = (File)getIntent().getExtras().get("FilePath");
////            File file = new File(getRealPathFromUri(tempUri));
////            Toast.makeText(ImagePicker.this,file.getPath(),Toast.LENGTH_LONG).show();
////
////            try {
////                final RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
////
////                MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestBody);
////
////                Call<List<MedImageModel>> call = apiInterface.uploadImage("TOKEN " + login.TOKEN, body);
////                call.enqueue(new Callback<List<MedImageModel>>() {
////                    @Override
////                    public void onResponse(Call<List<MedImageModel>> call, Response<List<MedImageModel>> response) {
////                        if (response.isSuccessful()) {
////                            mProgressBar.setVisibility(View.GONE);
////                            Toast.makeText(ImagePicker.this, "image uploaded!", Toast.LENGTH_LONG).show();
//////                        uploadbtn.setEnabled(false);
////                            imageView.setImageBitmap(null);
////                        }
////                    }
////
////                    @Override
////                    public void onFailure(Call<List<MedImageModel>> call, Throwable t) {
////                        Toast.makeText(ImagePicker.this, "Noooooooooo!", Toast.LENGTH_LONG).show();
////                    }
////                });
////            }catch (Exception e){  System.out.println(e.toString());}
////
//        }
//    }
//
//    public Uri getImageUri(Context inContext, Bitmap inImage) {
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
//        return Uri.parse(path);
//    }
//
//    public String getRealPathFromURI(Uri uri) {
//        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
//        cursor.moveToFirst();
//        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
//        return cursor.getString(idx);
//    }
//
//    String getRealPathFromUri(Uri uri)
//    {
//        String[] projection = {MediaStore.Images.Media.DATA};
//        CursorLoader loader = new CursorLoader(getApplicationContext(),uri,projection,null,null,null);
//        Cursor cursor = loader.loadInBackground();
//        int column_idx = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        cursor.moveToFirst();
//        String result = cursor.getString(column_idx);
//        cursor.close();
//        return result;
//
//    }
//
//    public String getRealPathFromUri2(final Uri uri) { // function for file path from uri,
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(mContext, uri)) {
//            // ExternalStorageProvider
//            if (isExternalStorageDocument(uri)) {
//                final String docId = DocumentsContract.getDocumentId(uri);
//                final String[] split = docId.split(":");
//                final String type = split[0];
//
//                if ("primary".equalsIgnoreCase(type)) {
//                    return Environment.getExternalStorageDirectory() + "/" + split[1];
//                }
//            }
//            // DownloadsProvider
//            else if (isDownloadsDocument(uri)) {
//
//                final String id = DocumentsContract.getDocumentId(uri);
//                final Uri contentUri = ContentUris.withAppendedId(
//                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
//
//                return getDataColumn(mContext, contentUri, null, null);
//            }
//            // MediaProvider
//            else if (isMediaDocument(uri)) {
//                final String docId = DocumentsContract.getDocumentId(uri);
//                final String[] split = docId.split(":");
//                final String type = split[0];
//
//                Uri contentUri = null;
//                if ("image".equals(type)) {
//                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//                } else if ("video".equals(type)) {
//                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
//                } else if ("audio".equals(type)) {
//                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//                }
//
//                final String selection = "_id=?";
//                final String[] selectionArgs = new String[]{
//                        split[1]
//                };
//
//                return getDataColumn(mContext, contentUri, selection, selectionArgs);
//            }
//        }
//        // MediaStore (and general)
//        else if ("content".equalsIgnoreCase(uri.getScheme())) {
//
//            // Return the remote address
////            if (isGooglePhotosUri(uri))
////                return uri.getLastPathSegment();
//
//            return getDataColumn(mContext, uri, null, null);
//        }
//        // File
//        else if ("file".equalsIgnoreCase(uri.getScheme())) {
//            return uri.getPath();
//        }
//
//        return null;
//    }


    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    public void uploadClick(View view) {
//
//    }
//
//
//    public void TakePhotoclick(View view) {
//        takePhoto();
//    }
//
//    private void takePhoto() {
//
//        dispatchTakePictureIntent();
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Log.i(TAG, "onActivityResult: " + this);
//        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
//            setPic();
////			Bitmap bitmap = (Bitmap) data.getExtras().get("data");
////			if (bitmap != null) {
////				imageView.setImageBitmap(bitmap);
////				try {
////					sendPhoto(bitmap);
////				} catch (Exception e) {
////					// TODO Auto-generated catch block
////					e.printStackTrace();
////				}
////			}
//        }
//    }
//
//    private void setPic() {
//        int targetW = imageView.getWidth();
//        int targetH = imageView.getHeight();
//
//        // Get the dimensions of the bitmap
//        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//        bmOptions.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
//        int photoW = bmOptions.outWidth;
//        int photoH = bmOptions.outHeight;
//
//        // Determine how much to scale down the image
//        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
//
//        // Decode the image file into a Bitmap sized to fill the View
//        bmOptions.inJustDecodeBounds = false;
//        bmOptions.inSampleSize = scaleFactor << 1;
//        bmOptions.inPurgeable = true;
//
//        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
//
//        Matrix mtx = new Matrix();
//        mtx.postRotate(90);
//        // Rotating Bitmap
//        Bitmap rotatedBMP = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mtx, true);
//
//        if (rotatedBMP != bitmap)
//            bitmap.recycle();
//
//        imageView.setImageBitmap(rotatedBMP);
//
//        try {
//            sendPhoto(rotatedBMP);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
//
//    private void sendPhoto(Bitmap rotatedBMP) {
//    }
//
//
//    String mCurrentPhotoPath;
//
//    static final int REQUEST_TAKE_PHOTO = 1;
//    File photoFile = null;
//
//    private void dispatchTakePictureIntent() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        // Ensure that there's a camera activity to handle the intent
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            // Create the File where the photo should go
//            File photoFile = null;
//            try {
//                photoFile = createImageFile();
//            } catch (IOException ex) {
//                // Error occurred while creating the File
//
//            }
//            // Continue only if the File was successfully created
//            if (photoFile != null) {
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
//                        Uri.fromFile(photoFile));
//                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
//            }
//        }
//    }
//
//    private File createImageFile() throws IOException {
//        // Create an image file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = "JPEG_" + timeStamp + "_";
//        String storageDir = Environment.getExternalStorageDirectory() + "/picupload";
//        File dir = new File(storageDir);
//        if (!dir.exists())
//            dir.mkdir();
//
//        File image = new File(storageDir + "/" + imageFileName + ".jpg");
//
//        // Save a file: path for use with ACTION_VIEW intents
//        mCurrentPhotoPath = image.getAbsolutePath();
//        Log.i(TAG, "photo path = " + mCurrentPhotoPath);
//        return image;
//    }
//
    private File createImageFile() throws IOException{

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        imageFilePath = image.getAbsolutePath();

        return image;
    }

    void UploadTheImage(String imagepath)
    {
//        mProgressBar.setVisibility(View.VISIBLE);
        //File file = new File(imagepath);

        final RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image",file.getName(),requestBody);

        Call<List<MedImageModel>> call = apiInterface.uploadImage("TOKEN "+login.TOKEN,body);
        call.enqueue(new Callback<List<MedImageModel>>() {
            @Override
            public void onResponse(Call<List<MedImageModel>> call, Response<List<MedImageModel>> response) {
                if(response.isSuccessful())
                {
//                    mProgressBar.setVisibility(View.GONE);
                    Toast.makeText(ImagePicker.this,"image uploaded!",Toast.LENGTH_LONG).show();
//                    uploadbtn.setEnabled(false);
//                    imageView.setImageBitmap(null);
                }
            }

            @Override
            public void onFailure(Call<List<MedImageModel>> call, Throwable t) {
                Toast.makeText(ImagePicker.this,"Noooooooooo!",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void uploadbrnClick(View view) {
        try{
            UploadTheImage(imageFilePath);}catch (Exception e){Toast.makeText(ImagePicker.this,e.toString(),Toast.LENGTH_LONG).show();;}
    }
}
