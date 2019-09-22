package ph.roadtrip.roadtrip.myfavorites;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ph.roadtrip.roadtrip.R;

public class SuccessAddFavoritesFragment extends Fragment {

    Button btnMyBookings;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_success_add_favorite, container, false);

        btnMyBookings = view.findViewById(R.id.btnMyBookings);

                btnMyBookings.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container, new ListMyFavoritesFragment());
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
            }
        });

        return view;
    }
}
