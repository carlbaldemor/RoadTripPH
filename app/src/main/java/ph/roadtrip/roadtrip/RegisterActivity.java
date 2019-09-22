package ph.roadtrip.roadtrip;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.ybs.passwordstrengthmeter.PasswordStrength;

import ph.roadtrip.roadtrip.classes.MySingleton;
import ph.roadtrip.roadtrip.classes.SessionHandler;
import ph.roadtrip.roadtrip.classes.UrlBean;
import ph.roadtrip.roadtrip.classes.updateData;

public class RegisterActivity extends AppCompatActivity implements TextWatcher {

    private static final String KEY_STATUS = "status1";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_FIRST_NAME = "firstName";
    private static final String KEY_MIDDLE_NAME = "middleName";
    private static final String KEY_LAST_NAME = "lastName";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_EMAIL_ADDRESS = "emailAddress";
    private static final String KEY_MOBILE_NUMBER = "mobileNumber";
    private static final String KEY_PHONE_NUMBER = "phoneNumber";
    private static final String KEY_USER_TYPE = "userTypeID";
    private static final String KEY_STATUS_USER = "status";
    private static final String KEY_USER_ID = "userID";
    private static final String KEY_BIRTH_DATE = "birthDate";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_EMPTY = "";
    private EditText etUsername;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private EditText etFirstName;
    private EditText etMiddleName;
    private EditText etLastName;
    private EditText etMobileNumber;
    private EditText etPhoneNumber;
    private EditText etEmailAddress;
    private CheckBox agree;
    private String emailAddress;
    private String username;
    private String password;
    private String confirmPassword;
    private String firstName;
    private String middleName;
    private String lastName;
    private String mobileNumber;
    private String phoneNumber;
    private int userID;
    private String status = "Inactive";
    private int isVerified;
    private String profilePicture;
    private String validationCode;
    private String email;
    private boolean getUser;
    private int userTypeID;
    private ProgressDialog pDialog;
    private String register_url;
    private String mailer_url;
    private String verifyCaptcha;
    private String safetyNetKey;
    private SessionHandler session;
    Intent load;
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    private String gender;
    private String gend_pos;
    private String birthDate;
    private String age;
    private TextView tvBirthDate;
    private DatePicker datePicker;

    private static final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionHandler(getApplicationContext());
        setContentView(R.layout.activity_register);

        //Link for the PHP file
        UrlBean url = new UrlBean();
        register_url = url.getRegisterLink();
        mailer_url = url.getMailerLinkRegister();
        verifyCaptcha = url.getVerifyCaptcha();
        safetyNetKey = url.getSAFETY_NET_API_SITE_KEY();

        tvBirthDate = findViewById(R.id.tvBirthDate);
        datePicker = findViewById(R.id.datePicker);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etFirstName = findViewById(R.id.etFirstName);
        etMiddleName = findViewById(R.id.etMiddleName);
        etLastName = findViewById(R.id.etLastName);
        etMobileNumber = findViewById(R.id.etMobileNumber);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etEmailAddress = findViewById(R.id.etEmailAddress);
        agree = findViewById(R.id.checkBox);
        radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);

        final Spinner mySpinner = (Spinner) findViewById(R.id.SpinnerFeedbackType);
        String size = mySpinner.getSelectedItem().toString();

        Button login = findViewById(R.id.btnRegisterLogin);
        Button register = findViewById(R.id.btnRegister);

        //Launch Login screen when Login Button is clicked
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth() + 1;
                int year = datePicker.getYear();

                //Get age
                age = getAge(year,month,day);

                //Get birthdate
                birthDate = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day);

                //Radio Button Male or Female or UD
                int selectedId = radioSexGroup.getCheckedRadioButtonId();
                radioSexButton = (RadioButton) findViewById(selectedId);
                gend_pos = radioSexButton.getText().toString();

                if (gend_pos.equalsIgnoreCase("Male")){
                    gender = "M";
                } else if (gend_pos.equalsIgnoreCase("Female")) {
                    gender = "F";
                } else {
                    gender = "UD";
                }

                //Retrieve the data entered in the edit texts
                username = etUsername.getText().toString().toLowerCase().trim();
                password = etPassword.getText().toString().trim();
                confirmPassword = etConfirmPassword.getText().toString().trim();
                firstName = etFirstName.getText().toString().trim();
                middleName = etMiddleName.getText().toString().trim();
                lastName = etLastName.getText().toString().trim();
                mobileNumber = etMobileNumber.getText().toString().trim();
                phoneNumber = etPhoneNumber.getText().toString().trim();
                emailAddress = etEmailAddress.getText().toString().trim();

                int spinner_pos = mySpinner.getSelectedItemPosition();
                String[] size_values = getResources().getStringArray(R.array.size_values);
                int size = Integer.valueOf(size_values[spinner_pos]);

                if (size == 1){
                    getUser = true;
                    userTypeID = 1;
                } else if (size == 2){
                    getUser = false;
                    userTypeID = 2;
                } else {
                    Toast.makeText(getApplicationContext(), "Error: on size boolean", Toast.LENGTH_SHORT).show();
                }

                if (validateInputs() == false) {
                    //Stay at sign up page.
                }else {
                        validateCaptcha();
                }


            }
        });

        etPassword.addTextChangedListener(this);

    }

    /**
     * Password Strength
     */

    @Override
    public void afterTextChanged(Editable s) {
    }
    @Override
    public void beforeTextChanged(
            CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        updatePasswordStrengthView(s.toString());
    }

    private void updatePasswordStrengthView(String password) {

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        TextView strengthView = (TextView) findViewById(R.id.password_strength);
        if (TextView.VISIBLE != strengthView.getVisibility())
            return;

        if (password.isEmpty()) {
            strengthView.setText("");
            progressBar.setProgress(0);
            return;
        }

        PasswordStrength str = PasswordStrength.calculateStrength(password);
        strengthView.setText(str.getText(this));
        strengthView.setTextColor(str.getColor());

        progressBar.getProgressDrawable().setColorFilter(str.getColor(), android.graphics.PorterDuff.Mode.SRC_IN);
        if (str.getText(this).equals("Weak")) {
            progressBar.setProgress(25);
        } else if (str.getText(this).equals("Medium")) {
            progressBar.setProgress(50);
        } else if (str.getText(this).equals("Strong")) {
            progressBar.setProgress(75);
        } else {
            progressBar.setProgress(100);
        }
    }

    /**
     * Display Progress bar while registering
     */
    private void displayLoader() {
        pDialog = new ProgressDialog(RegisterActivity.this);
        pDialog.setMessage("Signing Up.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }

    /**
     * Launch Dashboard Activity on Successful Sign Up
     */
    private void loadDashboard() {
        if (getUser == true){
            load = new Intent(this, DashboardActivity.class);
            startActivity(load);
            finish();
        } else if (getUser == false){
            load = new Intent(this, DashboardOwnerActivity.class);
            startActivity(load);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Error loading dashboard", Toast.LENGTH_SHORT);
        }

    }

    private void registerUser() {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            request.put(KEY_USERNAME, username);
            request.put(KEY_PASSWORD, password);
            request.put(KEY_FIRST_NAME, firstName);
            request.put(KEY_MIDDLE_NAME, middleName);
            request.put(KEY_LAST_NAME, lastName);
            request.put(KEY_EMAIL_ADDRESS, emailAddress);
            request.put(KEY_MOBILE_NUMBER, mobileNumber);
            request.put(KEY_PHONE_NUMBER, phoneNumber);
            request.put(KEY_USER_TYPE, userTypeID);
            request.put(KEY_BIRTH_DATE, birthDate);
            request.put(KEY_GENDER, gender);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, register_url, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        try {
                            //Check if user got registered successfully
                            if (response.getInt(KEY_STATUS) == 0) {
                                //Set the user session
                                userID = response.getInt(KEY_USER_ID);
                                session.loginUser(userID,username,firstName,middleName,lastName,emailAddress,mobileNumber,phoneNumber,status,isVerified,profilePicture, String.valueOf(userTypeID), gender);
                                loadDashboard();
                                Toast.makeText(getApplicationContext(),
                                        response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();

                                //Send Email
                                String link = mailer_url+emailAddress;
                                new updateData().execute(link);

                            }else if(response.getInt(KEY_STATUS) == 1){
                                Toast.makeText(getApplicationContext(),
                                        response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();
                                //Display error message if Email is already existsing
                                etEmailAddress.setError("Email Address is already taken!");
                                etEmailAddress.requestFocus();
                            }else if(response.getInt(KEY_STATUS) == 2){
                                Toast.makeText(getApplicationContext(),
                                        response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();
                                //Display error message if username is already existsing
                                etUsername.setError("Username is already taken!");
                                etUsername.requestFocus();
                            }else{
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
                        pDialog.dismiss();

                        //Display error message whenever an error occurs
                        Toast.makeText(getApplicationContext(),
                                error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }


    private String getAge(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }

    public boolean isPasswordValid(String passwordhere) {

        Pattern specailCharPatten = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Pattern digitCasePatten = Pattern.compile("[0-9 ]");
        boolean flag=true;

        if (passwordhere.length() < 8) {
            etPassword.setError("Password must have at least 8 characters");
            flag=false;
        }
        if (!specailCharPatten.matcher(passwordhere).find()) {
            etPassword.setError("Password must have at least 1 special character");
            flag=false;
        }
        if (!digitCasePatten.matcher(passwordhere).find()) {
            etPassword.setError("Password must have at least 8 characters");
            flag=false;
        }
        return flag;

    }

    /**
     * Validates inputs and shows error if any
     * @return
     */
    private boolean validateInputs() {

        if (KEY_EMPTY.equals(firstName)) {
            etFirstName.setError("First Name cannot be empty");
            etFirstName.requestFocus();
            return false;
        }

        if (KEY_EMPTY.equals(emailAddress)) {
            etEmailAddress.setError("Email Address cannot be empty");
            etEmailAddress.requestFocus();
            return false;
        }

        if (!isValidEmail(emailAddress)) {
            etEmailAddress.setError("Invalid Email");
            return false;
        }
        if (KEY_EMPTY.equals(lastName)) {
            etLastName.setError("Middle Name cannot be empty");
            etLastName.requestFocus();
            return false;
        }
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
        if (KEY_EMPTY.equals(mobileNumber)) {
            etMobileNumber.setError("Mobile Number cannot be empty");
            etMobileNumber.requestFocus();
            return false;
        }

        if (mobileNumber.length() != 11){
            etMobileNumber.setError("Mobile Number is Invalid");
            etMobileNumber.requestFocus();
            return false;
        }
        if (!agree.isChecked()){
            agree.setError("Please read and accept the terms and conditions of RoadTrip");
            agree.requestFocus();
            return false;
        }

        if (KEY_EMPTY.equals(confirmPassword)) {
            etConfirmPassword.setError("Confirm Password cannot be empty");
            etConfirmPassword.requestFocus();
            return false;
        }
        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Password does not match");
            etConfirmPassword.requestFocus();
            return false;
        }
        int result = Integer.parseInt(age);
        if(result < 18){
            tvBirthDate.setError("You must be 18 years old and above to register a RoadTrip account.");
            tvBirthDate.requestFocus();
            return false;
        }
        if(!isPasswordValid(password)){
            return false;
        }
        if (username.length() < 5){
            etUsername.setError("Username length must be at least 5");
            etUsername.requestFocus();
            return false;
        }

        return true;
    }

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }



    //@OnClick(R.id.btnRegister)
    public void validateCaptcha() {

        // Showing reCAPTCHA dialog
        SafetyNet.getClient(this).verifyWithRecaptcha(safetyNetKey)
                .addOnSuccessListener(this, new OnSuccessListener<SafetyNetApi.RecaptchaTokenResponse>() {
                    @Override
                    public void onSuccess(SafetyNetApi.RecaptchaTokenResponse response) {
                        Log.d(TAG, "onSuccess");

                        if (!response.getTokenResult().isEmpty()) {

                            // Received captcha token
                            // This token still needs to be validated on the server
                            // using the SECRET key
                            verifyTokenOnServer(response.getTokenResult());
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof ApiException) {
                            ApiException apiException = (ApiException) e;
                            Log.d(TAG, "Error message: " +
                                    CommonStatusCodes.getStatusCodeString(apiException.getStatusCode()));
                        } else {
                            Log.d(TAG, "Unknown type of error: " + e.getMessage());
                        }
                    }
                });
    }

    /**
     * Verifying the captcha token on the server
     * Post param: recaptcha-response
     * Server makes call to https://www.google.com/recaptcha/api/siteverify
     * with SECRET Key and Captcha token
     */
    public void verifyTokenOnServer(final String token) {
        Log.d(TAG, "Captcha Token" + token);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                verifyCaptcha, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    String message = jsonObject.getString("message");

                    if (success) {
                        // Congrats! captcha verified successfully on server
                        // TODO - submit the feedback to your servers
                        //Register to database registerUser();
                        registerUser();
                    } else {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("recaptcha-response", token);

                return params;
            }
        };

        MyApplication.getInstance().addToRequestQueue(strReq);
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
