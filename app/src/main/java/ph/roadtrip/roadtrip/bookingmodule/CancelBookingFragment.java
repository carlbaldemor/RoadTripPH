package ph.roadtrip.roadtrip.bookingmodule;

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

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

import ph.roadtrip.roadtrip.R;
import ph.roadtrip.roadtrip.classes.MySingleton;
import ph.roadtrip.roadtrip.classes.SessionHandler;
import ph.roadtrip.roadtrip.classes.UrlBean;
import ph.roadtrip.roadtrip.classes.User;
import ph.roadtrip.roadtrip.paypal.PayPalActivity;
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
    private String usertype;
    private String booking_status;
    private String cancelbooking, checkpaymentURL;
    private boolean paidPaypal = false;

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

        Booking booking = session.getBookingIDHistory();
        bookingID = booking.getBookingID();

        //Check if Paid through Paypal
        checkPayment();

        //carowner_userID = booking.getCarowner_userID();

        btnSubmit = (Button) view.findViewById(R.id.button2);
        etMessage = (EditText) view.findViewById(R.id.etMessage);



        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(validateInputs()){

                    if(paidPaypal){
                        RefundPayment();
                    }
                    //cancelBooking();
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

                                        //Toast.makeText(getActivity().getApplicationContext(), "Start Date: " + String.valueOf(date) + " Current Date : " + String.valueOf(date2), Toast.LENGTH_LONG).show();
                                        //Toast.makeText(getActivity().getApplicationContext(), String.valueOf(getDifferenceDays(date2, date)), Toast.LENGTH_LONG).show();

                                        if (getDifferenceDays(date2, date) > 2){
                                            tvPaypal.setText("Eligible");
                                            //tvPaypal.setTextColor(Color.parseColor("#FFFAFA"));
                                            paidPaypal = true;
                                        } else {
                                            tvPaypal.setText("Not Eligible");
                                            //tvPaypal.setTextColor(getResources().getColor(R.color.));
                                        }
                                    }


                                    //Toast.makeText(getActivity().getApplicationContext(), String.valueOf(currentDateFinal) + " " + startDate, Toast.LENGTH_LONG).show();



                                    paidPaypal = true;
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

                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.fragment_container, new SuccessCancelFragment());
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

    //PayPal Refund

    public class GetAccessToken extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            materialProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            StringBuffer stringBuffer = new StringBuffer("");
            try {
                URL url = new URL("https://api.sandbox.paypal.com/v1/oauth2/token");
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                httpsURLConnection.setRequestMethod("POST");
                httpsURLConnection.addRequestProperty("Accept", "application/json");
                httpsURLConnection.addRequestProperty("Accept-Language", "en_US");
                httpsURLConnection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                String basicAuth = "Basic " + base64;
                httpsURLConnection.setRequestProperty("Authorization", basicAuth);

                String data = "grant_type=client_credentials";

                OutputStreamWriter outputWriter = new OutputStreamWriter(httpsURLConnection.getOutputStream());
                outputWriter.write(data);
                outputWriter.flush();
                outputWriter.close();

                Log.d(TAG, "Response Code; " + httpsURLConnection.getResponseCode());

                InputStream is;

                int status = httpsURLConnection.getResponseCode();

                if (status >= 400)
                    is = httpsURLConnection.getErrorStream();
                else
                    is = httpsURLConnection.getInputStream();

                int read = -1;
                byte[] buffer = new byte[512];
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                while ((read = is.read(buffer)) > 0) {
                    baos.write(buffer, 0, read);
                    baos.flush();
                }

                stringBuffer.append(new String(baos.toByteArray()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return stringBuffer.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            materialProgressBar.setVisibility(View.GONE);
            onGettingAccessToken(s);
        }
    }

    public class GetTransactionDetail extends AsyncTask<String, Void, String> {

        private static final String URL = " https://api.sandbox.paypal.com/v1/payments/payment/%s";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            materialProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            String address = String.format(URL, strings[0]);
            StringBuffer stringBuffer = new StringBuffer("");
            try {
                URL url = new URL(address);
                Log.d(TAG, address);
                showLog(" Payment Id =" + strings[0] + " TOken = " + strings[1]);
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                httpsURLConnection.setRequestMethod("GET");
                httpsURLConnection.addRequestProperty("Content-Type", "application/json");
                String basicAuth = "Bearer " + strings[1];
                Log.d(TAG, basicAuth);
                httpsURLConnection.setRequestProperty("Authorization", basicAuth);
                Log.d(TAG, "Response Code; " + httpsURLConnection.getResponseCode());
                Log.i(TAG, "************GETTING TRANSACTIN  DETAILS ASYNC a********");

                Log.i(TAG, "Payment ID =" + strings[0] + " Access Token = " + strings[1]);


                InputStream is;

                int status = httpsURLConnection.getResponseCode();

                if (status >= 400)
                    is = httpsURLConnection.getErrorStream();
                else
                    is = httpsURLConnection.getInputStream();

                int read = -1;
                byte[] buffer = new byte[512];
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                while ((read = is.read(buffer)) > 0) {
                    baos.write(buffer, 0, read);
                    baos.flush();
                }

                stringBuffer.append(new String(baos.toByteArray()));
                showLog("Transaction Detail =" + stringBuffer.toString());
            } catch (Exception e) {
                e.printStackTrace();
                showLog("Exception " + e.toString());
            }
            return stringBuffer.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            materialProgressBar.setVisibility(View.GONE);
            // parse the json
            onTransactionDetails(s);
        }
    }

    public class RefundPayment extends AsyncTask<String, Void, String> {

        private static final String URL = "https://api.sandbox.paypal.com/v1/payments/sale/%s/refund";
        private static final String DATA = "{\"amount\":{\"total\": %s,\"currency\": \"%s\"}}";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            materialProgressBar.setVisibility(View.VISIBLE);
            showToastAlpha("Starting Payment Refund...");
       /* progressDialog.setMessage("Please wait...");
        progressDialog.show();*/
        }

        @Override
        protected String doInBackground(String... strings) {
            String address = String.format(URL, strings[0]);
            String data;
            if (strings[1] == null || strings[2] == null) {
                data = "{}";
            } else {
                data = String.format(DATA, strings[1], strings[2]);
            }

            StringBuffer stringBuffer = new StringBuffer("");
            try {
                java.net.URL url = new URL(address);
                Log.d(TAG, address);
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                httpsURLConnection.setRequestMethod("POST");
                httpsURLConnection.addRequestProperty("Accept", "application/json");
                httpsURLConnection.addRequestProperty("Accept-Language", "en_US");
                httpsURLConnection.addRequestProperty("Content-Type", "application/json");
                String basicAuth = "Bearer " + strings[3];
                Log.d(TAG, basicAuth);
                httpsURLConnection.setRequestProperty("Authorization", basicAuth);
                Log.i(TAG, "************GETTING REFUND PAYMENT a********");

                Log.i(TAG, "SAle id =" + strings[0] + " Amount to Refund = " + strings[1] + " Currency =" + strings[2] + " Access token  = " + strings[3]);


                OutputStreamWriter outputWriter = new OutputStreamWriter(httpsURLConnection.getOutputStream());
                Log.d(TAG, "Sending: " + data);
                outputWriter.write(data);
                outputWriter.flush();
                outputWriter.close();

                Log.d(TAG, "Response Code; " + httpsURLConnection.getResponseCode());

                InputStream is;

                int status = httpsURLConnection.getResponseCode();

                if (status >= 400)
                    is = httpsURLConnection.getErrorStream();
                else
                    is = httpsURLConnection.getInputStream();

                int read = -1;
                byte[] buffer = new byte[512];
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                while ((read = is.read(buffer)) > 0) {
                    baos.write(buffer, 0, read);
                    baos.flush();
                }

                stringBuffer.append(new String(baos.toByteArray()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return stringBuffer.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            materialProgressBar.setVisibility(View.GONE);
            onRefundPayment(s);
        }
    }



}
