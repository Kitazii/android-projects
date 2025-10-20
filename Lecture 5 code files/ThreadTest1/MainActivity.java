package org.me.gcu.threadtest1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

    } //End of onCreate

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

    public void onClick(View v)
    {
        // Create a Thread to handle "long running colculation"
        // Create another thread to update UI component by posting Thread on
        // UI component to be updated
        new Thread(new Runnable()
        {
            public void run()
            {
                // a potentially time consuming task
                result = calculate() ;
                // Now display on UI component
                displayAnswerTextView.post(new Runnable()
                {
                    public void run()
                    {
                        displayAnswerTextView.setText(result);;
                    }
                });
            }
        }).start();

    } // End of onClick

} // End of class
