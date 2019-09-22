package ph.roadtrip.roadtrip.classes;

public class UrlBean {
    private static final String ROOT_IP = "192.168.100.8";

    //Check Email
    private String forgetPasswordLink = "http://" + ROOT_IP +"/api/roadtrip/checkEmail.php";

    //ForgetPassword
    private String sendForgetEmailLink = "http://" + ROOT_IP +"/api/roadtrip/forgetpassword.php?emailAddress=";

    //Edit Profile
    private String editProfileLink = "http://" + ROOT_IP +"/api/roadtrip/editprofile.php";

    //Change Password
    private String changePasswordLink = "http://" + ROOT_IP +"/api/roadtrip/changepassword.php";

    //Recaptcha Verify
    private static final String recaptchaVerifyLink = "http://" + ROOT_IP +"/api/roadtrip/validate-captcha.php";

    //Login
    private String loginLink = "http://" + ROOT_IP +"/api/roadtrip/login.php";

    //Register
    private String registerLink = "http://" + ROOT_IP +"/api/roadtrip/register.php";

    //Mailer Link Register
    private String mailerLinkRegister = "http://" + ROOT_IP +"/api/roadtrip/mailer.php?emailAddress=";

    //Validate Captcha
    private String verifyCaptcha = "http://" + ROOT_IP +"/api/roadtrip/validate-captcha.php";

    //Get Profile Picture
    private String profilePicUrl = "http://" + ROOT_IP +"/api/roadtrip/uploads/";

    //get image list view car man
    private String getPicUrl = "http://" + ROOT_IP +"/api/roadtrip/carpics/";

    //Get User Data (ON BACKGROUND)
    private String getUserDataUrl = "http://" + ROOT_IP +"/api/roadtrip/getuserdata.php";

    //Add Car
    private String addCarLink = "http://" + ROOT_IP +"/api/roadtrip/addcar.php";

    //API Site key for captcha
    private String SAFETY_NET_API_SITE_KEY = "6Ldmi6MUAAAAAONCiiPYORudptwVQMWrOrid26Xe";

    //List Car Maps
    private String showCarsMaps = "http://" + ROOT_IP +"/api/roadtrip/listavailablecars.php";

    //Fetch booking data (View Booking offer)
    private String fetch_booking_data = "http://" + ROOT_IP +"/api/roadtrip/getrecorddata.php";

    //List cars maps activity
    private String listCarsMaps = "http://" + ROOT_IP +"/api/roadtrip/listavailablecars.php?startDate=";

    //List cars maps activity
    private String listCarsMapsFavorites = "http://" + ROOT_IP +"/api/roadtrip/listavailablecarsfavorites.php?startDate=";

    //Request Booking
    private String request_booking = "http://" + ROOT_IP +"/api/roadtrip/requestbooking.php";

    //List Cars (My Cars) CAR OWNER
    private String listCarsMyCars = "http://" + ROOT_IP +"/api/roadtrip/listcar.php?ownerID=";

    //List pending request CAR OWNER
    private String pendingRequests = "http://" + ROOT_IP +"/api/roadtrip/pendingrequests.php?ownerID=";

    //List pending request CAR OWNER
    private String pendingRequestsRenter = "http://" + ROOT_IP +"/api/roadtrip/pendingrequestsrenter.php?userID=";

    //Accept Booking Car Owner
    private String accept_booking = "http://" + ROOT_IP +"/api/roadtrip/acceptbooking.php";

    //Decline Booking Car Owner
    private String decline_booking = "http://" + ROOT_IP +"/api/roadtrip/declinebooking.php";

    //View booking request Car Owner
    private String view_booking_request = "http://" + ROOT_IP +"/api/roadtrip/viewbooking.php";

    //Accepted booking request display in listview
    private String acceptedBookings = "http://" + ROOT_IP +"/api/roadtrip/acceptedbookings.php?ownerID=";

    //Get current booking Car renter
    private String getCurrentBooking = "http://" + ROOT_IP +"/api/roadtrip/getcurrentbooking.php";

    //Scan QR car renter
    private String scan_qr = "http://" + ROOT_IP +"/api/roadtrip/scanqr.php?userID=";

    //Scan QR return renter
    private String scan_return_qr = "http://" + ROOT_IP +"/api/roadtrip/scanqrreturn.php?userID=";

    //Get History Car Renter
    private String get_history = "http://" + ROOT_IP +"/api/roadtrip/gethistory.php?userID=";

    //Get History Car Renter
    private String toReviewList = "http://" + ROOT_IP +"/api/roadtrip/toreview.php?userID=";

    //Get History Car Renter
    private String CarToReviewList = "http://" + ROOT_IP +"/api/roadtrip/cartoreview.php?userID=";

    //Add Review
    private String addReview = "http://" + ROOT_IP +"/api/roadtrip/addreview.php";

    //Add Review
    private String addReviewOwner = "http://" + ROOT_IP +"/api/roadtrip/addreviewowner.php";

    //Add Review
    private String carAddReview = "http://" + ROOT_IP +"/api/roadtrip/addcarreview.php";

    //Insert Paypal
    private String insertPaypal = "http://" + ROOT_IP +"/api/roadtrip/insertpaypal.php";

    //Get User Reviews to display in profile
    private String getFeedbackProfile = "http://" + ROOT_IP +"/api/roadtrip/getreviewsprofile.php";

    //Get User Reviews to display in profile
    private String getAverageRatingProfile = "http://" + ROOT_IP +"/api/roadtrip/getaveragerating.php";

    //Get User Reviews to display in profile
    private String getTotalTrips = "http://" + ROOT_IP +"/api/roadtrip/gettotaltrips.php";

    //Get User Reviews to display in profile
    private String getTotalTripsOwner = "http://" + ROOT_IP +"/api/roadtrip/gettotaltripsowner.php";

    //Get History Car Owner
    private String get_history_owner = "http://" + ROOT_IP +"/api/roadtrip/gethistoryowner.php?ownerID=";

    //View booking request Car renter display name
    private String view_booking_request_owner = "http://" + ROOT_IP +"/api/roadtrip/viewbookingowner.php";

    //Get History Car Renter
    private String toReviewListOwner = "http://" + ROOT_IP +"/api/roadtrip/toreviewowner.php?ownerID=";

    //List Review Profile
    private String listReviewProfile = "http://" + ROOT_IP +"/api/roadtrip/listreviewprofile.php?userID=";

    //Add Favorites
    private String addFavorites = "http://" + ROOT_IP +"/api/roadtrip/addfavorites.php";

    //Add Favorites
    private String removeFavorites = "http://" + ROOT_IP +"/api/roadtrip/removefavorite.php";

    //List Favorites
    private String listFavorites = "http://" + ROOT_IP +"/api/roadtrip/listfavorites.php?userID=";

    //Total Earnings My Earnings
    private String totalEarnings = "http://" + ROOT_IP +"/api/roadtrip/totalearnings.php";

    //Total Cancel My earnings
    private String totalCancel = "http://" + ROOT_IP +"/api/roadtrip/totalcancel.php";

    //Total Average My Earnings
    private String totalAverage = "http://" + ROOT_IP +"/api/roadtrip/totalaverage.php";

    //Total Trips My Earnings
    private String totalTrips = "http://" + ROOT_IP +"/api/roadtrip/totaltripsmyearnings.php";

    //Total Bookings My Earnings
    private String totalBookings = "http://" + ROOT_IP +"/api/roadtrip/totalbookingsmyearnings.php";

    //Total Bookings My Earnings
    private String chatlist = "http://" + ROOT_IP +"/api/roadtrip/listchats.php?userID=";

    //Total Bookings My Earnings
    private String chatlistowner = "http://" + ROOT_IP +"/api/roadtrip/listchatsowner.php?userID=";

    //Total Bookings My Earnings
    private String chatMessages = "http://" + ROOT_IP +"/api/roadtrip/chatcontent.php?chatID=";

    //Total Bookings My Earnings
    private String chatMessagesOwner = "http://" + ROOT_IP +"/api/roadtrip/chatcontentowner.php?chatID=";

    //Total Bookings My Earnings
    private String send_message_url = "http://" + ROOT_IP +"/api/roadtrip/sendmessage.php";

    //Get Faqs
    private String faqs_list = "http://" + ROOT_IP +"/api/roadtrip/listfaqs.php";

    //Get Faqs
    private String archive_record = "http://" + ROOT_IP +"/api/roadtrip/deleterecord.php";

    //Get Faqs
    private String cancelBooking = "http://" + ROOT_IP +"/api/roadtrip/cancelbooking.php";

    //Logout
    private String logout = "http://" + ROOT_IP +"/api/roadtrip/logout.php";

    //Deactivate account
    private String deactivate_account = "http://" + ROOT_IP +"/api/roadtrip/deactivateaccount.php";

    //Deactivate account owner
    private String deactivate_account_owner = "http://" + ROOT_IP +"/api/roadtrip/deactivateaccountowner.php";

    //Activate account
    private String activate_account = "http://" + ROOT_IP +"/api/roadtrip/activateaccount.php";

    //Archive account
    private String archive_account = "http://" + ROOT_IP +"/api/roadtrip/archiveaccount.php";

    //Mail Receipt
    private String mail_receipt = "http://" + ROOT_IP +"/api/roadtrip/mailreceipt.php?bookingID=";

    //Mail Receipt
    private String mail_receipt_owner = "http://" + ROOT_IP +"/api/roadtrip/mailreceiptowner.php?bookingID=";

    //Edit Car Record
    private String edit_car = "http://" + ROOT_IP +"/api/roadtrip/editcar.php";

    //Edit Car Record
    private String car_review = "http://" + ROOT_IP +"/api/roadtrip/listreviewcar.php?carID=";

    //Get total average car
    private String car_ave_review = "http://"+ ROOT_IP +"/api/roadtrip/totalaveragecar.php";





    /**
     *
     * GETTERS AND SETTERS
     */

    public String getCar_ave_review() {
        return car_ave_review;
    }

    public void setCar_ave_review(String car_ave_review) {
        this.car_ave_review = car_ave_review;
    }

    public String getCar_review() {
        return car_review;
    }

    public void setCar_review(String car_review) {
        this.car_review = car_review;
    }

    public String getEdit_car() {
        return edit_car;
    }

    public void setEdit_car(String edit_car) {
        this.edit_car = edit_car;
    }


    public String getTotalCancel() {
        return totalCancel;
    }

    public void setTotalCancel(String totalCancel) {
        this.totalCancel = totalCancel;
    }

    public String getDeactivate_account_owner() {
        return deactivate_account_owner;
    }

    public void setDeactivate_account_owner(String deactivate_account_owner) {
        this.deactivate_account_owner = deactivate_account_owner;
    }

    public String getArchive_account() {
        return archive_account;
    }

    public void setArchive_account(String archive_account) {
        this.archive_account = archive_account;
    }

    public String getMail_receipt_owner() {
        return mail_receipt_owner;
    }

    public void setMail_receipt_owner(String mail_receipt_owner) {
        this.mail_receipt_owner = mail_receipt_owner;
    }

    public String getMail_receipt() {
        return mail_receipt;
    }

    public void setMail_receipt(String mail_receipt) {
        this.mail_receipt = mail_receipt;
    }

    public String getActivate_account() {
        return activate_account;
    }

    public void setActivate_account(String activate_account) {
        this.activate_account = activate_account;
    }

    public String getDeactivate_account() {
        return deactivate_account;
    }

    public void setDeactivate_account(String deactivate_account) {
        this.deactivate_account = deactivate_account;
    }

    public String getLogout() {
        return logout;
    }

    public void setLogout(String logout) {
        this.logout = logout;
    }

    public String getCancelBooking() {
        return cancelBooking;
    }

    public void setCancelBooking(String cancelBooking) {
        this.cancelBooking = cancelBooking;
    }

    public String getArchive_record() {
        return archive_record;
    }

    public void setArchive_record(String archive_record) {
        this.archive_record = archive_record;
    }


    public String getPendingRequestsRenter() {
        return pendingRequestsRenter;
    }

    public void setPendingRequestsRenter(String pendingRequestsRenter) {
        this.pendingRequestsRenter = pendingRequestsRenter;
    }

    public String getFaqs_list() {
        return faqs_list;
    }

    public void setFaqs_list(String faqs_list) {
        this.faqs_list = faqs_list;
    }

    public String getChatMessagesOwner() {
        return chatMessagesOwner;
    }

    public void setChatMessagesOwner(String chatMessagesOwner) {
        this.chatMessagesOwner = chatMessagesOwner;
    }

    public String getChatlistowner() {
        return chatlistowner;
    }

    public void setChatlistowner(String chatlistowner) {
        this.chatlistowner = chatlistowner;
    }


    public String getSend_message_url() {
        return send_message_url;
    }

    public void setSend_message_url(String send_message_url) {
        this.send_message_url = send_message_url;
    }

    public String getChatMessages() {
        return chatMessages;
    }

    public void setChatMessages(String chatMessages) {
        this.chatMessages = chatMessages;
    }

    public String getChatlist() {
        return chatlist;
    }

    public void setChatlist(String chatlist) {
        this.chatlist = chatlist;
    }

    public String getTotalBookings() {
        return totalBookings;
    }

    public void setTotalBookings(String totalBookings) {
        this.totalBookings = totalBookings;
    }

    public String getTotalTrips() {
        return totalTrips;
    }

    public void setTotalTrips(String totalTrips) {
        this.totalTrips = totalTrips;
    }

    public String getTotalAverage() {
        return totalAverage;
    }

    public void setTotalAverage(String totalAverage) {
        this.totalAverage = totalAverage;
    }

    public String getTotalEarnings() {
        return totalEarnings;
    }

    public void setTotalEarnings(String totalEarnings) {
        this.totalEarnings = totalEarnings;
    }

    public String getListCarsMapsFavorites() {
        return listCarsMapsFavorites;
    }

    public void setListCarsMapsFavorites(String listCarsMapsFavorites) {
        this.listCarsMapsFavorites = listCarsMapsFavorites;
    }

    public String getListFavorites() {
        return listFavorites;
    }

    public void setListFavorites(String listFavorites) {
        this.listFavorites = listFavorites;
    }

    public String getRemoveFavorites() {
        return removeFavorites;
    }

    public void setRemoveFavorites(String removeFavorites) {
        this.removeFavorites = removeFavorites;
    }

    public String getAddFavorites() {
        return addFavorites;
    }

    public void setAddFavorites(String addFavorites) {
        this.addFavorites = addFavorites;
    }

    public String getListReviewProfile() {
        return listReviewProfile;
    }

    public void setListReviewProfile(String listReviewProfile) {
        this.listReviewProfile = listReviewProfile;
    }

    public String getAddReviewOwner() {
        return addReviewOwner;
    }

    public void setAddReviewOwner(String addReviewOwner) {
        this.addReviewOwner = addReviewOwner;
    }

    public String getToReviewListOwner() {
        return toReviewListOwner;
    }

    public void setToReviewListOwner(String toReviewListOwner) {
        this.toReviewListOwner = toReviewListOwner;
    }


    public String getView_booking_request_owner() {
        return view_booking_request_owner;
    }

    public void setView_booking_request_owner(String view_booking_request_owner) {
        this.view_booking_request_owner = view_booking_request_owner;
    }


    public String getGet_history_owner() {
        return get_history_owner;
    }

    public void setGet_history_owner(String get_history_owner) {
        this.get_history_owner = get_history_owner;
    }


    public String getGetTotalTripsOwner() {
        return getTotalTripsOwner;
    }

    public void setGetTotalTripsOwner(String getTotalTripsOwner) {
        this.getTotalTripsOwner = getTotalTripsOwner;
    }

    public String getGetTotalTrips() {
        return getTotalTrips;
    }

    public void setGetTotalTrips(String getTotalTrips) {
        this.getTotalTrips = getTotalTrips;
    }


    public String getGetAverageRatingProfile() {
        return getAverageRatingProfile;
    }

    public void setGetAverageRatingProfile(String getAverageRatingProfile) {
        this.getAverageRatingProfile = getAverageRatingProfile;
    }

    public String getGetFeedbackProfile() {
        return getFeedbackProfile;
    }

    public void setGetFeedbackProfile(String getFeedbackProfile) {
        this.getFeedbackProfile = getFeedbackProfile;
    }
    public String getCarAddReview() {
        return carAddReview;
    }

    public void setCarAddReview(String carAddReview) {
        this.carAddReview = carAddReview;
    }

    public String getCarToReviewList() {
        return CarToReviewList;
    }

    public void setCarToReviewList(String carToReviewList) {
        CarToReviewList = carToReviewList;
    }

    public String getInsertPaypal() {
        return insertPaypal;
    }

    public void setInsertPaypal(String insertPaypal) {
        this.insertPaypal = insertPaypal;
    }

    public String getToReviewList() {
        return toReviewList;
    }

    public void setToReviewList(String toReviewList) {
        this.toReviewList = toReviewList;
    }

    public String getAddReview() {
        return addReview;
    }

    public void setAddReview(String addReview) {
        this.addReview = addReview;
    }
    public String getGet_history() {
        return get_history;
    }

    public void setGet_history(String get_history) {
        this.get_history = get_history;
    }

    public String getScan_return_qr() {
        return scan_return_qr;
    }

    public void setScan_return_qr(String scan_return_qr) {
        this.scan_return_qr = scan_return_qr;
    }


    public String getScan_qr() {
        return scan_qr;
    }

    public void setScan_qr(String scan_qr) {
        this.scan_qr = scan_qr;
    }


    public String getGetCurrentBooking() {
        return getCurrentBooking;
    }

    public void setGetCurrentBooking(String getCurrentBooking) {
        this.getCurrentBooking = getCurrentBooking;
    }

    public String getAcceptedBookings() {
        return acceptedBookings;
    }

    public void setAcceptedBookings(String acceptedBookings) {
        this.acceptedBookings = acceptedBookings;
    }

    public String getView_booking_request() {
        return view_booking_request;
    }

    public void setView_booking_request(String view_booking_request) {
        this.view_booking_request = view_booking_request;
    }

    public String getDecline_booking() {
        return decline_booking;
    }

    public void setDecline_booking(String decline_booking) {
        this.decline_booking = decline_booking;
    }

    public String getAccept_booking() {
        return accept_booking;
    }

    public void setAccept_booking(String accept_booking) {
        this.accept_booking = accept_booking;
    }
    public String getPendingRequests() {
        return pendingRequests;
    }

    public void setPendingRequests(String pendingRequests) {
        this.pendingRequests = pendingRequests;
    }

    public String getListCarsMyCars() {
        return listCarsMyCars;
    }

    public void setListCarsMyCars(String listCarsMyCars) {
        this.listCarsMyCars = listCarsMyCars;
    }

    public String getRequest_booking() {
        return request_booking;
    }

    public void setRequest_booking(String request_booking) {
        this.request_booking = request_booking;
    }

    public String getListCarsMaps() {
        return listCarsMaps;
    }

    public void setListCarsMaps(String listCarsMaps) {
        this.listCarsMaps = listCarsMaps;
    }

    public String getFetch_booking_data() {
        return fetch_booking_data;
    }

    public void setFetch_booking_data(String fetch_booking_data) {
        this.fetch_booking_data = fetch_booking_data;
    }

    public String getShowCarsMaps() {
        return showCarsMaps;
    }

    public void setShowCarsMaps(String showCarsMaps) {
        this.showCarsMaps = showCarsMaps;
    }

    public String getGetPicUrl() {
        return getPicUrl;
    }

    public void setGetPicUrl(String getPicUrl) {
        this.getPicUrl = getPicUrl;
    }


    public String getAddCarLink() {
        return addCarLink;
    }

    public void setAddCarLink(String addCarLink) {
        this.addCarLink = addCarLink;
    }

    public String getForgetPasswordLink() {
        return forgetPasswordLink;
    }

    public void setForgetPasswordLink(String forgetPasswordLink) {
        this.forgetPasswordLink = forgetPasswordLink;
    }

    public String getEditProfileLink() {
        return editProfileLink;
    }

    public void setEditProfileLink(String editProfileLink) {
        this.editProfileLink = editProfileLink;
    }

    public String getChangePasswordLink() {
        return changePasswordLink;
    }

    public void setChangePasswordLink(String changePasswordLink) {
        this.changePasswordLink = changePasswordLink;
    }

    public static String getRecaptchaVerifyLink() {
        return recaptchaVerifyLink;
    }

    public String getLoginLink() {
        return loginLink;
    }

    public void setLoginLink(String loginLink) {
        this.loginLink = loginLink;
    }

    public String getRegisterLink() {
        return registerLink;
    }

    public void setRegisterLink(String registerLink) {
        this.registerLink = registerLink;
    }

    public String getMailerLinkRegister() {
        return mailerLinkRegister;
    }

    public void setMailerLinkRegister(String mailerLinkRegister) {
        this.mailerLinkRegister = mailerLinkRegister;
    }

    public String getVerifyCaptcha() {
        return verifyCaptcha;
    }

    public void setVerifyCaptcha(String verifyCaptcha) {
        this.verifyCaptcha = verifyCaptcha;
    }

    public String getSAFETY_NET_API_SITE_KEY() {
        return SAFETY_NET_API_SITE_KEY;
    }

    public void setSAFETY_NET_API_SITE_KEY(String SAFETY_NET_API_SITE_KEY) {
        this.SAFETY_NET_API_SITE_KEY = SAFETY_NET_API_SITE_KEY;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public String getGetUserDataUrl() {
        return getUserDataUrl;
    }

    public void setGetUserDataUrl(String getUserDataUrl) {
        this.getUserDataUrl = getUserDataUrl;
    }

    public String getSendForgetEmailLink() {
        return sendForgetEmailLink;
    }

    public void setSendForgetEmailLink(String sendForgetEmailLink) {
        this.sendForgetEmailLink = sendForgetEmailLink;
    }


}
