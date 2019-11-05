package ph.roadtrip.roadtrip.classes;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;

import ph.roadtrip.roadtrip.bookingmodule.BookingRequests;
import ph.roadtrip.roadtrip.carmanagement.CarRecord;
import ph.roadtrip.roadtrip.chat.Messages;
import ph.roadtrip.roadtrip.myfavorites.MyFavorites;
import ph.roadtrip.roadtrip.transactionhistory.Booking;


/**
 * Created by Abhi on 20 Jan 2018 020.
 */

public class SessionHandler {
    private static final String PREF_NAME = "UserSession";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EXPIRES = "expires";
    private static final String KEY_FIRST_NAME = "firstName";
    private static final String KEY_LAST_NAME = "lastName";
    private static final String KEY_MIDDLE_NAME = "middleName";
    private static final String KEY_EMAIL = "emailAddress";
    private static final String KEY_MOBILE_NUMBER = "mobileNumber";
    private static final String KEY_PHONE_NUMBER = "phoneNumber";
    private static final String KEY_STATUS_USER = "status";
    private static final String KEY_IS_VERIFIED = "isVerified";
    private static final String KEY_PROF_PIC = "profilePicture";
    private static final String KEY_EMPTY = "";
    private static final String KEY_USER_ID = "userID";
    private static final String KEY_OWNER_ID = "ownerID";
    private static final String KEY_RECORD_ID = "recordID";
    private static final String KEY_BRAND_POS = "brandPos";
    private static final String KEY_MODEL_POS = "modelPos";
    private static final String KEY_BOOKING_ID = "bookingID";
    private static final String KEY_CAR_ID = "carID";
    private static final String KEY_OWNER_USER_ID = "carowner_userID";
    private static final String KEY_RENTER_USER_ID = "renter_userID";
    private static final String KEY_OWNER_LAST_NAME = "owner_lastName";
    private static final String KEY_OWNER_FIRST_NAME = "owner_firstName";
    private static final String KEY_OWNER_PROF_PIC = "owner_profilePicture";
    private static final String KEY_CHAT_ID = "chatID";
    private static final String KEY_USER_TYPE_ID = "userTypeID";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_RECORD_STATUS = "status";

    //Car Pictures
    private static final String KEY_CAR_PIC_1 = "carPic1";
    private static final String KEY_CAR_PIC_2 = "carPic2";
    private static final String KEY_CAR_PIC_3 = "carPic3";
    private static final String KEY_CAR_PIC_4 = "carPic4";
    private static final String KEY_CAR_PIC_5 = "carPic5";
    private static final String KEY_CAR_PIC_6 = "carPic6";
    private static final String KEY_CAR_OR = "carOR";
    private static final String KEY_CAR_CR = "carCR";
    private static final String KEY_CAR_SIR = "carSIR";

    //BOOKING DETAILS
    private static final String KEY_BRAND_NAME = "brandName";
    private static final String KEY_MODEL_NAME = "modelName";
    private static final String KEY_ADDRESS = "address";


    private static final int EMPTY = 2;
    private Context mContext;
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mPreferences;

    public SessionHandler(Context mContext) {
        this.mContext = mContext;
        mPreferences = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.mEditor = mPreferences.edit();
    }

    /**
     * Logs in the user by saving user details and setting session
     *
     * @param username
     *
     */
    public void loginUser(int userID, String username, String firstName, String middleName, String lastName,
                          String emailAddress, String mobileNumber, String phoneNumber, String status,
                          int isVerified, String profilePicture, String userTypeID, String gender) {
        mEditor.putInt(KEY_USER_ID, userID);
        mEditor.putString(KEY_USERNAME, username);
        mEditor.putString(KEY_FIRST_NAME, firstName);
        mEditor.putString(KEY_MIDDLE_NAME, middleName);
        mEditor.putString(KEY_LAST_NAME, lastName);
        mEditor.putString(KEY_EMAIL, emailAddress);
        mEditor.putString(KEY_MOBILE_NUMBER, mobileNumber);
        mEditor.putString(KEY_PHONE_NUMBER, phoneNumber);
        mEditor.putString(KEY_STATUS_USER, status);
        mEditor.putInt(KEY_IS_VERIFIED, isVerified);
        mEditor.putString(KEY_PROF_PIC, profilePicture);
        mEditor.putString(KEY_USER_TYPE_ID, userTypeID);
        mEditor.putString(KEY_GENDER, gender);
        Date date = new Date();

        //Set user session for next 7 days
        long millis = date.getTime() + (7 * 24 * 60 * 60 * 1000);
        mEditor.putLong(KEY_EXPIRES, millis);
        mEditor.commit();
    }

    public void loginOwner(int ownerID) {
        mEditor.putInt(KEY_OWNER_ID, ownerID);
        Date date = new Date();

        //Set user session for next 7 days
        long millis = date.getTime() + (7 * 24 * 60 * 60 * 1000);
        mEditor.putLong(KEY_EXPIRES, millis);
        mEditor.commit();

    }

    public MyFavorites getOwnerIDFavorites(){
        MyFavorites myFavorites = new MyFavorites();
        myFavorites.setOwnerID(mPreferences.getInt(KEY_OWNER_ID, 0));
        return myFavorites;
    }

    public void setOwnerIDFavorites(int ownerID){
        mEditor.putInt(KEY_OWNER_ID, ownerID);
        mEditor.commit();
    }

    public void setChatID(int chatID){
        mEditor.putInt(KEY_CHAT_ID, chatID);
        mEditor.commit();
    }

    public Messages getChatID(){
        Messages messages = new Messages();
        messages.setChatID(mPreferences.getInt(KEY_CHAT_ID, 0));

        return messages;
    }

    public CarRecord getOwnerID(){
        CarRecord carRecord = new CarRecord();
        carRecord.setOwnerID(mPreferences.getInt(KEY_OWNER_ID, 0));

        return carRecord;
    }

    public CarRecord getRecordID(){
        CarRecord carRecord = new CarRecord();
        carRecord.setRecordID(mPreferences.getInt(KEY_RECORD_ID, 0));
        carRecord.setStatus(mPreferences.getString(KEY_RECORD_STATUS, "N/A"));
        carRecord.setCarID(mPreferences.getInt(KEY_CAR_ID, 0));

        return carRecord;
    }

    public Booking getCarReview(){
        Booking booking = new Booking();
        booking.setBookingID(mPreferences.getInt(KEY_BOOKING_ID, 0));
        booking.setCarID(mPreferences.getInt(KEY_CAR_ID, 0));

        return booking;
    }

    public void setCarReview(int bookingID, int carID){
        mEditor.putInt(KEY_BOOKING_ID, bookingID);
        mEditor.putInt(KEY_CAR_ID, carID);
        mEditor.commit();
    }

    public void setBookingIDHistory(int bookingID, int carowner_userID){
        mEditor.putInt(KEY_BOOKING_ID, bookingID);
        mEditor.putInt(KEY_OWNER_USER_ID, carowner_userID);
        mEditor.commit();
    }

    public void setBookingIDHistoryOwner(int bookingID, int renter_userID){
        mEditor.putInt(KEY_BOOKING_ID, bookingID);
        mEditor.putInt(KEY_RENTER_USER_ID, renter_userID);
        mEditor.commit();
    }

    public Booking getBookingIDHistoryOwner() {
        Booking booking = new Booking();
        booking.setBookingID(mPreferences.getInt(KEY_BOOKING_ID, 0));
        booking.setRenter_userID(mPreferences.getInt(KEY_RENTER_USER_ID, 0));

        return booking;
    }


    public void setBookingHistory(int bookingID, int ownerID){
        mEditor.putInt(KEY_BOOKING_ID, bookingID);
        mEditor.putInt(KEY_OWNER_ID, ownerID);
        mEditor.commit();
    }

    public Booking getBookingHistory(){
        Booking booking = new Booking();
        booking.setBookingID(mPreferences.getInt(KEY_BOOKING_ID, 0));
        booking.setOwnerID(mPreferences.getInt(KEY_OWNER_ID, 0));

        return booking;
    }


    public Booking getBookingIDHistory(){
        Booking booking = new Booking();
        booking.setBookingID(mPreferences.getInt(KEY_BOOKING_ID, 0));
        booking.setCarowner_userID(mPreferences.getInt(KEY_OWNER_USER_ID, 0));

        return booking;
    }

    public CarRecord getSpinnerPos(){
        CarRecord carRecord = new CarRecord();
        carRecord.setBrandPos(mPreferences.getInt(KEY_BRAND_POS, 0));
        carRecord.setModelPos(mPreferences.getInt(KEY_MODEL_POS, 0));

        return carRecord;
    }


    public void setItBaby(int recordID, String status, int carID){
        mEditor.putInt(KEY_RECORD_ID, recordID);
        mEditor.putString(KEY_RECORD_STATUS, status);
        mEditor.putInt(KEY_CAR_ID, carID);

        mEditor.commit();
    }

    public BookingRequests getBookingID(){
        BookingRequests bookingRequests = new BookingRequests();
        bookingRequests.setBookingID(mPreferences.getInt(KEY_BOOKING_ID, 0));

        return bookingRequests;
    }

    public BookingRequests getOwnerUserID(){
        BookingRequests bookingRequests = new BookingRequests();
        bookingRequests.setOwner_userID(mPreferences.getInt(KEY_OWNER_USER_ID, 0));
        bookingRequests.setOwner_firstName(mPreferences.getString(KEY_OWNER_FIRST_NAME, ""));
        bookingRequests.setOwner_lastName(mPreferences.getString(KEY_OWNER_LAST_NAME, ""));
        bookingRequests.setOwner_profilePicture(mPreferences.getString(KEY_OWNER_PROF_PIC, ""));

        return bookingRequests;
    }

    public void setOwnerUserID(int owner_userID, String owner_firstName, String owner_lastName, String owner_profilePicture){
        mEditor.putInt(KEY_OWNER_USER_ID, owner_userID);
        mEditor.putString(KEY_OWNER_FIRST_NAME, owner_firstName);
        mEditor.putString(KEY_OWNER_LAST_NAME, owner_lastName);
        mEditor.putString(KEY_OWNER_PROF_PIC, owner_profilePicture);
        mEditor.commit();
    }

    public void setBookingID (int bookingID) {
        mEditor.putInt(KEY_BOOKING_ID, bookingID);
        mEditor.commit();
    }

    //Edit Profile Update User Info
    public void editUser(String emailAddress, String mobileNumber, String phoneNumber, String status, String firstName, String middleName, String lastName, String gender) {
        mEditor.putString(KEY_EMAIL, emailAddress);
        mEditor.putString(KEY_MOBILE_NUMBER, mobileNumber);
        mEditor.putString(KEY_PHONE_NUMBER, phoneNumber);
        mEditor.putString(KEY_STATUS_USER, status);
        mEditor.putString(KEY_FIRST_NAME, firstName);
        mEditor.putString(KEY_MIDDLE_NAME, middleName);
        mEditor.putString(KEY_LAST_NAME, lastName);
        mEditor.putString(KEY_GENDER, gender);
        mEditor.commit();
    }

    public void updateUserData(String emailAddress, String mobileNumber, String phoneNumber, String status, String profilePicture, int isVerified, String firstName, String middleName, String lastName, String gender) {
        mEditor.putString(KEY_EMAIL, emailAddress);
        mEditor.putString(KEY_MOBILE_NUMBER, mobileNumber);
        mEditor.putString(KEY_PHONE_NUMBER, phoneNumber);
        mEditor.putString(KEY_STATUS_USER, status);
        mEditor.putString(KEY_PROF_PIC, profilePicture);
        mEditor.putInt(KEY_IS_VERIFIED, isVerified);
        mEditor.putString(KEY_FIRST_NAME, firstName);
        mEditor.putString(KEY_MIDDLE_NAME, middleName);
        mEditor.putString(KEY_LAST_NAME, lastName);
        mEditor.putString(KEY_GENDER, gender);
        mEditor.commit();
    }

    public void setBookingDetails(String brandName, String modelName, String address){
        mEditor.putString(KEY_BRAND_NAME, brandName);
        mEditor.putString(KEY_MODEL_NAME, modelName);
        mEditor.putString(KEY_ADDRESS, address);
        mEditor.commit();
    }

    /**
     * Checks whether user is logged in
     *
     * @return
     */
    public boolean isLoggedIn() {
        Date currentDate = new Date();

        long millis = mPreferences.getLong(KEY_EXPIRES, 0);

        /* If shared preferences does not have a value
         then user is not logged in
         */
        if (millis == 0) {
            return false;
        }
        Date expiryDate = new Date(millis);

        /* Check if session is expired by comparing
        current date and Session expiry date
        */
        return currentDate.before(expiryDate);
    }

    /**
     * Fetches and returns user details
     *
     * @return user details
     */
    public User getUserDetails() {
        //Check if user is logged in first
        if (!isLoggedIn()) {
            return null;
        }
        User user = new User();
        user.setUsername(mPreferences.getString(KEY_USERNAME, KEY_EMPTY));
        user.setFirstName(mPreferences.getString(KEY_FIRST_NAME, KEY_EMPTY));
        user.setLastName(mPreferences.getString(KEY_LAST_NAME, KEY_EMPTY));
        user.setMiddleName(mPreferences.getString(KEY_MIDDLE_NAME, KEY_EMPTY));
        user.setEmailAddress(mPreferences.getString(KEY_EMAIL, KEY_EMPTY));
        user.setMobileNumber(mPreferences.getString(KEY_MOBILE_NUMBER, KEY_EMPTY));
        user.setPhoneNumber(mPreferences.getString(KEY_PHONE_NUMBER, "N/A"));
        user.setStatus(mPreferences.getString(KEY_STATUS_USER, KEY_EMPTY));
        user.setProfilePicture(mPreferences.getString(KEY_PROF_PIC, KEY_EMPTY));
        user.setIsVerified(mPreferences.getInt(KEY_IS_VERIFIED, 0));
        user.setUserID(mPreferences.getInt(KEY_USER_ID, 0));
        user.setUserTypeID(mPreferences.getString(KEY_USER_TYPE_ID, "N/A"));
        user.setSessionExpiryDate(new Date(mPreferences.getLong(KEY_EXPIRES, 0)));
        user.setUserTypeID(mPreferences.getString(KEY_USER_TYPE_ID, "N/A"));
        user.setGender(mPreferences.getString(KEY_GENDER, "N/A"));

        return user;
    }

    public CarBooking getBookingDetails(){

        CarBooking carBooking = new CarBooking();
        carBooking.setBrandName(mPreferences.getString(KEY_BRAND_NAME, KEY_EMPTY));
        carBooking.setModelName(mPreferences.getString(KEY_MODEL_NAME, KEY_EMPTY));
        carBooking.setAddress(mPreferences.getString(KEY_ADDRESS, KEY_EMPTY));
        return carBooking;
    }

    /**
     * Car Pictures Add Car
     * @return
     */

    public CarPictures getCarPic1(){
        CarPictures carPictures = new CarPictures();
        carPictures.setCarPic1(mPreferences.getString(KEY_CAR_PIC_1, KEY_EMPTY));
        //bookingRequests.setOwner_userID(mPreferences.getInt(KEY_OWNER_USER_ID, 0));
        return carPictures;
    }

    public void setCarPic1(String carPic1){
        mEditor.putString(KEY_CAR_PIC_1, carPic1);
        mEditor.commit();
    }

    public CarPictures getCarPic2(){
        CarPictures carPictures = new CarPictures();
        carPictures.setCarPic2(mPreferences.getString(KEY_CAR_PIC_2, KEY_EMPTY));
        //bookingRequests.setOwner_userID(mPreferences.getInt(KEY_OWNER_USER_ID, 0));
        return carPictures;
    }

    public void setCarPic2(String carPic2){
        mEditor.putString(KEY_CAR_PIC_2, carPic2);
        mEditor.commit();
    }

    public CarPictures getCarPic3(){
        CarPictures carPictures = new CarPictures();
        carPictures.setCarPic3(mPreferences.getString(KEY_CAR_PIC_3, KEY_EMPTY));
        //bookingRequests.setOwner_userID(mPreferences.getInt(KEY_OWNER_USER_ID, 0));
        return carPictures;
    }

    public void setCarPic3(String carPic3){
        mEditor.putString(KEY_CAR_PIC_3, carPic3);
        mEditor.commit();
    }

    public CarPictures getCarPic4(){
        CarPictures carPictures = new CarPictures();
        carPictures.setCarPic4(mPreferences.getString(KEY_CAR_PIC_4, KEY_EMPTY));
        //bookingRequests.setOwner_userID(mPreferences.getInt(KEY_OWNER_USER_ID, 0));
        return carPictures;
    }

    public void setCarPic4(String carPic4){
        mEditor.putString(KEY_CAR_PIC_4, carPic4);
        mEditor.commit();
    }

    public CarPictures getCarPic5(){
        CarPictures carPictures = new CarPictures();
        carPictures.setCarPic5(mPreferences.getString(KEY_CAR_PIC_5, KEY_EMPTY));
        //bookingRequests.setOwner_userID(mPreferences.getInt(KEY_OWNER_USER_ID, 0));
        return carPictures;
    }

    public void setCarPic5(String carPic5){
        mEditor.putString(KEY_CAR_PIC_2, carPic5);
        mEditor.commit();
    }

    public CarPictures getCarPic6(){
        CarPictures carPictures = new CarPictures();
        carPictures.setCarPic5(mPreferences.getString(KEY_CAR_PIC_5, KEY_EMPTY));
        //bookingRequests.setOwner_userID(mPreferences.getInt(KEY_OWNER_USER_ID, 0));
        return carPictures;
    }

    public void setCarPic6(String carPic6){
        mEditor.putString(KEY_CAR_PIC_6, carPic6);
        mEditor.commit();
    }

    public CarPictures getCarOR(){
        CarPictures carPictures = new CarPictures();
        carPictures.setCarOR(mPreferences.getString(KEY_CAR_OR, KEY_EMPTY));
        //bookingRequests.setOwner_userID(mPreferences.getInt(KEY_OWNER_USER_ID, 0));
        return carPictures;
    }

    public void setCarOR(String carOR){
        mEditor.putString(KEY_CAR_OR, carOR);
        mEditor.commit();
    }

    public CarPictures getCarCR(){
        CarPictures carPictures = new CarPictures();
        carPictures.setCarCR(mPreferences.getString(KEY_CAR_CR, KEY_EMPTY));
        //bookingRequests.setOwner_userID(mPreferences.getInt(KEY_OWNER_USER_ID, 0));
        return carPictures;
    }

    public void setCarCR(String carCR){
        mEditor.putString(KEY_CAR_CR, carCR);
        mEditor.commit();
    }

    public CarPictures getCarSIR(){
        CarPictures carPictures = new CarPictures();
        carPictures.setCarSIR(mPreferences.getString(KEY_CAR_SIR, KEY_EMPTY));
        //bookingRequests.setOwner_userID(mPreferences.getInt(KEY_OWNER_USER_ID, 0));
        return carPictures;
    }

    public void setCarSIR(String carSIR){
        mEditor.putString(KEY_CAR_SIR, carSIR);
        mEditor.commit();
    }



    /**
     *
     * public void setItBaby(int recordID, String status, int carID){
     *         mEditor.putInt(KEY_RECORD_ID, recordID);
     *         mEditor.putString(KEY_RECORD_STATUS, status);
     *         mEditor.putInt(KEY_CAR_ID, carID);
     *
     *         mEditor.commit();
     *     }
     *
     * Logs out user by clearing the session
     */
    public void logoutUser(){
        mEditor.clear();
        mEditor.commit();
    }

}
