package org.me.gcu.lab4_pizza_app.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import org.me.gcu.lab4_pizza_app.R;


public class CrustFragment extends Fragment implements View.OnClickListener {

    private RadioButton radioStuffed, radioThin;
    private CrustFragmentListener listener;

    public interface CrustFragmentListener
    {
        void onInputSendCrustSelection(String input);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_crust, container, false);
        radioStuffed = v.findViewById(R.id.radioStuffed);
        radioThin = v.findViewById(R.id.radioThin);

        radioStuffed.setChecked(false);
        radioThin.setChecked(false);

        return v;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        String selectedOption = (radioStuffed.isChecked()
                ? "Stuffed Crust Pizza base"
                : "Thin and Crispy Pizza base");
        listener.onInputSendCrustSelection(selectedOption);
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof CrustFragmentListener)
            listener = (CrustFragmentListener)context;
        else
            throw new RuntimeException(context.toString()
                    + "must implement CrustFragmentListener");
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        listener = null;
    }
}