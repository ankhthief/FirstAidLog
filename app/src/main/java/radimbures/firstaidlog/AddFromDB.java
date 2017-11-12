package radimbures.firstaidlog;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddFromDB extends Fragment {


    public AddFromDB() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View root = inflater.inflate(R.layout.fragment_add_from_db, container, false);
        LinearLayout layout = root.findViewById(R.id.check_add_layout);
        for(int i = 0; i < 5; i++) {
            CheckBox cb = new CheckBox(root.getContext());
            cb.setText("I'm dynamic! Nr." + i);
            layout.addView(cb);
        }


        return root;
    }

}
