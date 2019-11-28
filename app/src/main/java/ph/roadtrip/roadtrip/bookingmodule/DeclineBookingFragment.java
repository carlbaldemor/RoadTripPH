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
import android.widget.EditText;
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
import ph.roadtrip.roadtrip.transactionhistory.Booking;


public class DeclineBookingFragment extends Fragment {

    private static final String KEY_MESSAGE = "message";
    private static final String KEY_STATUS = "status1";
    private static final String KEY_BOOKING_ID = "bookingID";
    private static final String KEY_BOOKING_STATUS = "booking_status";
    private static final String KEY_REASON = "reason";
    private static final String KEY_LOG = "log";

    private EditText etMessage;
    private Button btnSubmit;
    private SessionHandler session;
    private int bookingID;
    private int userID;
    private int carowner_userID;
    private String reason;
    private int rating;
    private String addReviewUrl;
    private String usertype;
    private String booking_status;
    private String declinebooking;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.decline_booking, container, false);

        //Get UserID
        session = new SessionHandler(getActivity().getApplicationContext());
        User user = session.getUserDetails();
        userID = user.getUserID();
        usertype = user.getUserTypeID();

        Booking booking = session.getBookingIDHistory();
        bookingID = booking.getBookingID();
        //carowner_userID = booking.getCarowner_userID();

        btnSubmit = (Button) view.findViewById(R.id.button2);
        etMessage = (EditText) view.findViewById(R.id.etMessage);

        UrlBean url = new UrlBean();
        declinebooking = url.getDecline_booking();

        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(validateInputs()) {
                    declinebooking();
                }

            }

        });

        return view;
    }

    private boolean validateInputs(){
        if(etMessage.getText().toString().trim().length() == 0){
            etMessage.setError("Message cannot be empty");
            etMessage.requestFocus();
            return false;
        }
        return true;
    }

    public void declinebooking(){

        reason = etMessage.getText().toString();
        String log = "Declined Reason: " + reason;

        JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            request.put(KEY_BOOKING_ID, bookingID);
            request.put(KEY_REASON, reason);
            request.put(KEY_LOG, log);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, declinebooking, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //Check if user got registered successfully
                            if (response.getInt(KEY_STATUS) == 0) {

                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.fragment_container, new SuccessDeclineFragment());
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
