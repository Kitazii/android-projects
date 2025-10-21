package org.me.gcu.fxmate.repository;

import android.util.Log;

import org.me.gcu.fxmate.model.CurrencyRate;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CurrencyRepository {

    private static final String TAG = "CurrencyRepository";

    // Pattern to extract currency codes from title: "British Pound Sterling(GBP)/United Arab Emirates Dirham(AED)"
    private static final Pattern TITLE_PATTERN = Pattern.compile("([^(]+)\\(([A-Z]{3})\\)/([^(]+)\\(([A-Z]{3})\\)");

    // Pattern to extract rate from description: "1 British Pound Sterling = 4.9354 United Arab Emirates Dirham"
    private static final Pattern RATE_PATTERN = Pattern.compile("1\\s+[^=]+=\\s+([0-9.]+)");

    private static CurrencyRepository instance;

    private CurrencyRepository() {}

    public static CurrencyRepository getInstance() {
        if (instance == null) {
            instance = new CurrencyRepository();
        }
        return instance;
    }

    /**
     * Parses XML data containing currency exchange rates using PullParser approach
     * @param dataToParse XML string containing RSS feed data
     * @return List of parsed CurrencyRate objects
     */
    public List<CurrencyRate> parseRates(String dataToParse) {
        List<CurrencyRate> results = new ArrayList<>();
        CurrencyRate current = null;        // the CurrencyRate we're building
        String currentText = null;           // holds inner text between tags

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(dataToParse));

            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG: {
                        String name = xpp.getName();

                        if ("item".equalsIgnoreCase(name)) {
                            current = new CurrencyRate();
                            Log.d(TAG, "↳ New Currency Rate item found!");
                        }
                        break;
                    }

                    case XmlPullParser.TEXT: {
                        currentText = xpp.getText();
                        break;
                    }

                    case XmlPullParser.END_TAG: {
                        String name = xpp.getName();

                        if (current != null) {
                            if ("title".equalsIgnoreCase(name)) {
                                String title = safe(currentText);
                                current.setTitle(title);

                                // Extract currency names and codes from title
                                // Example: "British Pound Sterling(GBP)/United Arab Emirates Dirham(AED)"
                                Matcher m = TITLE_PATTERN.matcher(title);
                                if (m.find()) {
                                    current.setBaseCurrency(safe(m.group(1)));
                                    current.setBaseCode(safe(m.group(2)));
                                    current.setTargetCurrency(safe(m.group(3)));
                                    current.setTargetCode(safe(m.group(4)));
                                } else {
                                    Log.w(TAG, "Could not parse currencies from title: " + title);
                                }

                                Log.d(TAG, "Title is " + title);
                            } else if ("link".equalsIgnoreCase(name)) {
                                String link = safe(currentText);
                                current.setLink(link);
                                Log.d(TAG, "Link is " + link);
                            } else if ("pubDate".equalsIgnoreCase(name)) {
                                String pubDate = safe(currentText);
                                current.setPubDate(pubDate);
                                Log.d(TAG, "PubDate is " + pubDate);
                            } else if ("description".equalsIgnoreCase(name)) {
                                String description = safe(currentText);
                                current.setDescription(description);

                                // Extract exchange rate from description
                                // Example: "1 British Pound Sterling = 4.9354 United Arab Emirates Dirham"
                                Matcher m = RATE_PATTERN.matcher(description);
                                if (m.find()) {
                                    current.setRate(parseDoubleSafe(m.group(1)));
                                } else {
                                    Log.w(TAG, "Could not parse rate from description: " + description);
                                }

                                Log.d(TAG, "Description is " + description);
                            } else if ("item".equalsIgnoreCase(name)) {
                                // One complete CurrencyRate
                                results.add(current);
                                Log.d(TAG, "✓ Currency Rate parsing completed: " + current.toString());
                                current = null;
                            }
                        }
                        break;
                    }
                }

                eventType = xpp.next();
            }
        } catch (XmlPullParserException e) {
            Log.e(TAG, "Parsing error: " + e, e);
        } catch (IOException e) {
            Log.e(TAG, "IO error during parsing", e);
        }

        return results;
    }

    // Helper methods
    private static String safe(String s) {
        return s == null ? "" : s.trim();
    }

    private static double parseDoubleSafe(String s) {
        try {
            return Double.parseDouble(s.trim());
        } catch (Exception e) {
            return 0.0;
        }
    }
}
