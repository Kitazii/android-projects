package org.me.gcu.fxmate.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import org.me.gcu.fxmate.R;
import org.me.gcu.fxmate.model.CurrencyRate;
import org.me.gcu.fxmate.viewmodel.CurrencyViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "FXMate";
    private CurrencyViewModel viewModel;
    private ProgressBar loadingSpinner;
    private TextView statusTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        loadingSpinner = findViewById(R.id.loadingSpinner);
        statusTextView = findViewById(R.id.statusTextView);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(CurrencyViewModel.class);

        // Setup observers
        setupObservers();

        // Fetch currency data from RSS feed using background thread
        // This triggers the Handler+Thread pattern in CurrencyRepository
        Log.d(TAG, "Initiating currency data fetch...");
        viewModel.fetchCurrencyData();
    }

    /**
     * Setup LiveData observers for ViewModel
     */
    private void setupObservers() {
        // Observe currency rates data
        viewModel.getCurrencyRates().observe(this, currencyRates -> {
            if (currencyRates != null && !currencyRates.isEmpty()) {
                Log.d(TAG, "---------- Received Currency Rates (" + currencyRates.size() + ") ----------");
                for (int i = 0; i < currencyRates.size(); i++) {
                    Log.d(TAG, "#" + (i + 1) + " " + currencyRates.get(i).toString());
                }

                // Update UI with success message
                statusTextView.setText("Successfully loaded " + currencyRates.size() + " currency rates");

                // Example: Get main currencies
                List<CurrencyRate> mainCurrencies = viewModel.getMainCurrencies();
                Log.d(TAG, "---------- Main Currencies (" + mainCurrencies.size() + ") ----------");
                for (CurrencyRate rate : mainCurrencies) {
                    Log.d(TAG, rate.getTargetCode() + ": " + rate.getRate());
                }
            }
        });

        // Observe loading state - control spinner visibility
        viewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading != null) {
                // Show/hide loading spinner based on loading state
                loadingSpinner.setVisibility(isLoading ? View.VISIBLE : View.GONE);

                if (isLoading) {
                    Log.d(TAG, "Loading data...");
                    statusTextView.setText("Loading currency data...");
                } else {
                    Log.d(TAG, "Data loading complete");
                }
            }
        });

        // Observe errors
        viewModel.getErrorMessage().observe(this, errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Log.e(TAG, "Error: " + errorMessage);
                statusTextView.setText("Error: " + errorMessage);
            }
        });
    }
}
