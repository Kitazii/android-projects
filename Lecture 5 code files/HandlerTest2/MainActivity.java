package org.me.gcu.handlertest2;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.os.Handler;

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
    @Override
    public void onClick(View v)
    {
		mHandler=new Handler(); //Handler to update UI
        // Create a Thread to handle "long-running colculation"
        // Use Handler to post
        new Thread(new Runnable() {
            @Override
            // This is where you implement cod for the task
            public void run()
            {
                // a potentially time consuming task
                result = calculate() ;
				// Update the value from worker thread to UI thread
                // This is done in a further short-life thread
                // to ensure the is no blocking of the "calculation" thread
				mHandler.post(new Runnable() {
                    @Override
                    public void run(){
                        displayAnswerTextView.setText(result);
                    }
                });
            }
        }).start();
    } // End of onClick
} // End of class
