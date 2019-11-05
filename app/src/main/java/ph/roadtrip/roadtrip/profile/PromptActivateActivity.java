package ph.roadtrip.roadtrip.profile;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import ph.roadtrip.roadtrip.LoginActivity;
import ph.roadtrip.roadtrip.R;
import ph.roadtrip.roadtrip.SplashActivity;
import ph.roadtrip.roadtrip.classes.MySingleton;
import ph.roadtrip.roadtrip.classes.SessionHandler;
import ph.roadtrip.roadtrip.classes.UrlBean;
import ph.roadtrip.roadtrip.classes.User;

public class PromptActivateActivity extends AppCompatActivity {

    private TextView tvStartDate, tvEndDate, tvBrandName, tvModelName, tvFullname, tvTotalAmount, tvCarType, tvServiceType;
    private Button btnAccept, btnDecline, btnActivate, btnCancel;

    private int recordID;
    private int renter_userID;
    private String startDate;
    private String endDate;
    private String brandName;
    private String modelName;
    private String firstName;
    private String lastName;
    private String totalAmount;
    private String carType;
    private String serviceType;
    private int userID;
    private SessionHandler session;

    private static final String KEY_MESSAGE = "message";
    private static final String KEY_STATUS  = "status1";
    private static final String KEY_BOOKING_ID = "bookingID";
    private static final String KEY_FIRST_NAME = "firstName";
    private static final String KEY_LAST_NAME = "lastName";
    private static final String KEY_CAR_TYPE = "carType";
    private static final String KEY_SERVICE_TYPE = "serviceType";
    private static final String KEY_START_DATE = "startDate";
    private static final String KEY_END_DATE = "endDate";
    private static final String KEY_TOTAL_AMOUNT = "totalAmount";
    private static final String KEY_BRAND_NAME = "brandName";
    private static final String KEY_MODEL_NAME = "modelName";
    private static final String KEY_RENTER_USER_ID = "renter_userID";
    private static final String KEY_USER_ID = "userID";
    private static final String KEY_RECORD_ID = "recordID";
    private static final String KEY_EMPTY = "";
    private static final String KEY_PASSWORD = "password";

    private String fetch_booking_data;
    private String activate_record;
    private String cancel;
    private EditText etPassword;
    private String password;
    private Button btnAccept2, btnCancel2;
    private String urlCheckPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prompt_activate);

        session = new SessionHandler(getApplicationContext());

        User user = session.getUserDetails();
        userID = user.getUserID();

        UrlBean urlBean = new UrlBean();
        activate_record = urlBean.getActivate_account(); // change this
        urlCheckPassword = urlBean.getCheck_password();

        btnActivate = findViewById(R.id.btnActivate);
        btnCancel = findViewById(R.id.btnCancel);

        btnActivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Deactivate Account
                callLoginDialog();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log out user and go back to login page
                session.logoutUser();
                Intent load = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(load);

            }
        });

    }

    private void callLoginDialog()
    {
        final Dialog myDialog = new Dialog(PromptActivateActivity.this);
        myDialog.setContentView(R.layout.prompt_password);
        myDialog.setCancelable(false);

        etPassword = (EditText) myDialog.findViewById(R.id.etPassword);
        btnAccept2 =  (Button) myDialog.findViewById(R.id.btnAccept2);
        btnCancel2 =  (Button) myDialog.findViewById(R.id.btnCancel2);
        myDialog.show();

        btnAccept2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                password = etPassword.getText().toString();

                if (validateInputs() == false) {

                } else {
                    checkPassword();
                    myDialog.hide();
                }
            }
        });
        btnCancel2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                myDialog.hide();
            }
        });

    }

    private boolean validateInputs() {

        if (KEY_EMPTY.equals(password)) {
            etPassword.setError("Password cannot be empty");
            etPassword.requestFocus();
            return false;
        }

        return true;
    }

    public void checkPassword(){
        final JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            if (userID == 0){

            } else {
                request.put(KEY_USER_ID, userID);
                request.put(KEY_PASSWORD, password);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(Request.Method.POST, urlCheckPassword, request, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //Check if user got logged in successfully

                    if (response.getInt(KEY_STATUS) == 0) {
                        //Go to Success page
                        activate();
                    } else{
                        Toast.makeText(PromptActivateActivity.this, response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                //Display error message whenever an error occurs
                Toast.makeText(PromptActivateActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsArrayRequest);
    }

    public void activate(){
        final JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            if (userID == 0){
                Toast.makeText(getApplicationContext().getApplicationContext(), "Record ID is 0", Toast.LENGTH_SHORT).show();
            } else {
                request.put(KEY_USER_ID, userID);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(Request.Method.POST, activate_record, request, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //Check if user got logged in successfully

                    if (response.getInt(KEY_STATUS) == 0) {

                        //Go to Success page
                        session.logoutUser();
                        Intent load = new Intent(getApplicationContext(), SplashActivity.class);
                        startActivity(load);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext().getApplicationContext(), response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                //Display error message whenever an error occurs
                Toast.makeText(getApplicationContext().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(getApplicationContext().getApplicationContext()).addToRequestQueue(jsArrayRequest);
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
