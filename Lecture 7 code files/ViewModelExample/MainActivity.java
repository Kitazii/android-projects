package org.me.gcu.basicviewmodel;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;


public class MainActivity extends AppCompatActivity {
    private Button btn;
    private TextView tv;
    private EditText et;
    private ExchangeViewModel viewModel; //Hides data model, gives data persistence on screen rotations

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.textViewResult);
        et = findViewById(R.id.editTextInput);
        btn = findViewById(R.id.buttonConvert);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = et.getText().toString();
                if(!amount.isEmpty()) { //the ViewModel gives access to the data and logic
                    viewModel.setAmount(amount);
                    tv.setText(viewModel.getResult());
                }
            }
        });
        //ViewModelProvider is used to get a ViewModel to use by this fragment or activity
        ViewModelProvider provider = new ViewModelProvider(this);
        //request a ViewModel of the type we need (ExchangeViewModel); it's created the first time this runs, reused later on
        viewModel = provider.get(ExchangeViewModel.class);
        //Now the activity can access the data in a ExchangeViewModel instance that survives the activity's lifecycle
    }

    /**
     * Recover existing data from the ViewModel.
     * Without this, the results in the TextView are lost if screen rotates.<p>
     * Try commenting out the <code>tv.setText(viewModel.getResult());</code> line and rotating after getting a result.
     */
    @Override
    protected void onStart() {
        super.onStart();
        //tv.setText(viewModel.getResult());
    }
}