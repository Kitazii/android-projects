/*
* USE LOGCAT TO CHECK Log.e OUTPUTS
* */

package org.me.gcu.lifecycle_example;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("Main","OnStart");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("Main","OnCreate");
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("Main","OnResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("Main", "OnPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("Main", "OnStop");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("Main", "OnDestroy");
    }


    public void button2OnClick(View view) {
        //start activity 2
        Intent startActivity2 = new Intent(MainActivity.this, Activity2.class);
        startActivity(startActivity2);
    }

    public void button3OnClick(View view) {
        //terminate app and related activities
        this.finishAffinity();
        //MainActivity.this.finish();
    }
}