package org.me.gcu.listview_test;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener
{
    private ListView listView;
    private TextView label2;
    private Button button1;
    private Button button2;
    private Button button3;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        String[] values = new String[]
                {"Red", "Green", "Blue", "Purple"};

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview,values);

        if (adapter == null)
        {
            Log.e("MyTag","Adapter error");
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);

        listView = (ListView) findViewById(R.id.list);
        listView.setOnItemClickListener(this);
        label2 = (TextView) findViewById(R.id.label2);

        // Assign adapter to ListView
        listView.setAdapter(adapter);


    }

    public void onItemClick(AdapterView<?> parenr, View view,int position,long id)
    {
        switch(position)
        {
            case 0: label2.setBackgroundColor( getResources().getColor(android.R.color.holo_red_light));
                break;
            case 1 : label2.setBackgroundColor( getResources().getColor(android.R.color.holo_green_light));
                break;
            case 2: label2.setBackgroundColor( getResources().getColor(android.R.color.holo_blue_light));
                break;
            case 3: label2.setBackgroundColor( getResources().getColor(android.R.color.holo_purple));
                break;

            default :
                break;
        }
    }

    public void onClick(View aview)
    {
        if (aview == button1)
        {
            label2.setBackgroundColor( getResources().getColor(android.R.color.holo_red_light));
        }
        else
        if (aview == button2)
        {
            label2.setBackgroundColor( getResources().getColor(android.R.color.holo_green_light));
        }
        else
        if (aview == button3)
        {
            label2.setBackgroundColor( getResources().getColor(android.R.color.holo_blue_light));
        }

    }
} // End of Activity class