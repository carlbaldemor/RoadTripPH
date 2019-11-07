package ph.roadtrip.roadtrip;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import ph.roadtrip.roadtrip.classes.MySingleton;
import ph.roadtrip.roadtrip.classes.SessionHandler;
import ph.roadtrip.roadtrip.classes.UrlBean;

public class LoginActivity extends AppCompatActivity {
    private static final String KEY_STATUS = "status1";
    private static final String KEY_STATUS_USER = "status";
    private static final String KEY_USER_ID = "userID";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_FIRST_NAME = "firstName";
    private static final String KEY_LAST_NAME = "lastName";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_USER_TYPE = "userTypeID";
    private static final String KEY_MIDDLE_NAME = "middleName";
    private static final String KEY_EMAIL = "emailAddress";
    private static final String KEY_MOBILE_NUMBER = "mobileNumber";
    private static final String KEY_PHONE_NUMBER = "phoneNumber";
    private static final String KEY_IS_VERIFIED = "isVerified";
    private static final String KEY_PROF_PIC = "profilePicture";
    private static final String KEY_EMPTY = "";
    private static final String KEY_OWNER_ID = "ownerID";
    private static final String KEY_GENDER = "gender";
    private EditText etUsername;
    private EditText etPassword;
    private String username;
    private String password;
    private int userTypeID;
    private String login_link;
    private Button register;
    private Button login;
    private Button forgot;
    private ProgressDialog pDialog;
    private SessionHandler session;
    private Intent load;
    private VideoView videoBG;
    MediaPlayer mMediaPlayer;
    int mCurrentVideoPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionHandler(getApplicationContext());

        if (session.isLoggedIn()) {
            loadDashboard();
        }
        setContentView(R.layout.activity_login);


        // Hook up the VideoView to our UI
        videoBG = (VideoView) findViewById(R.id.videoView);

        //build your video URI
        Uri uri = Uri.parse("android.resource://" //first start with this,
                + getPackageName() //then retrieve your package name,
                + "/" //add a slash,
                + R.raw.bgvideo2);

        //set the new URI to our videoView
        videoBG.setVideoURI(uri);
        //start the videoview
        videoBG.start();

        //set an On preparelistener for our video view.
        videoBG.setOnPreparedListener((mediaPlayer) -> {
            mMediaPlayer = mediaPlayer;
            mMediaPlayer.setLooping(true);
            if (mCurrentVideoPosition != 0) {
                mMediaPlayer.seekTo(mCurrentVideoPosition);
                mMediaPlayer.start();
            }
        });

        etUsername = findViewById(R.id.etLoginUsername);
        etPassword = findViewById(R.id.etLoginPassword);

        register = findViewById(R.id.btnLoginRegister);
        login = findViewById(R.id.btnLogin);
        forgot = findViewById(R.id.btnForgotPassword);

        //Link for the PHP file
        UrlBean url = new UrlBean();
        login_link = url.getLoginLink();

        //Launch Registration screen when Register Button is clicked
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(i);
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Retrieve the data entered in the edit texts
                username = etUsername.getText().toString();
                password = etPassword.getText().toString();
                if (validateInputs()) {
                    login();
                }
            }
        });
    }

    /**
     * Launch Dashboard Activity on Successful Login
     */
    private void loadDashboard() {
        load = new Intent(this, SplashActivity.class);
        startActivity(load);
        finish();
    }

    /**
     * Display Progress bar while Logging in
     */

    private void displayLoader() {
        pDialog = new ProgressDialog(LoginActivity.this);
        pDialog.setMessage("Logging In.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }

    private void login() {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            request.put(KEY_USERNAME, username);
            request.put(KEY_PASSWORD, password);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(Request.Method.POST, login_link, request, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pDialog.dismiss();
                try {
                    //Check if user got logged in successfully

                    if (response.getInt(KEY_STATUS) == 0) {
                        //Get User Type of User
                        userTypeID = response.getInt(KEY_USER_TYPE);
                        //Login User by creating a session
                        session.loginUser(response.getInt(KEY_USER_ID),username,response.getString(KEY_FIRST_NAME), response.getString(KEY_MIDDLE_NAME),
                                response.getString(KEY_LAST_NAME), response.getString(KEY_EMAIL), response.getString(KEY_MOBILE_NUMBER),
                                response.getString(KEY_PHONE_NUMBER), response.getString(KEY_STATUS_USER), response.getInt(KEY_IS_VERIFIED),
                                response.getString(KEY_PROF_PIC), response.getString(KEY_USER_TYPE), response.getString(KEY_GENDER));

                        if (userTypeID == 2){
                            session.loginOwner(response.getInt(KEY_OWNER_ID));
                        }

                        //Load dashboard depending on user type
                        loadDashboard();



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

    /**
     * Validates inputs and shows error if any
     *
     * @return
     */
    private boolean validateInputs() {
        if (KEY_EMPTY.equals(username)) {
            etUsername.setError("Username cannot be empty");
            etUsername.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(password)) {
            etPassword.setError("Password cannot be empty");
            etPassword.requestFocus();
            return false;
        }
        return true;
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

    @Override
    protected void onPause() {
        super.onPause();
        mCurrentVideoPosition = mMediaPlayer.getCurrentPosition();
        videoBG.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoBG.start();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMediaPlayer.release();
        mMediaPlayer = null;
    }
}
