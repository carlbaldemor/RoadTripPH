package ph.roadtrip.roadtrip.bookingmodule;

import android.content.Intent;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import io.grpc.netty.shaded.io.netty.handler.codec.spdy.SpdyHttpResponseStreamIdHandler;
import ph.roadtrip.roadtrip.classes.MySingleton;
import ph.roadtrip.roadtrip.classes.SessionHandler;
import ph.roadtrip.roadtrip.classes.UrlBean;
import ph.roadtrip.roadtrip.R;
import ph.roadtrip.roadtrip.classes.User;
import ph.roadtrip.roadtrip.classes.updateData;

import static com.google.android.gms.internal.zzbgp.NULL;

public class ViewAcceptedBooking extends Fragment {

    private TextView tvSpecialNote, tvStartDate, tvEndDate, tvBrandName, tvModelName, tvFullname, tvTotalAmount, tvCarType, tvServiceType;
    private TextView tvDriverFullName, tvDriverMobileNumber;
    private Button btnAccept, btnDecline;
    private FrameLayout frameDriverMobile, frameDriverFullName;

    private int bookingID;
    private String startDate;
    private String endDate;
    private String brandName;
    private String modelName;
    private String firstName;
    private String lastName;
    private String totalAmount;
    private String carType;
    private String serviceType;
    private String latIssue;
    private String longIssue;
    private String latReturn;
    private String longReturn;
    private boolean overEndDate;
    private int userID;
    private SessionHandler session;

    private static final String KEY_MOBILE_NUMER = "mobileNumber";
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
    private static final String KEY_DRIVER_FULL_NAME = "driverFullName";
    private static final String KEY_DRIVER_MOBILE_NUMBER = "driverMobileNumber";
    private static final String KEY_SPECIAL_NOTE = "specialNote";
    private static final String KEY_OVER_END_DATE = "overEndDate";

    private static final String KEY_LAT_ISSUE = "latIssue";
    private static final String KEY_LONG_ISSUE = "longIssue";
    private static final String KEY_LAT_RETURN = "latReturn";
    private static final String KEY_LONG_RETURN = "longReturn";

    private String fetch_booking_data;
    private String scanPickup;
    private String scanReturn;
    private Button btnPickup, btnReturn, btnCancel, btnComplete;
    private String driverFullName;
    private String driverMobileNumber;
    private String specialNote;
    private String mobileNumber;
    private ImageView sms, call;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_accepted, container, false);

        session = new SessionHandler(getActivity().getApplicationContext());

        BookingRequests bookingRequests = session.getBookingID();
        bookingID = bookingRequests.getBookingID();
        User user = session.getUserDetails();
        userID = user.getUserID();

        final UrlBean urlBean = new UrlBean();
        fetch_booking_data = urlBean.getView_booking_request();
        scanPickup = urlBean.getAccept_booking();
        scanReturn = urlBean.getDecline_booking();
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
        tvDriverFullName = view.findViewById(R.id.tvDriverFullName);
        tvDriverMobileNumber = view.findViewById(R.id.tvDriverMobileNumber);
        frameDriverMobile = view.findViewById(R.id.frameDriverMobile);
        frameDriverFullName = view.findViewById(R.id.frameDriverName);
        sms = view.findViewById(R.id.sms);
        call = view.findViewById(R.id.call);

        btnPickup = view.findViewById(R.id.btnPickup);
        btnReturn = view.findViewById(R.id.btnReturn);
        btnCancel = view.findViewById(R.id.btnCancel);
        btnComplete = view.findViewById(R.id.btnComplete);

        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Accept Booking Offer
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
                //Accept Booking Offer
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mobileNumber));
                startActivity(intent);

            }
        });

        btnPickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Accept Booking Offer
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new ScanQRPickupFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Decline Booking Offer
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new ScanQRPReturnFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.setBookingIDHistory(bookingID, userID);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new CancelBookingFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Complete the booking offer
                String complete = urlBean.getCompletebooking() + bookingID;
                new updateData().execute(complete);

                //Send email to respective users
                //Email Receipt to renter
                String mailreceipt = urlBean.getMail_receipt() + bookingID;
                new updateData().execute(mailreceipt);

                //Email Receipt to owner
                String mailreceiptowner = urlBean.getMail_receipt_owner() + bookingID;
                new updateData().execute(mailreceiptowner);

                //Success Page
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new SuccessCompleteFragment());
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
                        specialNote = response.getString(KEY_SPECIAL_NOTE);
                        latIssue = response.getString(KEY_LAT_ISSUE);
                        longIssue = response.getString(KEY_LONG_ISSUE);
                        latReturn = response.getString(KEY_LAT_RETURN);
                        longReturn = response.getString(KEY_LONG_RETURN);
                        overEndDate = response.getBoolean(KEY_OVER_END_DATE);
                        mobileNumber = response.getString(KEY_MOBILE_NUMER);

                        if (overEndDate){
                            btnComplete.setVisibility(View.VISIBLE);
                        }

                        Toast.makeText(getActivity().getApplicationContext(), String.valueOf(overEndDate), Toast.LENGTH_SHORT).show();

                        if (latIssue.equalsIgnoreCase("null")) {
                            btnReturn.setVisibility(View.GONE);
                        } else if (latReturn.equalsIgnoreCase("null")) {
                            btnPickup.setVisibility(View.GONE);
                        }

                        if (specialNote.equalsIgnoreCase("null")){
                            specialNote = "N/A";
                        }

                        if (serviceType.equals("Chauffeur")){
                            frameDriverFullName.setVisibility(View.VISIBLE);
                            frameDriverMobile.setVisibility(View.VISIBLE);

                            driverFullName = response.getString(KEY_DRIVER_FULL_NAME);
                            driverMobileNumber = response.getString(KEY_DRIVER_MOBILE_NUMBER);
                            tvDriverFullName.setText(driverFullName);
                            tvDriverMobileNumber.setText("+63 " + driverMobileNumber);
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

                        //Toast.makeText(getActivity().getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
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
