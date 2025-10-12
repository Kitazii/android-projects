package org.me.gcu.recyclerview_test;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btn = findViewById(R.id.button1);
        btn.setOnClickListener(this);
        rv = findViewById(R.id.recView);


    }

    @Override
    public void onClick(View view) {
        if(view == btn){
            //Set test data into the recycler view's adapter
            String[] data = new String[100]; //ArrayList<String> data = new ArrayList<>();
            for(int i=0; i<100; i++)
                data[i] = "data" + i;
            RecViewCustomAdapter myAdapter = new RecViewCustomAdapter(data);
            //Adapt into the recyclerView
            LinearLayoutManager llm = new LinearLayoutManager(this);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            rv.setLayoutManager(llm);
            rv.setAdapter(myAdapter);
        }
    }
}