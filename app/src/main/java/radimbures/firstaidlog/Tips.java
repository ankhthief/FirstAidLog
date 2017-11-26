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
                "tablets or suppositories for headache, teeth (analgesics)",
                "tablets or suppositories to reduce elevated temperature (antipyretics)",
                "anti-nausea tablets (vehicle antiemetics)",
                "charcoal",
                "nasal drops for nasal breathing (otorhinolaryngology)",
                "drops, solution (syrup) or cough tablets (antitussive, expectorant)",
                "mouthpiece for disinfection of oral cavity and pharyngeal pain (stomatology)",
                "ointment or healing accelerator such as burns (dexpanthenol spray)",
                "eye drops or eye ointment with disinfectant effect, eye water for eye flushing (ophthalmology)",
                "ointment or gel when insect bites (local antihistamines)",
                "tablets with systemic allergic reaction (total antihistamine)",
                "a product for skin disinfection and surface wounds",
                "disinfectant on the wound site",
                "inert ointment or grease"
        };
        String[] bandage = {
                "gauze hydrophilic folded sterile compression, various sizes",
                "patch on coil, different dimensions",
                "wound dressing, different sizes",
                "elastic bandage, various dimensions",
                "Bandage sterile, various sizes",
                "throttle bandage",
                "scarf triple",
                "cotton wool and cellulose wadding",
                "medical thermometer",
                "resuscitation strap",
                "anatomical tweezers",
                "surgical tweezers straight",
                "Medical wooden blades",
                "medical rubber gloves",
                "PVC stripper 45 x 55 cm",
                "fixation plates, various dimensions"
        };

        String[] other = {
                "scissors",
                "closing pins, various sizes"
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
