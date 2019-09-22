package ph.roadtrip.roadtrip.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import ph.roadtrip.roadtrip.R;

public class ValidIdFragment extends Fragment {

    private ListView lvRecommmended;
    private ListView lvOthers;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_valid_id, container, false);

        lvRecommmended = view.findViewById(R.id.lvRecommmended);
        ArrayList<String> recommended = new ArrayList<>();
        recommended.add("UMID");
        recommended.add("TIN");
        recommended.add("Philhealth Card");
        recommended.add("Driver's License");

        ArrayAdapter recoAdapter = new ArrayAdapter(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1,recommended);
        lvRecommmended.setAdapter(recoAdapter);

        lvOthers = view.findViewById(R.id.lvOthers);
        ArrayList<String> others = new ArrayList<>();
        others.add("Passport");
        others.add("Student's ID");
        others.add("Voter's ID");
        others.add("SSS ID");
        others.add("Alien/Immigrant COR");
        others.add("Government Office/GOCC ID");
        others.add("HDMF ID(Pagibig)");
        others.add("Postal ID");
        others.add("PRC ID");

        ArrayAdapter othersAdapater = new ArrayAdapter(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1,others);
        lvOthers.setAdapter(othersAdapater);

        return view;
    }

}
