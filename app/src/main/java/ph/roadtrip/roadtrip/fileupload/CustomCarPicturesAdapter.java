package ph.roadtrip.roadtrip.fileupload;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.HashMap;
import java.util.List;

import ph.roadtrip.roadtrip.MyApplication;
import ph.roadtrip.roadtrip.R;
import ph.roadtrip.roadtrip.carmanagement.AppController;
import ph.roadtrip.roadtrip.classes.SessionHandler;

public class CustomCarPicturesAdapter extends BaseAdapter {
    private SessionHandler session;
    private Activity activity;
    private int brandPos;
    private int modelPos;
    private LayoutInflater inflater;
    private List<CarPictures> toReviewItems;
    ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();

    public CustomCarPicturesAdapter(Activity activity, List<CarPictures> toReviewItems) {
        this.activity = activity;
        this.toReviewItems = toReviewItems;
    }

    @Override
    public int getCount() {
        return toReviewItems.size();
    }

    @Override
    public Object getItem(int location) {
        return toReviewItems.get(location);
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
            convertView = inflater.inflate(R.layout.list_car_attachment, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.apic1);
        TextView dateAdded = (TextView) convertView.findViewById(R.id.date1);
        TextView attachType = (TextView) convertView.findViewById(R.id.attachType);
        ImageView delete = (ImageView) convertView.findViewById(R.id.delete1);

        // getting movie data for the row
        CarPictures m = toReviewItems.get(position);

        final CarPictures items = toReviewItems.get(position);
        HashMap<String,String> Hash_file_maps ;
        Hash_file_maps = new HashMap<String, String>();



        return convertView;
    }


}
