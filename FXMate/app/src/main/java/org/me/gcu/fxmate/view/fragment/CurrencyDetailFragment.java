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
import androidx.lifecycle.ViewModelProvider;

import org.me.gcu.fxmate.R;
import org.me.gcu.fxmate.model.CurrencyRate;
import org.me.gcu.fxmate.utils.CurrencyUtils;
import org.me.gcu.fxmate.viewmodel.CurrencyDetailViewModel;

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

    // Data
    private CurrencyRate currencyRate;
    private CurrencyDetailViewModel viewModel;
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

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(CurrencyDetailViewModel.class);
        if (currencyRate != null) {
            viewModel.setCurrencyRate(currencyRate);
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

        // Setup back button
        backButton.setOnClickListener(v -> {
            // Navigate back to currency list
            if (getActivity() != null) {
                getActivity().getOnBackPressedDispatcher().onBackPressed();
            }
        });

        // Setup UI with currency data
        setupCurrencyDisplay();

        // Setup conversion listeners
        setupConversionListeners();

        // Setup swap button
        swapButton.setOnClickListener(v -> swapCurrencies());

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

        // Set actual flag icon
        CurrencyUtils.setFlagIcon(getContext(), currencyFlagImageView, currencyRate.getTargetCode());

        // Display exchange rate
        String formattedRate = CurrencyUtils.formatRateDetailed(currencyRate.getRate());
        exchangeRateTextView.setText(formattedRate);

        // Display timestamp
        String timestamp = "As of " + getCurrentTimestamp();
        timestampTextView.setText(timestamp);

        // Set initial currency codes
        updateCurrencyLabels();

        // Set default amount
        topAmountEditText.setText("100");

        // Set color indicator based on rate
        if (getContext() != null) {
            CurrencyUtils.setColorIndicatorGradient(getContext(), colorIndicatorBar, currencyRate.getRate());
        }
    }

    /**
     * Update currency code labels based on swap state
     */
    private void updateCurrencyLabels() {
        topCurrencyCodeTextView.setText(viewModel.getTopCurrencyCode());
        bottomCurrencyCodeTextView.setText(viewModel.getBottomCurrencyCode());
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
        String topText = topAmountEditText.getText().toString();
        String result = viewModel.convertTopToBottom(topText);

        isUpdating = true;
        bottomAmountEditText.setText(result);
        isUpdating = false;
    }

    /**
     * Convert bottom amount to top amount
     */
    private void convertBottomToTop() {
        String bottomText = bottomAmountEditText.getText().toString();
        String result = viewModel.convertBottomToTop(bottomText);

        isUpdating = true;
        topAmountEditText.setText(result);
        isUpdating = false;
    }

    /**
     * Swap conversion direction
     */
    private void swapCurrencies() {
        // Swap the amounts via ViewModel
        viewModel.swapCurrencies();
        updateCurrencyLabels();

        // Update UI with swapped amounts
        String topText = topAmountEditText.getText().toString();
        String bottomText = bottomAmountEditText.getText().toString();

        isUpdating = true;
        topAmountEditText.setText(bottomText);
        bottomAmountEditText.setText(topText);
        isUpdating = false;

        Log.d(TAG, "Currencies swapped. isSwapped: " + viewModel.isSwapped());
    }

    /**
     * Get current timestamp formatted as "dd/MM/yyyy HH:mm"
     */
    private String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.UK);
        return sdf.format(new Date());
    }
}
