package org.me.gcu.lab4_pizza_app;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import androidx.appcompat.app.AppCompatActivity;

import org.me.gcu.lab4_pizza_app.utility.EmailHandler;
/*
* Objectives:
* create 2 pages 1 for entering email before going to the main page
* main page:
* #choice of crust (one only)
* #choice of toppings (as many)
* #extra cheese (yes or no)
* */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewSwitcher avw;
    private Button s1Button;
    private Button s2Button;
    private EditText emailInput;
    private EmailHandler emailHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailHandler = new EmailHandler(this);

        Log.e(getPackageName(), "just before the view switcher");
        avw = findViewById(R.id.vwSwitch);
        if (avw == null)
        {
            Toast.makeText(getApplicationContext(), "Null ViewSwicther", Toast.LENGTH_LONG).show();
            Log.e(getPackageName(), "null pointer");
        }

        s1Button = findViewById(R.id.screen1Button);
        s2Button = findViewById(R.id.screen2Button);
        emailInput = findViewById(R.id.emailInput);

        s1Button.setOnClickListener(this);
        s2Button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        if (v == s1Button)
        {
            String email = emailInput.getText().toString().trim();

            if (emailHandler.inputCheck(email))
            {
                avw.showNext();
            }
        }
        else if (v == s2Button)
        {
            avw.showPrevious();
        }
        else
        {
            emailHandler.showEmailError("Error", "Please enter a valid email address.");
        }
    }
}