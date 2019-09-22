package ph.roadtrip.roadtrip.profile;

import android.content.Intent;
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
import ph.roadtrip.roadtrip.SplashActivity;
import ph.roadtrip.roadtrip.carmanagement.CarRecord;
import ph.roadtrip.roadtrip.classes.MySingleton;
import ph.roadtrip.roadtrip.classes.SessionHandler;
import ph.roadtrip.roadtrip.classes.UrlBean;
import ph.roadtrip.roadtrip.classes.User;

public class PromptDeactivateAccountOwnerFragment extends Fragment {

    private TextView tvStartDate, tvEndDate, tvBrandName, tvModelName, tvFullname, tvTotalAmount, tvCarType, tvServiceType;
    private Button btnAccept, btnDecline, btnDeactivate, btnCancel;

    private int recordID;
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
    private int ownerID;
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
    private static final String KEY_RECORD_ID = "recordID";
    private static final String KEY_OWNER_ID = "ownerID";

    private String fetch_booking_data;
    private String deactivate_record;
    private String cancel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_prompt_deactivate_account, container, false);

        session = new SessionHandler(getActivity().getApplicationContext());

        User user = session.getUserDetails();
        userID = user.getUserID();
        CarRecord carRecord = session.getOwnerID();
        ownerID = carRecord.getOwnerID();

        Toast.makeText(getActivity(), String.valueOf(ownerID), Toast.LENGTH_LONG).show();

        UrlBean urlBean = new UrlBean();
        deactivate_record = urlBean.getDeactivate_account_owner(); // change this

        btnDeactivate = view.findViewById(R.id.btnDeactivate);
        btnCancel = view.findViewById(R.id.btnCancel);

        btnDeactivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Deactivate Account
                deactivate();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Decline Booking Offer
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new EditProfileFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return view;
    }

    public void deactivate(){
        final JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            if (ownerID == 0 || userID == 0){
                Toast.makeText(getActivity().getApplicationContext(), "Record ID is 0", Toast.LENGTH_SHORT).show();
            } else {
                request.put(KEY_OWNER_ID, ownerID);
                request.put(KEY_USER_ID, userID);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(Request.Method.POST, deactivate_record, request, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //Check if user got logged in successfully

                    if (response.getInt(KEY_STATUS) == 0) {

                        //Go to Success page
                        session.logoutUser();
                        Intent load = new Intent(getActivity(), SplashActivity.class);
                        startActivity(load);
                        getActivity().finish();
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
