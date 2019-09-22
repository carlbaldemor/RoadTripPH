package ph.roadtrip.roadtrip.myearnings;
import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import ph.roadtrip.roadtrip.DashboardActivity;
import ph.roadtrip.roadtrip.DashboardOwnerActivity;
import ph.roadtrip.roadtrip.R;
import ph.roadtrip.roadtrip.carmanagement.CarRecord;
import ph.roadtrip.roadtrip.classes.MySingleton;
import ph.roadtrip.roadtrip.classes.SessionHandler;
import ph.roadtrip.roadtrip.classes.UrlBean;
import ph.roadtrip.roadtrip.classes.User;

public class MyEarningsActivity extends DashboardOwnerActivity {

    private static final String KEY_STATUS = "status1";
    private static final String KEY_OWNER_ID = "ownerID";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_TOTAL_EARNINGS = "totalEarnings";
    private static final String KEY_TOTAL_AVERAGE = "totalAverage";
    private static final String KEY_TOTAL_TRIPS = "totalTrips";
    private static final String KEY_TOTAL_BOOKINGS = "totalBookings";
    private static final String KEY_TOTAL_CANCEL = "totalCancel";
    private static final String KEY_SELECTED_YEAR = "selectedYear";

    private TextView tvTotalEarnings, tvTotalAverage, tvTotalTrips, tvTotalBookings, tvTotalCancel;
    private TextView tvTotalEarningsYear, tvTotalAverageYear, tvTotalTripsYear, tvTotalBookingsYear, tvTotalCancelYear;
    private String totalEarnings, totalTrips, totalBookings, totalCancel;
    private String totalEarningsYear, totalTripsYear, totalBookingsYear, totalCancelYear, selectedYear;
    private Double totalAverage;
    private int ownerID;
    private SessionHandler session;
    int size;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //setContentView(R.layout.activity_my_earnings);
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("InflateParams")
            View contentView = inflater.inflate(R.layout.activity_my_earnings, null, false);
            drawer.addView(contentView, 0);

            final Spinner mySpinner = (Spinner) findViewById(R.id.SpinnerFilter);
            final Spinner SpinnerYear = (Spinner) findViewById(R.id.SpinnerYear);

            session = new SessionHandler(getApplicationContext());
            CarRecord carRecord = session.getOwnerID();
            ownerID = carRecord.getOwnerID();

            //Total
            tvTotalEarnings = findViewById(R.id.totalEarnings);
            tvTotalAverage = findViewById(R.id.average);
            tvTotalTrips = findViewById(R.id.totalTrips);
            tvTotalBookings = findViewById(R.id.totalBookings);
            tvTotalCancel = findViewById(R.id.totalCancel);

            tvTotalEarnings.setVisibility(View.VISIBLE);
            tvTotalAverage.setVisibility(View.VISIBLE);

            //Total Year
            tvTotalEarningsYear = findViewById(R.id.totalEarningsYear);
            tvTotalAverageYear = findViewById(R.id.averageYear);
            tvTotalTripsYear = findViewById(R.id.totalTripsYear);
            tvTotalBookingsYear = findViewById(R.id.totalBookingsYear);
            tvTotalCancelYear = findViewById(R.id.totalCancelYear);

            mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    int spinner_pos = mySpinner.getSelectedItemPosition();
                    String[] size_values = getResources().getStringArray(R.array.filter_size);
                    int size = Integer.valueOf(size_values[spinner_pos]);

                    switch (size){
                        case 1: // TOTAL
                            hideYear();

                            //Hide Spinners
                            SpinnerYear.setVisibility(View.GONE);

                            //Show
                            tvTotalEarnings.setVisibility(View.VISIBLE);
                            tvTotalAverage.setVisibility(View.VISIBLE);
                            tvTotalTrips.setVisibility(View.VISIBLE);
                            tvTotalBookings.setVisibility(View.VISIBLE);
                            tvTotalCancel.setVisibility(View.VISIBLE);

                            break;

                        case 2: // YEAR
                            hideTotal();

                            //Show Year Spinner
                            SpinnerYear.setVisibility(View.VISIBLE);

                            //Show
                            tvTotalEarningsYear.setVisibility(View.VISIBLE);
                            tvTotalAverageYear.setVisibility(View.VISIBLE);
                            tvTotalTripsYear.setVisibility(View.VISIBLE);
                            tvTotalBookingsYear.setVisibility(View.VISIBLE);
                            tvTotalCancelYear.setVisibility(View.VISIBLE);

                            selectedYear = SpinnerYear.getSelectedItem().toString();

                            break;

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });



            //Call The methods for Total
            getTotalEarnings(); // get Total Earnings
            getTotalAverage(); // get Total Average
            getTotalTrips(); // get Total Trips
            getTotalBookings(); // get Total Bookings
            getTotalCancellations(); // get Total Cancellations

        }

        public void hideTotal(){
            //Hide Total Textviews
            tvTotalEarnings.setVisibility(View.GONE);
            tvTotalAverage.setVisibility(View.GONE);
            tvTotalTrips.setVisibility(View.GONE);
            tvTotalBookings.setVisibility(View.GONE);
            tvTotalCancel.setVisibility(View.GONE);
        }

        public void hideYear(){

            //Hide YEAR textviews
            tvTotalEarningsYear.setVisibility(View.GONE);
            tvTotalAverageYear.setVisibility(View.GONE);
            tvTotalTripsYear.setVisibility(View.GONE);
            tvTotalBookingsYear.setVisibility(View.GONE);
            tvTotalCancelYear.setVisibility(View.GONE);
        }


        public void  calculateYear(){

        }


        public void getTotalEarnings(){
            UrlBean url = new UrlBean();
            String getTotalEarnings = url.getTotalEarnings();
            JSONObject request = new JSONObject();
            try {
                request.put(KEY_OWNER_ID, ownerID);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                    (Request.Method.POST, getTotalEarnings, request, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                //Check if user got registered successfully
                                if (response.getInt(KEY_STATUS) == 0) {
                                    totalEarnings = response.getString(KEY_TOTAL_EARNINGS);

                                    tvTotalEarnings.setText(" ₱" + totalEarnings);

                                    tvTotalEarningsYear.setText(" ₱" + totalEarnings);


                                } else{
                                    Toast.makeText(getApplicationContext(),
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
                            Toast.makeText(getApplicationContext(),
                                    error.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });

            // Access the RequestQueue through your singleton class.
            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsArrayRequest);
        }

    public void getTotalAverage(){
        UrlBean url = new UrlBean();
        String getTotalAverage = url.getTotalAverage();
        JSONObject request = new JSONObject();
        try {
            request.put(KEY_OWNER_ID, ownerID);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, getTotalAverage, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //Check if user got registered successfully
                            if (response.getInt(KEY_STATUS) == 0) {

                                Double totalAverage = response.getDouble(KEY_TOTAL_AVERAGE);

                                DecimalFormat df = new DecimalFormat("#.#");
                                df.format(totalAverage);

                                tvTotalAverage.setText(" ₱" + String.valueOf(totalAverage));
                                tvTotalAverageYear.setText(" ₱" + String.valueOf(totalAverage));


                            } else{
                                Toast.makeText(getApplicationContext(),
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
                        Toast.makeText(getApplicationContext(),
                                error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsArrayRequest);
    }

    public void getTotalTrips(){
        UrlBean url = new UrlBean();
        String getTotalTrips = url.getTotalTrips();
        JSONObject request = new JSONObject();
        try {
            request.put(KEY_OWNER_ID, ownerID);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, getTotalTrips, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //Check if user got registered successfully
                            if (response.getInt(KEY_STATUS) == 0) {
                                totalTrips = response.getString(KEY_TOTAL_TRIPS);

                                tvTotalTrips.setText(" " + totalTrips);
                                tvTotalTripsYear.setText(" " + totalTrips);


                            } else{
                                Toast.makeText(getApplicationContext(),
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
                        Toast.makeText(getApplicationContext(),
                                error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsArrayRequest);
    }

    public void getTotalBookings(){
        UrlBean url = new UrlBean();
        String getTotalBookings = url.getTotalBookings();
        JSONObject request = new JSONObject();
        try {
            request.put(KEY_OWNER_ID, ownerID);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, getTotalBookings, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //Check if user got registered successfully
                            if (response.getInt(KEY_STATUS) == 0) {
                                totalBookings = response.getString(KEY_TOTAL_BOOKINGS);

                                tvTotalBookings.setText(totalBookings);
                                tvTotalBookingsYear.setText(totalBookings);


                            } else{
                                Toast.makeText(getApplicationContext(),
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
                        Toast.makeText(getApplicationContext(),
                                error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsArrayRequest);
    }

    public void getTotalCancellations(){
        UrlBean url = new UrlBean();
        String getTotalCancel = url.getTotalCancel();
        JSONObject request = new JSONObject();
        try {
            request.put(KEY_OWNER_ID, ownerID);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, getTotalCancel, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //Check if user got registered successfully
                            if (response.getInt(KEY_STATUS) == 0) {
                                totalCancel = response.getString(KEY_TOTAL_CANCEL);

                                tvTotalCancel.setText(totalCancel);
                                tvTotalCancelYear.setText(totalCancel);

                            } else{
                                Toast.makeText(getApplicationContext(),
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
                        Toast.makeText(getApplicationContext(),
                                error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsArrayRequest);
    }

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getSupportFragmentManager().popBackStack();
        }

    }

    }
