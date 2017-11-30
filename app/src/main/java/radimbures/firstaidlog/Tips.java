package radimbures.firstaidlog;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class Tips extends Fragment {

    LinearLayout layout;
    LinearLayout layout1;
    LinearLayout layout2;
    View root;


    public Tips() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_tips, container, false);
        layout = root.findViewById(R.id.check_medicalproducts_layout);
        layout1 = root.findViewById(R.id.check_bandage_layout);
        layout2 = root.findViewById(R.id.check_other_layout);
        String[] medicalproducts = {
                getString(R.string.tips1),
                getString(R.string.tips2),
                getString(R.string.tips3),
                getString(R.string.tips4),
                getString(R.string.tips5),
                getString(R.string.tips6),
                getString(R.string.tips7),
                getString(R.string.tips8),
                getString(R.string.tips9),
                getString(R.string.tips10),
                getString(R.string.tips11),
                getString(R.string.tips12),
                getString(R.string.tips13),
                getString(R.string.tips14)
        };
        String[] bandage = new String[]{
                getString(R.string.tips15),
                getString(R.string.tips16),
                getString(R.string.tips17),
                getString(R.string.tips18),
                getString(R.string.tips19),
                getString(R.string.tips20),
                getString(R.string.tips21),
                getString(R.string.tips22),
                getString(R.string.tips23),
                getString(R.string.tips24),
                getString(R.string.tips25),
                getString(R.string.tips26),
                getString(R.string.tips27),
                getString(R.string.tips28),
                getString(R.string.tips29),
                getString(R.string.tips30)
        };

        String[] other = {
                getString(R.string.tips31),
                getString(R.string.tips32)
        };

        for (String medicalproduct : medicalproducts) {
            CheckBox cb = new CheckBox(root.getContext());
            cb.setText(medicalproduct);
            cb.setHeight(105);
            layout.addView(cb);
        }
        for (String aBandage : bandage) {
            CheckBox cb = new CheckBox(root.getContext());
            cb.setText(aBandage);
            cb.setHeight(105);
            layout1.addView(cb);
        }
        for (String anOther : other) {
            CheckBox cb = new CheckBox(root.getContext());
            cb.setText(anOther);
            cb.setHeight(105);
            layout2.addView(cb);
        }
        return root;
    }

}
