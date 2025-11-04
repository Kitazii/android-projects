package org.me.gcu.handlerprogressbar;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private Handler mHandler;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.startButton);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        runProgress();
    }

    private void runProgress()
    {
        mHandler=new Handler();

        // Set up and display the progressBar
        mProgressBar= findViewById(R.id.bar1);
        mProgressBar.setMax(100);

        // Create Thread to handle the "long running task"
        // In this case it is a counter that will be used
        // represent progress via a progress bar
        new Thread(new Runnable()
        {
            @Override
            // This is where you implement cod for the task
            public void run()
            {
                for (int i = 0; i <= 100; i++)
                {
                    final int currentProgressCount = i;
                    try
                    {
                        Thread.sleep(50);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    // Update the value background thread to UI thread
                    // This is done in a further short life length thread
                    // to ensure the is no blocking of the "calculation" thread
                    mHandler.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            mProgressBar.setProgress(currentProgressCount);
                        }
                    });
                }
            }
        }).start();
    }
}