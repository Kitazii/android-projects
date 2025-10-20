package gcu.mpd.fragmentcommunication1;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Context;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

public class MainActivity extends Activity implements FragmentA.FragmentAListener, FragmentB.FragmentBListener, View.OnClickListener
{
    private FragmentA fragmentA;
    private FragmentB fragmentB;
    private Button clearButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
        {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clearButton=(Button)findViewById(R.id.button_clearAll);
        clearButton.setOnClickListener(this);

        fragmentA=new FragmentA();
        fragmentB=new FragmentB();

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        //transaction.beginTransaction()
            transaction.replace(R.id.container_a,fragmentA);
            transaction.replace(R.id.container_b,fragmentB);
            transaction.commit();
        }

        @Override
        public void onInputASent(CharSequence input)
        {
            fragmentB.updateEditText(input);
        }

        @Override
        public void onInputBSent(CharSequence input)
        {
            fragmentA.updateEditText(input);
        }

        public void onClick(View aview)
        {
            fragmentA.updateEditText("");
            fragmentB.updateEditText("");
        }

} // End of MainActivity