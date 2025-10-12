package org.me.gcu.switches;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tV;
    private Switch sW;
    private ToggleButton tB;
    private SwitchCompat sWC;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tV = (TextView)findViewById(R.id.textView);
        sW = (Switch)findViewById(R.id.switch1);
        sW.setOnClickListener(this);
        tB = (ToggleButton)findViewById(R.id.myToggleButton);
        tB.setOnClickListener(this);
        sWC = (SwitchCompat)findViewById(R.id.switch2);
        sWC.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == sW) {
            if (sW.isChecked()) { //switch ON
                tV.setText("Switch went ON");
            } else
                tV.setText("Switch went OFF");
        }
        else if(view == tB) {
            if (tB.isChecked()) { //switch ON
                tV.setText("Toggle went ON");
            } else
                tV.setText("Toggle went OFF");
        }
        else if(view == sWC) {
            if (sWC.isChecked()) { //switch ON
                tV.setText("SwitchCompat went ON");
            } else
                tV.setText("SwitchCompat went OFF");
        }
    }
}