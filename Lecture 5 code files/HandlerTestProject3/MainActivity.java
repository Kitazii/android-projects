package org.me.gcu.handlertestproject3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private EditText changeTextEditor;
    private Button performTaskButton;
    private TextView displayAnswerTextView;
    private String result;
    private HandlerThread ht;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // User input text editor.
        changeTextEditor = (EditText)findViewById(R.id.enterNumber);
        // Change text button.
        performTaskButton = (Button)findViewById(R.id.PerformTaskinThreadButton);
        // Show text textview.
        displayAnswerTextView = (TextView)findViewById(R.id.displayAnswer);
        // Click this button to start a child thread.
        performTaskButton.setOnClickListener(this);

    }

    private void doSomethingOnUi(Object response)
    {
        Handler uiThread = new Handler(Looper.getMainLooper());
        uiThread.post(new Runnable() {
            @Override
            public void run() {
                // now update your UI here
                // cast response to whatever you specified earlier
                String message = (String) response;
                Log.e("MyTag",message);
                displayAnswerTextView.setText(message);
            }
        });
    }

    public void doSomeTaskAsync() {
        HandlerThread ht = new HandlerThread("MyHandlerThread");
        ht.start();
        Handler asyncHandler = new Handler(ht.getLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                Object response = msg.obj;
                doSomethingOnUi(response);
            }
        };
        Runnable runnable = new Runnable() {
            @Override
            public void run()
            {
                // your async code goes here.
                Log.e("MyTg","In asynctaskrun");

                // create message and pass any object here doesn't matter
                // for a simple example I have used a simple string
                Message message = new Message();
                message.obj = calculate();

                asyncHandler.sendMessage(message);

            }
        };
        asyncHandler.post(runnable);
    }

    private String calculate()
    {
        // a potentially time consuming task
        String answer = "";
        String userInputText = changeTextEditor.getText().toString();
        try
        {
            double radius = Double.parseDouble(userInputText);
            double area = 3.14 * radius * radius;
            answer = "Area is " + area;
            return answer;
        } catch (NumberFormatException ae)
        {
            answer = "Conversion error";
            return answer;
        }
    }

    @Override
    public void onClick(View v)
    {
        ht = new HandlerThread("MyHandlerThread");
        ht.start();
        Handler handler = new Handler(ht.getLooper());
        Runnable runnable = new Runnable()
        {
            public void run()
            {
                // Async code is entered here
                doSomeTaskAsync();
                Log.e("MyTag","In run");
            }
        };
        handler.post(runnable);
    }
}