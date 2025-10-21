package org.me.gcu.pullparser1;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        parseData();
    } // End of onCreate

    // THIS EXAMPLE JUST OUTPUTS TO THE SYSTEM CONSOLE AND LOGCAT, DOES NOT USE THE APP SCREEN
    // GO TO THE "Run" OR "Logcat" TABS AT THE BOTTOM TO SEE IT
    private void parseData()
    {
        try
        {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput( new StringReader( "<foo>Hello World!</foo>" ) );
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT)
            {
                if(eventType == XmlPullParser.START_DOCUMENT)
                {
                    System.out.println("Start document");
                    Log.d("MyTag","Start document");
                }
                else
                if(eventType == XmlPullParser.START_TAG)
                {
                    String temp = xpp.getName();
                    System.out.println("Start tag "+temp);
                    Log.d("MyTag","Start tag "+temp);
                }
                else
                if(eventType == XmlPullParser.END_TAG)
                {
                    String temp = xpp.getName();
                    System.out.println("End tag "+temp);
                    Log.d("MyTag","End tag "+temp);
                }
                else
                if(eventType == XmlPullParser.TEXT)
                {
                    String temp = xpp.getText();
                    System.out.println("Text "+temp);
                    Log.d("MyTag","Text is "+temp);
                }
                eventType = xpp.next();
            } // End of while
        }
        catch (XmlPullParserException ae1)
        {
            Log.e("MyTag","Parsing error" + ae1.toString());
        }
        catch (IOException ae1)
        {
            Log.e("MyTag","IO error during parsing");
        }
        System.out.println("End document");
    } // End of ParseData
} // End of class