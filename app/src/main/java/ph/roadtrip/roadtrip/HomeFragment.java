package ph.roadtrip.roadtrip;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ph.roadtrip.roadtrip.classes.SessionHandler;
import ph.roadtrip.roadtrip.classes.UrlBean;
import ph.roadtrip.roadtrip.classes.User;
import ph.roadtrip.roadtrip.myfavorites.CustomFavoritesAdapter;
import ph.roadtrip.roadtrip.myfavorites.ListMyFavoritesFragment;
import ph.roadtrip.roadtrip.myfavorites.MyFavorites;

public class HomeFragment extends Fragment {

    // Log tag
    private static final String TAG = ListMyFavoritesFragment.class.getSimpleName();

    // Movies json url
    private String url;
    private ProgressDialog pDialog;
    private List<MyFavorites> myFavoritesList = new ArrayList<>();
    private ListView listView;
    private CustomFavoritesAdapter adapter;


    private ListView lv;
    private ImageView add_pic, profilePicture, iv_checkmark;
    private String getPicUrl;
    private String carPicture;
    private int userID;
    private String firstName, lastName, phoneNumber, mobileNumber, status, emailAddress, profpic;
    private TextView tvName, tvEmail, tvMobileNumber, tvPhoneNumber;

    private SessionHandler session;
    //public static ParseUser mUserClicked;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        myFavoritesList.clear();
        //ListView
        listView = (ListView) view.findViewById(R.id.listview);
        adapter = new CustomFavoritesAdapter(getActivity(), myFavoritesList);
        listView.setAdapter(adapter);

        //Get UserID
        session = new SessionHandler(getActivity().getApplicationContext());
        User user = session.getUserDetails();
        userID = user.getUserID();
        firstName = user.getFirstName();
        lastName = user.getLastName();
        status = user.getStatus();
        emailAddress = user.getEmailAddress();
        mobileNumber = user.getMobileNumber();
        phoneNumber = user.getPhoneNumber();
        profpic = user.getProfilePicture();

        profilePicture = view.findViewById(R.id.profilePicture);
        tvName = view.findViewById(R.id.tvName);
        iv_checkmark = view.findViewById(R.id.iv_checkmark);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvMobileNumber = view.findViewById(R.id.tvMobileNumber);
        tvPhoneNumber = view.findViewById(R.id.tvPhoneNumber);

        if (!status.equalsIgnoreCase("Activated")){
            iv_checkmark.setVisibility(View.GONE);
        }
        tvEmail.setText(emailAddress);
        tvName.setText(firstName + " " + lastName);
        tvMobileNumber.setText(mobileNumber);
        tvPhoneNumber.setText(phoneNumber);

        UrlBean url2 = new UrlBean();
        String myUrl = url2.getProfilePicUrl()+profpic;

        Glide.with(this)
                .load(myUrl)
                .into(profilePicture);

        //Url for getting JSON files
        UrlBean getUrl = new UrlBean();
        url = getUrl.getListFavorites()+userID;

        //Get Picture
        UrlBean picUrl = new UrlBean();
        final String getPicUrl = picUrl.getProfilePicUrl();

        pDialog = new ProgressDialog(getActivity());
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        // Creating volley request obj
        final JsonArrayRequest movieReq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());
                hidePDialog();

                // Parsing json
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        MyFavorites movie = new MyFavorites();
                        movie.setUserID(userID);
                        movie.setOwnerID(obj.getInt("ownerID"));
                        movie.setOwner_firstName(obj.getString("firstName"));
                        movie.setOwner_lastName(obj.getString("lastName"));
                        movie.setProfilePicture(getPicUrl+obj.getString("profilePicture"));
                        movie.setDateAdded(obj.getString("dateAdded"));
                        // adding movie to movies array
                        myFavoritesList.add(movie);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                // notifying list adapter about data changes
                // so that it renders the list view with updated data
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePDialog();

            }
        });

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(movieReq);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }
}
