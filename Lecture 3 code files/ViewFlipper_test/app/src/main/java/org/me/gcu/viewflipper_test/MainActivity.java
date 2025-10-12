package org.me.gcu.viewflipper_test;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ViewFlipper;

public class MainActivity extends AppCompatActivity implements OnClickListener
{
    private Button btnFwd, btnBack;
    private ViewFlipper flip;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnFwd = (Button) findViewById(R.id.btnNext);
        btnBack = (Button) findViewById(R.id.btnPrev);
        btnFwd.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        flip = (ViewFlipper) findViewById(R.id.myVFlip);
        //when a view is displayed
        flip.setInAnimation(this, android.R.anim.fade_in);
        //when a view disappears
        flip.setOutAnimation(this, android.R.anim.fade_out);
        //flip.setFlipInterval(1000); // Every second
        //flip.setAutoStart(true); //optional autostart

    }

    @Override
    public void onClick(View v)
    {
       if(v == btnFwd) flip.showNext();
       else flip.showPrevious();
    }
}