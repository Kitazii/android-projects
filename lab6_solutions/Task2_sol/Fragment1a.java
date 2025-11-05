package gcu.mpd.pizzafragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment1a#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment1a extends Fragment implements View.OnClickListener
{
    private CheckBox mushrooms, sweetcorn, onions, peppers;
    private Fragment1a.Fragment1aListener listener;

    public interface Fragment1aListener
    {
        void onInputSendToppings(String input);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.frag_fragment1a, container, false);

        mushrooms = (CheckBox) v.findViewById(R.id.mushrooms);
        sweetcorn = (CheckBox) v.findViewById(R.id.sweetcorn);
        onions = (CheckBox) v.findViewById(R.id.onions);
        peppers = (CheckBox) v.findViewById(R.id.peppers);

        mushrooms.setOnClickListener(this);
        sweetcorn.setOnClickListener(this);
        onions.setOnClickListener(this);
        peppers.setOnClickListener(this);

        return v;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);


    }

    public void onClick(View v)
    {
        String toppings = "";

        if (mushrooms.isChecked())
            toppings = toppings + " Mushrooms";
        if (sweetcorn.isChecked())
            toppings = toppings + " Sweetcorn";
        if (onions.isChecked())
            toppings = toppings + " Onions";
        if (peppers.isChecked())
            toppings = toppings + " Peppers";

        listener.onInputSendToppings(toppings);

    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof Fragment1a.Fragment1aListener)
        {
            listener = (Fragment1a.Fragment1aListener) context;
        }
        else
        {
            throw new RuntimeException(context.toString()
                    + " must implement Fragment1aListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        listener = null;
    }
}
