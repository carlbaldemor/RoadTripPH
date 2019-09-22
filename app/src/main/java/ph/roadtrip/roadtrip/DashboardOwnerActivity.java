package ph.roadtrip.roadtrip;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import ph.roadtrip.roadtrip.bookingmodule.MyBookingOwnerFragment;
import ph.roadtrip.roadtrip.carmanagement.CarManagementFragment;
import ph.roadtrip.roadtrip.chat.ListChatsFragment;
import ph.roadtrip.roadtrip.chat.ListChatsOwnerFragment;
import ph.roadtrip.roadtrip.chatbot.ChatBotActivity;
import ph.roadtrip.roadtrip.chatbot.ChatBotOwnerActivity;
import ph.roadtrip.roadtrip.classes.MySingleton;
import ph.roadtrip.roadtrip.classes.SessionHandler;
import ph.roadtrip.roadtrip.classes.UrlBean;
import ph.roadtrip.roadtrip.classes.User;
import ph.roadtrip.roadtrip.faqs.FaqsOwnerActivity;
import ph.roadtrip.roadtrip.myearnings.MyEarningsActivity;
import ph.roadtrip.roadtrip.profile.OwnerProfileFragment;
import ph.roadtrip.roadtrip.transactionhistory.TransactionHistoryOwnerFragment;

public class DashboardOwnerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String KEY_EMAIL = "emailAddress";
    private static final String KEY_MOBILE_NUMBER = "mobileNumber";
    private static final String KEY_PHONE_NUMBER = "phoneNumber";
    private static final String KEY_STATUS_USER ="status";
    private static final String KEY_IS_VERIFIED ="isVerified";
    private static final String KEY_STATUS ="status1";
    private static final String KEY_PROF_PIC ="profilePicture";
    private static final String KEY_MESSAGE ="message";
    private static final String KEY_USER_ID ="userID";
    private static final String KEY_FIRST_NAME = "firstName";
    private static final String KEY_MIDDLE_NAME = "middleName";
    private static final String KEY_LAST_NAME = "lastName";
    private static final String KEY_GENDER = "gender";

    private SessionHandler session;
    protected DrawerLayout drawer;
    private Intent load;
    private NavigationView navigationView;
    private TextView menu_name;
    private TextView menu_email;
    private ImageView menu_prof_pic;
    private String profilePicture;
    private String myUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_owner);
//test
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                onBackground();
                session = new SessionHandler(getApplicationContext());
                User user = session.getUserDetails();

                TextView menu_name = findViewById(R.id.menu_name);
                TextView menu_email = findViewById(R.id.menu_email);
                ImageView menu_prof_pic = findViewById(R.id.menu_prof_pic);
                String profilePicture = user.getProfilePicture();

                UrlBean url = new UrlBean();
                String myUrl = url.getProfilePicUrl()+profilePicture;

                Glide.with(getApplicationContext())
                        .load(myUrl)
                        .into(menu_prof_pic);

                //Get and Set User details
                menu_name.setText(user.getFullName());
                menu_email.setText(user.getEmailAddress());
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        navigationView = (NavigationView) drawer.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);

        menu_name = headerView.findViewById(R.id.menu_name);
        menu_email = headerView.findViewById(R.id.menu_email);
        menu_prof_pic = headerView.findViewById(R.id.menu_prof_pic);

        session = new SessionHandler(getApplicationContext());

        User user = session.getUserDetails();

        profilePicture = user.getProfilePicture();

        UrlBean url = new UrlBean();
        myUrl = url.getProfilePicUrl()+profilePicture;

        Glide.with(this)
                .load(myUrl)
                .into(menu_prof_pic);

        //Get and Set User details
        menu_name.setText(user.getFullName());
        menu_email.setText(user.getEmailAddress());

        drawer.setDrawerListener(toggle);
        toggle.syncState();

        onBackground();
        /*FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new HomeOwnerFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();*/
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new HomeOwnerFragment()).commit();
                        break;
                    case R.id.nav_message_owner:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new ListChatsOwnerFragment()).commit();
                        break;
                    case R.id.nav_profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new OwnerProfileFragment()).commit();
                        break;
                    case R.id.nav_earnings:
                        load = new Intent(getApplicationContext(), MyEarningsActivity.class);
                        startActivity(load);
                        finish();
                        break;
                    case R.id.nav_car:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new CarManagementFragment()).commit();
                        break;
                    case R.id.nav_history:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new TransactionHistoryOwnerFragment()).commit();
                        break;
                    case R.id.nav_book:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new MyBookingOwnerFragment()).commit();
                        break;
                    case R.id.nav_cust:
                        load = new Intent(getApplicationContext(), ChatBotOwnerActivity.class);
                        startActivity(load);
                        finish();
                        break;
                    case R.id.nav_help:
                        load = new Intent(getApplicationContext(), FaqsOwnerActivity.class);
                        startActivity(load);
                        finish();
                        break;
                    case R.id.nav_logout:
                        session.logoutUser();
                        load = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(load);
                        finish();
                        break;
                }
        }
    }, 200);
     drawer.closeDrawer(GravityCompat.START);
     return true;
}

    public void onBackground(){
        UrlBean url = new UrlBean();
        String getUserData = url.getGetUserDataUrl();
        session = new SessionHandler(getApplicationContext());
        User user = session.getUserDetails();
        int userID = user.getUserID();

        JSONObject request = new JSONObject();
        try {
            request.put(KEY_USER_ID, userID);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, getUserData, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //Check if user got registered successfully
                            if (response.getInt(KEY_STATUS) == 0) {
                                session.updateUserData(response.getString(KEY_EMAIL), response.getString(KEY_MOBILE_NUMBER), response.getString(KEY_PHONE_NUMBER), response.getString(KEY_STATUS_USER), response.getString(KEY_PROF_PIC), response.getInt(KEY_IS_VERIFIED), response.getString(KEY_FIRST_NAME), response.getString(KEY_MIDDLE_NAME), response.getString(KEY_LAST_NAME), response.getString(KEY_GENDER));

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
