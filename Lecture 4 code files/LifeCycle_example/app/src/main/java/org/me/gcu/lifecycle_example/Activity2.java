/*
* USE BACK BUTTON TO EXIT ACTIVITY 2
* */
package org.me.gcu.lifecycle_example;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class Activity2 extends AppCompatActivity {

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("ACT 2","OnStart");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("ACT 2","OnCreate");
        setContentView(R.layout.activity_2);
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.e("ACT 2","OnResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("ACT 2", "OnPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("ACT 2", "OnStop");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("ACT 2", "OnDestroy");
    }

}