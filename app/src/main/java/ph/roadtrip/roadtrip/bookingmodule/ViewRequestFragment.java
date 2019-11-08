package ph.roadtrip.roadtrip.bookingmodule;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.ImageView;
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
import java.text.NumberFormat;

import ph.roadtrip.roadtrip.classes.MySingleton;
import ph.roadtrip.roadtrip.classes.SessionHandler;
import ph.roadtrip.roadtrip.classes.UrlBean;
import ph.roadtrip.roadtrip.R;
import ph.roadtrip.roadtrip.classes.User;

import static com.google.android.gms.internal.zzbgp.NULL;

public class ViewRequestFragment extends Fragment {

    private TextView tvSpecialNote, tvStartDate, tvEndDate, tvBrandName, tvModelName, tvFullname, tvTotalAmount, tvCarType, tvServiceType;
    private Button btnAccept, btnDecline;
    private ImageView imgPicture;

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
    private static final String KEY_RENTER_PROF_PIC = "renterProfilePicture";
    private static final String KEY_SPECIAL_NOTE = "specialNote";
    private static final String KEY_DRIVER_FULL_NAME = "driverFullName";
    private static final String KEY_DRIVER_MOBILE_NUMBER = "driverMobileNumber";
    private static final String KEY_EMPTY = "";

    private String fetch_booking_data;
    private String accept_booking;
    private String decline_booking;
    private String renterProfilePic;
    private String specialNote;
    private EditText driverName, mobileNumber;
    private String driverFullName = "", driverMobileNumber = "";
    private Button btnAccept2, btnCancel2;
    private ProgressDialog pDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.accept_decline, container, false);

        session = new SessionHandler(getActivity().getApplicationContext());

        BookingRequests bookingRequests = session.getBookingID();
        bookingID = bookingRequests.getBookingID();

        User user = session.getUserDetails();
        userID = user.getUserID();

        pDialog = new ProgressDialog(getActivity());
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        UrlBean urlBean = new UrlBean();
        fetch_booking_data = urlBean.getFetch_bdata();
        accept_booking = urlBean.getAccept_booking();
        decline_booking = urlBean.getDecline_booking();
        onBackground();

        tvStartDate = view.findViewById(R.id.tvStartDate);
        tvEndDate = view.findViewById(R.id.tvEndDate);
        tvBrandName = view.findViewById(R.id.tvBrandName);
        tvModelName = view.findViewById(R.id.tvModel);
        tvFullname = view.findViewById(R.id.tvName);
        tvTotalAmount = view.findViewById(R.id.tvTotalAmount);
        tvCarType = view.findViewById(R.id.tvCarType);
        tvServiceType = view.findViewById(R.id.tvServiceType);
        tvSpecialNote = view.findViewById(R.id.tvSpecialNote);
        imgPicture = view.findViewById(R.id.imgPicture);

        btnAccept = view.findViewById(R.id.btnAccept);
        btnDecline = view.findViewById(R.id.btnDecline);

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (serviceType.equals("Chauffeur")) {
                    callLoginDialog();
                } else if (serviceType.equals("Self-drive")){
                    //Accept Booking Offer
                    acceptBooking();
                }

            }
        });

        btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Decline Booking Offer
                declineBooking();
            }
        });

        return view;
    }

    public void onBackground() {
        final JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            if (bookingID == 0){

            } else {
                request.put(KEY_BOOKING_ID, bookingID);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(Request.Method.POST, fetch_booking_data, request, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hidePDialog();
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
                        renterProfilePic = response.getString(KEY_RENTER_PROF_PIC);
                        specialNote = response.getString(KEY_SPECIAL_NOTE);



                        if (specialNote.equalsIgnoreCase("null")){
                            specialNote = "N/A";
                        }

                        tvStartDate.setText(startDate);
                        tvEndDate.setText(endDate);
                        tvBrandName.setText(brandName);
                        tvModelName.setText(modelName);
                        tvFullname.setText(firstName + " " + lastName);
                        tvTotalAmount.setText(totalAmount);
                        tvCarType.setText(carType);
                        tvServiceType.setText(serviceType);
                        tvSpecialNote.setText(specialNote);

                        //Display Picture of renter
                        UrlBean url = new UrlBean();
                        String profPicUrl = url.getProfilePicUrl();
                        String image = profPicUrl+renterProfilePic;
                        Glide.with(getActivity().getApplicationContext())
                                .load(image)
                                .into(imgPicture);


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

    public void acceptBooking(){
        if (driverFullName.equals("")){
            driverFullName = "";
            driverMobileNumber = "";
        }
        Toast.makeText(getActivity().getApplicationContext(), driverFullName + " " + driverMobileNumber, Toast.LENGTH_SHORT).show();

           final JSONObject request = new JSONObject();
           try {
               //Populate the request parameters
               if (bookingID == 0){

               } else {
                   request.put(KEY_BOOKING_ID, bookingID);
                   request.put(KEY_USER_ID, userID);
                   request.put(KEY_RENTER_USER_ID, renter_userID);
                   request.put(KEY_DRIVER_FULL_NAME, driverFullName);
                   request.put(KEY_DRIVER_MOBILE_NUMBER, driverMobileNumber);
               }
           } catch (JSONException e) {
               e.printStackTrace();
           }
           JsonObjectRequest jsArrayRequest = new JsonObjectRequest(Request.Method.POST, accept_booking, request, new Response.Listener<JSONObject>() {
               @Override
               public void onResponse(JSONObject response) {
                   try {
                       //Check if user got logged in successfully

                       if (response.getInt(KEY_STATUS) == 0) {

                           Toast.makeText(getActivity().getApplicationContext(), response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();
                           FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                           FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                           fragmentTransaction.replace(R.id.fragment_container, new SuccessAcceptFragment());
                           fragmentTransaction.addToBackStack(null);
                           fragmentTransaction.commit();
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

    private void callLoginDialog()
    {
        final Dialog myDialog = new Dialog(getActivity());
        myDialog.setContentView(R.layout.driver_details);
        myDialog.setCancelable(false);

        driverName = (EditText) myDialog.findViewById(R.id.etDriverName);
        mobileNumber = (EditText) myDialog.findViewById(R.id.etDriverMobileNumber);
        btnAccept2 =  (Button) myDialog.findViewById(R.id.btnAccept2);
        btnCancel2 =  (Button) myDialog.findViewById(R.id.btnCancel2);
        myDialog.show();

        btnAccept2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                driverFullName = driverName.getText().toString();
                driverMobileNumber = mobileNumber.getText().toString();

                if (validateInputs() == false) {

                } else {
                    acceptBooking();
                    myDialog.hide();
                }
            }
        });
        btnCancel2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                myDialog.hide();
            }
        });

    }

    public void declineBooking(){
        final JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            if (bookingID == 0){

            } else {
                request.put(KEY_BOOKING_ID, bookingID);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(Request.Method.POST, decline_booking, request, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //Check if user got logged in successfully
                    if (response.getInt(KEY_STATUS) == 0) {

                        Toast.makeText(getActivity().getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
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

    private boolean validateInputs() {

        if (KEY_EMPTY.equals(driverFullName)) {
            driverName.setError("Driver Name cannot be empty");
            driverName.requestFocus();
            return false;
        }

        if (KEY_EMPTY.equals(driverMobileNumber)) {
            mobileNumber.setError("Driver Number cannot be empty");
            mobileNumber.requestFocus();
            return false;
        }
        if (mobileNumber.length() != 11){
            mobileNumber.setError("Mobile Number is Invalid");
            mobileNumber.requestFocus();
            return false;
        }

        return true;
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
