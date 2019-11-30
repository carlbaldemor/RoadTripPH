package ph.roadtrip.roadtrip.bookingmodule;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
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
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

import ph.roadtrip.roadtrip.R;
import ph.roadtrip.roadtrip.carmanagement.AddCarThreeFragment;
import ph.roadtrip.roadtrip.classes.MySingleton;
import ph.roadtrip.roadtrip.classes.SessionHandler;
import ph.roadtrip.roadtrip.classes.UrlBean;
import ph.roadtrip.roadtrip.classes.User;
import ph.roadtrip.roadtrip.paypal.PayPalActivity;
import ph.roadtrip.roadtrip.paypal.PayPalConfig;
import ph.roadtrip.roadtrip.transactionhistory.Booking;
import ph.roadtrip.roadtrip.transactionhistory.SuccessReviewFragment;

import static ai.api.android.AIDataService.TAG;


public class CancelBookingFragment extends Fragment {

    private static final String KEY_MESSAGE = "message";
    private static final String KEY_STATUS = "status1";
    private static final String KEY_BOOKING_ID = "bookingID";
    private static final String KEY_BOOKING_STATUS = "booking_status";
    private static final String KEY_REASON = "reason";
    private static final String KEY_LOG = "log";
    private static final String KEY_PAYMENT_ID = "paymentID";
    private static final String KEY_PNAME = "pName";
    private static final String KEY_START_DATE = "startDate";
    private static final String KEY_TOTAL_AMOUNT = "totalAmount";
    private static final String KEY_TOTAL_REFUND = "totalRefund";
    private static final String KEY_USER_ID = "userID";
    private static final String KEY_REFERENCE = "reference";

    private TextView tvStatusPaypal, tvPaypal;
    private EditText etMessage;
    private Button btnSubmit;
    private SessionHandler session;
    private int bookingID;
    private int userID;
    private int carowner_userID;
    private String reason;
    private String startDate, paymentID, pName;
    private int rating;
    private String addReviewUrl;
    private String reference;
    private String usertype;
    private String booking_status;
    private String cancelbooking, checkpaymentURL, insertrefund;
    private Double totalAmount, paypalFee, totalRefund;
    private boolean paidPaypal = false;
    private ProgressDialog pDialog;

    //Paypal intent request code to track onActivityResult method
    public static final int PAYPAL_REQUEST_CODE = 123;

    //Paypal Configuration Object
    private static PayPalConfiguration config = new PayPalConfiguration()
            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PayPalConfig.PAYPAL_CLIENT_ID);

    private static final String URL = "https://api.sandbox.paypal.com/v1/payments/sale/%s/refund";
    private static final String DATA = "{\"amount\":{\"total\": %s,\"currency\": \"%s\"}}";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cancel_booking, container, false);

        //Get UserID
        session = new SessionHandler(getActivity().getApplicationContext());
        User user = session.getUserDetails();
        userID = user.getUserID();
        usertype = user.getUserTypeID();

        tvStatusPaypal = view.findViewById(R.id.tvStatusPaypal);
        tvPaypal = view.findViewById(R.id.tvPaypal);

        if (usertype.equalsIgnoreCase("1")){
            booking_status = "Canceled by Car Renter";
        } else {
            booking_status = "Canceled by Car Owner";
        }

        UrlBean url = new UrlBean();
        cancelbooking = url.getCancelBooking();
        checkpaymentURL = url.getCheckpayment();
        insertrefund = url.getInsertpaypalrefund();

        Booking booking = session.getBookingIDHistory();
        bookingID = booking.getBookingID();

        //Check if Paid through Paypal
        checkPayment();

        //carowner_userID = booking.getCarowner_userID();

        btnSubmit = (Button) view.findViewById(R.id.button2);
        etMessage = (EditText) view.findViewById(R.id.etMessage);

        pDialog = new ProgressDialog(getActivity());
        // Showing progress dialog before making http request
        pDialog.setMessage("Refunding Payment....");

        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(validateInputs()){

                    cancelBooking();
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

    public void checkPayment(){
        JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            request.put(KEY_BOOKING_ID, bookingID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, checkpaymentURL, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //Check if user got registered successfully
                            if (response.getInt(KEY_STATUS) == 0) {
                                pName = response.getString(KEY_PNAME);
                                startDate = response.getString(KEY_START_DATE);
                                paymentID = response.getString(KEY_PAYMENT_ID);
                                totalAmount = response.getDouble(KEY_TOTAL_AMOUNT);

                                if(pName.equalsIgnoreCase("Paypal")){

                                    tvStatusPaypal.setVisibility(View.VISIBLE);
                                    tvPaypal.setVisibility(View.VISIBLE);
                                    //Convert DateTime
                                    final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    Date current = new Date();
                                    final String currentDateFinal = formatter.format(current);

                                    DateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                    DateFormat outFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                                    Date date = null;
                                    Date date2 = null;
                                    Date date3 = null;

                                    try {
                                        date = inFormat.parse(startDate);
                                        date2 = inFormat.parse(currentDateFinal);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    if (date != null && date2 != null) {

                                        if (getDifferenceDays(date2, date) > 2){
                                            tvPaypal.setText("Eligible");
                                            //tvPaypal.setTextColor(Color.parseColor("#FFFAFA"));
                                            paidPaypal = true;
                                            paypalFee = totalAmount*0.03;
                                            totalRefund = totalAmount-paypalFee;
                                            reference = generateString();


                                            //Toast.makeText(getActivity(), "paypalFee: " + String.valueOf(paypalFee) + " totalRefund: " + String.valueOf(totalRefund) + " reference: " + reference, Toast.LENGTH_LONG).show();


                                        } else {
                                            tvPaypal.setText("Not Eligible");
                                            //tvPaypal.setTextColor(getResources().getColor(R.color.));
                                        }
                                    }
                                }
                                //Toast.makeText(getActivity(), "Start Date: " + startDate + " PaymentID: " + paymentID + " pName: " + pName, Toast.LENGTH_LONG).show();
                            } else{
                                Toast.makeText(getActivity(), response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();
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

    public static long getDifferenceDays(Date d1, Date d2) {
        long diff = d2.getTime() - d1.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    public static String generateString() {
        String uuid = UUID.randomUUID().toString();
        return "REF-" + uuid;
    }


    public void cancelBooking(){

        reason = etMessage.getText().toString();
        String log = booking_status + " Reason: " + reason;

        JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            request.put(KEY_BOOKING_ID, bookingID);
            request.put(KEY_REASON, reason);
            request.put(KEY_BOOKING_STATUS, booking_status);
            request.put(KEY_LOG, log);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, cancelbooking, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //Check if user got registered successfully
                            if (response.getInt(KEY_STATUS) == 0) {

                                if (paidPaypal){
                                    pDialog.show();
                                    refund();
                                    SuccessRefundFragment ldf = new SuccessRefundFragment();
                                    Bundle args = new Bundle();
                                    args.putDouble("totalRefund", totalRefund);
                                    args.putDouble("paypalFee", paypalFee);
                                    args.putString("reference", reference);
                                    args.putDouble("totalAmount", totalAmount);
                                    ldf.setArguments(args);

                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.fragment_container, ldf);
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.commit();
                                } else {
                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.fragment_container, new SuccessCancelFragment());
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.commit();
                                }



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

    private void refund(){
        JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            request.put(KEY_USER_ID, userID);
            request.put(KEY_TOTAL_REFUND, totalRefund);
            request.put(KEY_REFERENCE, reference);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, insertrefund, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hidePDialog();
                        try {
                            //Check if user got registered successfully
                            if (response.getInt(KEY_STATUS) == 0) {

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

    private void refundPayment() {
        //Getting the amount from editText
        //paymentAmount = editTextAmount.getText().toString();

        //Creating a paypalpayment
        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf("")), "PHP", "Total Fee",
                PayPalPayment.PAYMENT_INTENT_SALE);

        //Creating Paypal Payment activity intent
        Intent intent = new Intent(getActivity(), PaymentActivity.class);

        //putting the paypal configuration to the intent
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        //Puting paypal payment to the intent
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        //Starting the intent activity for result
        //the request code will be used on the method onActivityResult
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
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
