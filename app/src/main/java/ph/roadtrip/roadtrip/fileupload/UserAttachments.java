package ph.roadtrip.roadtrip.fileupload;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ph.roadtrip.roadtrip.classes.Reviews;

public class UserAttachments {

    private String dateAdded;
    private int attachmentID;
    private String attachment;
    private int userID;

    private String imageUrl;

    public UserAttachments(JSONObject object){
        try {
            this.attachmentID = object.getInt("attachmentID");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public UserAttachments() {

    }

    // Factory method to convert an array of JSON objects into a list of objects
    // User.fromJson(jsonArray);
    public static ArrayList<UserAttachments> fromJson(JSONArray jsonObjects) {
        ArrayList<UserAttachments> reviews = new ArrayList<UserAttachments>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                reviews.add(new UserAttachments(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return reviews;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public int getAttachmentID() {
        return attachmentID;
    }

    public void setAttachmentID(int attachmentID) {
        this.attachmentID = attachmentID;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

}
