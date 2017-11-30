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
    public long id_eventu;
    Class S;

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
        ParticipantsList frag = new ParticipantsList();
        Bundle bundle1 = new Bundle();
        bundle1.putLong("key", id_eventu);
        frag.setArguments(bundle1);

        //myDB =  new DBAdapter(getActivity());
        //myDB.open();
        //myDB.close();

        mTabHost = root.findViewById(R.id.tabHost);
        mTabHost.setup(getContext(), getChildFragmentManager(), android.R.id.tabcontent);
        mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator(getString(R.string.list_participants)),
                ParticipantsList.class, bundle1);
        mTabHost.addTab(mTabHost.newTabSpec("tab2").setIndicator(getString(R.string.event_info)),
                EventInfo.class, bundle1);



        return root;
    }

}
