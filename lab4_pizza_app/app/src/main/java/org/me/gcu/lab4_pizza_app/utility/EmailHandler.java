package org.me.gcu.lab4_pizza_app.utility;

import android.content.Context;
import android.util.Patterns;

import androidx.appcompat.app.AlertDialog;

public class EmailHandler
{
    private Context context;

    public EmailHandler(Context context)
    {
        this.context = context;
    }

    public boolean isValidEmail(String email)
    {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void showEmailError(String title, String message)
    {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public boolean inputCheck(String email)
    {
        if (email.isEmpty())
        {
            showEmailError("Email Required", "Please enter your email address to continue.");
        }
        else if (!isValidEmail(email))
        {
            showEmailError("Invalid Email", "Please enter a valid email address format (e.g., user@example.com).");
        }
        else
        {
            return true;
        }

        return false;
    }
}
