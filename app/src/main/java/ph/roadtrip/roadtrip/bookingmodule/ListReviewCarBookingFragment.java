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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ph.roadtrip.roadtrip.MyApplication;
import ph.roadtrip.roadtrip.R;
import ph.roadtrip.roadtrip.classes.MySingleton;
import ph.roadtrip.roadtrip.classes.Reviews;
import ph.roadtrip.roadtrip.classes.SessionHandler;
import ph.roadtrip.roadtrip.classes.UrlBean;
import ph.roadtrip.roadtrip.classes.User;
import ph.roadtrip.roadtrip.profile.CustomListReviewAdapter;
import ph.roadtrip.roadtrip.transactionhistory.Booking;


public class ListReviewCarBookingFragment extends Fragment {


    private static final String KEY_STATUS = "status1";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_CAR_ID = "carID";
    private static final String KEY_TOTAL_RATING = "totalAverage";
    private static final String KEY_COLOR = "color";
    private static final String KEY_MODEL_NAME = "modelName";
    private static final String KEY_MODEL_YEAR = "modelYear";
    private static final String KEY_BRAND_NAME = "brandName";

    // Log tag
    private static final String TAG = ListReviewCarBookingFragment.class.getSimpleName();

    // Movies json url
    private String url, color, modelName, modelYear, brandName, carHeader;
    private ProgressDialog pDialog;
    private List<Reviews> reviewlist = new ArrayList<>();
    private ListView listView;
    private CustomListCarReviewAdapter adapter;

    private ListView lv;
    private ImageView add_pic, profile_image;
    private String getPicUrl;
    private String carPicture;
    private int userID;
    private String firstName;
    private String lastName;
    private TextView fullname, rating, tvBrand;
    private String propPic = "";
    private int carID;

    private SessionHandler session;
    //public static ParseUser mUserClicked;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_car_review, container, false);
        reviewlist.clear();
        //ListView
        listView = (ListView) view.findViewById(R.id.listview);
        adapter = new CustomListCarReviewAdapter(getActivity(), reviewlist);
        listView.setAdapter(adapter);

        fullname = view.findViewById(R.id.fullname);
        profile_image = view.findViewById(R.id.profile_image);
        rating = view.findViewById(R.id.rating);
        tvBrand = view.findViewById(R.id.tvBrand);

        //Get UserID
        session = new SessionHandler(getActivity().getApplicationContext());
        BookingRequests bookingRequests = session.getOwnerUserID();
        Booking booking = session.getCarReview();
        carID = booking.getCarID();

        userID = bookingRequests.getOwner_userID();
        firstName = bookingRequests.getOwner_firstName();
        lastName = bookingRequests.getOwner_lastName();
        propPic = bookingRequests.getOwner_profilePicture();

        //Url for getting JSON files
        UrlBean getUrl = new UrlBean();
        url = getUrl.getCar_review()+carID;

        fullname.setText("Owner: " + capitalize(firstName) + " " + capitalize(lastName));
        getAverageRating();
        //Get Picture
        UrlBean picUrl = new UrlBean();
        final String getPicUrl = picUrl.getProfilePicUrl();

        String profPic = getPicUrl + propPic;

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
                        movie.setReviewID(obj.getInt("carReviewID"));
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

    public void getAverageRating(){
        UrlBean url = new UrlBean();
        String getUserData3 = url.getCar_ave_review();

        JSONObject request = new JSONObject();
        try {
            request.put(KEY_CAR_ID, carID);

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
                                Double rate = response.getDouble(KEY_TOTAL_RATING);
                                modelName = response.getString(KEY_MODEL_NAME);
                                brandName = response.getString(KEY_BRAND_NAME);
                                color = response.getString(KEY_COLOR);
                                modelYear = response.getString(KEY_MODEL_YEAR);

                                carHeader = color + " " + brandName + " " + modelName + " " + modelYear;

                                DecimalFormat df = new DecimalFormat("#.#");
                                df.format(rate);

                                rating.setText(String.valueOf(rate));
                                tvBrand.setText(carHeader);

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
