package ph.roadtrip.roadtrip.carmanagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CarRecord {
    private String brandName;
    private String modelName;
    private String recordLat;
    private String recordLong;
    private String modelYear;
    private String plateNumber;
    private String carType;
    private String status;
    private String imageUrl;
    private String dateAdded;
    private int recordID;
    private int carID;
    private int modelID;
    private int ownerID;
    private double amount;
    private int brandPos;
    private int modelPos;

    public CarRecord(){

    }

    public CarRecord(JSONObject object){
        try {
            this.recordID = object.getInt("recordID");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Factory method to convert an array of JSON objects into a list of objects
    // User.fromJson(jsonArray);
    public static ArrayList<CarRecord> fromJson(JSONArray jsonObjects) {
        ArrayList<CarRecord> carRecord = new ArrayList<CarRecord>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                carRecord.add(new CarRecord(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return carRecord;
    }



    public CarRecord(int ownerID, String brandName, String modelName, String recordLat, String recordLong, String modelYear,
                     String plateNumber, String carType, String status, String imageUrl, int recordID,
                     int carID, int modelID, double amount, int brandPos, int modelPos){
        this.ownerID = ownerID;
        this.brandName = brandName;
        this.modelName = modelName;
        this.recordLat = recordLat;
        this.recordLong = recordLong;
        this.modelYear = modelYear;
        this.plateNumber = plateNumber;
        this.recordID = recordID;
        this.carID = carID;
        this.modelID = modelID;
        this.amount = amount;
        this.carType = carType;
        this.status = status;
        this.imageUrl = imageUrl;
        this.brandPos = brandPos;
        this.modelPos = modelPos;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }
    public int getBrandPos() {
        return brandPos;
    }

    public void setBrandPos(int brandPos) {
        this.brandPos = brandPos;
    }

    public int getModelPos() {
        return modelPos;
    }

    public void setModelPos(int modelPos) {
        this.modelPos = modelPos;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getModelYear() {
        return modelYear;
    }

    public void setModelYear(String modelYear) {
        this.modelYear = modelYear;
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

    public String getRecordLat() {
        return recordLat;
    }

    public void setRecordLat(String recordLat) {
        this.recordLat = recordLat;
    }

    public String getRecordLong() {
        return recordLong;
    }

    public void setRecordLong(String recordLong) {
        this.recordLong = recordLong;
    }

    public int getRecordID() {
        return recordID;
    }

    public void setRecordID(int recordID) {
        this.recordID = recordID;
    }

    public int getCarID() {
        return carID;
    }

    public void setCarID(int carID) {
        this.carID = carID;
    }

    public int getModelID() {
        return modelID;
    }

    public void setModelID(int modelID) {
        this.modelID = modelID;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
