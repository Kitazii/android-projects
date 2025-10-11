package org.me.gcu.testapplication3;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button exitButton;
    private EditText nameEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        exitButton = findViewById(R.id.exitButton);
        nameEntry = findViewById(R.id.nameEntry);
        nameEntry.setWidth(120);
        exitButton.setOnClickListener(this);
        nameEntry.setFocusable(true);
    }

    @Override
    public void onClick(View v) {
        String name;
        if (v == exitButton)
        {
            name = nameEntry.getText().toString();
            showDialog(name);
        }
    }

    private void showDialog(String salutationName)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(salutationName + ", Are you sure you want to exit?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", (dialog, which) -> {
            Toast.makeText(getApplicationContext(), "You Pressed Yes", Toast.LENGTH_SHORT).show();
            MainActivity.this.finish();
        });
        builder.setNegativeButton("No", (dialog, which) -> {
            Toast.makeText(getApplicationContext(), "You Pressed No", Toast.LENGTH_SHORT).show();
            dialog.cancel();
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}