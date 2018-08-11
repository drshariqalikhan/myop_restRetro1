package spero.shariq.com.myop_restretro1;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;
import static android.support.v4.app.ActivityCompat.startActivityForResult;

/**
 * Created by drsha on 7/8/2018.
 */

public class CustomDialogClass extends Dialog implements
        android.view.View.OnClickListener {

    Bitmap bitmap;
    ImageView GalleryImg;
    public Activity c;
    public Dialog d;
    public Button btn_AddAnother, btn_Done,SelectUploadBtn;

    public CustomDialogClass(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
        btn_AddAnother = (Button) findViewById(R.id.btn_AddAnother);
        btn_Done = (Button) findViewById(R.id.btn_Done);
        SelectUploadBtn = (Button) findViewById(R.id.SelectUploadBtn);
        GalleryImg = findViewById(R.id.GalleryImg);
        btn_AddAnother.setOnClickListener(this);
        btn_Done.setOnClickListener(this);
        SelectUploadBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_AddAnother:
                c.finish();
                break;
            case R.id.btn_Done:
                dismiss();
                break;
            case R.id.SelectUploadBtn:
                break;
            default:
                break;
        }
        dismiss();
    }
//
//    void SelectTheImage()
//    {
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType("image/*");
//        getOwnerActivity().startActivityForResult(intent,0);
//
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode == RESULT_OK)
//        {
//            if(data == null)
//            {
////                Toast.makeText(Splash.this,"data null",Toast.LENGTH_LONG).show();
//                return;
//            }
//
//            Uri uri = data.getData();
//            try {
//                //img.setEnabled(true);
//                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
//                GalleryImg.setImageBitmap(bitmap);
////                Choosebtn.setEnabled(false);
////                uploadbtn.setEnabled(true);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            imagepath = getRealPathFromUri(uri);
//        }
//
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
}
