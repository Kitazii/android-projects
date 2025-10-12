package org.me.gcu.spinner_test;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.graphics.Color;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{
    private Spinner spinner1;
    private View mainView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner1 = (Spinner) findViewById(R.id.cSpinner);
        mainView = (View) findViewById(R.id.mainView);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(
                this, R.array.colours, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);
        spinner1.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
    {
        // TODO Auto-generated method stub
        if (arg0 == spinner1)
        {
            String text = (String)spinner1.getSelectedItem();
            if (text.equals("RED"))
            {
                mainView.setBackgroundColor(Color.RED);
            }
            else
            if (text.equals("GREEN"))
            {
                mainView.setBackgroundColor(Color.GREEN);
            }
            else
            if (text.equals("BLUE"))
            {
                mainView.setBackgroundColor(Color.BLUE);
            }
        }

    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0)
    {
        // Nothing implemented on this occasion

    }

} //