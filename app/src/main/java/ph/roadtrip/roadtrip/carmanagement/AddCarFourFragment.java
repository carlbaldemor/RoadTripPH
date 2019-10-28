package ph.roadtrip.roadtrip.carmanagement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.kimkevin.cachepot.CachePot;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ph.roadtrip.roadtrip.R;
import ph.roadtrip.roadtrip.classes.SessionHandler;
import ph.roadtrip.roadtrip.classes.User;

import static android.app.Activity.RESULT_OK;

public class AddCarFourFragment extends Fragment {

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

    private ImageView apic1, bpic1;
    public static final int REQUEST_LOCATION_CODE = 99;
    private Button btnNext;
    private SessionHandler session;
    private String username;
    private Bitmap img1, img2;
    private boolean validPic1, validPic2;
    private String user_status;
    private String str1 = null, str2 = null, str3 = null, str4 = null, str5 = null, str6 = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_car_page_four, container, false);


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
        serviceType = getArguments().getString("serviceType");
        amount = getArguments().getString("amount");
        if (getArguments().getString("testImage1") != null){
            str1 = getArguments().getString("testImage1");
        }
        if (getArguments().getString("testImage2") != null){
            str2 = getArguments().getString("testImage2");
        }
        if (getArguments().getString("testImage3") != null){
            str3 = getArguments().getString("testImage3");
        }
        if (getArguments().getString("testImage4") != null){
            str4 = getArguments().getString("testImage4");
        }
        if (getArguments().getString("testImage5") != null){
            str5 = getArguments().getString("testImage5");
        }
        if (getArguments().getString("testImage6") != null){
            str6 = getArguments().getString("testImage6");
        }

        //Get Username of user
        session = new SessionHandler(getActivity().getApplicationContext());
        User user = session.getUserDetails();
        username = user.getUsername();
        user_status = user.getStatus();

        CarRecord user2 = session.getOwnerID();
        ownerID = user2.getOwnerID();

        apic1 = view.findViewById(R.id.apic1);
        bpic1 = view.findViewById(R.id.bpic1);

        //Open Gallery
        view.findViewById(R.id.apic1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Open Gallery
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 100);
            }
        });
        view.findViewById(R.id.bpic1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Open Gallery
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 200);
            }
        });

        view.findViewById(R.id.btnNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Put the value
                AddCarFiveFragment ldf = new AddCarFiveFragment ();
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
                if (str1 != null){
                    args.putString("testImage1", str1);
                }
                if (str2 != null) {
                    args.putString("testImage2", str2);
                }
                if (str3 != null) {
                    args.putString("testImage3", str3);
                }
                if (str4 != null) {
                    args.putString("testImage4", str4);
                }
                if (str5 != null) {
                    args.putString("testImage5", str5);
                }
                if (str6 != null){
                    args.putString("testImage6", str6);
                }
                if (img1 != null){
                    args.putString("imageOR", BitMapToString(img1));
                }
                if (img2 != null) {
                    args.putString("imageCR", BitMapToString(img2));
                }
                ldf.setArguments(args);

                //Inflate the fragment
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, ldf);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return view;
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp=Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    /**
     * @param encodedString
     * @return bitmap (from given string)
     */
    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
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

                //displaying selected image to imageview
                apic1.setImageBitmap(bitmap);

                //Check this if user placed a picture
                validPic1 = true;
                img1 = bitmap;

                //calling the method uploadBitmap to upload image
                //uploadBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == 200 && resultCode == RESULT_OK && data != null){

            //getting the image Uri
            Uri imageUri = data.getData();
            try {
                //getting bitmap object from uri
                Bitmap bitmap2 = MediaStore.Images.Media.getBitmap(this.getActivity().getApplicationContext().getContentResolver(), imageUri);

                //displaying selected image to imageview
                bpic1.setImageBitmap(bitmap2);
                //Check this if user placed a picture
                validPic2 = true;
                img2 = bitmap2;

                //calling the method uploadBitmap to upload image
                //uploadBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

        }
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

}

