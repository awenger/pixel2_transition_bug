package com.example.transition_pixel2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        findViewById(R.id.start_second_with_fix).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSecond(true);
            }
        });

        findViewById(R.id.start_second_without_fix).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSecond(false);
            }
        });

        findViewById(R.id.start_second_without_transition).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("First", "perm result for reqCode: " + requestCode + " ");
        for (int i = 0; i < permissions.length; i++) {
            Log.d("First", "result includes: " + permissions[i] + " -> " + grantResults[i]);
        }
    }

    private void startSecond(boolean withFix) {
        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        intent.putExtra("fix", withFix);

        Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                MainActivity.this,
                Pair.create(
                        MainActivity.this.findViewById(android.R.id.statusBarBackground),
                        Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME
                ),
                Pair.create(
                        MainActivity.this.findViewById(android.R.id.navigationBarBackground),
                        Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME
                ),
                Pair.create(
                        MainActivity.this.findViewById(R.id.toolbar),
                        "toolbar"
                )
        ).toBundle();
        ActivityCompat.startActivity(MainActivity.this, intent, bundle);
    }
}
