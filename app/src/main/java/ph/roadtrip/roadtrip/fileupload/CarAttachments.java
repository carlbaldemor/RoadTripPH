package ph.roadtrip.roadtrip.fileupload;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CarAttachments {

    private String dateAdded;
    private int carAttachID;
    private String attachmentType;
    private String attachment;
    private int carID;

    public CarAttachments(JSONObject object){
        try {
            this.carAttachID = object.getInt("carAttachID");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public CarAttachments() {

    }

    // Factory method to convert an array of JSON objects into a list of objects
    // User.fromJson(jsonArray);
    public static ArrayList<CarAttachments> fromJson(JSONArray jsonObjects) {
        ArrayList<CarAttachments> reviews = new ArrayList<CarAttachments>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                reviews.add(new CarAttachments(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return reviews;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public int getCarAttachID() {
        return carAttachID;
    }

    public void setCarAttachID(int carAttachID) {
        this.carAttachID = carAttachID;
    }

    public String getAttachmentType() {
        return attachmentType;
    }

    public void setAttachmentType(String attachmentType) {
        this.attachmentType = attachmentType;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public int getCarID() {
        return carID;
    }

    public void setCarID(int carID) {
        this.carID = carID;
    }


}
