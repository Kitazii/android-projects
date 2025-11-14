package org.me.gcu.fxmate.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.me.gcu.fxmate.model.CurrencyRate;
import org.me.gcu.fxmate.repository.CurrencyRepository;

import java.util.List;

public class CurrencyViewModel extends ViewModel {

    private static final String TAG = "CurrencyViewModel";

    private final CurrencyRepository repository;
    private final MutableLiveData<List<CurrencyRate>> currencyRates;
    private final MutableLiveData<Boolean> isLoading;
    private final MutableLiveData<String> errorMessage;

    // Guard flag to prevent multiple simultaneous fetches
    private boolean isFetching = false;

    public CurrencyViewModel() {
        repository = CurrencyRepository.getInstance();
        currencyRates = new MutableLiveData<>();
        isLoading = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
    }

    public LiveData<List<CurrencyRate>> getCurrencyRates() {
        return currencyRates;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    /**
     * Fetches currency data from RSS feed using background thread
     * This method uses the Repository's Handler pattern implementation
     * The callback will be invoked on the main thread, making it safe to update LiveData
     */
    public void fetchCurrencyData() {
        // Guard: prevent multiple simultaneous fetches
        if (isFetching) {
            Log.w(TAG, "Fetch already in progress, ignoring duplicate request");
            return;
        }

        // Also skip if we already have data
        if (currencyRates.getValue() != null && !currencyRates.getValue().isEmpty()) {
            Log.d(TAG, "Data already loaded (" + currencyRates.getValue().size() + " rates), skipping fetch");
            return;
        }

        isFetching = true;
        isLoading.setValue(true);
        errorMessage.setValue(null);

        Log.d(TAG, "Requesting currency data from repository...");

        // Call repository method which handles threading internally
        repository.fetchAndParseRates(new CurrencyRepository.DataCallback() {
            @Override
            public void onDataLoaded(List<CurrencyRate> rates) {
                // This runs on main thread thanks to Handler.post() in repository
                Log.d(TAG, "Successfully received " + rates.size() + " currency rates");
                currencyRates.setValue(rates);
                isLoading.setValue(false);
                isFetching = false;
            }

            @Override
            public void onError(String error) {
                // This runs on main thread thanks to Handler.post() in repository
                Log.e(TAG, "Error loading currency data: " + error);
                errorMessage.setValue(error);
                isLoading.setValue(false);
                isFetching = false;
            }
        });
    }

    /**
     * Loads and parses currency data from XML string (for testing purposes)
     * @param xmlData XML string containing RSS feed data
     */
    public void loadCurrencyData(String xmlData) {
        isLoading.setValue(true);
        errorMessage.setValue(null);

        try {
            Log.d(TAG, "Starting to parse currency data...");
            List<CurrencyRate> rates = repository.parseRates(xmlData);

            if (rates != null && !rates.isEmpty()) {
                currencyRates.setValue(rates);
                Log.d(TAG, "Successfully loaded " + rates.size() + " currency rates");
            } else {
                errorMessage.setValue("No currency data found");
                Log.w(TAG, "No currency rates parsed from XML");
            }
        } catch (Exception e) {
            errorMessage.setValue("Error parsing currency data: " + e.getMessage());
            Log.e(TAG, "Error loading currency data", e);
        } finally {
            isLoading.setValue(false);
        }
    }

    /**
     * Filters currency rates by search query (currency name, code, or country)
     * @param query Search query
     * @return Filtered list of currency rates
     */
    public List<CurrencyRate> searchCurrencies(String query) {
        List<CurrencyRate> allRates = currencyRates.getValue();
        if (allRates == null || query == null || query.trim().isEmpty()) {
            return allRates;
        }

        String searchQuery = query.toLowerCase().trim();
        List<CurrencyRate> filtered = new java.util.ArrayList<>();

        for (CurrencyRate rate : allRates) {
            if (rate.getTargetCurrency().toLowerCase().contains(searchQuery) ||
                rate.getTargetCode().toLowerCase().contains(searchQuery) ||
                rate.getTitle().toLowerCase().contains(searchQuery)) {
                filtered.add(rate);
            }
        }

        Log.d(TAG, "Search for '" + query + "' returned " + filtered.size() + " results");
        return filtered;
    }

    /**
     * Gets main currencies (USD, EUR, JPY)
     * @return List of main currency rates
     */
    public List<CurrencyRate> getMainCurrencies() {
        List<CurrencyRate> allRates = currencyRates.getValue();
        if (allRates == null) {
            return new java.util.ArrayList<>();
        }

        List<CurrencyRate> mainCurrencies = new java.util.ArrayList<>();
        for (CurrencyRate rate : allRates) {
            String code = rate.getTargetCode();
            if ("USD".equals(code) || "EUR".equals(code) || "JPY".equals(code)) {
                mainCurrencies.add(rate);
            }
        }

        Log.d(TAG, "Retrieved " + mainCurrencies.size() + " main currencies");
        return mainCurrencies;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "ViewModel cleared");
    }
}
