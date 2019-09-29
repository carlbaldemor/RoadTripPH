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
            View contentView = inflater.inflate(R.layout.activity_earnings, null, false);
            drawer.addView(contentView, 0);



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
