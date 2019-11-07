package ph.roadtrip.roadtrip.fileupload;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import ph.roadtrip.roadtrip.classes.EndPoints;
import ph.roadtrip.roadtrip.classes.MySingleton;
import ph.roadtrip.roadtrip.classes.UrlBean;
import ph.roadtrip.roadtrip.profile.ValidIdFragment;
import ph.roadtrip.roadtrip.R;
import ph.roadtrip.roadtrip.classes.SessionHandler;
import ph.roadtrip.roadtrip.classes.User;

import static android.app.Activity.RESULT_OK;

public class AttachmentsFragment extends android.support.v4.app.Fragment {
    private SessionHandler session;
    private String username;
    private int userID;
    private TextView tvList;
    private TextView tvStatus;
    private ImageView checkmark;
    private Button buttonUploadImage, btnView;

    private static final String KEY_USER_ID = "userID";
    private static final String KEY_STATUS = "status1";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_EMAIL = "emailAddress";
    private static final String KEY_MOBILE_NUMBER = "mobileNumber";
    private static final String KEY_PHONE_NUMBER = "phoneNumber";
    private static final String KEY_PROF_PIC = "profilePicture";
    private static final String KEY_STATUS_USER = "status";
    private static final String KEY_IS_VERIFIED = "isVerified";
    private static final String KEY_FIRST_NAME = "firstName";
    private static final String KEY_MIDDLE_NAME = "middleName";
    private static final String KEY_LAST_NAME = "lastsName";
    private static final String KEY_GENDER = "gender";

    public AttachmentsFragment() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attachments, container, false);

        tvList = view.findViewById(R.id.tvList);
        checkmark = view.findViewById(R.id.iv_checkmark);
        tvStatus = view.findViewById(R.id.tvStatus);
        buttonUploadImage = view.findViewById(R.id.btnUpload);
        btnView = view.findViewById(R.id.btnView);

        session = new SessionHandler(getActivity());
        User user = session.getUserDetails();
        username = user.getUsername();

        String status = user.getStatus();
        if (!status.equalsIgnoreCase("Activated")){
            checkmark.setVisibility(View.GONE);
            tvStatus.setText("Not Verified");
        } else {
            buttonUploadImage.setVisibility(View.GONE);
        }


        view.findViewById(R.id.tvList).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new ValidIdFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        view.findViewById(R.id.btnUpload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Open Gallery
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 100);
            }
        });

        view.findViewById(R.id.btnView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Open Gallery
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new ViewAttachmentsFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {

            //getting the image Uri
            Uri imageUri = data.getData();
            try {
                //getting bitmap object from uri
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getActivity().getApplicationContext().getContentResolver(), imageUri);

                //calling the method uploadBitmap to upload image
                uploadBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * The method is taking Bitmap as an argument
     * then it will return the byte[] array for the given bitmap
     * and we will send this array to the server
     * here we are using PNG Compression with 80% quality
     * you can give quality between 0 to 100
     * 0 means worse quality
     * 100 means best quality
     * */
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 20, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void uploadBitmap(final Bitmap bitmap) {

        //getting the tag from the edittext
        final String tags = username;
        Toast.makeText(getActivity().getApplicationContext(), "Username : " + tags, Toast.LENGTH_SHORT).show();
        //our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, EndPoints.UPLOAD_ATTACHMENTS_URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            Toast.makeText(getActivity().getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            /*
             * If you want to add more parameters with the image
             * you can do it here
             * here we have only one parameter with the image
             * which is tags
             * */
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("tags", tags);
                return params;
            }

            /*
             * Here we are passing image by renaming it with a unique name
             * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("pic", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(getActivity().getApplicationContext()).add(volleyMultipartRequest);
    }

    public void onBackground(){
        UrlBean urlBean = new UrlBean();
        String url = urlBean.getGetUserDataUrl();
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
                (Request.Method.POST, url, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //Check if user got registered successfully
                            if (response.getInt(KEY_STATUS) == 0) {
                                session.updateUserData(response.getString(KEY_EMAIL), response.getString(KEY_MOBILE_NUMBER), response.getString(KEY_PHONE_NUMBER), response.getString(KEY_STATUS_USER), response.getString(KEY_PROF_PIC), response.getInt(KEY_IS_VERIFIED), response.getString(KEY_FIRST_NAME), response.getString(KEY_MIDDLE_NAME), response.getString(KEY_LAST_NAME), response.getString(KEY_GENDER));

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
