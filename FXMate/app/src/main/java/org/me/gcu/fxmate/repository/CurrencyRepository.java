package org.me.gcu.fxmate.repository;

import android.os.Handler;
import android.util.Log;

import org.me.gcu.fxmate.model.CurrencyRate;
import org.me.gcu.fxmate.network.RssFeedFetcher;
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
    private static final String RSS_FEED_URL = "https://www.fx-exchange.com/gbp/rss.xml";

    /**
     * Callback interface for asynchronous data fetching
     * This allows the Repository to communicate back to the ViewModel on the main thread
     */
    public interface DataCallback {
        void onDataLoaded(List<CurrencyRate> rates);
        void onError(String errorMessage);
    }

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
     * Fetches and parses currency data from RSS feed using background thread
     * This method implements the Handler pattern following ProgressBar_solutions example
     *
     * Threading approach:
     * 1. Creates Handler on main thread (for UI updates)
     * 2. Spawns worker thread to fetch RSS feed and parse data
     * 3. Uses Handler.post() to send results back to main thread
     *
     * @param callback Callback to receive parsed data on main thread
     */
    public void fetchAndParseRates(final DataCallback callback) {
        // Create Handler bound to the main thread's message queue
        final Handler mHandler = new Handler();

        Log.d(TAG, "Starting background thread to fetch RSS feed...");

        // Create Thread to handle the long-running network operation
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "Worker thread started - fetching RSS feed from: " + RSS_FEED_URL);

                try {
                    // Step 1: Fetch RSS feed from network (blocking I/O operation)
                    RssFeedFetcher fetcher = new RssFeedFetcher();
                    final String xmlData = fetcher.fetchRssFeed(RSS_FEED_URL);

                    if (xmlData == null || xmlData.isEmpty()) {
                        // Network error - post error to main thread
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onError("Failed to download RSS feed");
                            }
                        });
                        return;
                    }

                    Log.d(TAG, "RSS feed downloaded successfully, parsing XML...");

                    // Step 2: Parse the XML data (still on worker thread)
                    final List<CurrencyRate> rates = parseRates(xmlData);

                    if (rates == null || rates.isEmpty()) {
                        // Parsing error - post error to main thread
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onError("Failed to parse currency data");
                            }
                        });
                        return;
                    }

                    Log.d(TAG, "Parsing complete. Posting " + rates.size() + " rates to main thread...");

                    // Step 3: Post results to main thread using Handler
                    // This ensures UI updates happen on the main thread
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onDataLoaded(rates);
                        }
                    });

                } catch (Exception e) {
                    Log.e(TAG, "Error in worker thread: " + e.getMessage(), e);

                    // Post error to main thread
                    final String errorMsg = e.getMessage();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onError("Error fetching data: " + errorMsg);
                        }
                    });
                }
            }
        }).start(); // Start the worker thread
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
                            Log.d(TAG, "New Currency Rate item found!");
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
