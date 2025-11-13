package gcu.mpd.pizzafragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment3 extends Fragment
{
    private String orderDetails = "";
    private String base = "";
    private String toppings = "";
    private String extraCheese = "";
    private TextView orderInfo;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    )
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.frag_fragment3, container, false);
        orderInfo = (TextView) v.findViewById(R.id.orderInfo);

        return v;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);


    }

    public void updatebase(String astring)
    {
        base = astring;
        Log.d("Fragment 3", base);
        if (orderInfo != null) {
            orderInfo.setText(getOrderInfo());
        }
        else
        {
            Log.d("Mytag","Order text is null");
        }
    }

    public void updateToppings(String astring)
    {
        toppings = astring;
        Log.d("Fragment 3", toppings);
        orderInfo.setText(getOrderInfo());
    }

    private String getOrderInfo()
    {
       orderDetails =  base + " " + toppings + " " + extraCheese;
        //orderDetails = "Details";
       return orderDetails;
    }

} // End of Fragment3
