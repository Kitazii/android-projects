package org.me.gcu.calculator_lab2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText inputLength;
    private EditText inputWidth;
    private Button btnCalculate;
    private TextView txtResult;
    private final CalculateArea calculateArea = new CalculateArea();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputLength = findViewById(R.id.inputLength);
        inputWidth = findViewById(R.id.inputWidth);
        txtResult = findViewById(R.id.txtResult);
        btnCalculate = findViewById(R.id.btnCalculate);

        btnCalculate.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == btnCalculate)
        {
            String lengthStr = inputLength.getText().toString().trim();
            String widthStr  = inputWidth.getText().toString().trim();

            emptyTextFields(lengthStr, widthStr);

            try{
                float length = Float.parseFloat(lengthStr);
                float width = Float.parseFloat(widthStr);
                float area = calculateArea.calculateArea(length,width);

                txtResult.setText(String.format(Locale.getDefault(), "Area: %.2f", area));
            } catch (NumberFormatException ex) {
                // Handle invalid number formats (e.g., multiple dots, letters)
                Toast.makeText(this, "Invalid number format", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void emptyTextFields(String lengthStr, String widthStr)
    {
        if (lengthStr.isEmpty()) {
            inputLength.setError("Please enter a length");
        }
        if (widthStr.isEmpty()) {
            inputWidth.setError("Please enter a width");
        }
    }
}