package com.example.transition_pixel2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

public class SecondActivity extends AppCompatActivity {

    private final int CAMERA_PERMISSION_REQ_CODE = 9876;

    private boolean denied = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        boolean withTransitionFix = getIntent().getBooleanExtra("fix", false);
        Log.d("Second", "started with transition fix? " + withTransitionFix);

        if (withTransitionFix) {
            Log.d("Second", "postponing enter transition");
            postponeEnterTransition();
            final View decor = getWindow().getDecorView();
            decor.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    decor.getViewTreeObserver().removeOnPreDrawListener(this);
                    Log.d("Second", "starting postponed enter transition");
                    SecondActivity.this.startPostponedEnterTransition();
                    return true;
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != CAMERA_PERMISSION_REQ_CODE || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.finishAfterTransition(this);
            denied = true;
        } else {
            Log.d("Second", "Granted");
        }
    }

    @Override
    protected void onResume() {
        Log.d("Second", "onResume");
        super.onResume();

        if (!denied && ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQ_CODE);
        }

        // similar behavior if we trigger a intent chooser
        //sendTextIntentOnce();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Second", "onPause");
    }


    private boolean wasSend = false;

    private void sendTextIntentOnce() {
        if (!wasSend) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
            wasSend = true;
        } else {
            ActivityCompat.finishAfterTransition(this);
        }
    }

}
