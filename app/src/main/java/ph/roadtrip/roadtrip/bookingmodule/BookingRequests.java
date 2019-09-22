package ph.roadtrip.roadtrip.bookingmodule;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BookingRequests {

    private String color;
    private String brandName;
    private String modelName;
    private String modelYear;
    private String imageUrl;
    private String firstName;
    private int bookingID;
    private String totalAmount;
    private int owner_userID;
    private String owner_firstName;
    private String owner_lastName;


    private String owner_profilePicture;

    public BookingRequests(){

    }

    public BookingRequests(JSONObject object){
        try {
            this.bookingID = object.getInt("bookingID");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Factory method to convert an array of JSON objects into a list of objects
    // User.fromJson(jsonArray);
    public static ArrayList<BookingRequests> fromJson(JSONArray jsonObjects) {
        ArrayList<BookingRequests> bookingRequests = new ArrayList<BookingRequests>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                bookingRequests.add(new BookingRequests(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return bookingRequests;
    }

    public BookingRequests( String color, String brandName , String modelName, String modelYear, String imageUrl, String firstName, int bookingID, String totalAmount){
        this.color = color;
        this.brandName = brandName;
        this.modelName = modelName;
        this.modelYear = modelYear;
        this.imageUrl = imageUrl;
        this.firstName = firstName;
        this.bookingID = bookingID;
        this.totalAmount = totalAmount;
    }

    public String getOwner_profilePicture() {
        return owner_profilePicture;
    }

    public void setOwner_profilePicture(String owner_profilePicture) {
        this.owner_profilePicture = owner_profilePicture;
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

    public int getOwner_userID() {
        return owner_userID;
    }

    public void setOwner_userID(int owner_userID) {
        this.owner_userID = owner_userID;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
    public int getBookingID() {
        return bookingID;
    }

    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }


    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getModelYear() {
        return modelYear;
    }

    public void setModelYear(String modelYear) {
        this.modelYear = modelYear;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    private String lastName;

}
