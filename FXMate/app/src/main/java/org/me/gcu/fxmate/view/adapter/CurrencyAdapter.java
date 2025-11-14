package org.me.gcu.fxmate.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.me.gcu.fxmate.R;
import org.me.gcu.fxmate.model.CurrencyRate;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

/**
 * RecyclerView Adapter for displaying currency exchange rates
 * Follows MPD_03a1_RecyclerView pattern with ViewHolder
 * Implements color coding based on exchange rate strength
 * Uses color resources from colors.xml following lab pattern
 */
public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.ViewHolder> {

    private List<CurrencyRate> currencyRates;
    private OnItemClickListener clickListener;
    private Context context;

    // Interface for handling item clicks
    public interface OnItemClickListener {
        void onItemClick(CurrencyRate rate);
    }

    // Constructor
    public CurrencyAdapter(Context context, List<CurrencyRate> currencyRates, OnItemClickListener clickListener) {
        this.context = context;
        this.currencyRates = currencyRates;
        this.clickListener = clickListener;
    }

    /**
     * ViewHolder class following RecyclerView pattern
     * Holds references to views for each item
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView flagImageView;
        public TextView currencyPairTextView;
        public TextView rateTextView;
        public View itemContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            flagImageView = itemView.findViewById(R.id.flagImageView);
            currencyPairTextView = itemView.findViewById(R.id.currencyPairTextView);
            rateTextView = itemView.findViewById(R.id.rateTextView);
            itemContainer = itemView;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the currency_item layout
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.currency_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CurrencyRate rate = currencyRates.get(position);

        // Set currency pair text (e.g., "GBP -> USD")
        String currencyPair = rate.getBaseCode() + " -> " + rate.getTargetCode();
        holder.currencyPairTextView.setText(currencyPair);

        // Format and display exchange rate using European format (comma for decimal)
        String formattedRate = formatRate(rate.getRate());
        holder.rateTextView.setText(formattedRate);

        // Apply color coding based on exchange rate strength
        int backgroundColor = getColorForRate(rate.getRate());
        holder.itemContainer.setBackgroundColor(backgroundColor);

        // Set flag icon based on target currency code
        setFlagIcon(holder.flagImageView, rate.getTargetCode());

        // Handle click events
        holder.itemContainer.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onItemClick(rate);
            }
        });
    }

    @Override
    public int getItemCount() {
        return currencyRates.size();
    }

    /**
     * Update adapter data and refresh RecyclerView
     */
    public void updateData(List<CurrencyRate> newRates) {
        this.currencyRates = newRates;
        notifyDataSetChanged();
    }

    /**
     * Format exchange rate using European number format (comma as decimal separator)
     * Examples: 1,24 | 157,8 | 4718,5
     */
    private String formatRate(double rate) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.GERMAN);
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');

        DecimalFormat df = new DecimalFormat("#,##0.##", symbols);
        return df.format(rate);
    }

    /**
     * Determine background color based on exchange rate strength
     * Uses color resources from colors.xml following lab pattern
     * Color coding scale (5 colors as per requirements):
     * - Very High (rate >= 100): Dark Red/Pink
     * - High (rate >= 10): Light Red/Pink
     * - Medium (rate >= 2): Light Yellow
     * - Low (rate >= 1): Light Green
     * - Very Low (rate < 1): Dark Green
     */
    private int getColorForRate(double rate) {
        if (rate >= 100) {
            // Very High - Dark Red/Pink
            return context.getResources().getColor(R.color.rate_very_high, null);
        } else if (rate >= 10) {
            // High - Light Red/Pink
            return context.getResources().getColor(R.color.rate_high, null);
        } else if (rate >= 2) {
            // Medium - Light Yellow
            return context.getResources().getColor(R.color.rate_medium, null);
        } else if (rate >= 1) {
            // Low - Light Green
            return context.getResources().getColor(R.color.rate_low, null);
        } else {
            // Very Low - Dark Green
            return context.getResources().getColor(R.color.rate_very_low, null);
        }
    }

    /**
     * Set flag icon based on currency code
     * This is a placeholder implementation using Android drawable resources
     * In a production app, you would use actual flag image resources
     */
    private void setFlagIcon(ImageView imageView, String currencyCode) {
        // Placeholder implementation - using default icon
        // In production, you would map currency codes to flag drawable resources
        // Example: if (currencyCode.equals("USD")) imageView.setImageResource(R.drawable.flag_usd);

        // For now, use a simple colored circle to represent different currencies
        int iconResource = getFlagResourceForCurrency(currencyCode);
        imageView.setImageResource(iconResource);
    }

    /**
     * Get flag icon resource for currency code
     * Placeholder implementation using Android system icons
     */
    private int getFlagResourceForCurrency(String currencyCode) {
        // Placeholder - in production, map to actual flag images
        // For now, using system icons as placeholders
        switch (currencyCode) {
            case "USD":
            case "CAD":
            case "AUD":
            case "NZD":
                return android.R.drawable.ic_menu_compass;
            case "EUR":
                return android.R.drawable.ic_menu_info_details;
            case "JPY":
            case "CNY":
            case "KRW":
                return android.R.drawable.ic_menu_gallery;
            case "CHF":
            case "SEK":
                return android.R.drawable.ic_menu_agenda;
            default:
                return android.R.drawable.ic_menu_mapmode;
        }
    }
}
