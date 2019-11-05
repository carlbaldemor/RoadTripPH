package ph.roadtrip.roadtrip.profile;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import ph.roadtrip.roadtrip.carmanagement.AppController;
import ph.roadtrip.roadtrip.classes.Reviews;
import ph.roadtrip.roadtrip.classes.SessionHandler;
import ph.roadtrip.roadtrip.MyApplication;
import ph.roadtrip.roadtrip.R;

public class CustomListReviewAdapter extends BaseAdapter {
    private SessionHandler session;
    private Activity activity;
    private int brandPos;
    private int modelPos;
    private LayoutInflater inflater;
    private List<Reviews> toReviewItems;
    ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();

    public CustomListReviewAdapter(Activity activity, List<Reviews> toReviewItems) {
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
            convertView = inflater.inflate(R.layout.list_profile_review, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.picture_list);
        TextView firstName = (TextView) convertView.findViewById(R.id.firstName);
        TextView lastName = (TextView) convertView.findViewById(R.id.lastName);
        TextView dateAdded = (TextView) convertView.findViewById(R.id.dateAdded);
        TextView tvRating = (TextView) convertView.findViewById(R.id.tvRating);
        RatingBar ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBar);
        TextView tvMessage = (TextView) convertView.findViewById(R.id.tvMessage);

        // getting movie data for the row
        Reviews m = toReviewItems.get(position);

        // thumbnail imageS
        thumbNail.setImageUrl(m.getImageUrl(), imageLoader);

        // title
        firstName.setText(m.getFirstName());
        lastName.setText(m.getLastName());
        dateAdded.setText(m.getDateAdded());
        tvRating.setText(String.valueOf(m.getRating()));
        ratingBar.setRating(m.getRating());
        tvMessage.setText(m.getMessage());

        final Reviews items = toReviewItems.get(position);

        return convertView;
    }


}
