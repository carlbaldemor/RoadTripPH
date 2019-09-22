package ph.roadtrip.roadtrip;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ph.roadtrip.roadtrip.classes.MySingleton;
import ph.roadtrip.roadtrip.classes.UrlBean;
import ph.roadtrip.roadtrip.classes.updateData;


public class ForgotPasswordActivity extends AppCompatActivity {

    private static final String KEY_EMPTY = "";
    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_EMAIL_ADDRESS = "emailAddress";
    private Button login;
    private Button forgotPassword;
    private EditText etEmailAddress;
    private String emailAddress;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        forgotPassword = findViewById(R.id.btnForgotPassword);
        login = findViewById(R.id.btnLogin);
        etEmailAddress = findViewById(R.id.etEmailAddress);


        //Launch Registration screen when Register Button is clicked
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Retrieve the data entered in the edit texts
                emailAddress = etEmailAddress.getText().toString().toLowerCase().trim();

                if (validateInputs()) {
                    forgetPassword();
                }

            }
        });

    }

    public void forgetPassword() {
        UrlBean url = new UrlBean();
        String forgotPasswordLink = url.getForgetPasswordLink();

        displayLoader();
        JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            request.put(KEY_EMAIL_ADDRESS, emailAddress);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(Request.Method.POST, forgotPasswordLink, request, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pDialog.dismiss();
                try {
                    //Check if user got registered successfully
                    if (response.getInt(KEY_STATUS) == 0) {
                        //Set the user session
                        Toast.makeText(getApplicationContext(), response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();

                        UrlBean url = new UrlBean();
                        String forgetPasswordLink = url.getSendForgetEmailLink();
                        String link =  forgetPasswordLink+emailAddress; //using this IP for Genymotion emulator
                        new updateData().execute(link);

                    } else if (response.getInt(KEY_STATUS) == 1) {
                        Toast.makeText(getApplicationContext(), response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();
                        //Display error message if Email is already existsing
                        etEmailAddress.setError("Sorry, there is no " + emailAddress + " in our server. Please try again.");
                        etEmailAddress.requestFocus();
                    } else {
                        Toast.makeText(getApplicationContext(), response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();

                //Display error message whenever an error occurs
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }

    //Validations
    private boolean validateInputs() {

        if (KEY_EMPTY.equals(emailAddress)) {
            etEmailAddress.setError("Email Address cannot be empty");
            etEmailAddress.requestFocus();
            return false;
        }
        if (!isValidEmail(emailAddress)) {
            etEmailAddress.setError("Invalid Email");
            return false;
        }
        return true;
    }

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * Display Progress bar while registering
     */
    private void displayLoader() {
        pDialog = new ProgressDialog(ForgotPasswordActivity.this);
        pDialog.setMessage("Loading Please Wait..");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

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
