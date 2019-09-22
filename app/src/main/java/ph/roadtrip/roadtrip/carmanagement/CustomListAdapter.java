package ph.roadtrip.roadtrip.carmanagement;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import ph.roadtrip.roadtrip.classes.SessionHandler;
import ph.roadtrip.roadtrip.MyApplication;
import ph.roadtrip.roadtrip.R;

public class CustomListAdapter extends BaseAdapter {
    private SessionHandler session;
    private Activity activity;
    private int brandPos;
    private int modelPos;
    private LayoutInflater inflater;
    private List<CarRecord> movieItems;
    ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();

    public CustomListAdapter(Activity activity, List<CarRecord> movieItems) {
        this.activity = activity;
        this.movieItems = movieItems;
    }

    @Override
    public int getCount() {
        return movieItems.size();
    }

    @Override
    public Object getItem(int location) {
        return movieItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_layout, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.picture_list);
        TextView brandName = (TextView) convertView.findViewById(R.id.brandName);
        TextView modelName = (TextView) convertView.findViewById(R.id.modelName);
        TextView modelYear = (TextView) convertView.findViewById(R.id.year);
        TextView plateNumber = (TextView) convertView.findViewById(R.id.plateNumber);
        TextView carType = (TextView) convertView.findViewById(R.id.carType);
        TextView status = (TextView) convertView.findViewById(R.id.status);
        final TextView recordID = (TextView) convertView.findViewById(R.id.recordID);
        Button btnView = (Button) convertView.findViewById(R.id.btnView);

        // getting movie data for the row
        CarRecord m = movieItems.get(position);

        // thumbnail imageS
        thumbNail.setImageUrl(m.getImageUrl(), imageLoader);

        // title
        brandName.setText(m.getBrandName());
        modelName.setText(m.getModelName());
        modelYear.setText(m.getModelYear());
        plateNumber.setText(m.getModelYear());
        carType.setText(m.getCarType());
        status.setText(m.getStatus());
        recordID.setText(String.valueOf(m.getRecordID()));

        final CarRecord items = movieItems.get(position);

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewRecordFragment fragment = new ViewRecordFragment();
                session = new SessionHandler(activity);
                session.setItBaby(items.getRecordID(), items.getStatus(), items.getCarID());

                ((AppCompatActivity)activity).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            }
        });



        return convertView;
    }


}
