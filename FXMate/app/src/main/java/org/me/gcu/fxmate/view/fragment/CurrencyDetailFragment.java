package org.me.gcu.fxmate.view.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.me.gcu.fxmate.R;
import org.me.gcu.fxmate.model.CurrencyRate;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Fragment displaying currency conversion calculator
 * Allows bidirectional conversion between GBP and selected currency
 */
public class CurrencyDetailFragment extends Fragment {

    private static final String TAG = "CurrencyDetailFragment";
    private static final String ARG_CURRENCY_RATE = "currency_rate";

    // UI Components
    private ImageButton backButton;
    private ImageView currencyFlagImageView;
    private TextView currencyNameTextView;
    private TextView exchangeRateTextView;
    private TextView timestampTextView;
    private EditText topAmountEditText;
    private TextView topCurrencyCodeTextView;
    private ImageButton swapButton;
    private EditText bottomAmountEditText;
    private TextView bottomCurrencyCodeTextView;
    private View colorIndicatorBar;
    private Button viewOnMapButton;

    // Data
    private CurrencyRate currencyRate;
    private boolean isSwapped = false; // Track conversion direction
    private boolean isUpdating = false; // Prevent infinite loop in TextWatchers

    /**
     * Factory method to create fragment with currency rate argument
     */
    public static CurrencyDetailFragment newInstance(CurrencyRate rate) {
        CurrencyDetailFragment fragment = new CurrencyDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CURRENCY_RATE, rate);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currencyRate = (CurrencyRate) getArguments().getSerializable(ARG_CURRENCY_RATE);
            Log.d(TAG, "Currency rate loaded: " + currencyRate.getTargetCode());
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_currency_detail, container, false);

        // Initialize UI components
        backButton = view.findViewById(R.id.backButton);
        currencyFlagImageView = view.findViewById(R.id.currencyFlagImageView);
        currencyNameTextView = view.findViewById(R.id.currencyNameTextView);
        exchangeRateTextView = view.findViewById(R.id.exchangeRateTextView);
        timestampTextView = view.findViewById(R.id.timestampTextView);
        topAmountEditText = view.findViewById(R.id.topAmountEditText);
        topCurrencyCodeTextView = view.findViewById(R.id.topCurrencyCodeTextView);
        swapButton = view.findViewById(R.id.swapButton);
        bottomAmountEditText = view.findViewById(R.id.bottomAmountEditText);
        bottomCurrencyCodeTextView = view.findViewById(R.id.bottomCurrencyCodeTextView);
        colorIndicatorBar = view.findViewById(R.id.colorIndicatorBar);
        viewOnMapButton = view.findViewById(R.id.viewOnMapButton);

        // Setup back button
        backButton.setOnClickListener(v -> {
            // Navigate back to currency list
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });

        // Setup UI with currency data
        setupCurrencyDisplay();

        // Setup conversion listeners
        setupConversionListeners();

        // Setup swap button
        swapButton.setOnClickListener(v -> swapCurrencies());

        // Placeholder for "View on map" button
        viewOnMapButton.setOnClickListener(v -> {
            // TODO: Implement map view functionality
            Log.d(TAG, "View on map clicked for " + currencyRate.getTargetCode());
        });

        Log.d(TAG, "CurrencyDetailFragment view created");
        return view;
    }

    /**
     * Setup currency display with rate information
     */
    private void setupCurrencyDisplay() {
        if (currencyRate == null) return;

        // Set currency name and flag
        String currencyName = currencyRate.getTargetCode() + " - " + currencyRate.getTargetCurrency();
        currencyNameTextView.setText(currencyName);

        // Placeholder flag icon
        currencyFlagImageView.setImageResource(android.R.drawable.ic_menu_mapmode);

        // Display exchange rate
        String formattedRate = formatRate(currencyRate.getRate());
        exchangeRateTextView.setText(formattedRate);

        // Display timestamp
        String timestamp = "As of " + getCurrentTimestamp();
        timestampTextView.setText(timestamp);

        // Set initial currency codes
        updateCurrencyLabels();

        // Set default amount
        topAmountEditText.setText("100");

        // Set color indicator based on rate
        setColorIndicator(currencyRate.getRate());
    }

    /**
     * Update currency code labels based on swap state
     */
    private void updateCurrencyLabels() {
        if (isSwapped) {
            topCurrencyCodeTextView.setText(currencyRate.getTargetCode());
            bottomCurrencyCodeTextView.setText(currencyRate.getBaseCode());
        } else {
            topCurrencyCodeTextView.setText(currencyRate.getBaseCode());
            bottomCurrencyCodeTextView.setText(currencyRate.getTargetCode());
        }
    }

    /**
     * Setup text change listeners for bidirectional conversion
     */
    private void setupConversionListeners() {
        // Top amount changes -> update bottom amount
        topAmountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isUpdating) {
                    convertTopToBottom();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Bottom amount changes -> update top amount
        bottomAmountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isUpdating) {
                    convertBottomToTop();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    /**
     * Convert top amount to bottom amount
     */
    private void convertTopToBottom() {
        String topText = topAmountEditText.getText().toString().replace(',', '.');
        if (topText.isEmpty()) {
            bottomAmountEditText.setText("");
            return;
        }

        try {
            double topAmount = Double.parseDouble(topText);
            double result;

            if (isSwapped) {
                // Converting from target currency to GBP (divide by rate)
                result = topAmount / currencyRate.getRate();
            } else {
                // Converting from GBP to target currency (multiply by rate)
                result = topAmount * currencyRate.getRate();
            }

            isUpdating = true;
            bottomAmountEditText.setText(formatAmount(result));
            isUpdating = false;

        } catch (NumberFormatException e) {
            Log.e(TAG, "Invalid number format: " + topText);
        }
    }

    /**
     * Convert bottom amount to top amount
     */
    private void convertBottomToTop() {
        String bottomText = bottomAmountEditText.getText().toString().replace(',', '.');
        if (bottomText.isEmpty()) {
            topAmountEditText.setText("");
            return;
        }

        try {
            double bottomAmount = Double.parseDouble(bottomText);
            double result;

            if (isSwapped) {
                // Converting from GBP to target currency (multiply by rate)
                result = bottomAmount * currencyRate.getRate();
            } else {
                // Converting from target currency to GBP (divide by rate)
                result = bottomAmount / currencyRate.getRate();
            }

            isUpdating = true;
            topAmountEditText.setText(formatAmount(result));
            isUpdating = false;

        } catch (NumberFormatException e) {
            Log.e(TAG, "Invalid number format: " + bottomText);
        }
    }

    /**
     * Swap conversion direction
     */
    private void swapCurrencies() {
        isSwapped = !isSwapped;
        updateCurrencyLabels();

        // Swap the amounts
        String topText = topAmountEditText.getText().toString();
        String bottomText = bottomAmountEditText.getText().toString();

        isUpdating = true;
        topAmountEditText.setText(bottomText);
        bottomAmountEditText.setText(topText);
        isUpdating = false;

        Log.d(TAG, "Currencies swapped. isSwapped: " + isSwapped);
    }

    /**
     * Format exchange rate using European format (comma as decimal separator)
     */
    private String formatRate(double rate) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.GERMAN);
        symbols.setDecimalSeparator(',');
        DecimalFormat df = new DecimalFormat("0.000", symbols);
        return df.format(rate);
    }

    /**
     * Format amount for display (2 decimal places, comma separator)
     */
    private String formatAmount(double amount) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.GERMAN);
        symbols.setDecimalSeparator(',');
        DecimalFormat df = new DecimalFormat("0.00", symbols);
        return df.format(amount);
    }

    /**
     * Get current timestamp formatted as "dd/MM/yyyy HH:mm"
     */
    private String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.UK);
        return sdf.format(new Date());
    }

    /**
     * Set color indicator bar based on exchange rate
     * Uses color resources from colors.xml following lab pattern
     */
    private void setColorIndicator(double rate) {
        int[] colors;

        if (rate >= 100) {
            // Very High - Red gradient
            colors = new int[]{
                getResources().getColor(R.color.gradient_very_high_start, null),
                getResources().getColor(R.color.gradient_very_high_mid, null),
                getResources().getColor(R.color.gradient_very_high_end, null)
            };
        } else if (rate >= 10) {
            // High - Orange/Red gradient
            colors = new int[]{
                getResources().getColor(R.color.gradient_high_start, null),
                getResources().getColor(R.color.gradient_high_mid, null),
                getResources().getColor(R.color.gradient_high_end, null)
            };
        } else if (rate >= 2) {
            // Medium - Yellow gradient
            colors = new int[]{
                getResources().getColor(R.color.gradient_medium_start, null),
                getResources().getColor(R.color.gradient_medium_mid, null),
                getResources().getColor(R.color.gradient_medium_end, null)
            };
        } else if (rate >= 1) {
            // Low - Light Green gradient
            colors = new int[]{
                getResources().getColor(R.color.gradient_low_start, null),
                getResources().getColor(R.color.gradient_low_mid, null),
                getResources().getColor(R.color.gradient_low_end, null)
            };
        } else {
            // Very Low - Dark Green gradient
            colors = new int[]{
                getResources().getColor(R.color.gradient_very_low_start, null),
                getResources().getColor(R.color.gradient_very_low_mid, null),
                getResources().getColor(R.color.gradient_very_low_end, null)
            };
        }

        // Create gradient drawable
        android.graphics.drawable.GradientDrawable gradient =
            new android.graphics.drawable.GradientDrawable(
                android.graphics.drawable.GradientDrawable.Orientation.LEFT_RIGHT, colors);
        colorIndicatorBar.setBackground(gradient);
    }
}
