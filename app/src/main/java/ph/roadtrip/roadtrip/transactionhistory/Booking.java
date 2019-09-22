package ph.roadtrip.roadtrip.transactionhistory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Booking {

    private int userID;
    private int ownerID;
    private int bookingID;
    private int recordID;
    private int carID;
    private String totalAmount;
    private String status;
    private String startDate;
    private String endDate;
    private String longIssue;
    private String latIssue;
    private String longReturn;
    private String latReturn;
    private String brandName;
    private String modelName;
    private String modelYear;
    private String color;
    private String recordPicture;
    private String plateNumber;
    private int carowner_userID;
    private int renter_userID;
    private boolean favorite;


    public Booking(){

    }

    public Booking(JSONObject object){
        try {
            this.bookingID = object.getInt("bookingID");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Factory method to convert an array of JSON objects into a list of objects
    // User.fromJson(jsonArray);
    public static ArrayList<Booking> fromJson(JSONArray jsonObjects) {
        ArrayList<Booking> bookings = new ArrayList<Booking>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                bookings.add(new Booking(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return bookings;
    }

    public boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public int getRenter_userID() {
        return renter_userID;
    }

    public void setRenter_userID(int renter_userID) {
        this.renter_userID = renter_userID;
    }

    public int getCarID() {
        return carID;
    }

    public void setCarID(int carID) {
        this.carID = carID;
    }

    public int getCarowner_userID() {
        return carowner_userID;
    }

    public void setCarowner_userID(int carowner_userID) {
        this.carowner_userID = carowner_userID;
    }

    public Booking (int bookingID){
        this.bookingID = bookingID;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getRecordPicture() {
        return recordPicture;
    }

    public void setRecordPicture(String recordPicture) {
        this.recordPicture = recordPicture;
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

    public int getBookingID() {
        return bookingID;
    }

    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }

    public int getRecordID() {
        return recordID;
    }

    public void setRecordID(int recordID) {
        this.recordID = recordID;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getLongIssue() {
        return longIssue;
    }

    public void setLongIssue(String longIssue) {
        this.longIssue = longIssue;
    }

    public String getLatIssue() {
        return latIssue;
    }

    public void setLatIssue(String latIssue) {
        this.latIssue = latIssue;
    }

    public String getLongReturn() {
        return longReturn;
    }

    public void setLongReturn(String longReturn) {
        this.longReturn = longReturn;
    }

    public String getLatReturn() {
        return latReturn;
    }

    public void setLatReturn(String latReturn) {
        this.latReturn = latReturn;
    }



}
