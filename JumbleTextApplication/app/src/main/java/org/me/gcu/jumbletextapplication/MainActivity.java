package org.me.gcu.jumbletextapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText inputText;
    private TextView outputText;
    private Button reverseButton;
    private final ReverseWords reverser = new ReverseWords();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputText = findViewById(R.id.inputText);
        outputText = findViewById(R.id.outputText);
        reverseButton = findViewById(R.id.reverseButton);
        reverseButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == reverseButton)
        {
            String inputString = inputText.getText().toString();
            String outputString = reverser.reverseWords(inputString);
            outputText.setText(outputString);
        }
    }
}