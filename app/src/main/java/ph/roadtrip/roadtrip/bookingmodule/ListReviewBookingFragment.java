package ph.roadtrip.roadtrip.bookingmodule;

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

import ph.roadtrip.roadtrip.classes.Reviews;
import ph.roadtrip.roadtrip.classes.SessionHandler;
import ph.roadtrip.roadtrip.classes.UrlBean;
import ph.roadtrip.roadtrip.MyApplication;
import ph.roadtrip.roadtrip.profile.CustomListReviewAdapter;
import ph.roadtrip.roadtrip.R;


public class ListReviewBookingFragment extends Fragment {


    // Log tag
    private static final String TAG = ListReviewBookingFragment.class.getSimpleName();

    // Movies json url
    private String url;
    private ProgressDialog pDialog;
    private List<Reviews> reviewlist = new ArrayList<>();
    private ListView listView;
    private CustomListReviewAdapter adapter;


    private ListView lv;
    private ImageView add_pic, profile_image;
    private String getPicUrl;
    private String carPicture;
    private int userID;
    private String firstName;
    private String lastName;
    private TextView fullname;
    private String propPic = "";

    private SessionHandler session;
    //public static ParseUser mUserClicked;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_review, container, false);
        reviewlist.clear();
        //ListView
        listView = (ListView) view.findViewById(R.id.listview);
        adapter = new CustomListReviewAdapter(getActivity(), reviewlist);
        listView.setAdapter(adapter);

        fullname = view.findViewById(R.id.fullname);
        profile_image = view.findViewById(R.id.profile_image);

        //Get UserID
        session = new SessionHandler(getActivity().getApplicationContext());
        BookingRequests bookingRequests = session.getOwnerUserID();
        userID = bookingRequests.getOwner_userID();
        firstName = bookingRequests.getOwner_firstName();
        lastName = bookingRequests.getOwner_lastName();
        propPic = bookingRequests.getOwner_profilePicture();

        //Url for getting JSON files
        UrlBean getUrl = new UrlBean();
        url = getUrl.getListReviewProfile()+userID;


        fullname.setText(capitalize(firstName) + " " + capitalize(lastName));

        //Get Picture
        UrlBean picUrl = new UrlBean();
        final String getPicUrl = picUrl.getProfilePicUrl();

        String profPic = getPicUrl + propPic;

        Glide.with(getActivity())
                .load(profPic)
                .into(profile_image);

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
                        Reviews movie = new Reviews();
                        movie.setReviewID(obj.getInt("reviewID"));
                        movie.setBookingID(obj.getInt("bookingID"));
                        movie.setReviewer(obj.getInt("reviewer"));
                        movie.setReviewerFirstName(obj.getString("firstName"));
                        movie.setReviewerLastName(obj.getString("lastName"));
                        movie.setMessage(obj.getString("message"));
                        movie.setRating(obj.getInt("rating"));
                        movie.setDateAdded(obj.getString("dateAdded"));
                        movie.setImageUrl(getPicUrl+obj.getString("profilePicture"));
                        // adding movie to movies array
                        reviewlist.add(movie);

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

    //Capitalize first letter of the word
    public String capitalize(String word){

        String s1 = word.substring(0, 1).toUpperCase();
        String stringCapitalized = s1 + word.substring(1);

        return stringCapitalized;
    }
}
