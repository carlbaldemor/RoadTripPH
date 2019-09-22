package ph.roadtrip.roadtrip.myfavorites;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyFavorites {

    private int userID;
    private int ownerID;
    private String owner_firstName;
    private String owner_lastName;
    private String profilePicture;
    private String dateAdded;
    private int favoriteID;

    public MyFavorites(){

    }

    public MyFavorites(JSONObject object){
        try {
            this.favoriteID = object.getInt("favoriteID");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Factory method to convert an array of JSON objects into a list of objects
    // User.fromJson(jsonArray);
    public static ArrayList<MyFavorites> fromJson(JSONArray jsonObjects) {
        ArrayList<MyFavorites> myFavorites = new ArrayList<MyFavorites>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                myFavorites.add(new MyFavorites(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return myFavorites;
    }


    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }
    public int getFavoriteID() {
        return favoriteID;
    }

    public void setFavoriteID(int favoriteID) {
        this.favoriteID = favoriteID;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }

    public String getOwner_firstName() {
        return owner_firstName;
    }

    public void setOwner_firstName(String owner_firstName) {
        this.owner_firstName = owner_firstName;
    }

    public String getOwner_lastName() {
        return owner_lastName;
    }

    public void setOwner_lastName(String owner_lastName) {
        this.owner_lastName = owner_lastName;
    }

}
