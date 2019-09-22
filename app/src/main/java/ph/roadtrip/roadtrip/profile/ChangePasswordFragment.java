package ph.roadtrip.roadtrip.profile;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import ph.roadtrip.roadtrip.classes.MySingleton;
import ph.roadtrip.roadtrip.classes.UrlBean;
import ph.roadtrip.roadtrip.R;
import ph.roadtrip.roadtrip.classes.SessionHandler;
import ph.roadtrip.roadtrip.classes.User;

public class ChangePasswordFragment extends Fragment {
    private SessionHandler session;
    private ProgressDialog pDialog;
    private static final String KEY_EMPTY = "";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_NEW_PASSWORD = "newPassword";
    private static final String KEY_CONFIRM_PASSWORD = "confirmPassword";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_USER_ID = "userID";
    private static String username;
    private static int userID;
    private String password;
    private String newPassword;
    private String confirmPassword;
    private TextView etCurrentPassword;
    private TextView etNewPassword;
    private TextView etNewConfirmPassword;
    private Button btnSave;
    private String changePasswordLink;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);

        UrlBean url = new UrlBean();
        changePasswordLink = url.getChangePasswordLink();

        etCurrentPassword = view.findViewById(R.id.etCurrentPassword);
        etNewPassword = view.findViewById(R.id.etNewPassword);
        etNewConfirmPassword = view.findViewById(R.id.etNewConfirmPassword);
        btnSave = view.findViewById(R.id.btnSave);

        session = new SessionHandler(getActivity().getApplicationContext());
        User user = session.getUserDetails();
        userID = user.getUserID();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Retrieve the data entered in the edit texts
                password = etCurrentPassword.getText().toString().trim();
                newPassword = etNewPassword.getText().toString().trim();
                confirmPassword = etNewConfirmPassword.getText().toString().trim();

                if (validateInputs()) {
                    changePassword();
                }
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

    private void changePassword() {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            request.put(KEY_PASSWORD, password);
            request.put(KEY_NEW_PASSWORD, newPassword);
            request.put(KEY_CONFIRM_PASSWORD, confirmPassword);
            request.put(KEY_USER_ID, userID);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, changePasswordLink, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        try {
                            //Check if user got registered successfully
                            if (response.getInt(KEY_STATUS) == 0) {
                                Toast.makeText(getActivity(),
                                        response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();

                                //Go back to Edit Info Page
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.fragment_container, new EditProfileFragment());
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                            }else{
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

    /**
     * Validates inputs and shows error if any
     *
     * @return
     */
    private boolean validateInputs() {
        if (KEY_EMPTY.equals(etCurrentPassword)) {
            etCurrentPassword.setError("Password cannot be empty");
            etCurrentPassword.requestFocus();
            return false;
        }
        if (password.length() < 7){
            etCurrentPassword.setError("Password length should be 8 or more");
            etCurrentPassword.requestFocus();
        }
        if (KEY_EMPTY.equals(etNewPassword)) {
            etNewPassword.setError("New Password cannot be empty");
            etNewPassword.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(confirmPassword)) {
            etNewConfirmPassword.setError("Confirm Password cannot be empty");
            etNewConfirmPassword.requestFocus();
            return false;
        }
        return true;
    }


}
