package radimbures.firstaidlog;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.DialogFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddEvent extends android.support.v4.app.DialogFragment {


    public AddEvent() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //TODO vyčistit kod
        getDialog().setTitle("My Dialog Title");
        return inflater.inflate(R.layout.fragment_add_event, container, false);
    }

}
