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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import ph.roadtrip.roadtrip.classes.MySingleton;
import ph.roadtrip.roadtrip.classes.SessionHandler;
import ph.roadtrip.roadtrip.classes.UrlBean;
import ph.roadtrip.roadtrip.classes.User;
import ph.roadtrip.roadtrip.R;
import ph.roadtrip.roadtrip.transactionhistory.Booking;
import ph.roadtrip.roadtrip.transactionhistory.TransactionHistoryFragment;

public class AddFavoriteFragment extends Fragment {

    private static final String KEY_USER_ID = "userID";
    private static final String KEY_OWNER_ID = "ownerID";
    private static final String KEY_STATUS = "status1";
    private static final String KEY_MESSAGE = "message";

    Button btnAdd, btnDecline;
    SessionHandler session;
    private int userID;
    private int ownerID;
    private String url;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_favorite, container, false);

        UrlBean getUrl = new UrlBean();
        url = getUrl.getAddFavorites();

        session = new SessionHandler(getActivity());
        User user = session.getUserDetails();
        userID = user.getUserID();

        btnAdd = view.findViewById(R.id.btnAdd);
        btnDecline = view.findViewById(R.id.btnDecline);

        Booking booking = session.getBookingHistory();
        ownerID = booking.getOwnerID();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFavorite();
            }
        });

        btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new TransactionHistoryFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return view;
    }

    public void addFavorite(){

        JSONObject request = new JSONObject();
        try {
            request.put(KEY_USER_ID, userID);
            request.put(KEY_OWNER_ID, ownerID);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, url, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //Check if user got registered successfully
                            if (response.getInt(KEY_STATUS) == 0) {

                                //Redirect to Success Page
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.fragment_container, new SuccessAddFavoritesFragment());
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();

                            } else{
                                Toast.makeText(getActivity(),
                                        response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Display error message whenever an error occurs
                        Toast.makeText(getActivity(),
                                error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(getActivity()).addToRequestQueue(jsArrayRequest);

    }
}
