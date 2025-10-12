package org.me.gcu.labdemos.dialogtestproject;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Dialog;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private Button tbDialogButton;
    private Button thbDialogButton;
    private Button listDialogButton;
    private Button rbDialogButton;
    private Button customdialogButton;
    private Button customdialogScrollButton;
    private View mainView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainView = (View)findViewById(R.id.mainView);
        tbDialogButton = (Button)findViewById(R.id.tbdialogButton);
        thbDialogButton = (Button)findViewById(R.id.thbdialogButton);
        listDialogButton = (Button)findViewById(R.id.listdialogButton);
        rbDialogButton = (Button)findViewById(R.id.rbdialogButton);
        customdialogButton = (Button)findViewById(R.id.customdialogButton);
        customdialogScrollButton = (Button)findViewById(R.id.customdialogScrollButton);
        tbDialogButton.setOnClickListener(this);
        thbDialogButton.setOnClickListener(this);
        listDialogButton.setOnClickListener(this);
        rbDialogButton.setOnClickListener(this);
        customdialogButton.setOnClickListener(this);
        customdialogScrollButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View viewparam)
    {
        // TODO Auto-generated method stub
        switch(viewparam.getId())
        {
            case R.id.tbdialogButton :
                showtbDialog();
                break;
            case R.id.thbdialogButton :
                showthbDialog();
                break;
            case R.id.listdialogButton :
                showlistDialog();
                break;
            case R.id.rbdialogButton :
                showrbDialog();
                break;
            case R.id.customdialogButton :
                showcustomDialog("This is a Custom Dialog");
                break;
            case R.id.customdialogScrollButton :
                showcustomDialogScroll("This is a Custom Dialog with the ability to scroll through the text because it is too big to fit on a single screen. The user can scroll through the info being displayed using the in built scroll bar. Blah blah blah blah blah blah blah");
                break;
        }


    }

    private void showtbDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id)
            {
                Toast.makeText(getApplicationContext(), "You Pressed Yes", Toast.LENGTH_SHORT).show();
                MainActivity.this.finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id)
            {
                Toast.makeText(getApplicationContext(), "You Pressed No", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void showthbDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Save Confirm");
        builder.setMessage("Do you want to save this file");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id)
            {
                Toast.makeText(getApplicationContext(), "You Pressed Yes", Toast.LENGTH_SHORT).show();
                MainActivity.this.finish();
            }
        });
        builder.setNeutralButton("Cancel",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id)
            {
                Toast.makeText(getApplicationContext(), "You Pressed Cancel", Toast.LENGTH_SHORT).show();

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id)
            {
                Toast.makeText(getApplicationContext(), "You Pressed No", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void showlistDialog()
    {
        final String[] colours ={"Red","Green","Blue"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Colour");
        builder.setItems(colours,new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int whichItem)
            {
                Toast.makeText(getApplicationContext(), colours[whichItem], Toast.LENGTH_SHORT).show();
                switch(whichItem)
                {
                    case 0 :
                        mainView.setBackgroundColor(Color.RED);
                        break;
                    case 1 :
                        mainView.setBackgroundColor(Color.GREEN);
                        break;
                    case 2 :
                        mainView.setBackgroundColor(Color.BLUE);
                        break;
                }
            }
        });
        builder.setCancelable(false);

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void showrbDialog()
    {
        final String[] colours ={"Red","Green","Blue"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Colour");
        builder.setSingleChoiceItems(colours, -1, new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int whichItem)
            {
                Toast.makeText(getApplicationContext(), colours[whichItem], Toast.LENGTH_SHORT).show();
                switch(whichItem)
                {
                    case 0 :
                        mainView.setBackgroundColor(Color.RED);
                        break;
                    case 1 :
                        mainView.setBackgroundColor(Color.GREEN);
                        break;
                    case 2 :
                        mainView.setBackgroundColor(Color.BLUE);
                        break;
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void showcustomDialog(String info)
    {
        // Custom dialog setup
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_info);
        dialog.setTitle("Custom Dialog Example");

        // Set the custom dialog components as a TextView and Button component
        TextView text = (TextView) dialog.findViewById(R.id.infoView);
        text.setText(info);
        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        dialogButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();

            }
        });

        dialog.show();
    }

    private void showcustomDialogScroll(String info)
    {
        // Custom dialog setup
        Log.e("Mytag","In scroll dialog");
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_info_scroll);
        dialog.setTitle("Custom Dialog Example With Scrolling");
        Log.e("Mytag","In scroll dialog after set up");
        // Set the custom dialog components as a TextView and Button component
        TextView text = (TextView) dialog.findViewById(R.id.infoViewScroll);
        text.setText(info);
        Button dialogButton = (Button) dialog.findViewById(R.id.finishOK);
        dialogButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();

            }
        });

        dialog.show();
    }


}