package org.me.gcu.calculator_lab2.data;

import org.me.gcu.calculator_lab2.models.Rectangle;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class dataParsing {
    public List<Rectangle> parseRectangles(String xmlData)
    {
        List<Rectangle> rectangles = new ArrayList<>();
        Rectangle currentRectangle = null;
        String currentValue = null;

        try{
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xmlData));

            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName();

                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if ("Rectangle".equalsIgnoreCase(tagName)) {
                            currentRectangle = new Rectangle();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        currentValue = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (currentRectangle != null) {
                            if ("Length".equalsIgnoreCase(tagName)) {
                                currentRectangle.setLength(Float.parseFloat(currentValue));
                            } else if ("Width".equalsIgnoreCase(tagName)) {
                                currentRectangle.setWidth(Float.parseFloat(currentValue));
                            } else if ("Rectangle".equalsIgnoreCase(tagName)) {
                                rectangles.add(currentRectangle);
                                currentRectangle = null;
                            }
                        }
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rectangles;
    }
}
