package gcu.mpd.pizzafragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements Fragment1.Fragment1Listener,Fragment1a.Fragment1aListener
{
    private Fragment1 fragment1;
    private Fragment1a fragment1a;
    private Fragment2 fragment2;
    private Fragment3 fragment3;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragment1 = new Fragment1();
        fragment1a = new Fragment1a();
        fragment2 = new Fragment2();
        fragment3 = new Fragment3();

        FragmentManager manager1 = getSupportFragmentManager();
        FragmentTransaction transaction1 = manager1.beginTransaction();
        transaction1.replace(R.id.fragmentFr1, fragment1);
        transaction1.commit();

        FragmentManager manager1a = getSupportFragmentManager();
        FragmentTransaction transaction1a = manager1a.beginTransaction();
        transaction1a.replace(R.id.fragmentFr1a, fragment1a);
        transaction1a.commit();

        FragmentManager manager2 = getSupportFragmentManager();
        FragmentTransaction transaction2 = manager2.beginTransaction();
        transaction2.replace(R.id.fragmentFr2, fragment2);
        transaction2.commit();

        FragmentManager manager3 = getSupportFragmentManager();
        FragmentTransaction transaction3 = manager3.beginTransaction();
        transaction3.replace(R.id.fragmentFr3, fragment3);
        transaction3.commit();

    }

    public void onInputSendCrustSelection(String input)
    {
        Log.d("MainTag",input);
        fragment3.updatebase(input);
    }

    public void onInputSendToppings(String input)
    {
        Log.d("Main Tag",input);
        fragment3.updateToppings(input);
    }

} // End of MainActivity