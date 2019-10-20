package ph.roadtrip.roadtrip.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import ph.roadtrip.roadtrip.classes.MySingleton;
import ph.roadtrip.roadtrip.classes.UrlBean;
import ph.roadtrip.roadtrip.R;
import ph.roadtrip.roadtrip.classes.SessionHandler;
import ph.roadtrip.roadtrip.classes.User;
import ph.roadtrip.roadtrip.transactionhistory.TransactionHistoryFragment;

public class ProfileFragment extends android.support.v4.app.Fragment {
    private SessionHandler session;
    private static final String KEY_EMAIL = "emailAddress";
    private static final String KEY_MOBILE_NUMBER = "mobileNumber";
    private static final String KEY_PHONE_NUMBER = "phoneNumber";
    private static final String KEY_STATUS_USER ="status";
    private static final String KEY_IS_VERIFIED ="isVerified";
    private static final String KEY_USERNAME ="username";
    private static final String KEY_STATUS ="status1";
    private static final String KEY_PROF_PIC ="profilePicture";
    private static final String KEY_MESSAGE ="message";
    private static final String KEY_USER_ID ="userID";
    private static final String KEY_TOTAL = "total";
    private static final String KEY_TOTAL_RATING = "totalRating";
    private static final String KEY_TOTAL_TRIPS = "totalTrips";
    private static final String KEY_FIRST_NAME = "firstName";
    private static final String KEY_MIDDLE_NAME = "middleName";
    private static final String KEY_LAST_NAME = "lastName";
    private static final String KEY_GENDER = "gender";

    private TextView tvFeedbacks, tvRating, tvTrips;
    private String totalFeedbacks;
    private String totalTrips;
    private String getFeedbackTotalLink;
    private String rating;
    private double rate;
    private String getUserData4, getUserData3, getUserData2;
    private LinearLayout linear1, linear2, linear3;
    private String firstName, lastName;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Menu menuInfo;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        UrlBean url = new UrlBean();
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        setHasOptionsMenu(true);

        //Update User Data
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        onBackground();
        String profPicUrl = url.getProfilePicUrl();

        tvFeedbacks = view.findViewById(R.id.tvFeedbacks);
        tvRating = view.findViewById(R.id.tvRating);
        tvTrips = view.findViewById(R.id.tvTrips);
        linear1 = view.findViewById(R.id.linear1);
        linear2 = view.findViewById(R.id.linear2);
        linear3 = view.findViewById(R.id.linear3);
        getReviewsTotal();
        getAverageRating();
        getTotalTrips();

        //Get User Data from session
        session = new SessionHandler(getActivity());
        User user = session.getUserDetails();

        String profilePicture = user.getProfilePicture();

        String image = profPicUrl+profilePicture;
        ImageView profPic = (ImageView) view.findViewById(R.id.profilePicture);
        ImageView menu_prof_pic = view.findViewById(R.id.menu_prof_pic);

        if (user.getProfilePicture().equalsIgnoreCase("N/A")){
            //DO NOTHING
        } else {
            Glide.with(this)
                    .load(image)
                    .into(profPic);
        }



        TextView tvName = (TextView) view.findViewById(R.id.tvName);
        TextView tvEmail = (TextView) view.findViewById(R.id.tvEmail);
        TextView tvMobileNumber = (TextView) view.findViewById(R.id.tvMobileNumber);
        TextView tvPhoneNumber = (TextView) view.findViewById(R.id.tvPhoneNumber);
        Button btnEditProfile = (Button) view.findViewById(R.id.btnEditProfile);
        ImageView checkmark = (ImageView) view.findViewById(R.id.iv_checkmark);

        String status = user.getStatus();

        tvName.setText(capitalize(user.getFirstName()) + " " + capitalize(user.getLastName()));
        tvEmail.setText(user.getEmailAddress());
        tvMobileNumber.setText(user.getMobileNumber());
        tvPhoneNumber.setText(user.getPhoneNumber());


        if (status.equalsIgnoreCase("Activated")){
            checkmark.setVisibility(View.VISIBLE);
        }

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new EditProfileFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        linear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new ListReviewProfileFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        linear2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new ListReviewProfileFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        linear3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new TransactionHistoryFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        onBackground();
                    }
                }
        );

                return view;

    }



    public void onBackground(){
        UrlBean url = new UrlBean();
        String getUserData = url.getGetUserDataUrl();
        session = new SessionHandler(getActivity());
        User user = session.getUserDetails();
        int userID = user.getUserID();
        swipeRefreshLayout.setRefreshing(false);
        JSONObject request = new JSONObject();
        try {
            request.put(KEY_USER_ID, userID);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, getUserData, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //Check if user got registered successfully
                            if (response.getInt(KEY_STATUS) == 0) {
                                session.updateUserData(response.getString(KEY_EMAIL), response.getString(KEY_MOBILE_NUMBER), response.getString(KEY_PHONE_NUMBER), response.getString(KEY_STATUS_USER), response.getString(KEY_PROF_PIC), response.getInt(KEY_IS_VERIFIED), response.getString(KEY_FIRST_NAME), response.getString(KEY_MIDDLE_NAME), response.getString(KEY_LAST_NAME), response.getString(KEY_GENDER));

                                Toast.makeText(getActivity(),
                                        response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();
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
    }

    public void getReviewsTotal(){
        UrlBean url = new UrlBean();
        getUserData2 = url.getGetFeedbackProfile();
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
                (Request.Method.POST, getUserData2, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //Check if user got registered successfully
                            if (response.getInt(KEY_STATUS) == 0) {
                                totalFeedbacks = response.getString(KEY_TOTAL);

                                tvFeedbacks.setText(totalFeedbacks);

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

    public void getAverageRating(){
        UrlBean url = new UrlBean();
        getUserData3 = url.getGetAverageRatingProfile();
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

    public void getTotalTrips(){
        UrlBean url = new UrlBean();
        getUserData4 = url.getGetTotalTrips();
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
                (Request.Method.POST, getUserData4, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //Check if user got registered successfully
                            if (response.getInt(KEY_STATUS) == 0) {
                                totalTrips = response.getString(KEY_TOTAL_TRIPS);

                                tvTrips.setText(totalTrips);

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

    //Capitalize first letter of the word
    public String capitalize(String word){

        String s1 = word.substring(0, 1).toUpperCase();
        String stringCapitalized = s1 + word.substring(1);

        return stringCapitalized;
    }



}
