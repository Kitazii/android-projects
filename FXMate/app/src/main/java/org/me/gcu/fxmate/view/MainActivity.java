package org.me.gcu.fxmate.view;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import org.me.gcu.fxmate.R;
import org.me.gcu.fxmate.model.CurrencyRate;
import org.me.gcu.fxmate.view.fragment.SummaryFragment;
import org.me.gcu.fxmate.view.fragment.CurrencyListFragment;
import org.me.gcu.fxmate.view.fragment.CurrencyDetailFragment;
import org.me.gcu.fxmate.viewmodel.CurrencyViewModel;

/**
 * Main Activity implementing Fragment-based architecture
 */
public class MainActivity extends AppCompatActivity
        implements SummaryFragment.SummaryListener,
                   CurrencyListFragment.CurrencyListListener {

    private static final String TAG = "FXMate";
    private CurrencyViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize shared ViewModel
        viewModel = new ViewModelProvider(this).get(CurrencyViewModel.class);

        // Load SummaryFragment on first launch (shows main currencies only)
        if (savedInstanceState == null) {
            loadSummaryFragment();

            // Fetch currency data from RSS feed using background thread
            // This triggers the Handler+Thread pattern in CurrencyRepository
            Log.d(TAG, "Initiating currency data fetch...");
            viewModel.fetchCurrencyData();
        }
    }

    /**
     * Load SummaryFragment into the container (default view)
     */
    private void loadSummaryFragment() {
        Log.d(TAG, "Loading SummaryFragment...");

        SummaryFragment summaryFragment = new SummaryFragment();

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragmentContainer, summaryFragment);
        transaction.commit();
    }

    /**
     * Load CurrencyListFragment into the container
     */
    private void loadCurrencyListFragment() {
        Log.d(TAG, "Loading CurrencyListFragment...");

        CurrencyListFragment listFragment = new CurrencyListFragment();

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragmentContainer, listFragment);
        transaction.addToBackStack(null); // Enable back navigation to Summary
        transaction.commit();
    }

    /**
     * SummaryFragment.SummaryListener implementation
     * Called when user clicks "Show All Currencies" from summary
     */
    @Override
    public void onShowAllCurrencies() {
        Log.d(TAG, "Show all currencies requested");
        loadCurrencyListFragment();
    }

    /**
     * CurrencyListFragment.CurrencyListListener implementation
     * Called when user selects a currency from the list
     * Navigates to CurrencyDetailFragment
     */
    @Override
    public void onCurrencySelected(CurrencyRate selectedRate) {
        Log.d(TAG, "Currency selected: " + selectedRate.getTargetCode());

        // Create detail fragment with selected currency
        CurrencyDetailFragment detailFragment = CurrencyDetailFragment.newInstance(selectedRate);

        // Navigate to detail fragment with back stack
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragmentContainer, detailFragment);
        transaction.addToBackStack(null); // Enable back navigation
        transaction.commit();
    }
}
