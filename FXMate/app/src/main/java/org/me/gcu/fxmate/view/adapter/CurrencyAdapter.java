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
     * Format exchange rate using UK/US number format (period as decimal separator)
     * Examples: 1.24 | 157.8 | 4718.5
     */
    private String formatRate(double rate) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.UK);
        symbols.setDecimalSeparator('.');
        symbols.setGroupingSeparator(',');

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
     * Maps currency codes to their corresponding country flag images
     */
    private void setFlagIcon(ImageView imageView, String currencyCode) {
        int iconResource = getFlagResourceForCurrency(currencyCode);
        imageView.setImageResource(iconResource);
    }

    /**
     * Get flag icon resource for currency code
     * Maps 3-letter currency codes (ISO 4217) to 2-letter country codes (ISO 3166-1)
     * Returns actual flag drawable resources
     */
    private int getFlagResourceForCurrency(String currencyCode) {
        // Map currency codes to country codes and get flag resource
        String countryCode = getCurrencyToCountryCode(currencyCode);

        // Build resource name: flag_{countrycode}
        String resourceName = "flag_" + countryCode;

        // Get resource ID dynamically
        int resourceId = context.getResources().getIdentifier(
            resourceName,
            "drawable",
            context.getPackageName()
        );

        // Return flag resource or fallback to a default icon if not found
        if (resourceId != 0) {
            return resourceId;
        } else {
            // Fallback for currencies without flags
            return android.R.drawable.ic_menu_mapmode;
        }
    }

    /**
     * Map currency codes (ISO 4217) to country codes (ISO 3166-1 alpha-2)
     * Comprehensive mapping for major world currencies
     */
    private String getCurrencyToCountryCode(String currencyCode) {
        switch (currencyCode) {
            // Major currencies
            case "USD": return "us";    // United States Dollar
            case "EUR": return "eu";    // Euro
            case "GBP": return "gb";    // British Pound
            case "JPY": return "jp";    // Japanese Yen
            case "CHF": return "ch";    // Swiss Franc

            // Americas
            case "CAD": return "ca";    // Canadian Dollar
            case "MXN": return "mx";    // Mexican Peso
            case "BRL": return "br";    // Brazilian Real
            case "ARS": return "ar";    // Argentine Peso
            case "CLP": return "cl";    // Chilean Peso
            case "COP": return "co";    // Colombian Peso
            case "PEN": return "pe";    // Peruvian Sol
            case "VEF": return "ve";    // Venezuelan Bolívar
            case "BOB": return "bo";    // Bolivian Boliviano
            case "UYU": return "uy";    // Uruguayan Peso

            // Europe
            case "NOK": return "no";    // Norwegian Krone
            case "SEK": return "se";    // Swedish Krona
            case "DKK": return "dk";    // Danish Krone
            case "ISK": return "is";    // Icelandic Króna
            case "CZK": return "cz";    // Czech Koruna
            case "PLN": return "pl";    // Polish Złoty
            case "HUF": return "hu";    // Hungarian Forint
            case "RON": return "ro";    // Romanian Leu
            case "BGN": return "bg";    // Bulgarian Lev
            case "HRK": return "hr";    // Croatian Kuna
            case "RSD": return "rs";    // Serbian Dinar
            case "UAH": return "ua";    // Ukrainian Hryvnia
            case "TRY": return "tr";    // Turkish Lira
            case "RUB": return "ru";    // Russian Ruble

            // Asia-Pacific
            case "CNY": return "cn";    // Chinese Yuan
            case "HKD": return "hk";    // Hong Kong Dollar
            case "TWD": return "tw";    // Taiwan Dollar
            case "KRW": return "kr";    // South Korean Won
            case "INR": return "in";    // Indian Rupee
            case "PKR": return "pk";    // Pakistani Rupee
            case "BDT": return "bd";    // Bangladeshi Taka
            case "LKR": return "lk";    // Sri Lankan Rupee
            case "NPR": return "np";    // Nepalese Rupee
            case "IDR": return "id";    // Indonesian Rupiah
            case "MYR": return "my";    // Malaysian Ringgit
            case "SGD": return "sg";    // Singapore Dollar
            case "THB": return "th";    // Thai Baht
            case "VND": return "vn";    // Vietnamese Dong
            case "PHP": return "ph";    // Philippine Peso
            case "AUD": return "au";    // Australian Dollar
            case "NZD": return "nz";    // New Zealand Dollar

            // Middle East
            case "SAR": return "sa";    // Saudi Riyal
            case "AED": return "ae";    // UAE Dirham
            case "QAR": return "qa";    // Qatari Riyal
            case "KWD": return "kw";    // Kuwaiti Dinar
            case "BHD": return "bh";    // Bahraini Dinar
            case "OMR": return "om";    // Omani Rial
            case "JOD": return "jo";    // Jordanian Dinar
            case "ILS": return "il";    // Israeli Shekel
            case "IQD": return "iq";    // Iraqi Dinar
            case "IRR": return "ir";    // Iranian Rial

            // Africa
            case "ZAR": return "za";    // South African Rand
            case "EGP": return "eg";    // Egyptian Pound
            case "NGN": return "ng";    // Nigerian Naira
            case "KES": return "ke";    // Kenyan Shilling
            case "TZS": return "tz";    // Tanzanian Shilling
            case "UGX": return "ug";    // Ugandan Shilling
            case "GHS": return "gh";    // Ghanaian Cedi
            case "MAD": return "ma";    // Moroccan Dirham
            case "TND": return "tn";    // Tunisian Dinar
            case "DZD": return "dz";    // Algerian Dinar
            case "AOA": return "ao";    // Angolan Kwanza
            case "ETB": return "et";    // Ethiopian Birr

            // Default: use first 2 characters of currency code as country code
            // This works for many currencies (e.g., MXN->mx, CZK->cz, etc.)
            default:
                if (currencyCode != null && currencyCode.length() >= 2) {
                    return currencyCode.substring(0, 2).toLowerCase();
                }
                return "xx";  // Fallback to unknown country
        }
    }
}
