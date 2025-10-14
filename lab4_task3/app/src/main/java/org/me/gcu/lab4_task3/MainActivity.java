package org.me.gcu.lab4_task3;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private TextView label2;
    private Button button1;
    private Button button2;
    private Button button3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String[] values = new String[]
                {"Red", "Green", "Blue", "Purple"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.activity_listview, R.id.label, values);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);

        ListView listView = findViewById(R.id.list);
        listView.setOnItemClickListener(this);
        label2 = findViewById(R.id.label2);

        // Assign adapter to ListView
        listView.setAdapter(adapter);
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                label2.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light, null));
                break;
            case 1:
                label2.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light, null));
                break;
            case 2:
                label2.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light, null));
                break;
            case 3:
                label2.setBackgroundColor(getResources().getColor(android.R.color.holo_purple, null));
                break;

            default:
                break;
        }
    }

    public void onClick(View aview) {
        if (aview == button1) {
            label2.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light, null));
        } else if (aview == button2) {
            label2.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light, null));
        } else if (aview == button3) {
            label2.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light, null));
        }
    }
}
