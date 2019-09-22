package ph.roadtrip.roadtrip.bookingmodule;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import ph.roadtrip.roadtrip.DashboardActivity;
import ph.roadtrip.roadtrip.R;

public class SuccessBookingRequestActivity extends DashboardActivity {

    private Button btnMyBookings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_book_service);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_success_booking_request, null, false);
        drawer.addView(contentView, 0);

        btnMyBookings = findViewById(R.id.btnMyBookings);

        btnMyBookings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_container, new BookingFragment());
                    ft.commit();
            }
        });
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
