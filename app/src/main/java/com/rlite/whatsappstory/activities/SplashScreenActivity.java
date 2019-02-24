package com.rlite.whatsappstory.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.rlite.whatsappstory.R;

import java.io.File;

public class SplashScreenActivity extends Activity {


    public static final String TAG = SplashScreenActivity.class.getSimpleName();
    private static int SPLASH_TIME_OUT = 2000;
    Context context;
    private static final int MY_PERMISSIONS_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        checkPermission();
    }

    // This method check the permission of the user
    public void checkPermission(){
        if (ContextCompat.checkSelfPermission(SplashScreenActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(SplashScreenActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED ) {

            ActivityCompat.requestPermissions(SplashScreenActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST);
        }
        else {
//            copyAllStories();
            showSplashScreen();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST:
                // If request is cancelled, the result array are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED){

//                    copyAllStories();
                    showSplashScreen();

                } else {
                    // permission was deny, then again requesting the permission
                    checkPermission();
                }
                break;
        }
    }

    /**
     * show splashscreen
     */

    public void showSplashScreen(){
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                context = getApplicationContext();

                //creating our folder
                File file = new File(Environment.getExternalStorageDirectory(),"WhatsAppStories");

                if (!file.exists()){
                    file.mkdir();
                }

                Intent i = new Intent(SplashScreenActivity.this, HomeActivity.class);
                startActivity(i);
                //overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

                finish();
            }
        }, SPLASH_TIME_OUT);
    }




}
