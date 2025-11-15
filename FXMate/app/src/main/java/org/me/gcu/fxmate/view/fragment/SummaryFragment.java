package org.me.gcu.fxmate.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.me.gcu.fxmate.R;
import org.me.gcu.fxmate.model.CurrencyRate;
import org.me.gcu.fxmate.viewmodel.CurrencyViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Fragment displaying summary of main currencies (USD, EUR, JPY)
 * Follows the mockup design with color-coded exchange rates
 */
public class SummaryFragment extends Fragment {

    private static final String TAG = "SummaryFragment";

    // Interface for communication with MainActivity
    public interface SummaryListener {
        void onShowAllCurrencies();
    }

    private SummaryListener listener;
    private CurrencyViewModel viewModel;

    // UI Components
    private TextView updatedTimestamp;
    private TextView usdName, usdRate;
    private TextView eurName, eurRate;
    private TextView jpyName, jpyRate;
    private com.google.android.material.button.MaterialButton showAllButton;
    private ProgressBar loadingSpinner;
    private TextView statusTextView;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Set up listener
        if (context instanceof SummaryListener) {
            listener = (SummaryListener) context;
            Log.d(TAG, "SummaryListener attached");
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement SummaryFragment.SummaryListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
        Log.d(TAG, "SummaryListener detached");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_summary, container, false);

        // Initialize UI components
        updatedTimestamp = view.findViewById(R.id.updatedTimestamp);
        usdName = view.findViewById(R.id.usdName);
        usdRate = view.findViewById(R.id.usdRate);
        eurName = view.findViewById(R.id.eurName);
        eurRate = view.findViewById(R.id.eurRate);
        jpyName = view.findViewById(R.id.jpyName);
        jpyRate = view.findViewById(R.id.jpyRate);
        showAllButton = view.findViewById(R.id.showAllButton);
        loadingSpinner = view.findViewById(R.id.loadingSpinner);
        statusTextView = view.findViewById(R.id.statusTextView);

        // Set up "Show All" button
        showAllButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onShowAllCurrencies();
            }
        });

        Log.d(TAG, "SummaryFragment view created");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get shared ViewModel from Activity scope
        viewModel = new ViewModelProvider(requireActivity()).get(CurrencyViewModel.class);

        // Setup observers for LiveData
        setupObservers();

        Log.d(TAG, "SummaryFragment observers set up");
    }

    /**
     * Setup LiveData observers
     */
    private void setupObservers() {
        // Observe currency rates data
        viewModel.getCurrencyRates().observe(getViewLifecycleOwner(), currencyRates -> {
            // Safety check: ensure Fragment is still attached
            if (!isAdded() || getView() == null) {
                Log.w(TAG, "Fragment not attached, skipping UI update");
                return;
            }

            if (currencyRates != null && !currencyRates.isEmpty()) {
                Log.d(TAG, "Received " + currencyRates.size() + " currency rates");

                // Extract main currencies
                List<CurrencyRate> mainCurrencies = viewModel.getMainCurrencies();
                Log.d(TAG, "Found " + mainCurrencies.size() + " main currencies");

                // Populate summary view
                populateSummaryView(mainCurrencies);

                // Update timestamp
                updateTimestamp();

                // Hide status, show content
                if (statusTextView != null) {
                    statusTextView.setVisibility(View.GONE);
                }
            }
        });

        // Observe loading state
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (!isAdded() || getView() == null) {
                return;
            }

            if (isLoading != null && loadingSpinner != null) {
                loadingSpinner.setVisibility(isLoading ? View.VISIBLE : View.GONE);

                if (isLoading && statusTextView != null) {
                    statusTextView.setText("Loading currency data...");
                    statusTextView.setVisibility(View.VISIBLE);
                }
            }
        });

        // Observe errors
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (!isAdded() || getView() == null) {
                return;
            }

            if (errorMessage != null && !errorMessage.isEmpty()) {
                Log.e(TAG, "Error: " + errorMessage);

                if (statusTextView != null) {
                    statusTextView.setText("Error: " + errorMessage);
                    statusTextView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    /**
     * Populate the summary view with main currency data
     */
    private void populateSummaryView(List<CurrencyRate> mainCurrencies) {
        // Find each currency
        CurrencyRate usdCurrency = null, eurCurrency = null, jpyCurrency = null;

        for (CurrencyRate rate : mainCurrencies) {
            String code = rate.getTargetCode();
            if ("USD".equals(code)) usdCurrency = rate;
            else if ("EUR".equals(code)) eurCurrency = rate;
            else if ("JPY".equals(code)) jpyCurrency = rate;
        }

        // Populate USD
        if (usdCurrency != null) {
            usdName.setText(usdCurrency.getTargetCurrency());
            usdRate.setText(String.format("1 GBP = %s %s",
                formatRate(usdCurrency.getRate()), usdCurrency.getTargetCode()));
            // Set color based on strength
            usdRate.setBackgroundColor(getColorForRate(usdCurrency.getRate()));
        }

        // Populate EUR
        if (eurCurrency != null) {
            eurName.setText(eurCurrency.getTargetCurrency());
            eurRate.setText(String.format("1 GBP = %s %s",
                formatRate(eurCurrency.getRate()), eurCurrency.getTargetCode()));
            eurRate.setBackgroundColor(getColorForRate(eurCurrency.getRate()));
        }

        // Populate JPY
        if (jpyCurrency != null) {
            jpyName.setText(jpyCurrency.getTargetCurrency());
            jpyRate.setText(String.format("1 GBP = %s %s",
                formatRate(jpyCurrency.getRate()), jpyCurrency.getTargetCode()));
            jpyRate.setBackgroundColor(getColorForRate(jpyCurrency.getRate()));
        }
    }

    /**
     * Update the timestamp with current time
     */
    private void updateTimestamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm, dd MMM yyyy", Locale.getDefault());
        String timestamp = dateFormat.format(new Date());
        updatedTimestamp.setText("Updated: " + timestamp);
    }

    /**
     * Format rate with appropriate decimal places using period as decimal separator
     */
    private String formatRate(double rate) {
        if (rate >= 100) {
            return String.format("%.1f", rate);
        } else {
            return String.format("%.2f", rate);
        }
    }

    /**
     * Get color for currency rate based on strength
     * Higher rate = weaker target currency (more units needed per GBP) = red
     * Lower rate = stronger target currency (fewer units needed per GBP) = green
     * Matches the color coding from CurrencyAdapter
     */
    private int getColorForRate(double rate) {
        if (rate >= 100) {
            // Very High - Dark Red/Pink (very weak currency)
            return getResources().getColor(R.color.rate_very_high, null);
        } else if (rate >= 10) {
            // High - Light Red/Pink (weak currency)
            return getResources().getColor(R.color.rate_high, null);
        } else if (rate >= 2) {
            // Medium - Light Yellow
            return getResources().getColor(R.color.rate_medium, null);
        } else if (rate >= 1) {
            // Low - Light Green (strong currency)
            return getResources().getColor(R.color.rate_low, null);
        } else {
            // Very Low - Dark Green (very strong currency)
            return getResources().getColor(R.color.rate_very_low, null);
        }
    }
}
