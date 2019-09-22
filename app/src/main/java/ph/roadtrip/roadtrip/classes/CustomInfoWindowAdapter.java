package ph.roadtrip.roadtrip.classes;

import android.app.Activity;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import org.json.JSONException;
import org.json.JSONObject;

import ph.roadtrip.roadtrip.R;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter{

    private Activity context;
    private int rating;
    private RatingBar RTB;
    private String brandName;
    private String modelName;
    private String address;

    public CustomInfoWindowAdapter(Activity context,String brandName, String address, String modelName){
        this.context = context;
        this.brandName = brandName;
        this.modelName = modelName;
        this.address = address;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = context.getLayoutInflater().inflate(R.layout.custom_info_layout, null);

        TextView tvBrandName = (TextView) view.findViewById(R.id.title);
        TextView tvModelName = (TextView) view.findViewById(R.id.details);
        TextView tvAddress = (TextView) view.findViewById(R.id.address);

        // get json object associated with it:
        JSONObject obj = (JSONObject) marker.getTag();

        try {
            brandName = obj.getString("brandName");
            modelName = obj.getString("modelName");
            address = obj.getString("address");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        tvBrandName.setText(brandName);
        tvModelName.setText(modelName);
        tvAddress.setText(marker.getSnippet());

        return view;
    }
}
