package ph.roadtrip.roadtrip.chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.w3c.dom.Text;

import java.util.List;

import ph.roadtrip.roadtrip.MyApplication;
import ph.roadtrip.roadtrip.R;
import ph.roadtrip.roadtrip.bookingmodule.BookServiceActivity;
import ph.roadtrip.roadtrip.carmanagement.AppController;
import ph.roadtrip.roadtrip.classes.SessionHandler;
import ph.roadtrip.roadtrip.classes.User;
import ph.roadtrip.roadtrip.myfavorites.MyFavorites;
import ph.roadtrip.roadtrip.transactionhistory.AddReviewFragment;

public class CustomMessagesAdapter extends BaseAdapter {
    private SessionHandler session;
    private Activity activity;
    private int brandPos;
    private int modelPos;
    private int userID;
    private LayoutInflater inflater;
    private List<Messages> messageItems;
    ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();

    public CustomMessagesAdapter(Activity activity, List<Messages> messageItems) {
        this.activity = activity;
        this.messageItems = messageItems;
    }

    @Override
    public int getCount() {
        return messageItems.size();
    }

    @Override
    public Object getItem(int location) {
        return messageItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MessageViewHolder holder = new MessageViewHolder();
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        session = new SessionHandler(activity);
        User user = session.getUserDetails();
        userID = user.getUserID();
        // getting movie data for the row

        Messages m = messageItems.get(position);

        if(m.getChatcontent_sender() != userID){
                convertView = inflater.inflate(R.layout.their_messages, null);

            holder.firstName = (TextView) convertView.findViewById(R.id.firstName);
            holder.lastName = (TextView) convertView.findViewById(R.id.lastName);
            holder.messageBody = (TextView) convertView.findViewById(R.id.message_body);
            holder.dateAdded = (TextView) convertView.findViewById(R.id.dateAdded);
            convertView.setTag(holder);

            // title
            holder.firstName.setText(m.getSender_firstName());
            holder.lastName.setText(m.getSender_lastName());
            holder.messageBody.setText(m.getMessage());
            holder.dateAdded.setText(m.getDateAdded());

        } else {
                convertView = inflater.inflate(R.layout.my_messages, null);
            holder.messageBody = (TextView) convertView.findViewById(R.id.message_body);
            convertView.setTag(holder);
            holder.messageBody.setText(m.getMessage());
        }


        //dateAdded.setText(m.getDateAdded());


        //final Messages items = messageItems.get(position);

        return convertView;
    }

    class MessageViewHolder {
        public NetworkImageView avatar;
        public TextView firstName;
        public TextView lastName;
        public TextView messageBody;
        public TextView dateAdded;
        //public NetworkImageView avatar;
    }


}
