package gcu.mpd.pizzafragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment1 extends Fragment implements View.OnClickListener
{
    private RadioButton stuffedCrust, thinAndCrispy;
    private Fragment1Listener listener;

    public interface Fragment1Listener
    {
        void onInputSendCrustSelection(String input);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    )
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.frag_fragment1, container, false);

        stuffedCrust = (RadioButton) v.findViewById(R.id.stuffedCrust);
        thinAndCrispy = (RadioButton) v.findViewById(R.id.thinAndCrispy);
        stuffedCrust.setChecked(false);
        thinAndCrispy.setChecked(false);
        stuffedCrust.setOnClickListener(this);
        thinAndCrispy.setOnClickListener(this);

        //listener.onInputSendCrustSelection("Stuffed Crust");

        return v;

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

    }

    public void onClick(View v)
    {
        String base = "";

        if (stuffedCrust.isChecked())
            base = "Stuffed Crust Pizza base";
        else
            base = "Thin and Crispy Pizza base";

        listener.onInputSendCrustSelection(base);
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof Fragment1Listener)
        {
            listener = (Fragment1Listener) context;
        }
        else
        {
            throw new RuntimeException(context.toString()
                    + " must implement Fragment1Listener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        listener = null;
    }

}
