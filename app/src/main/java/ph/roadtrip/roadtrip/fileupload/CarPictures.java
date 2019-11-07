package ph.roadtrip.roadtrip.fileupload;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CarPictures {

    private String dateAdded;
    private int carpictureID;
    private String attachment;
    private int recordID;

    public CarPictures(JSONObject object){
        try {
            this.carpictureID = object.getInt("carpictureID");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public CarPictures() {

    }

    // Factory method to convert an array of JSON objects into a list of objects
    // User.fromJson(jsonArray);
    public static ArrayList<CarPictures> fromJson(JSONArray jsonObjects) {
        ArrayList<CarPictures> reviews = new ArrayList<CarPictures>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                reviews.add(new CarPictures(jsonObjects.getJSONObject(i)));
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

    public int getCarpictureID() {
        return carpictureID;
    }

    public void setCarpictureID(int carpictureID) {
        this.carpictureID = carpictureID;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public int getRecordID() {
        return recordID;
    }

    public void setRecordID(int recordID) {
        this.recordID = recordID;
    }



}
