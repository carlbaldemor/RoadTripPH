package ph.roadtrip.roadtrip.profile;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import ph.roadtrip.roadtrip.classes.MySingleton;
import ph.roadtrip.roadtrip.classes.Reviews;
import ph.roadtrip.roadtrip.classes.SessionHandler;
import ph.roadtrip.roadtrip.classes.UrlBean;
import ph.roadtrip.roadtrip.classes.User;
import ph.roadtrip.roadtrip.MyApplication;
import ph.roadtrip.roadtrip.R;


public class ListReviewProfileFragment extends Fragment {


    // Log tag
    private static final String TAG = ListReviewProfileFragment.class.getSimpleName();

    private static final String KEY_USER_ID = "userID";
    private static final String KEY_STATUS = "status1";
    private static final String KEY_TOTAL_RATING = "totalRating";
    private static final String KEY_MESSAGE ="message";

    // Movies json url
    private String url;
    private ProgressDialog pDialog;
    private List<Reviews> reviewlist = new ArrayList<>();
    private ListView listView;
    private CustomListReviewAdapter adapter;


    private ListView lv;
    private ImageView add_pic,profile_image;
    private String getPicUrl;
    private String carPicture;
    private int userID;
    private String firstName;
    private String lastName;
    private TextView fullname, tvRating;
    private String profilePicture;
    private double rate;


    private SessionHandler session;
    //public static ParseUser mUserClicked;

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_review, container, false);
        reviewlist.clear();
        //hide info button actionbar

        //ListView
        listView = (ListView) view.findViewById(R.id.listview);
        adapter = new CustomListReviewAdapter(getActivity(), reviewlist);
        listView.setAdapter(adapter);

        fullname = view.findViewById(R.id.fullname);
        profile_image = view.findViewById(R.id.profile_image);
        tvRating = view.findViewById(R.id.rating);

        //Get UserID
        session = new SessionHandler(getActivity().getApplicationContext());
        //Get UserID
        session = new SessionHandler(getActivity().getApplicationContext());
        User user = session.getUserDetails();
        userID = user.getUserID();
        firstName = user.getFirstName();
        lastName = user.getLastName();
        profilePicture = user.getProfilePicture();


        fullname.setText(capitalize(firstName) + " " + capitalize(lastName));

        //Get Picture
        UrlBean pickUrl = new UrlBean();
        final String getPickUrl = pickUrl.getProfilePicUrl();

        String profPic = getPickUrl + profilePicture;

        Glide.with(getActivity())
                .load(profPic)
                .into(profile_image);


        //Url for getting JSON files
        UrlBean getUrl = new UrlBean();
        url = getUrl.getListReviewProfile()+userID;

        //Get Picture
        UrlBean picUrl = new UrlBean();
        final String getPicUrl = picUrl.getProfilePicUrl();

        pDialog = new ProgressDialog(getActivity());
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        getAverageRating();

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
                        movie.setFirstName(obj.getString("firstName"));
                        movie.setLastName(obj.getString("lastName"));
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

    public void getAverageRating(){
        UrlBean url = new UrlBean();
        String getUserData3 = url.getGetAverageRatingProfile();
        session = new SessionHandler(getActivity());
        User user = session.getUserDetails();
        int userID = user.getUserID();

        JSONObject request = new JSONObject();
        try {
            request.put(KEY_USER_ID, userID);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, getUserData3, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //Check if user got registered successfully
                            if (response.getInt(KEY_STATUS) == 0) {
                                rate = response.getDouble(KEY_TOTAL_RATING);

                                DecimalFormat df = new DecimalFormat("#.#");
                                df.format(rate);

                                tvRating.setText(String.valueOf(rate));

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


    //hide info button actionbar
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item=menu.findItem(R.id.action_info);
        if(item!=null)
            item.setVisible(false);
        MenuItem item2=menu.findItem(R.id.action_add_car);
        if (item2!=null)
            item2.setVisible(false);
    }


    //Capitalize first letter of the word
    public String capitalize(String word){

        String s1 = word.substring(0, 1).toUpperCase();
        String stringCapitalized = s1 + word.substring(1);

        return stringCapitalized;
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
