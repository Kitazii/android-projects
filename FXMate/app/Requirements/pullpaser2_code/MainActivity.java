package org.me.gcu.pullparser2;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.IOException;
import java.io.StringReader;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity
{
    // XML that requires to be parsed
    // Note this is not the best place to define string
    // String defined here for testing purposes
    private final String thingString = "<ThingCollection> " +
            "<Thing> " +
            "<Bolt>M8 x 100</Bolt>	" +
            "<Nut>M8 Hex</Nut>	" +
            "<Washer>M8 Penny Washers</Washer>	" +
            "</Thing>" +
            "<Thing> " +
            "<Bolt>M8 x 150</Bolt>	" +
            "<Nut>M8 Hex</Nut>	" +
            "<Washer>M8 Penny Washers</Washer>	" +
            "</Thing>" +
            "<Thing> " +
            "<Bolt>M6 x 100</Bolt>	" +
            "<Nut>68 Hex</Nut>	" +
            "<Washer>M6 Penny Washers</Washer>	" +
            "	</Thing>" +
            "</ThingCollection>";

    private static final String TAG = "PullParserTask1";
    private static final Pattern BOLT_PATTERN = Pattern.compile("M\\s*(\\d+)\\s*[xX]\\s*(\\d+)");

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Thing> things = parseThings(thingString);

        Log.d(TAG, "---------- Parsed Things (" + things.size() + ") ----------");
        for (int i = 0; i < things.size(); i++) {
            Log.d(TAG, "#" + (i + 1) + " " + things.get(i).toString());
        }
        Log.d(TAG, "End of document reached");

    } // End of onCreate

    private List<Thing> parseThings(String dataToParse) {
        List<Thing> results = new ArrayList<>();
        Thing current = null;        // the Thing we’re building
        String currentText = null;   // holds inner text between tags

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

                        if ("Thing".equalsIgnoreCase(name)) {
                            current = new Thing();
                            Log.d(TAG, "↳ New Thing found!");
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
                            if ("Bolt".equalsIgnoreCase(name)) {
                                String bolt = safe(currentText);
                                current.setBolt(bolt);

                                // Extract metric & length (e.g., "M8 x 100" -> 8, 100)
                                Matcher m = BOLT_PATTERN.matcher(bolt);
                                if (m.find()) {
                                    current.setBoltMetric(parseIntSafe(m.group(1)));
                                    current.setBoltLength(parseIntSafe(m.group(2)));
                                } else {
                                    Log.w(TAG, "Could not parse metric/length from: " + bolt);
                                }

                                Log.d(TAG, "Bolt is " + bolt);
                            } else if ("Nut".equalsIgnoreCase(name)) {
                                String nut = safe(currentText);
                                current.setNut(nut);
                                Log.d(TAG, "Nut is " + nut);
                            } else if ("Washer".equalsIgnoreCase(name)) {
                                String washer = safe(currentText);
                                current.setWasher(washer);
                                Log.d(TAG, "Washer is " + washer);
                            } else if ("Thing".equalsIgnoreCase(name)) {
                                // One complete Thing
                                results.add(current);
                                Log.d(TAG, "✓ Thing parsing completed: " + current.toString());
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

    // Helpers
    private static String safe(String s) {
        return s == null ? "" : s.trim();
    }

    private static int parseIntSafe(String s) {
        try {
            return Integer.parseInt(s.trim());
        } catch (Exception e) {
            return 0;
        }
    }

} //End of class