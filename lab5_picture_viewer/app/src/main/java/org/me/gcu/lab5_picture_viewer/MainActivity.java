package org.me.gcu.lab5_picture_viewer;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewFlipper viewFlipper;
    private Button btnPrevious;
    private Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize ViewFlipper and buttons
        viewFlipper = findViewById(R.id.viewFlipper);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);

        // Set up button click listeners
        btnNext.setOnClickListener(this);
        btnPrevious.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == btnNext)
        {
            viewFlipper.showNext();
        }
        if (v == btnPrevious)
        {
            viewFlipper.showPrevious();
        }
    }
}