package ph.roadtrip.roadtrip.transactionhistory;

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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import ph.roadtrip.roadtrip.classes.MySingleton;
import ph.roadtrip.roadtrip.classes.SessionHandler;
import ph.roadtrip.roadtrip.classes.UrlBean;
import ph.roadtrip.roadtrip.classes.User;
import ph.roadtrip.roadtrip.R;


public class AddCarReviewFragment extends Fragment {

    private static final String KEY_MESSAGE = "message";
    private static final String KEY_STATUS = "status1";
    private static final String KEY_OWNER_ID = "ownerID";
    private static final String KEY_BOOKING_ID = "bookingID";
    private static final String KEY_REVIEW_MESSAGE = "reviewMessage";
    private static final String KEY_RATING = "rating";
    private static final String KEY_USER_ID = "userID";
    private static final String KEY_CAR_ID = "carID";
    private static final String KEY_EMPTY = "";

    private RatingBar ratingBar, rbQuality, rbSafety, rbCleanliness;
    private TextView txtRatingValue;
    private EditText etMessage;
    private Button btnSubmit;
    private SessionHandler session;
    private int bookingID;
    private int ownerID;
    private int userID;
    private int carID;
    private String message;
    private int rating1, rating2, rating3;
    private String addReviewUrl;
    private Double totalRating;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_review_car, container, false);

        //Get UserID
        session = new SessionHandler(getActivity().getApplicationContext());
        User user = session.getUserDetails();
        userID = user.getUserID();
        Booking booking = session.getCarReview();
        bookingID = booking.getBookingID();
        carID = booking.getCarID();

        rbQuality = (RatingBar) view.findViewById(R.id.rbQuality);
        rbSafety = (RatingBar) view.findViewById(R.id.rbSafety);
        rbCleanliness = (RatingBar) view.findViewById(R.id.rbCleanliness);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        etMessage = (EditText) view.findViewById(R.id.etComment);

        UrlBean url = new UrlBean();
        addReviewUrl = url.getCarAddReview();

        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (validateInputs()) {
                    message = etMessage.getText().toString();
                    rating1 = Math.round(rbQuality.getRating());
                    rating2 = Math.round(rbSafety.getRating());
                    rating3 = Math.round(rbCleanliness.getRating());
                    totalRating = (((double) rating1 + (double) rating2 + (double) rating3 ) / 3);
                    addReview();
                }

            }

        });

        return view;
    }

    public boolean validateInputs(){
        if (etMessage.getText().toString().equalsIgnoreCase(KEY_EMPTY)){
            etMessage.setError("Comment cannot be empty!");
            etMessage.requestFocus();
            return false;
        }
        return true;
    }

    public void addReview(){
        JSONObject request = new JSONObject();
        try {
            DecimalFormat df = new DecimalFormat("#.00");
            //Populate the request parameters
            request.put(KEY_BOOKING_ID, bookingID);
            request.put(KEY_CAR_ID, carID);
            request.put(KEY_REVIEW_MESSAGE, message);
            request.put(KEY_RATING, String.valueOf(df.format((totalRating))));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, addReviewUrl, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //Check if user got registered successfully
                            if (response.getInt(KEY_STATUS) == 0) {

                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.fragment_container, new SuccessReviewFragment());
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
