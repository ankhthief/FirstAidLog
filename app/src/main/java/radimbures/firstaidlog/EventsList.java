package radimbures.firstaidlog;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventsList extends Fragment {



    public EventsList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_events_list, container, false);

        final FragmentManager fm = getFragmentManager();
        final AddEvent addEvent = new AddEvent();
        FloatingActionButton fab = (FloatingActionButton) root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //ukáže dialog
                //TODO vyčistit kod
                addEvent.show(fm, "AddEvent");
                ///fm.beginTransaction().replace(R.id.fragment_holder, new ParticipantsList()).addToBackStack(null).commit();

            }
        });

        return root;

    }

}
