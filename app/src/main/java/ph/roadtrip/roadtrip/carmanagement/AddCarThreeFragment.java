package ph.roadtrip.roadtrip.carmanagement;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.kimkevin.cachepot.CachePot;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ph.roadtrip.roadtrip.R;
import ph.roadtrip.roadtrip.classes.CarPictures;
import ph.roadtrip.roadtrip.classes.EndPoints;
import ph.roadtrip.roadtrip.classes.MySingleton;
import ph.roadtrip.roadtrip.classes.SessionHandler;
import ph.roadtrip.roadtrip.classes.UrlBean;
import ph.roadtrip.roadtrip.classes.User;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;

public class AddCarThreeFragment extends Fragment {

    private static final String KEY_STATUS = "status1";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_OWNER_ID = "ownerID";
    private static final String KEY_RECORD_ID = "recordID";
    private static final String KEY_MODEL_ID = "modelID";
    private static final String KEY_COLOR = "color";
    private static final String KEY_YEAR = "year";
    private static final String KEY_PLATE_NUMBER = "plateNumber";
    private static final String KEY_CHASSIS_NUMBER = "chassisNumber";
    private static final String KEY_LAT_RETURN = "latReturn";
    private static final String KEY_LONG_RETURN = "longReturn";
    private static final String KEY_LAT_ISSUE = "latIssue";
    private static final String KEY_LONG_ISSUE = "longIssue";
    private static final String KEY_SERVICE_TYPE = "serviceType";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_RECORD_STATUS = "status";

    private static final String KEY_IMG1 = "image1";
    private static final String KEY_IMG2 = "image2";

    private int ownerID;
    private int modelID;
    private String recordID;
    private String color;
    private String year;
    private String plateNumber;
    private String chassisNumber;
    private String latIssue;
    private String longIssue;
    private String latReturn;
    private String longReturn;
    private String serviceType;
    private String amount;
    private int model_pos;

    Spinner brandSpinner, modelSpinner, colorSpinner, yearSpinner, serviceSpinner;
    ArrayAdapter<String> dataAdapter2;
    private ImageView pic1, pic2, pic3, pic4, pic5, pic6;
    private GoogleMap mMap;
    private GoogleApiClient client;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private Marker currentLocationMarker;
    public static final int REQUEST_LOCATION_CODE = 99;
    private Button search, addCar, search2;
    private EditText tf_location, etPlateNumber, etChassisNumber, etAmount, tf_location2;
    private ProgressDialog pDialog;
    private String addCarLink;
    private SessionHandler session;
    private String username;
    private Bitmap img1, img2, img3, img4, img5, img6;
    private boolean validPic1, validPic2, validPic3, validPic4, validPic5, validPic6;
    private String user_status;
    private long imageSize;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_car_page_three, container, false);


        //Retrieve the value
        color = getArguments().getString("color");
        year = getArguments().getString("year");
        modelID = getArguments().getInt("modelID");
        plateNumber = getArguments().getString("plateNumber");
        chassisNumber = getArguments().getString("chassisNumber");
        latIssue = getArguments().getString("latIssue");
        longIssue = getArguments().getString("longIssue");
        latReturn = getArguments().getString("latReturn");
        longReturn = getArguments().getString("longReturn");

        //Get Username of user
        session = new SessionHandler(getActivity().getApplicationContext());
        User user = session.getUserDetails();
        username = user.getUsername();
        user_status = user.getStatus();

        CarRecord user2 = session.getOwnerID();
        ownerID = user2.getOwnerID();

        pic1 = view.findViewById(R.id.pic1);
        pic2 = view.findViewById(R.id.pic2);
        pic3 = view.findViewById(R.id.pic3);
        pic4 = view.findViewById(R.id.pic4);
        pic5 = view.findViewById(R.id.pic5);
        pic6 = view.findViewById(R.id.pic6);
        etAmount = view.findViewById(R.id.etAmount);
        serviceSpinner = (Spinner) view.findViewById(R.id.serviceSpinner);

        final CarPictures carPictures = new CarPictures();


        //Open Gallery
        view.findViewById(R.id.pic1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Open Gallery
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 100);
            }
        });
        view.findViewById(R.id.pic2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Open Gallery
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 200);
            }
        });
        view.findViewById(R.id.pic3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Open Gallery
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 300);
            }
        });
        view.findViewById(R.id.pic4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Open Gallery
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 400);
            }
        });
        view.findViewById(R.id.pic5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Open Gallery
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 500);
            }
        });
        view.findViewById(R.id.pic6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Open Gallery
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 600);
            }
        });

        view.findViewById(R.id.btnNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validateInputs()) {
                    serviceType = serviceSpinner.getSelectedItem().toString();
                    amount = etAmount.getText().toString();

                    //Put the value
                    AddCarFourFragment ldf = new AddCarFourFragment();
                    Bundle args = new Bundle();
                    args.putString("color", color);
                    args.putString("year", year);
                    args.putInt("modelID", modelID);
                    args.putString("plateNumber", plateNumber);
                    args.putString("chassisNumber", chassisNumber);
                    args.putString("latIssue", latIssue);
                    args.putString("longIssue", longIssue);
                    args.putString("latReturn", latReturn);
                    args.putString("longReturn", longReturn);
                    args.putString("serviceType", serviceType);
                    args.putString("amount", amount);
                    if (img1 != null) {
                        session.setCarPic1(BitMapToString(img1));
                    }
                    if (img2 != null) {
                        session.setCarPic2(BitMapToString(img2));
                    }
                    if (img3 != null) {
                        session.setCarPic3(BitMapToString(img3));
                    }
                    if (img4 != null) {
                        session.setCarPic4(BitMapToString(img4));
                    }
                    if (img5 != null) {
                        session.setCarPic5(BitMapToString(img5));
                    }
                    if (img6 != null) {
                        session.setCarPic6(BitMapToString(img6));
                    }
                    ldf.setArguments(args);

                    //Inflate the fragment
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, ldf);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                }
            }
        });

        return view;
    }

    private boolean validateInputs(){
        if(etAmount.getText().toString().trim().length() == 0){
            etAmount.setError("Amount cannot be empty");
            etAmount.requestFocus();
            return false;
        }
        return true;
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,60, baos);
        byte [] b=baos.toByteArray();
        String temp=Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
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

                imageSize = getFileDataFromDrawable(bitmap).length / 1024;

                if (imageSize > 2000) {
                    Toast.makeText(getActivity(), String.valueOf(imageSize) + "KB " + "File too large!", Toast.LENGTH_LONG).show();
                } else {
                    //displaying selected image to imageview
                    pic1.setImageBitmap(bitmap);
                    //Check this if user placed a picture
                    validPic1 = true;
                    img1 = bitmap;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == 200 && resultCode == RESULT_OK && data != null) {

            //getting the image Uri
            Uri imageUri = data.getData();
            try {
                //getting bitmap object from uri
                Bitmap bitmap2 = MediaStore.Images.Media.getBitmap(this.getActivity().getApplicationContext().getContentResolver(), imageUri);

                imageSize = getFileDataFromDrawable(bitmap2).length / 1024;

                if (imageSize > 2000) {
                    Toast.makeText(getActivity(), String.valueOf(imageSize) + "KB " + "File too large!", Toast.LENGTH_LONG).show();
                } else {
                    //displaying selected image to imageview
                    pic2.setImageBitmap(bitmap2);
                    //Check this if user placed a picture
                    validPic2 = true;
                    img2 = bitmap2;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == 300 && resultCode == RESULT_OK && data != null) {

            //getting the image Uri
            Uri imageUri = data.getData();
            try {
                //getting bitmap object from uri
                Bitmap bitmap3 = MediaStore.Images.Media.getBitmap(this.getActivity().getApplicationContext().getContentResolver(), imageUri);

                imageSize = getFileDataFromDrawable(bitmap3).length / 1024;

                if (imageSize > 2000) {
                    Toast.makeText(getActivity(), String.valueOf(imageSize) + "KB " + "File too large!", Toast.LENGTH_LONG).show();
                } else {
                    //displaying selected image to imageview
                    pic3.setImageBitmap(bitmap3);
                    //Check this if user placed a picture
                    validPic3 = true;
                    img3 = bitmap3;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == 400 && resultCode == RESULT_OK && data != null) {

            //getting the image Uri
            Uri imageUri = data.getData();
            try {
                //getting bitmap object from uri
                Bitmap bitmap4 = MediaStore.Images.Media.getBitmap(this.getActivity().getApplicationContext().getContentResolver(), imageUri);

                imageSize = getFileDataFromDrawable(bitmap4).length / 1024;

                if (imageSize > 2000) {
                    Toast.makeText(getActivity(), String.valueOf(imageSize) + "KB " + "File too large!", Toast.LENGTH_LONG).show();
                } else {
                    //displaying selected image to imageview
                    pic4.setImageBitmap(bitmap4);
                    //Check this if user placed a picture
                    validPic4 = true;
                    img4 = bitmap4;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == 500 && resultCode == RESULT_OK && data != null) {

            //getting the image Uri
            Uri imageUri = data.getData();
            try {
                //getting bitmap object from uri
                Bitmap bitmap5 = MediaStore.Images.Media.getBitmap(this.getActivity().getApplicationContext().getContentResolver(), imageUri);

                imageSize = getFileDataFromDrawable(bitmap5).length / 1024;

                if (imageSize > 2000) {
                    Toast.makeText(getActivity(), String.valueOf(imageSize) + "KB " + "File too large!", Toast.LENGTH_LONG).show();
                } else {
                    //displaying selected image to imageview
                    pic5.setImageBitmap(bitmap5);
                    //Check this if user placed a picture
                    validPic5 = true;
                    img5 = bitmap5;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == 600 && resultCode == RESULT_OK && data != null) {

            //getting the image Uri
            Uri imageUri = data.getData();
            try {
                //getting bitmap object from uri
                Bitmap bitmap6 = MediaStore.Images.Media.getBitmap(this.getActivity().getApplicationContext().getContentResolver(), imageUri);

                imageSize = getFileDataFromDrawable(bitmap6).length / 1024;

                if (imageSize > 2000) {
                    Toast.makeText(getActivity(), String.valueOf(imageSize) + "KB " + "File too large!", Toast.LENGTH_LONG).show();
                } else {
                    //displaying selected image to imageview
                    pic6.setImageBitmap(bitmap6);
                    //Check this if user placed a picture
                    validPic6 = true;
                    img6 = bitmap6;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

}

