package org.me.gcu.fxmate.view;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import org.me.gcu.fxmate.R;
import org.me.gcu.fxmate.model.CurrencyRate;
import org.me.gcu.fxmate.viewmodel.CurrencyViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "FXMate";
    private CurrencyViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(CurrencyViewModel.class);

        // Setup observers
        setupObservers();

        // TODO: Replace with actual XML data from network fetch
        // For now, testing with sample data
        String sampleXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<rss version=\"2.0\">" +
                "<channel>" +
                "<title>British Pound Sterling(GBP) Currency Exchange Rates</title>" +
                "<link>https://www.fx-exchange.com/gbp/</link>" +
                "<description>British Pound Sterling(GBP) Currency Exchange Rates</description>" +
                "<pubDate>Mon Oct 20 2025 6:01:00 UTC</pubDate>" +
                "<item>" +
                "<title>British Pound Sterling(GBP)/United Arab Emirates Dirham(AED)</title>" +
                "<link>https://www.fx-exchange.com/gbp/aed.html</link>" +
                "<pubDate>Mon Oct 20 2025 6:01:00 UTC</pubDate>" +
                "<description>1 British Pound Sterling = 4.9354 United Arab Emirates Dirham</description>" +
                "</item>" +
                "<item>" +
                "<title>British Pound Sterling(GBP)/United States Dollar(USD)</title>" +
                "<link>https://www.fx-exchange.com/gbp/usd.html</link>" +
                "<pubDate>Mon Oct 20 2025 6:01:00 UTC</pubDate>" +
                "<description>1 British Pound Sterling = 1.3436 United States Dollar</description>" +
                "</item>" +
                "<item>" +
                "<title>British Pound Sterling(GBP)/Euro(EUR)</title>" +
                "<link>https://www.fx-exchange.com/gbp/eur.html</link>" +
                "<pubDate>Mon Oct 20 2025 6:01:00 UTC</pubDate>" +
                "<description>1 British Pound Sterling = 1.1678 Euro</description>" +
                "</item>" +
                "<item>" +
                "<title>British Pound Sterling(GBP)/Japanese Yen(JPY)</title>" +
                "<link>https://www.fx-exchange.com/gbp/jpy.html</link>" +
                "<pubDate>Mon Oct 20 2025 6:01:00 UTC</pubDate>" +
                "<description>1 British Pound Sterling = 149.8234 Japanese Yen</description>" +
                "</item>" +
                "</channel>" +
                "</rss>";

        // Load data through ViewModel
        viewModel.loadCurrencyData(sampleXML);
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

                // Example: Get main currencies
                List<CurrencyRate> mainCurrencies = viewModel.getMainCurrencies();
                Log.d(TAG, "---------- Main Currencies (" + mainCurrencies.size() + ") ----------");
                for (CurrencyRate rate : mainCurrencies) {
                    Log.d(TAG, rate.getTargetCode() + ": " + rate.getRate());
                }
            }
        });

        // Observe loading state
        viewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading != null) {
                Log.d(TAG, isLoading ? "Loading data..." : "Data loading complete");
            }
        });

        // Observe errors
        viewModel.getErrorMessage().observe(this, errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Log.e(TAG, "Error: " + errorMessage);
            }
        });
    }
}
