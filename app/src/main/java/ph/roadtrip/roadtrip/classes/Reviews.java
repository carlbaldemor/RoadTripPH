package ph.roadtrip.roadtrip.classes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Reviews {

    private int reviewID;
    private int bookingID;
    private int reviewer;
    private int reviewee;
    private int rating;
    private String dateAdded;
    private String reviewerFirstName;
    private String reviewerLastName;
    private String message;
    private String profPic;
    private String imageUrl;

    public Reviews(){

    }

    public Reviews(JSONObject object){
        try {
            this.reviewID = object.getInt("reviewID");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Factory method to convert an array of JSON objects into a list of objects
    // User.fromJson(jsonArray);
    public static ArrayList<Reviews> fromJson(JSONArray jsonObjects) {
        ArrayList<Reviews> reviews = new ArrayList<Reviews>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                reviews.add(new Reviews(jsonObjects.getJSONObject(i)));
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getProfPic() {
        return profPic;
    }

    public void setProfPic(String profPic) {
        this.profPic = profPic;
    }


    public int getReviewID() {
        return reviewID;
    }

    public void setReviewID(int reviewID) {
        this.reviewID = reviewID;
    }

    public int getBookingID() {
        return bookingID;
    }

    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }

    public int getReviewer() {
        return reviewer;
    }

    public void setReviewer(int reviewer) {
        this.reviewer = reviewer;
    }

    public int getReviewee() {
        return reviewee;
    }

    public void setReviewee(int reviewee) {
        this.reviewee = reviewee;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getReviewerFirstName() {
        return reviewerFirstName;
    }

    public void setReviewerFirstName(String reviewerFirstName) {
        this.reviewerFirstName = reviewerFirstName;
    }

    public String getReviewerLastName() {
        return reviewerLastName;
    }

    public void setReviewerLastName(String reviewerLastName) {
        this.reviewerLastName = reviewerLastName;
    }
}
