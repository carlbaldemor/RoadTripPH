package ph.roadtrip.roadtrip.bookingmodule;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
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

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import ph.roadtrip.roadtrip.classes.MySingleton;
import ph.roadtrip.roadtrip.classes.SessionHandler;
import ph.roadtrip.roadtrip.classes.UrlBean;
import ph.roadtrip.roadtrip.classes.User;
import ph.roadtrip.roadtrip.MainActivity;
import ph.roadtrip.roadtrip.paypal.PayPalActivity;
import ph.roadtrip.roadtrip.R;
import ph.roadtrip.roadtrip.transactionhistory.Booking;

public class CurrentBookingFragment extends Fragment {

    private static final String KEY_MESSAGE = "message";
    private static final String KEY_RATING = "rating";
    private static final String KEY_BOOKING_ID = "bookingID";
    private static final String KEY_USER_ID = "userID";
    private static final String KEY_STATUS = "status1";
    private static final String KEY_PAYMENT_ID = "paymentID";
    private static final String KEY_START_DATE = "startDate";
    private static final String KEY_END_DATE = "endDate";
    private static final String KEY_LAT_ISSUE = "latIssue";
    private static final String KEY_LONG_ISSUE = "longIssue";
    private static final String KEY_LAT_RETURN = "latReturn";
    private static final String KEY_LONG_RETURN = "longReturn";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_TOTAL_AMOUNT = "totalAmount";
    private static final String KEY_OWNER_ID = "ownerID";
    private static final String KEY_BRAND_NAME = "brandName";
    private static final String KEY_MODEL_NAME = "modelName";
    private static final String KEY_COLOR = "color";
    private static final String KEY_MODEL_YEAR = "modelYear";
    private static final String KEY_SERVICE_TYPE = "serviceType";
    private static final String KEY_RECORD_PICTURE = "recordPicture";
    private static final String KEY_CAR_TYPE = "carType";
    private static final String KEY_PROFILE_PICTURE = "profilePicture";
    private static final String KEY_FIRST_NAME = "firstName";
    private static final String KEY_LAST_NAME = "lastName";
    private static final String KEY_TOTAL_RATING = "totalRating";
    private static final String KEY_MOBILE_NUMBER = "mobileNumber";
    private static final String KEY_SPECIAL_NOTE = "specialNote";
    private static final String KEY_DRIVER_FULL_NAME = "driverFullName";
    private static final String KEY_DRIVER_MOBILE_NUMBER = "driverMobileNumber";


    private Button btnBookNow, btnpaypal, btnCancel;
    private String url;
    private SessionHandler session;

    //Instance Variables
    private int userID;
    private int bookingID = 0;
    private int paymentID = 0;
    private String mobileNumber;
    private String startDate;
    private String endDate;
    private String latIssue;
    private String longIssue;
    private String latReturn;
    private String longReturn;
    private String amount;
    private String totalAmount;
    private int ownerID;
    private String brandName;
    private String modelName;
    private String color;
    private String modelYear;
    private String serviceType;
    private String recordPicture;
    private String carType;
    private String firstName;
    private String lastName;
    private String profilePicture;
    private String specialNote;
    private String driverFullName;
    private String driverMobileNumber;
    private String address;
    private String address2;

    private TextView tvService, tvPaid, tvSpecialNote, tvDriverMobileNumber, tvDriverFullName;
    private TextView tvBrand, tvRating, tvName, tvPickup, tvReturn, tvAmount, tvTotalAmount, tvCarType, tvStartDate, tvEndDate;
    private View blank;
    private LinearLayout view1, view2;
    private ImageView proppic, call, sms, call2, sms2;
    private String rating;
    private LinearLayout frameDriverName, frameDriverMobileNumber;
    private ProgressDialog pDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.current_booking_adapter, container, false);
        UrlBean urlBean = new UrlBean();
        url = urlBean.getGetCurrentBooking();

        view1 = view.findViewById(R.id.layout1);
        view2 = view.findViewById(R.id.layout2);

        session = new SessionHandler(getActivity().getApplicationContext());
        User user = session.getUserDetails();
        userID = user.getUserID();


        tvPaid = view.findViewById(R.id.tvPaid);
        tvBrand = view.findViewById(R.id.tvBrand);
        tvRating = view.findViewById(R.id.tvRating);
        tvName = view.findViewById(R.id.tvName);
        tvPickup = view.findViewById(R.id.tvPickup);
        tvReturn = view.findViewById(R.id.tvReturn);
        tvAmount = view.findViewById(R.id.tvAmount);
        tvTotalAmount = view.findViewById(R.id.tvTotalAmount);
        tvCarType = view.findViewById(R.id.tvCarType);
        proppic = view.findViewById(R.id.profilePicture);
        tvStartDate = view.findViewById(R.id.tvStartDate);
        tvEndDate = view.findViewById(R.id.tvEndDate);
        tvRating = view.findViewById(R.id.tvRating);
        tvSpecialNote = view.findViewById(R.id.tvSpecialNote);
        call = view.findViewById(R.id.call);
        sms = view.findViewById(R.id.sms);
        tvDriverMobileNumber = view.findViewById(R.id.tvDriverMobileNumber);
        tvDriverFullName = view.findViewById(R.id.tvDriverFullName);

        frameDriverName = view.findViewById(R.id.frameDriverName);
        frameDriverMobileNumber = view.findViewById(R.id.frameDriverMobile);
        sms2 = view.findViewById(R.id.sms2);
        call2 = view.findViewById(R.id.call2);

        pDialog = new ProgressDialog(getActivity());
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        onBackground();

        btnpaypal = view.findViewById(R.id.btnpaypal);
        btnBookNow = view.findViewById(R.id.btnBookNow);
        btnCancel = view.findViewById(R.id.btnCancel);
        btnBookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                i.putExtra("KEY_BOOKING_ID", bookingID);
                startActivity(i);
            }
        });

        btnpaypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(), PayPalActivity.class);
                i.putExtra("KEY_BOOKING_ID", bookingID);
                startActivity(i);
            }
        });

        tvRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new ListReviewBookingFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        proppic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new ListReviewBookingFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new ListReviewBookingFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setType("vnd.android-dir/mms-sms");
                sendIntent.putExtra("address"  , new String(mobileNumber));
                sendIntent.putExtra("sms_body", "");
                startActivity(sendIntent);
            }
        });
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mobileNumber));
                startActivity(intent);
            }
        });

        call2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + driverMobileNumber));
                startActivity(intent);
            }
        });
        sms2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setType("vnd.android-dir/mms-sms");
                sendIntent.putExtra("address"  , new String(driverMobileNumber));
                sendIntent.putExtra("sms_body", "");
                startActivity(sendIntent);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.setBookingIDHistory(bookingID, userID);
                //Go to cancel page
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new CancelBookingFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });



       return view;
    }

    public void onBackground(){
        final JSONObject request = new JSONObject();
        try {
                request.put(KEY_USER_ID, userID);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(Request.Method.POST, url, request, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hidePDialog();
                try {
                    //Check if user got logged in successfully
                    if (response.getInt(KEY_STATUS) == 0) {

                        bookingID = response.getInt(KEY_BOOKING_ID);

                        try {
                            paymentID = response.getInt(KEY_PAYMENT_ID);
                        }catch (JSONException je){
                            paymentID = 0;
                        }
                        //Instance Variables

                        latIssue = response.getString(KEY_LAT_ISSUE);
                        longIssue = response.getString(KEY_LONG_ISSUE);
                        latReturn = response.getString(KEY_LAT_RETURN);
                        longReturn = response.getString(KEY_LONG_RETURN);
                        brandName = response.getString(KEY_BRAND_NAME);
                        modelName = response.getString(KEY_MODEL_NAME);
                        amount = response.getString(KEY_AMOUNT);
                        totalAmount = response.getString(KEY_TOTAL_AMOUNT);
                        carType = response.getString(KEY_CAR_TYPE);
                        color = response.getString(KEY_COLOR);
                        modelYear = response.getString(KEY_MODEL_YEAR);
                        profilePicture = response.getString(KEY_PROFILE_PICTURE);
                        firstName = response.getString(KEY_FIRST_NAME);
                        lastName = response.getString(KEY_LAST_NAME);
                        startDate = response.getString(KEY_START_DATE);
                        endDate = response.getString(KEY_END_DATE);
                        userID = response.getInt(KEY_USER_ID);
                        mobileNumber = response.getString(KEY_MOBILE_NUMBER);
                        specialNote = response.getString(KEY_SPECIAL_NOTE);
                        serviceType = response.getString(KEY_SERVICE_TYPE);

                        if (serviceType.equalsIgnoreCase("Chauffeur")){
                            frameDriverName.setVisibility(View.VISIBLE);
                            frameDriverMobileNumber.setVisibility(View.VISIBLE);
                            sms2.setVisibility(View.VISIBLE);
                            call2.setVisibility(View.VISIBLE);


                            driverFullName = response.getString(KEY_DRIVER_FULL_NAME);
                            driverMobileNumber = response.getString(KEY_DRIVER_MOBILE_NUMBER);

                            tvDriverMobileNumber.setText(driverMobileNumber);
                            tvDriverFullName.setText(driverFullName);
                        }

                        if (specialNote.equalsIgnoreCase("null")){
                            specialNote = "N/A";
                        }

                        session.setOwnerUserID(userID, firstName, lastName, profilePicture);

                        Geocoder geocoder;
                        geocoder = new Geocoder(getActivity(), Locale.getDefault());
                        List<Address> addresses = null;
                        try {
                            addresses = geocoder.getFromLocation(Double.parseDouble(latIssue), Double.parseDouble(longIssue), 1);
                            address = addresses.get(0).getAddressLine(0);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        List<Address> addresses2 = null;
                        try {
                            addresses2 = geocoder.getFromLocation(Double.parseDouble(latReturn), Double.parseDouble(longReturn), 1);
                            address2 = addresses2.get(0).getAddressLine(0);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        setView();

                        tvBrand.setText(brandName + " " + modelName + " " + modelYear + " " + color);
                        tvName.setText(firstName + " " + lastName);
                        tvPickup.setText(address);
                        tvReturn.setText(address2);
                        tvAmount.setText("₱" + amount);
                        tvTotalAmount.setText("₱" + totalAmount);
                        tvCarType.setText(carType);
                        tvStartDate.setText(startDate);
                        tvEndDate.setText(endDate);
                        tvSpecialNote.setText(specialNote);

                        UrlBean getPic = new UrlBean();
                        String getPickUrl = getPic.getProfilePicUrl()+profilePicture;

                        Glide.with(getActivity())
                                .load(getPickUrl)
                                .into(proppic);

                        getRating();

                    } else {

                        Toast.makeText(getActivity().getApplicationContext(), response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                //Display error message whenever an error occurs
                Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsArrayRequest);
    }

    public void getRating(){
        UrlBean urlBeaners = new UrlBean();
        String urlRating = urlBeaners.getGetAverageRatingProfile();

        final JSONObject request = new JSONObject();
        try {
            request.put(KEY_USER_ID, userID);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(Request.Method.POST, urlRating, request, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //Check if user got logged in successfully
                    if (response.getInt(KEY_STATUS) == 0) {
                        double rate = response.getDouble(KEY_TOTAL_RATING);

                        DecimalFormat df = new DecimalFormat("#.#");
                        df.format(rate);

                        tvRating.setText(String.valueOf(rate));
                    } else {

                        Toast.makeText(getActivity().getApplicationContext(), response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                //Display error message whenever an error occurs
                Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsArrayRequest);
    }

    public void setView(){
        if (bookingID != 0){
            view1.setVisibility(View.VISIBLE);
            view2.setVisibility(View.GONE);

            if (paymentID != 0){
                btnpaypal.setVisibility(View.GONE);
                tvPaid.setText("Payment: Paid");
            }else {
                tvPaid.setText("Payment: Not Paid");
            }

        }
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
