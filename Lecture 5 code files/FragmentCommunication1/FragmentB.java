package gcu.mpd.fragmentcommunication1;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.Nullable;


public class FragmentB extends Fragment
{
    private FragmentBListener listener;
    private EditText editText;
    private TextView receivedinB;
    private Button buttonOk;
    private Button buttonClear;

    public interface FragmentBListener
    {
        void onInputBSent(CharSequence input);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_b, container, false);

        editText = v.findViewById(R.id.edit_text);
        receivedinB = v.findViewById(R.id.received_textInB);
        buttonOk = v.findViewById(R.id.button_ok);
        buttonClear = v.findViewById(R.id.button_clear);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence input = editText.getText();
                listener.onInputBSent(input);
            }
        });
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                receivedinB.setText("");
            }
        });

        return v;
    }

    public void updateEditText(CharSequence newText) {
        receivedinB.setText(newText);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentBListener) {
            listener = (FragmentBListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentBListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
