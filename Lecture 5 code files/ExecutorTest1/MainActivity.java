package org.me.gcu.executortest1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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
    }

    // This is the runnable task that we will run 100 times
    private Runnable runnable = new Runnable()
    {
        @Override
        public void run()
        {
            // Do some work that takes 50 milliseconds
            // a potentially time consuming task
            result = calculate() ;

            // Update the UI with progress
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    displayAnswerTextView.setText(result);
                }
            });

        }
    };

    @Override
    public void onClick(View v)
    {
        Executor mSingleThreadExecutor = Executors.newSingleThreadExecutor();

            mSingleThreadExecutor.execute(runnable);

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
}