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
public class Injuries extends Fragment {

    public long id_eventu;
    public long id_participant;
    DBAdapter myDB;
    Class S;

    public Injuries() {
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
            id_eventu = bundle.getLong("key");
            id_participant = bundle.getLong("idparticipant");
        }

        InjuriesList frag = new InjuriesList();
        Bundle bundle2 = new Bundle();
        bundle2.putLong("idevent", id_eventu);
        bundle2.putLong("idparticipant",id_participant);
        frag.setArguments(bundle2);

        myDB =  new DBAdapter(getActivity());
        myDB.open();
        S = InjuriesList.class;
        myDB.close();

        mTabHost = root.findViewById(R.id.tabHost);
        mTabHost.setup(getContext(), getChildFragmentManager(), android.R.id.tabcontent);
        mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator("List of injuries"),
                S, bundle2);
        mTabHost.addTab(mTabHost.newTabSpec("tab2").setIndicator("Personal information"),
                ParticipantInfo.class, bundle2);



        return root;
    }

}
