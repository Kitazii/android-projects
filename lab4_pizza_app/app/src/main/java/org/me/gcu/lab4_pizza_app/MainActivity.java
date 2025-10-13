package org.me.gcu.lab4_pizza_app;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

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

    // Pizza customization UI elements
    private RadioGroup crustRadioGroup;
    private RadioButton radioStuffed;
    private RadioButton radioThin;
    private CheckBox checkboxMushrooms;
    private CheckBox checkboxSweetcorn;
    private CheckBox checkboxOnions;
    private CheckBox checkboxPeppers;
    private SwitchCompat switchExtraCheese;

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

        // Initialize pizza customization UI elements
        crustRadioGroup = findViewById(R.id.sizeRadioGroup);
        radioStuffed = findViewById(R.id.radioStuffed);
        radioThin = findViewById(R.id.radioThin);
        checkboxMushrooms = findViewById(R.id.checkboxMushrooms);
        checkboxSweetcorn = findViewById(R.id.checkboxSweetcorn);
        checkboxOnions = findViewById(R.id.checkboxOnions);
        checkboxPeppers = findViewById(R.id.checkboxPeppers);
        switchExtraCheese = findViewById(R.id.switchExtraCheese);

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
                emailInput.setText("");
            }
        }
        else if (v == s2Button)
        {
            showOrderConfirmationDialog();
        }
        else
        {
            emailHandler.showEmailError("Error", "Please enter a valid email address.");
        }
    }

    private void showOrderConfirmationDialog()
    {
        String orderDetails = getOrderDetails();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Order");
        builder.setMessage("Are you sure you want to place this order?\n\n" + orderDetails);

        builder.setPositiveButton("Yes", (dialog, which) -> {
            // Show confirmation toast
            Toast.makeText(getApplicationContext(), "Order placed successfully!", Toast.LENGTH_SHORT).show();

            // Go back to previous screen
            avw.showPrevious();
        });

        builder.setNegativeButton("No", (dialog, which) -> {
            // Just dismiss the dialog
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private String getOrderDetails()
    {
        StringBuilder orderDetails = new StringBuilder("Pizza Order:\n");

        // Get crust type
        int selectedCrustId = crustRadioGroup.getCheckedRadioButtonId();
        if (selectedCrustId != -1)
        {
            RadioButton selectedCrust = findViewById(selectedCrustId);
            orderDetails.append("Crust: ").append(selectedCrust.getText()).append("\n");
        }
        else
        {
            orderDetails.append("Crust: None selected\n");
        }

        // Get toppings
        orderDetails.append("Toppings: ");
        StringBuilder toppings = new StringBuilder();
        if (checkboxMushrooms.isChecked())
            toppings.append("Mushrooms, ");
        if (checkboxSweetcorn.isChecked())
            toppings.append("Sweetcorn, ");
        if (checkboxOnions.isChecked())
            toppings.append("Onions, ");
        if (checkboxPeppers.isChecked())
            toppings.append("Peppers, ");

        if (toppings.length() > 0)
        {
            // Remove trailing comma and space
            toppings.setLength(toppings.length() - 2);
            orderDetails.append(toppings);
        }
        else
        {
            orderDetails.append("None");
        }
        orderDetails.append("\n");

        // Get extra cheese
        orderDetails.append("Extra Cheese: ");
        orderDetails.append(switchExtraCheese.isChecked() ? "Yes" : "No");

        return orderDetails.toString();
    }
}