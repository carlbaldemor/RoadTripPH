package ph.roadtrip.roadtrip.profile;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ph.roadtrip.roadtrip.R;
import ph.roadtrip.roadtrip.carmanagement.CarRecord;
import ph.roadtrip.roadtrip.classes.MySingleton;
import ph.roadtrip.roadtrip.classes.SessionHandler;
import ph.roadtrip.roadtrip.classes.UrlBean;
import ph.roadtrip.roadtrip.classes.User;
import ph.roadtrip.roadtrip.classes.updateData;
import ph.roadtrip.roadtrip.fileupload.ChangeProfilePictureFragment;

public class EditInfoOwnerFragment extends android.support.v4.app.Fragment {
    private SessionHandler session;
    private ProgressDialog pDialog;
    private String emailAddress;
    private String mobileNumber;
    private String phoneNumber;
    private String password;
    private String firstName;
    private String middleName;
    private String lastName;
    private int ownerID;
    private static String username;
    private static int userID;
    private EditText etEmail;
    private EditText etMobileNumber;
    private EditText etPhoneNumber;
    private EditText etProfilePassword;
    private TextView tvChangePassword;
    private static final String KEY_EMPTY = "";
    private static final String KEY_EMAIL = "emailAddress";
    private static final String KEY_MOBILE_NUMBER = "mobileNumber";
    private static final String KEY_PHONE_NUMBER = "phoneNumber";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_USERNAME ="username";
    private static final String KEY_STATUS_USER ="status_user";
    private static final String KEY_USER_ID ="userID";
    private static final String KEY_PROF_PIC = "profilePicture";
    private static final String KEY_IS_VERIFIED = "isVerified";
    private static final String KEY_FIRST_NAME = "firstName";
    private static final String KEY_MIDDLE_NAME = "middleName";
    private static final String KEY_LAST_NAME = "lastName";
    private static final String KEY_GENDER = "gender";
    private ImageView checkmark;
    private String gender;
    private String editProfileLink;
    private String status;
    private Spinner SpinnerGender;
    private EditText etFirstName, etMiddleName, etLastName;
    private Button btnDeactivate;

    public EditInfoOwnerFragment(){
        //Requiredd
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editinfo, container, false);


        UrlBean url = new UrlBean();
        editProfileLink = url.getEditProfileLink();

        onBackground();
        etEmail = view.findViewById(R.id.etEmail);
        etFirstName = view.findViewById(R.id.etFirstName);
        etMiddleName = view.findViewById(R.id.etMiddleName);
        etLastName = view.findViewById(R.id.etLastName);
        etMobileNumber = view.findViewById(R.id.etMobileNumber);
        etPhoneNumber = view.findViewById(R.id.etPhoneNumber);
        etProfilePassword = view.findViewById(R.id.etProfilePassword);
        tvChangePassword = view.findViewById(R.id.tvChangePassword);
        Button save = view.findViewById(R.id.btnSave);
        Button changeProfilePicture = view.findViewById(R.id.btnChangeProfilePicture);
        btnDeactivate = view.findViewById(R.id.btnDeactivate);
        checkmark = view.findViewById(R.id.iv_checkmark);
        SpinnerGender = view.findViewById(R.id.SpinnerGender);

        session = new SessionHandler(getActivity().getApplicationContext());
        User user = session.getUserDetails();
        CarRecord carRecord= session.getOwnerID();

        etEmail.setText(user.getEmailAddress());
        etMobileNumber.setText(user.getMobileNumber());
        etPhoneNumber.setText(user.getPhoneNumber());
        etFirstName.setText(user.getFirstName());
        etMiddleName.setText(user.getMiddleName());
        etLastName.setText(user.getLastName());
        userID = user.getUserID();
        ownerID = carRecord.getOwnerID();

        if (user.getGender().equalsIgnoreCase("M")){
            SpinnerGender.setSelection(0);
        }else if (user.getGender().equalsIgnoreCase("F")){
            SpinnerGender.setSelection(1);
        } else {
            SpinnerGender.setSelection(2);
        }


        if (user.getIsVerified() == 1){
            checkmark.setVisibility(View.VISIBLE);
        }

        tvChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new ChangePasswordFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = SpinnerGender.getSelectedItem().toString();

                if (text.equalsIgnoreCase("Male")){
                    gender = "M";
                } else if (text.equalsIgnoreCase("Female")){
                    gender = "F";
                } else {
                    gender = "UD";
                }

                //Retrieve the data entered in the edit texts
                emailAddress = etEmail.getText().toString().trim();
                mobileNumber = etMobileNumber.getText().toString().trim();
                phoneNumber = etPhoneNumber.getText().toString().trim();
                password = etProfilePassword.getText().toString().trim();
                firstName = etFirstName.getText().toString().trim();
                middleName = etMiddleName.getText().toString().trim();
                lastName = etLastName.getText().toString().trim();

                if (validateInputs()) {
                    saveInfo();
                }
            }
        });

        changeProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new ChangeProfilePictureFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        btnDeactivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Deactivate Account Prompt
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new PromptDeactivateAccountOwnerFragment());
                //fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return view;
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Saving Info.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }

    private void saveInfo() {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            request.put(KEY_EMAIL, emailAddress);
            request.put(KEY_MOBILE_NUMBER, mobileNumber);
            request.put(KEY_PHONE_NUMBER, phoneNumber);
            request.put(KEY_PASSWORD, password);
            request.put(KEY_USER_ID, userID);
            request.put(KEY_FIRST_NAME, firstName);
            request.put(KEY_MIDDLE_NAME, middleName);
            request.put(KEY_LAST_NAME, lastName);
            request.put(KEY_GENDER, gender);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, editProfileLink, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        try {
                            //Check if user got registered successfully
                            if (response.getInt(KEY_STATUS) == 0) {
                                etProfilePassword.setText("");
                                session.editUser(response.getString(KEY_EMAIL), response.getString(KEY_MOBILE_NUMBER), response.getString(KEY_PHONE_NUMBER), response.getString(KEY_STATUS_USER), response.getString(KEY_FIRST_NAME), response.getString(KEY_MIDDLE_NAME), response.getString(KEY_LAST_NAME), response.getString(KEY_GENDER));

                                Toast.makeText(getActivity(),
                                        response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();
                            }else if(response.getInt(KEY_STATUS) == 2){
                                etProfilePassword.setText("");
                                UrlBean url = new UrlBean();
                                String link = url.getMailerLinkRegister()+emailAddress; //using this IP for Genymotion emulator
                                new updateData().execute(link);

                                session.editUser(response.getString(KEY_EMAIL), response.getString(KEY_MOBILE_NUMBER), response.getString(KEY_PHONE_NUMBER), response.getString(KEY_STATUS_USER), response.getString(KEY_FIRST_NAME), response.getString(KEY_MIDDLE_NAME), response.getString(KEY_LAST_NAME), response.getString(KEY_GENDER));

                                Toast.makeText(getActivity(),
                                        response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();
                            } else{
                                Toast.makeText(getActivity(),
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
                        Toast.makeText(getActivity(),
                                error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(getActivity()).addToRequestQueue(jsArrayRequest);
    }

    private boolean validateInputs() {
        if (KEY_EMPTY.equals(emailAddress)) {
            etEmail.setError("Email Address cannot be empty");
            etEmail.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(mobileNumber)) {
            etMobileNumber.setError("Mobile Number cannot be empty");
            etMobileNumber.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(password)) {
            etProfilePassword.setError("Password cannot be empty");
            etProfilePassword.requestFocus();
            return false;
        }
        if (mobileNumber.length() != 11){
            etMobileNumber.setError("Mobile Number is Invalid");
            etMobileNumber.requestFocus();
            return false;
        }
        if (!isValidEmail(emailAddress)) {
            etEmail.setError("Invalid Email");
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

    public void onBackground(){
        UrlBean url = new UrlBean();
        String getUserData = url.getGetUserDataUrl();
        session = new SessionHandler(getActivity());
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

                                User user = new User();
                                if (response.getString(KEY_STATUS).equals("Active")){
                                    user.setStatus("Active");
                                    checkmark.setVisibility(View.VISIBLE);
                                }

                                Toast.makeText(getActivity(),
                                        response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();
                            } else{
                                Toast.makeText(getActivity(),
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
                        Toast.makeText(getActivity(),
                                error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(getActivity()).addToRequestQueue(jsArrayRequest);

    }

}
