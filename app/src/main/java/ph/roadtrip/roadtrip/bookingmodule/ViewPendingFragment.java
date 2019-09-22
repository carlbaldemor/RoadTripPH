package ph.roadtrip.roadtrip.bookingmodule;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import ph.roadtrip.roadtrip.R;
import ph.roadtrip.roadtrip.classes.MySingleton;
import ph.roadtrip.roadtrip.classes.SessionHandler;
import ph.roadtrip.roadtrip.classes.UrlBean;
import ph.roadtrip.roadtrip.classes.User;
import ph.roadtrip.roadtrip.profile.ListReviewProfileFragment;

public class ViewPendingFragment extends Fragment {

    private TextView tvStartDate, tvEndDate, tvBrandName, tvModelName, tvFullname, tvTotalAmount, tvCarType, tvServiceType;
    private Button btnCancel, btnGoBack;

    private int bookingID;
    private int renter_userID;
    private String startDate;
    private String endDate;
    private String brandName;
    private String modelName;
    private String firstName;
    private String lastName;
    private String totalAmount;
    private String carType;
    private String serviceType;
    private int userID;
    private SessionHandler session;

    private static final String KEY_MESSAGE = "message";
    private static final String KEY_STATUS  = "status1";
    private static final String KEY_BOOKING_ID = "bookingID";
    private static final String KEY_FIRST_NAME = "firstName";
    private static final String KEY_LAST_NAME = "lastName";
    private static final String KEY_CAR_TYPE = "carType";
    private static final String KEY_SERVICE_TYPE = "serviceType";
    private static final String KEY_START_DATE = "startDate";
    private static final String KEY_END_DATE = "endDate";
    private static final String KEY_TOTAL_AMOUNT = "totalAmount";
    private static final String KEY_BRAND_NAME = "brandName";
    private static final String KEY_MODEL_NAME = "modelName";
    private static final String KEY_RENTER_USER_ID = "renter_userID";
    private static final String KEY_USER_ID = "userID";

    private String fetch_booking_data;
    private String accept_booking;
    private String decline_booking;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_pending_offer, container, false);

        session = new SessionHandler(getActivity().getApplicationContext());

        BookingRequests bookingRequests = session.getBookingID();
        bookingID = bookingRequests.getBookingID();

        User user = session.getUserDetails();
        userID = user.getUserID();

        UrlBean urlBean = new UrlBean();
        fetch_booking_data = urlBean.getView_booking_request();
        onBackground();

        tvStartDate = view.findViewById(R.id.tvStartDate);
        tvEndDate = view.findViewById(R.id.tvEndDate);
        tvBrandName = view.findViewById(R.id.tvBrandName);
        tvModelName = view.findViewById(R.id.tvModel);
        tvFullname = view.findViewById(R.id.tvName);
        tvTotalAmount = view.findViewById(R.id.tvTotalAmount);
        tvCarType = view.findViewById(R.id.tvCarType);
        tvServiceType = view.findViewById(R.id.tvServiceType);

        btnCancel = view.findViewById(R.id.btnCancel);
        btnGoBack = view.findViewById(R.id.btnGoBack);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Go to cancel page
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new CancelBookingFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Go back to list
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new BookingFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return view;
    }

    public void onBackground() {
        final JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            if (bookingID == 0){
                Toast.makeText(getActivity().getApplicationContext(), "Record ID is 0", Toast.LENGTH_SHORT).show();
            } else {
                request.put(KEY_BOOKING_ID, bookingID);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(Request.Method.POST, fetch_booking_data, request, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //Check if user got logged in successfully

                    if (response.getInt(KEY_STATUS) == 0) {
                        startDate = response.getString(KEY_START_DATE);
                        endDate = response.getString(KEY_END_DATE);
                        brandName = response.getString(KEY_BRAND_NAME);
                        modelName = response.getString(KEY_MODEL_NAME);
                        firstName = response.getString(KEY_FIRST_NAME);
                        lastName = response.getString(KEY_LAST_NAME);
                        totalAmount = response.getString(KEY_TOTAL_AMOUNT);
                        carType = response.getString(KEY_CAR_TYPE);
                        serviceType = response.getString(KEY_SERVICE_TYPE);
                        renter_userID = response.getInt(KEY_RENTER_USER_ID);

                        tvStartDate.setText(startDate);
                        tvEndDate.setText(endDate);
                        tvBrandName.setText(brandName);
                        tvModelName.setText(modelName);
                        tvFullname.setText(firstName + " " + lastName);
                        tvTotalAmount.setText(totalAmount);
                        tvCarType.setText(carType);
                        tvServiceType.setText(serviceType);


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
}
