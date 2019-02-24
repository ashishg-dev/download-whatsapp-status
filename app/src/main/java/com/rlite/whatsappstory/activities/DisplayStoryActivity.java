package com.rlite.whatsappstory.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.rlite.whatsappstory.CheckInternetConnection;
import com.rlite.whatsappstory.R;
import com.rlite.whatsappstory.constant.Constant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;


public class DisplayStoryActivity extends AppCompatActivity {

    private static final String TAG = DisplayStoryActivity.class.getSimpleName();

    ImageView imageView,buttonShare,buttonDelete,buttonDownload;
    VideoView videoView;
    String uri, story;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_story);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        imageView = (ImageView) findViewById(R.id.imageView);
        buttonShare = (ImageView) findViewById(R.id.buttonShare);
        buttonDelete = (ImageView) findViewById(R.id.buttonDelete);
        buttonDownload = (ImageView) findViewById(R.id.buttonDownload);
        videoView = (VideoView) findViewById(R.id.videoView);

        MobileAds.initialize(this, getResources().getString(R.string.admob_app_id));

        if (CheckInternetConnection.checkInternetConnection(DisplayStoryActivity.this)){
            mAdView = (AdView) findViewById(R.id.adViewDisplayStoryActivity);
            mAdView.setVisibility(View.VISIBLE);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }

        Intent intent = getIntent();
        uri = intent.getStringExtra("uri");
        story = intent.getStringExtra("stories");
        if (!story.equalsIgnoreCase("download")) {
            buttonDownload.setVisibility(View.VISIBLE);
        }

        if (uri.contains("jpg")) {
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageURI(Uri.parse(uri));
        } else {
            videoView.setVisibility(View.VISIBLE);
            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(videoView);
            videoView.setMediaController(mediaController);
            videoView.setVideoURI(Uri.parse(uri));
            videoView.requestFocus();
            videoView.start();
        }

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteStory(uri);
            }
        });

        buttonDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadStory(uri);
            }
        });

        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareStory(uri);
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void shareStory(String uri){
        File f = new File(uri);
        String filename[] = uri.split("/");
        Uri uri1 = Uri.parse("file://" + f.getAbsolutePath());
        Intent share = new Intent(Intent.ACTION_SEND);
        share.putExtra(Intent.EXTRA_STREAM, uri1);
        if (filename[filename.length - 1].contains("jpg")) {
            share.setType("image/*");
        } else {
            share.setType("video/*");
        }
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(share, "Share Story"));
    }

    public void deleteStory(String uri) {
        try {
            File file = new File(uri);
            if (file.exists()) {
                file.delete();
                Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "File not found to delete", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void downloadStory(String uri) {
        try {
            String filename[] = uri.split("/");
            File f = new File(Constant.MY_URI, filename[filename.length - 1]);
            if (!f.exists()) {
                if (filename[filename.length - 1].contains("jpg")) {
                    File story = new File(Constant.MY_URI, filename[filename.length - 1]);
                    FileOutputStream os = new FileOutputStream(story);
                    Bitmap bitmap = BitmapFactory.decodeFile(uri);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, os);
                    os.flush();
                    os.close();
                    Toast.makeText(DisplayStoryActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                } else {
                    FileInputStream videoStory = new FileInputStream(uri);
                    FileOutputStream os = new FileOutputStream(Constant.MY_URI + filename[filename.length - 1]);
                    byte[] buf = new byte[10240];
                    int len;
                    while ((len = videoStory.read(buf)) > 0) {
                        os.write(buf, 0, len);
                    }
                    os.flush();
                    os.close();
                    videoStory.close();
                    Toast.makeText(DisplayStoryActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Story Already Exists", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "onClick: " + e.getMessage());
        }
    }

/*    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(DisplayStoryActivity.this,StoryActivity.class));
    }*/
}
