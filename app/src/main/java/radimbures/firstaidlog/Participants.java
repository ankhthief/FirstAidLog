package radimbures.firstaidlog;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class Participants extends Fragment {

    DBAdapter myDB;
    long id_eventu;

    public Participants() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_participants, container, false);
        // Inflate the layout for this fragment
        FragmentTabHost mTabHost;

        Bundle bundle = getArguments();
        if (bundle != null) {
            id_eventu = bundle.getLong("key");
        }
        Toast.makeText(getActivity(),"id eventu: "+id_eventu, Toast.LENGTH_LONG).show();

        myDB =  new DBAdapter(getActivity());
        myDB.open();
        Class S;
        if (myDB.isEmptyParticipants()) {
            S = EmptyParticipantsList.class;
        } else {
            S = ParticipantsList.class;
        }
        myDB.close();

        mTabHost = (FragmentTabHost) root.findViewById(R.id.tabHost);
        mTabHost.setup(getContext(), getChildFragmentManager(), android.R.id.tabcontent);

        mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator("List of participants"),
        S, null);
        mTabHost.addTab(mTabHost.newTabSpec("tab2").setIndicator("Event information"),
                EventInfo.class, null);



        return root;
    }

}
