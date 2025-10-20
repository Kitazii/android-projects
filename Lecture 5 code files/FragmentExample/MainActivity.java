package gcu.mpd.fragmentexample;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
//import android.support.v4.app.Fragment;
//import android.suppport.v4.app.FragmentManager;
//import android.suppport.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity implements View.OnClickListener
{
    private Button frButton1;
    private Button frButton2;
    private Fragment fr1;
    private Fragment fr2;
    private Fragment fr;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frButton1 = findViewById(R.id.frbutton1);
        frButton2 = findViewById(R.id.frbutton2);
        frButton1.setOnClickListener(this);
        frButton2.setOnClickListener(this);

        fr1 = new FragmentOne();
        fr2 = new FragmentTwo();

        fr = fr2;

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment,fr);
        transaction.commit();

    }

    public void onClick(View aview)
    {
        if (aview == frButton1)
        {
            fr = fr1;
            Log.e("MyTag","Fragment 1 selected");
        }
        else
        if (aview == frButton2)
        {
            fr = fr2;
            Log.e("MyTag","Fragment 2 selected");
        }

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment,fr);
        transaction.commit();
    }

}