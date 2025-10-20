package org.me.gcu.handlertestproject1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{

    private EditText changeTextEditor;
    private Button performTaskButton;
    private TextView displayAnswerTextView;
    // This is the activity main thread Handler.
    private Handler updateUIHandler = null;
    //private String answer = "";
    // Message type code.
    private final static int MESSAGE_UPDATE_TEXT_CHILD_THREAD =1;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Calculation in Thread");
        // Initialize Handler.
        createUpdateUiHandler();
        // User input text editor.
        changeTextEditor = (EditText)findViewById(R.id.enterNumber);
        // Change text button.
        performTaskButton = (Button)findViewById(R.id.PerformTaskinThreadButton);
        // Show text textview.
        displayAnswerTextView = (TextView)findViewById(R.id.displayAnswer);
        // Click this button to start a child thread.
        performTaskButton.setOnClickListener(this);
    }

    public void onClick(View view)
    {
        //When the button is clicked run the method
        if (view == performTaskButton)
        {
            doATaskAsynchronously();
        }
    }

    private void doATaskAsynchronously()
    {
        // Code here will create a separate Thread to do some
        // work that may be time consuming
        Thread workerThread = new Thread()
        {
            @Override
            public void run()
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

                // Can not update ui component directly when child thread run.
                // updateText();
                // Build message object.
                Message message = new Message();
                // Set message type.
                message.what = MESSAGE_UPDATE_TEXT_CHILD_THREAD;
                message.obj = answer;
                // Send message to main thread Handler.
                updateUIHandler.sendMessage(message);
            }
        };
        workerThread.start();

    } // End of method


    /* Update ui text.*/
    private void updateText(String value)
    {
        //String userInputText = changeTextEditor.getText().toString();
        displayAnswerTextView.setText(value);
    }
    /* Create Handler object in main thread. */
    private void createUpdateUiHandler()
    {
        if(updateUIHandler == null)
        {
            updateUIHandler = new Handler(Looper.getMainLooper())
            {
                @Override
                public void handleMessage(Message msg)
                {
                    // Means the message is sent from child thread.
                    if(msg.what == MESSAGE_UPDATE_TEXT_CHILD_THREAD)
                    {
                        // Update ui in main thread.
                        updateText((String) msg.obj);
                    }
                }
            };
        }
    }
} // End of ActivityClass