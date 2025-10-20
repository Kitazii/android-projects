package org.me.gcu.handlermessage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private String MSG_KEY = "";
    private TextView myTextView;
    private TextView changeTextEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myTextView = (TextView)findViewById(R.id.myTextView);
        changeTextEditor = (TextView)findViewById(R.id.changeTextEditor);

        Button button = (Button) findViewById(R.id.myButton);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        // When the single button is clicked
        // Start a new thread to do some "heavy" processing
        new Thread(mMessageSender).start();
    }

    private final Runnable mMessageSender = new Runnable()
    {
        // This is the "long running" activity
        // It is instigated by a button click
        public void run()
        {
            Message msg = mHandler.obtainMessage();
            Bundle bundle = new Bundle();
            // This is the complex "processing"
            // It involves calculating the area of a circle
            // given the radius
            // Package answer of calculation into a Bundle
            bundle.putString(MSG_KEY, calculateAreaOfCircle());
            // Task done, bundle aswer up into a message
            msg.setData(bundle);
            mHandler.sendMessage(msg);
        }
    };

    // This is the handler object
    private final Handler mHandler = new Handler(Looper.myLooper())
    {
        @Override
        public void handleMessage(Message msg)
        {
            // Unbundle the message and get it as a string
            Bundle bundle = msg.getData();
            String string = bundle.getString(MSG_KEY);
            // Now display the message text in a TextView
            myTextView.setText(string);
        }
    };

    private String calculateAreaOfCircle()
    {
        String answer = "";
        String userInputText = changeTextEditor.getText().toString();
        try
        {
            double radius = Double.parseDouble(userInputText);
            double area = 3.14 * radius *radius;
            answer = "Area is " + area;
        }
        catch (NumberFormatException ae)
        {
            answer = "Conversion error";
        }

        return answer;
    }

} // End of Class