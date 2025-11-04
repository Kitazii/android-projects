package org.me.gcu.lab6_threads_fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View

        .OnClickListener {

    private Handler mHandler;
    private ProgressBar mProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.startButton);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        runProgress();
    }

    private void runProgress()
    {
        mHandler = new Handler();

        //set up and display the progressBar
        mProgressBar = findViewById(R.id.bar1);
        mProgressBar.setMax(100);

        //create thread to handle the long running task
        //in this case it is a counter that will be used
        // represent progress via a progress bar

        new Thread(new Runnable()
        {
            @Override
            public void run() {
                for (int i = 0; i <= 100; i++)
                {
                    final int currentProgressCount = i;
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //update the value background thread to UI thread
                    //this is done in a further short life length thread
                    //to ensure there is no blocking of the 'calculation' thread
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBar.setProgress(currentProgressCount);
                        }
                    });
                }
            }
        }).start();
    }
}