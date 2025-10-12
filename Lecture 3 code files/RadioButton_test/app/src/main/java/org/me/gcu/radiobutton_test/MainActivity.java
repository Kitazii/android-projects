package org.me.gcu.radiobutton_test;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private RadioGroup colourGroup;
    private RadioButton redButton;
    private RadioButton greenButton;
    private RadioButton blueButton;
    private View mainView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainView = (View)findViewById(R.id.mainView);

        colourGroup = (RadioGroup)findViewById(R.id.colourRadioGroup);
        redButton = (RadioButton)findViewById(R.id.redRadio);
        greenButton = (RadioButton)findViewById(R.id.greenRadio);
        blueButton = (RadioButton)findViewById(R.id.blueRadio);

        // Set the initially selected radio button and
        // set the background accordingly
        redButton.toggle();
        mainView.setBackgroundColor(Color.RED);

        redButton.setOnClickListener(this);
        greenButton.setOnClickListener(this);
        blueButton.setOnClickListener(this);

        colourGroup.setEnabled(true);
    }

    @Override
    public void onClick(View arg0)
    {
        // TODO Auto-generated method stub
        if (redButton.isChecked())
        {
            mainView.setBackgroundColor(Color.RED);
        }
        else
        if (greenButton.isChecked())
        {
            //mainView.setBackgroundColor(Color.GREEN);
            mainView.setBackgroundColor(Color.parseColor("#96ff96"));
        }
        else
        if (blueButton.isChecked())
        {
            mainView.setBackgroundColor(Color.BLUE);
        }

    }
}