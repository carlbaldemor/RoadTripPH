package ph.roadtrip.roadtrip.chat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ph.roadtrip.roadtrip.myfavorites.MyFavorites;

public class Messages {

    private int chatID;
    private int chatContentID;
    private String dateAdded;
    private String startedDateTime;
    private String endedDateTime;
    private String message;
    private String sender;
    private String receiver;
    private String sender_firstName;
    private String sender_lastName;
    private String imageUrl;
    private int chatcontent_sender;

    public Messages(){

    }

    public Messages(String sender_firstName, String sender_lastName, String message, String imageUrl) {
        this.sender_firstName = sender_firstName;
        this.sender_lastName = sender_lastName;
        this.message = message;
        this.imageUrl = imageUrl;
    }


    public Messages(JSONObject object){
        try {
            this.chatContentID = object.getInt("chatContentID");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    // Factory method to convert an array of JSON objects into a list of objects
    //User.fromJson(jsonArray);
    public static ArrayList<Messages> fromJson(JSONArray jsonObjects) {
        ArrayList<Messages> messages = new ArrayList<Messages>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                messages.add(new Messages(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return messages;
    }

    public int getChatcontent_sender() {
        return chatcontent_sender;
    }

    public void setChatcontent_sender(int chatcontent_sender) {
        this.chatcontent_sender = chatcontent_sender;
    }
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSender_firstName() {
        return sender_firstName;
    }

    public void setSender_firstName(String sender_firstName) {
        this.sender_firstName = sender_firstName;
    }

    public String getSender_lastName() {
        return sender_lastName;
    }

    public void setSender_lastName(String sender_lastName) {
        this.sender_lastName = sender_lastName;
    }

    public int getChatID() {
        return chatID;
    }

    public void setChatID(int chatID) {
        this.chatID = chatID;
    }

    public int getChatContentID() {
        return chatContentID;
    }

    public void setChatContentID(int chatContentID) {
        this.chatContentID = chatContentID;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getStartedDateTime() {
        return startedDateTime;
    }

    public void setStartedDateTime(String startedDateTime) {
        this.startedDateTime = startedDateTime;
    }

    public String getEndedDateTime() {
        return endedDateTime;
    }

    public void setEndedDateTime(String endedDateTime) {
        this.endedDateTime = endedDateTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
}
