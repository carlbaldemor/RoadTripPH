package ph.roadtrip.roadtrip;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import ph.roadtrip.roadtrip.classes.SessionHandler;
import ph.roadtrip.roadtrip.classes.User;
import ph.roadtrip.roadtrip.profile.PromptActivateActivity;

public class SplashActivity extends AppCompatActivity {
    SessionHandler session;
    private String userTypeID;
    private String status;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionHandler(getApplicationContext());

        if (session.isLoggedIn()) {
            User user = session.getUserDetails();
            userTypeID = user.getUserTypeID();
            status = user.getStatus();

            if (status.equals("Deactivated")){
                Intent load = new Intent(getApplicationContext(), PromptActivateActivity.class);
                startActivity(load);
                finish();
            }else if (status.equalsIgnoreCase("Archived")){

                Toast.makeText(getApplicationContext(), "It seems your account has been archived. You cannot login to it anymore.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            } else {
                if (userTypeID.equalsIgnoreCase("1")){
                    Intent load = new Intent(getApplicationContext(), DashboardActivity.class);
                    startActivity(load);
                    finish();
                } else if (userTypeID.equalsIgnoreCase("2")){
                    Intent load = new Intent(getApplicationContext(), DashboardOwnerActivity.class);
                    startActivity(load);
                    finish();
                } else{
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

    }
}
