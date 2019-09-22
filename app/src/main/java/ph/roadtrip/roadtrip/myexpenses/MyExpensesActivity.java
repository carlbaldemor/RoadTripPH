package ph.roadtrip.roadtrip.myexpenses;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import ph.roadtrip.roadtrip.DashboardActivity;
import ph.roadtrip.roadtrip.R;
import ph.roadtrip.roadtrip.classes.SessionHandler;

public class MyExpensesActivity extends DashboardActivity {

    private static final String KEY_STATUS = "status1";
    private static final String KEY_OWNER_ID = "ownerID";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_TOTAL_EARNINGS = "totalExpenses";
    private static final String KEY_TOTAL_AVERAGE = "totalAverage";
    private static final String KEY_TOTAL_TRIPS = "totalTrips";
    private static final String KEY_TOTAL_BOOKINGS = "totalBookings";
    private static final String KEY_TOTAL_CANCEL = "totalCancel";
    private static final String KEY_SELECTED_YEAR = "selectedYear";

    private TextView tvTotalExpenses, tvTotalAverage, tvTotalTrips, tvTotalBookings, tvTotalCancel;
    private TextView tvTotalExpensesYear, tvTotalAverageYear, tvTotalTripsYear, tvTotalBookingsYear, tvTotalCancelYear;
    private String totalExpenses, totalTrips, totalBookings, totalCancel;
    private String totalExpensesYear, totalTripsYear, totalBookingsYear, totalCancelYear, selectedYear;
    private Double totalAverage;
    private int ownerID;
    private SessionHandler session;
    int size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_expenses);




    }
}
