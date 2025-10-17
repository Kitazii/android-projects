package org.me.gcu.labdemos.pictureviewer;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private Button btn;
    private Button btnForward;
    private Button btnBack;
    private TextView picNumbDisplay;
    private ViewFlipper flip;
    private int picCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnForward=(Button)findViewById(R.id.btnForward);
        btnBack=(Button)findViewById(R.id.btnBack);
        picNumbDisplay = findViewById(R.id.picNumbDisplay);
        btnForward.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        flip=(ViewFlipper)findViewById(R.id.flip);
        //when a view is displayed
        flip.setInAnimation(this,android.R.anim.fade_in);
        //when a view disappears
        flip.setOutAnimation(this, android.R.anim.fade_out);

        picCount = 1;

        Log.e("MyTag",picCount + "");
        picNumbDisplay.setText("Picture Count " + picCount + " of 5");
    }

    public void onClick(View aview)
    {
        if (aview == btnForward)
        {
            flip.showNext();
            if (picCount == 5)
                picCount = 1;
            else
                picCount = picCount + 1;

            Log.e("MyTag",picCount + "");
            picNumbDisplay.setText("Picture Count " + picCount + " of 5");
        }
        else
        {
            flip.showPrevious();
            if (picCount == 1)
               picCount = 5;
            else
                picCount = picCount - 1;

            Log.e("MyTag",picCount + "");
            picNumbDisplay.setText("Picture Count " + picCount + " of 5");
        }

    }
}