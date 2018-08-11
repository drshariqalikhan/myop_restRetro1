package spero.shariq.com.myop_restretro1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TiledDashboard extends AppCompatActivity {
    AlertDialog alertDialog;

    //TODO: set vars from json to
    // isTaskIncomplete,
    // Tasks_to_do,
    // go_to_activity,
    // time_left_forTask,
    // Days_left_for_OP
    boolean isTaskIncomplete =true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiled_dashboard);

        //TODO:

        if (isTaskIncomplete) {
            //TODO: replace this with custom dialog box and class
            alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("THINGS YOU NEED TO DO");
            alertDialog.setMessage("LIST ITEM 1 \n LIST ITEM 2");

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "COMPLETE TASKS", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {


                }
            });

            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "LATER", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertDialog.show();
        }
    }



    public void MessageClick(View view) {
        //TODO: create messaging page
        startActivity(new Intent(TiledDashboard.this, Splash.class));
    }

    public void ThingsToDOClick(View view) {
        //TODO: click should show 'things to do' dialogue box

        startActivity(new Intent(TiledDashboard.this, AddMeds.class));

    }


    public void AboutOPClick(View view) {
        //TODO: create about op , anaesth , physio and postop care page
    }


}
