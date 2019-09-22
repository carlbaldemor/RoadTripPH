package ph.roadtrip.roadtrip;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import ph.roadtrip.roadtrip.bookingmodule.BookServiceActivity;
import ph.roadtrip.roadtrip.classes.SessionHandler;
import ph.roadtrip.roadtrip.classes.User;

public class HomeActivity extends AppCompatActivity {

    private SessionHandler session;
    private String userTypeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        session = new SessionHandler(getApplicationContext());

        if (session.isLoggedIn()) {
            User user = session.getUserDetails();
            userTypeID = user.getUserTypeID();

            Toast.makeText(getApplicationContext(), userTypeID, Toast.LENGTH_LONG).show();
            /*if (userTypeID.equalsIgnoreCase("1")){
                Intent load = new Intent(getApplicationContext(), DashboardActivity.class);
                startActivity(load);
                finish();
            } else if (userTypeID.equalsIgnoreCase("2")){
                Intent load = new Intent(getApplicationContext(), DashboardOwnerActivity.class);
                startActivity(load);
                finish();
            }*/
        } else {
            Intent load = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(load);
            finish();
        }
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
