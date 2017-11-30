package radimbures.firstaidlog;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class Incidents extends Fragment {

    public long id_eventu;
    public long id_participant;
    DBAdapter myDB;

    public Incidents() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_injuries, container, false);
        // Inflate the layout for this fragment
        FragmentTabHost mTabHost;

        Bundle bundle = getArguments();
        if (bundle != null) {
            id_eventu = bundle.getLong("idevent");
            id_participant = bundle.getLong("idparticipant");
        }
        IncidentsList frag = new IncidentsList();
        Bundle bundle2 = new Bundle();
        bundle2.putLong("idevent", id_eventu);
        bundle2.putLong("idparticipant",id_participant);
        frag.setArguments(bundle2);

        mTabHost = root.findViewById(R.id.tabHost);
        mTabHost.setup(getContext(), getChildFragmentManager(), android.R.id.tabcontent);
        mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator(getString(R.string.incident_list)),
                IncidentsList.class, bundle2);
        mTabHost.addTab(mTabHost.newTabSpec("tab2").setIndicator(getString(R.string.personal_info)),
                ParticipantInfo.class, bundle2);



        return root;
    }

}
