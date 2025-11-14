package org.me.gcu.testapplication2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button exitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //set as local variables as it's only being used as such
        TextView salutation;
        View mainView;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainView = findViewById(R.id.mainView);
        salutation = findViewById(R.id.salutation);
        exitButton = findViewById(R.id.exitButton);

        mainView.setBackgroundColor(getResources().getColor(R.color.DarkGray,null));
        salutation.setTextColor(getResources().getColor(R.color.LightYellow,null));
        exitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // Check for exit button. Pop up dialogue if found
        if (v == exitButton)
        {
            showDialog();
        }
    }
    private void showDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", (dialog, id) -> {
            Toast.makeText(getApplicationContext(), "You Pressed Yes", Toast.LENGTH_SHORT).show();
            MainActivity.this.finish();
        });
        builder.setNegativeButton("No", (dialog, id) -> {
            Toast.makeText(getApplicationContext(), "You Pressed No", Toast.LENGTH_SHORT).show();
            dialog.cancel();
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}