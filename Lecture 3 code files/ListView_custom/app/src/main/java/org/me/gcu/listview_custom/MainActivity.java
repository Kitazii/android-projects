package org.me.gcu.listview_custom;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
//import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    TextView tV;
    ListView myListView;
    //texts for the items
    String[] birdList = {"Raven", "Magpie", "Puffin"};
    //IDs for the item drawings (same order as text items)
    int[] imgs = {R.drawable._20pxcorvuscorax, R.drawable._20pxmagpie, R.drawable._20pxpuffin};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tV = (TextView)findViewById(R.id.textView1);
        myListView = (ListView) findViewById(R.id.simpleListView);
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), birdList, imgs);
        myListView.setAdapter(customAdapter);
        myListView.setOnItemClickListener(this);

    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        switch(position)
        {
            case 0: //raven
                tV.setText("It's my fav too!");
                break;
            case 1 : //magpie
                tV.setText("One for sorrow, two for joy...");
                break;
            case 2: //puffin
                tV.setText("Cool one!");
                break;
            default :
                break;
        }
    }
}