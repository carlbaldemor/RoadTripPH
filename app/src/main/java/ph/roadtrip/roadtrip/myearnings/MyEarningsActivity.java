package ph.roadtrip.roadtrip.myearnings;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ph.roadtrip.roadtrip.BaseOwnerActivity;
import ph.roadtrip.roadtrip.DashboardActivity;
import ph.roadtrip.roadtrip.DashboardOwnerActivity;
import ph.roadtrip.roadtrip.R;
import ph.roadtrip.roadtrip.carmanagement.CarRecord;
import ph.roadtrip.roadtrip.classes.MySingleton;
import ph.roadtrip.roadtrip.classes.SessionHandler;
import ph.roadtrip.roadtrip.classes.UrlBean;
import ph.roadtrip.roadtrip.classes.User;

public class MyEarningsActivity extends BaseOwnerActivity {

    private static final String KEY_STATUS = "status1";
    private static final String KEY_OWNER_ID = "ownerID";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_TOTAL_EARNINGS = "totalEarnings";
    private static final String KEY_TOTAL_AVERAGE = "totalAverage";
    private static final String KEY_TOTAL_TRIPS = "totalTrips";
    private static final String KEY_TOTAL_BOOKINGS = "totalBookings";
    private static final String KEY_TOTAL_CANCEL = "totalCancel";
    private static final String KEY_SELECTED_YEAR = "selectedYear";
    private static final String KEY_TOTAL_YEAR = "totalEarningsYear";
    private static final String KEY_TOTAL_MONTH = "totalEarningsMonth";
    private static final String KEY_TOTAL_WEEK = "totalEarningsWeek";
    private static final String KEY_START_DATE = "startDate";
    private static final String KEY_END_DATE = "endDate";

    private TextView tvTotalEarnings, tvTotalEarningsDef, tvTotalCancel, tvTotalAverage, tvTotalTrips,
            tvTotalYear, tvTotalMonth, tvTotalWeek;
    private Button btnFilter, btnDefault;
    private int ownerID;
    private SessionHandler session;

    private DatePicker datePicker;
    private Calendar calendar;
    private TextView startDate, endDate;
    private int year, month, day;
    private String sdate, edate;
    private String dateStart, dateEnd, startTime, returnTime, serviceType, myDate, myDate2;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("InflateParams")
            View contentView = inflater.inflate(R.layout.activity_earnings, null, false);
            drawer.addView(contentView, 0);

            //Get Owner ID
            session = new SessionHandler(getApplicationContext().getApplicationContext());
            CarRecord carRecord = session.getOwnerID();
            ownerID = carRecord.getOwnerID();

            tvTotalEarnings = findViewById(R.id.tvTotalEarnings);
            tvTotalEarningsDef = findViewById(R.id.tvTotalEarningsDef);
            tvTotalCancel = findViewById(R.id.tvTotalCancel);
            tvTotalAverage = findViewById(R.id.tvTotalAverage);
            tvTotalTrips = findViewById(R.id.tvTotalTrips);
            tvTotalYear = findViewById(R.id.tvTotalYear);
            tvTotalMonth = findViewById(R.id.tvTotalMonth);
            tvTotalWeek = findViewById(R.id.tvTotalWeek);
            btnFilter = findViewById(R.id.btnFilter);
            btnDefault = findViewById(R.id.btnDefault);

            startDate = findViewById(R.id.start_date);
            endDate = findViewById(R.id.end_date);
            calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);

            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            showDate(year, month+1, day);

            onLoad();



            btnFilter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    sdate = startDate.getText().toString();
                    edate = endDate.getText().toString();


                    //Convert DateTime
                    DateFormat inFormat = new SimpleDateFormat( "yyyy-MM-dd");
                    DateFormat outFormat = new SimpleDateFormat( "yyyyMMdd");


                    Date date = null;
                    Date date2 = null;
                    try {
                        date = inFormat.parse(sdate);
                        date2 = inFormat.parse(edate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                    if (date != null && date2 != null) {

                        myDate = outFormat.format(date);
                        myDate2 = outFormat.format(date2);

                        filterResult();
                    }
                }
                });

            btnDefault.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onLoad();
                }
            });

        }

    private void filterResult(){
        UrlBean url = new UrlBean();
        String getFilterEarnings = url.getFilterEarnings();

        JSONObject request = new JSONObject();
        try {
            request.put(KEY_OWNER_ID, ownerID);
            request.put(KEY_START_DATE, myDate);
            request.put(KEY_END_DATE, myDate2);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, getFilterEarnings, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //Check if user got registered successfully
                            if (response.getInt(KEY_STATUS) == 0) {
                                tvTotalEarningsDef.setText("P" + response.getString(KEY_TOTAL_EARNINGS));
                                tvTotalCancel.setText(response.getString(KEY_TOTAL_CANCEL));
                                tvTotalAverage.setText("P" + response.getString(KEY_TOTAL_AVERAGE));
                                tvTotalTrips.setText(response.getString(KEY_TOTAL_TRIPS));

                                Toast.makeText(getApplicationContext(),
                                        response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();
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

    private void onLoad(){
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
                                tvTotalEarnings.setText("P" + response.getString(KEY_TOTAL_EARNINGS));
                                tvTotalEarningsDef.setText("P" + response.getString(KEY_TOTAL_EARNINGS));
                                tvTotalCancel.setText(response.getString(KEY_TOTAL_CANCEL));
                                tvTotalAverage.setText("P" + response.getString(KEY_TOTAL_AVERAGE));
                                tvTotalTrips.setText(response.getString(KEY_TOTAL_TRIPS));
                                tvTotalYear.setText("+ P" + response.getString(KEY_TOTAL_YEAR));
                                tvTotalMonth.setText("+ P" + response.getString(KEY_TOTAL_MONTH));
                                tvTotalWeek.setText("+ P" + response.getString(KEY_TOTAL_WEEK));

                                Toast.makeText(getApplicationContext(),
                                        response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();
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

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);

    }

    @SuppressWarnings("deprecation")
    public void setEndDate(View view) {
        showDialog(998);

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        if (id == 998){
            return new DatePickerDialog(this,
                    myEndDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2+1, arg3);
                }
            };

    private DatePickerDialog.OnDateSetListener myEndDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showEndDate(arg1, arg2+1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        startDate.setText(new StringBuilder().append(year).append("-")
                .append(month).append("-").append(day));
    }

    private void showEndDate(int year, int month, int day) {
        endDate.setText(new StringBuilder().append(year).append("-")
                .append(month).append("-").append(day));
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
