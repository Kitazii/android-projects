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
import org.me.gcu.fxmate.utils.CurrencyUtils;
import org.me.gcu.fxmate.utils.DateUtils;
import org.me.gcu.fxmate.viewmodel.CurrencyViewModel;

import java.util.List;

/**
 * Fragment displaying summary of main currencies (USD, EUR, JPY)
 * Follows the mockup design with color-coded exchange rates
 */
public class SummaryFragment extends Fragment {

    private static final String TAG = "SummaryFragment";

    // Interface for communication with MainActivity
    public interface SummaryListener {
        void onShowAllCurrencies();
        void onCurrencySelected(CurrencyRate selectedRate);
    }

    private SummaryListener listener;
    private CurrencyViewModel viewModel;

    // UI Components
    private TextView updatedTimestamp;
    private android.widget.ImageView usdFlag, eurFlag, jpyFlag;
    private TextView usdName, usdRate;
    private TextView eurName, eurRate;
    private TextView jpyName, jpyRate;
    private android.widget.LinearLayout usdCard, eurCard, jpyCard;
    private com.google.android.material.button.MaterialButton showAllButton;
    private ProgressBar loadingSpinner;
    private TextView statusTextView;

    // Stored currency rate objects for click handling
    private CurrencyRate storedUsdRate, storedEurRate, storedJpyRate;

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
        usdFlag = view.findViewById(R.id.usdFlag);
        usdName = view.findViewById(R.id.usdName);
        usdRate = view.findViewById(R.id.usdRate);
        eurFlag = view.findViewById(R.id.eurFlag);
        eurName = view.findViewById(R.id.eurName);
        eurRate = view.findViewById(R.id.eurRate);
        jpyFlag = view.findViewById(R.id.jpyFlag);
        jpyName = view.findViewById(R.id.jpyName);
        jpyRate = view.findViewById(R.id.jpyRate);
        usdCard = view.findViewById(R.id.usdCard);
        eurCard = view.findViewById(R.id.eurCard);
        jpyCard = view.findViewById(R.id.jpyCard);
        showAllButton = view.findViewById(R.id.showAllButton);
        loadingSpinner = view.findViewById(R.id.loadingSpinner);
        statusTextView = view.findViewById(R.id.statusTextView);

        // Set up "Show All" button
        showAllButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onShowAllCurrencies();
            }
        });

        // Set up currency card click listeners
        usdCard.setOnClickListener(v -> {
            if (listener != null && storedUsdRate != null) {
                listener.onCurrencySelected(storedUsdRate);
            }
        });

        eurCard.setOnClickListener(v -> {
            if (listener != null && storedEurRate != null) {
                listener.onCurrencySelected(storedEurRate);
            }
        });

        jpyCard.setOnClickListener(v -> {
            if (listener != null && storedJpyRate != null) {
                listener.onCurrencySelected(storedJpyRate);
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
        // Find each currency and store references for click handling
        storedUsdRate = null;
        storedEurRate = null;
        storedJpyRate = null;

        for (CurrencyRate rate : mainCurrencies) {
            String code = rate.getTargetCode();
            if ("USD".equals(code)) storedUsdRate = rate;
            else if ("EUR".equals(code)) storedEurRate = rate;
            else if ("JPY".equals(code)) storedJpyRate = rate;
        }

        // Populate USD
        if (storedUsdRate != null && getContext() != null) {
            CurrencyUtils.setFlagIcon(getContext(), usdFlag, storedUsdRate.getTargetCode());
            usdName.setText(storedUsdRate.getTargetCurrency());
            usdRate.setText(String.format("1 GBP = %s %s",
                CurrencyUtils.formatRateSummary(storedUsdRate.getRate()), storedUsdRate.getTargetCode()));
            usdRate.setBackgroundColor(CurrencyUtils.getColorForRate(getContext(), storedUsdRate.getRate()));
        }

        // Populate EUR
        if (storedEurRate != null && getContext() != null) {
            CurrencyUtils.setFlagIcon(getContext(), eurFlag, storedEurRate.getTargetCode());
            eurName.setText(storedEurRate.getTargetCurrency());
            eurRate.setText(String.format("1 GBP = %s %s",
                CurrencyUtils.formatRateSummary(storedEurRate.getRate()), storedEurRate.getTargetCode()));
            eurRate.setBackgroundColor(CurrencyUtils.getColorForRate(getContext(), storedEurRate.getRate()));
        }

        // Populate JPY
        if (storedJpyRate != null && getContext() != null) {
            CurrencyUtils.setFlagIcon(getContext(), jpyFlag, storedJpyRate.getTargetCode());
            jpyName.setText(storedJpyRate.getTargetCurrency());
            jpyRate.setText(String.format("1 GBP = %s %s",
                CurrencyUtils.formatRateSummary(storedJpyRate.getRate()), storedJpyRate.getTargetCode()));
            jpyRate.setBackgroundColor(CurrencyUtils.getColorForRate(getContext(), storedJpyRate.getRate()));
        }
    }

    /**
     * Update the timestamp with current time
     * Uses DateUtils for consistent formatting across the app
     */
    private void updateTimestamp() {
        updatedTimestamp.setText(DateUtils.formatSummaryTimestamp());
    }
}
