package ph.roadtrip.roadtrip.classes;

public class UrlBean {
    private static final String ROOT_IP = "https://isproj2b.benilde.edu.ph/RoadTrip_Mobile/roadtrip/";
    //private static final String ROOT_IP = "http://192.168.100.5/api/roadtrip/";
    //Total Expenses
    private String totalExpenses = ROOT_IP + "totalexpenses.php";

    //Filter expenses
    private String filterExpenses = ROOT_IP + "filterexpenses.php";

    //Get brand name
    private String getBrandName = ROOT_IP + "getbrandname.php";

    //filter Earnings
    private String filterEarnings = ROOT_IP + "filterearnings.php";

    //Check Email
    private String forgetPasswordLink = ROOT_IP + "checkEmail.php";

    //ForgetPassword
    private String sendForgetEmailLink = ROOT_IP +"forgetpassword.php?emailAddress=";

    //Edit Profile
    private String editProfileLink = ROOT_IP +"editprofile.php";

    //Change Password
    private String changePasswordLink = ROOT_IP +"changepassword.php";

    //Recaptcha Verify
    private static final String recaptchaVerifyLink = ROOT_IP +"validate-captcha.php";

    //Login
    private String loginLink = ROOT_IP +"login.php";

    //Register
    private String registerLink = ROOT_IP +"register.php";

    //Mailer Link Register
    private String mailerLinkRegister = ROOT_IP +"mailer.php?emailAddress=";

    //Validate Captcha
    private String verifyCaptcha = ROOT_IP +"validate-captcha.php";

    //Get Profile Picture
    private String profilePicUrl = ROOT_IP +"uploads/";

    //Get Profile Picture
    private String userAttachmentPicUrl = ROOT_IP +"attachments/";

    //Get Profile Picture
    private String carAttachmentPicUrl = ROOT_IP +"carattachments/";

    //get image list view car man
    private String getPicUrl = ROOT_IP +"carpics/";

    //Get User Data (ON BACKGROUND)
    private String getUserDataUrl = ROOT_IP +"getuserdata.php";

    //Add Car
    private String addCarLink = ROOT_IP +"addcar.php";

    //API Site key for captcha
    private String SAFETY_NET_API_SITE_KEY = "6Ldmi6MUAAAAAONCiiPYORudptwVQMWrOrid26Xe";

    //List Car Maps
    private String showCarsMaps = ROOT_IP +"listavailablecars.php";

    //Fetch booking data (View Booking offer)
    private String fetch_booking_data = ROOT_IP +"getrecorddata.php";



    //Get Booking data (view booking offer owner)
    private String fetch_bdata = ROOT_IP + "getbookingdata.php";

    //List cars maps activity
    private String listCarsMaps =ROOT_IP +"listavailablecars.php?startDate=";

    //List cars maps activity
    private String listCarsMapsFavorites = ROOT_IP +"listavailablecarsfavorites.php?startDate=";

    //Request Booking
    private String request_booking = ROOT_IP +"requestbooking.php";

    //List Cars (My Cars) CAR OWNER
    private String listCarsMyCars = ROOT_IP +"listcar.php?ownerID=";

    //List pending request CAR OWNER
    private String pendingRequests = ROOT_IP +"pendingrequests.php?ownerID=";

    //List pending request CAR OWNER
    private String pendingRequestsRenter = ROOT_IP +"pendingrequestsrenter.php?userID=";

    //Accept Booking Car Owner
    private String accept_booking = ROOT_IP +"acceptbooking.php";

    //Decline Booking Car Owner
    private String decline_booking = ROOT_IP +"declinebooking.php";

    //View booking request Car Owner
    private String view_booking_request = ROOT_IP +"viewbooking.php";

    private String view_booking_request_history = ROOT_IP +"viewbookinghistory.php";

    private String view_booking_request_history_owner = ROOT_IP + "viewbookinghistoryowner.php";

    //Accepted booking request display in listview
    private String acceptedBookings = ROOT_IP +"acceptedbookings.php?ownerID=";

    //Get current booking Car renter
    private String getCurrentBooking = ROOT_IP +"getcurrentbooking.php";

    //Scan QR car renter
    private String scan_qr = ROOT_IP +"scanqr.php?userID=";

    //Scan QR return renter
    private String scan_return_qr = ROOT_IP +"scanqrreturn.php?userID=";

    //Get History Car Renter
    private String get_history = ROOT_IP +"gethistory.php?userID=";

    //Get History Car Renter
    private String toReviewList = ROOT_IP +"toreview.php?userID=";

    //Get History Car Renter
    private String CarToReviewList = ROOT_IP +"cartoreview.php?userID=";

    //Add Review
    private String addReview = ROOT_IP +"addreview.php";

    //Add Review
    private String addReviewOwner = ROOT_IP +"addreviewowner.php";

    //Add Review
    private String carAddReview = ROOT_IP +"addcarreview.php";

    //Insert Paypal
    private String insertPaypal = ROOT_IP +"insertpaypal.php";

    //Get User Reviews to display in profile
    private String getFeedbackProfile = ROOT_IP +"getreviewsprofile.php";

    //Get User Reviews to display in profile
    private String getAverageRatingProfile = ROOT_IP +"getaveragerating.php";

    //Get User Reviews to display in profile
    private String getTotalTrips = ROOT_IP +"gettotaltrips.php";

    //Get User Reviews to display in profile
    private String getTotalTripsOwner = ROOT_IP +"gettotaltripsowner.php";

    //Get History Car Owner
    private String get_history_owner = ROOT_IP +"gethistoryowner.php?ownerID=";

    //View booking request Car renter display name
    private String view_booking_request_owner = ROOT_IP +"viewbookingowner.php";

    //Get History Car Renter
    private String toReviewListOwner = ROOT_IP +"toreviewowner.php?ownerID=";

    //List Review Profile
    private String listReviewProfile = ROOT_IP +"listreviewprofile.php?userID=";

    //Add Favorites
    private String addFavorites = ROOT_IP +"addfavorites.php";

    //Add Favorites
    private String removeFavorites = ROOT_IP +"removefavorite.php";

    //List Favorites
    private String listFavorites = ROOT_IP +"listfavorites.php?userID=";

    //Total Earnings My Earnings
    private String totalEarnings = ROOT_IP +"totalearnings.php";

    //Total Cancel My earnings
    private String totalCancel = ROOT_IP +"totalcancel.php";

    //Total Average My Earnings
    private String totalAverage = ROOT_IP +"totalaverage.php";

    //Total Trips My Earnings
    private String totalTrips = ROOT_IP +"totaltripsmyearnings.php";

    //Total Bookings My Earnings
    private String totalBookings = ROOT_IP +"totalbookingsmyearnings.php";

    //Total Bookings My Earnings
    private String chatlist = ROOT_IP +"listchats.php?userID=";

    //Total Bookings My Earnings
    private String chatlistowner = ROOT_IP +"listchatsowner.php?userID=";

    //Total Bookings My Earnings
    private String chatMessages = ROOT_IP +"chatcontent.php?chatID=";

    //Total Bookings My Earnings
    private String chatMessagesOwner = ROOT_IP +"chatcontentowner.php?chatID=";

    //Total Bookings My Earnings
    private String send_message_url = ROOT_IP +"sendmessage.php";

    //Get Faqs
    private String faqs_list = ROOT_IP +"listfaqs.php";

    //Get Faqs
    private String archive_record = ROOT_IP +"deleterecord.php";

    //Get Faqs
    private String cancelBooking = ROOT_IP +"cancelbooking.php";

    //Logout
    private String logout = ROOT_IP +"logout.php";

    //Deactivate account
    private String deactivate_account = ROOT_IP +"deactivateaccount.php";

    //Deactivate account owner
    private String deactivate_account_owner = ROOT_IP +"deactivateaccountowner.php";

    //Activate account
    private String activate_account = ROOT_IP +"activateaccount.php";

    //Archive account
    private String archive_account = ROOT_IP +"archiveaccount.php";

    //Mail Receipt
    private String mail_receipt = ROOT_IP +"mailreceipt.php?bookingID=";

    //Mail Receipt
    private String mail_receipt_owner = ROOT_IP +"mailreceiptowner.php?bookingID=";

    //Edit Car Record
    private String edit_car = ROOT_IP +"editcar.php";

    //Edit Car Record
    private String car_review = ROOT_IP +"listreviewcar.php?carID=";

    //Get total average car
    private String car_ave_review = ROOT_IP +"totalaveragecar.php";

    //check password
    private String check_password = ROOT_IP + "checkpassword.php";

    //complete button booking (owner)
    private String completebooking = ROOT_IP + "completebooking.php?bookingID=";

    //complete button booking (owner)
    private String viewuserattachments = ROOT_IP + "viewuserdocuments.php?userID=";

    //complete button booking (owner)
    private String viewcardocuments = ROOT_IP + "viewcardocuments.php?recordID=";

    //complete button booking (owner)
    private String viewcarpictures = ROOT_IP + "viewcarpics.php?recordID=";

    //check current booking renter
    private String checkcurrentbooking = ROOT_IP + "checkcurrentbooking.php";

    //check payment startdate and payment ID
    private String checkpayment = ROOT_IP + "checkpayment.php";

    //Insert paypal refund
    private String insertpaypalrefund = ROOT_IP + "insertpaypalrefund.php";

    //Get end date
    private String getenddate = ROOT_IP + "getenddate.php";

    //Insert Penalty
    private String insertpenaltyURL = ROOT_IP + "insertpenalty.php";

    //List Penalty
    private String penaltyList = ROOT_IP + "listpenalty.php?userID=";

    //List Penalty
    private String viewpenalty = ROOT_IP + "viewpenalty.php";

    //List Penalty
    private String insertpaypalpenalty = ROOT_IP + "insertpaypalpenalty.php";





    /**
     *
     * GETTERS AND SETTERS
     */

    public String getInsertpaypalpenalty() {
        return insertpaypalpenalty;
    }

    public void setInsertpaypalpenalty(String insertpaypalpenalty) {
        this.insertpaypalpenalty = insertpaypalpenalty;
    }

    public String getViewpenalty() {
        return viewpenalty;
    }

    public void setViewpenalty(String viewpenalty) {
        this.viewpenalty = viewpenalty;
    }

    public String getPenaltyList() {
        return penaltyList;
    }

    public void setPenaltyList(String penaltyList) {
        this.penaltyList = penaltyList;
    }

    public String getInsertpenaltyURL() {
        return insertpenaltyURL;
    }

    public void setInsertpenaltyURL(String insertpenaltyURL) {
        this.insertpenaltyURL = insertpenaltyURL;
    }

    public String getGetenddate() {
        return getenddate;
    }

    public void setGetenddate(String getenddate) {
        this.getenddate = getenddate;
    }

    public String getInsertpaypalrefund() {
        return insertpaypalrefund;
    }

    public void setInsertpaypalrefund(String insertpaypalrefund) {
        this.insertpaypalrefund = insertpaypalrefund;
    }

    public String getCheckpayment() {
        return checkpayment;
    }

    public void setCheckpayment(String checkpayment) {
        this.checkpayment = checkpayment;
    }

    public String getViewcarpictures() {
        return viewcarpictures;
    }

    public void setViewcarpictures(String viewcarpictures) {
        this.viewcarpictures = viewcarpictures;
    }

    public String getCarAttachmentPicUrl() {
        return carAttachmentPicUrl;
    }

    public void setCarAttachmentPicUrl(String carAttachmentPicUrl) {
        this.carAttachmentPicUrl = carAttachmentPicUrl;
    }

    public String getViewcardocuments() {
        return viewcardocuments;
    }

    public void setViewcardocuments(String viewcardocuments) {
        this.viewcardocuments = viewcardocuments;
    }

    public String getUserAttachmentPicUrl() {
        return userAttachmentPicUrl;
    }

    public void setUserAttachmentPicUrl(String userAttachmentPicUrl) {
        this.userAttachmentPicUrl = userAttachmentPicUrl;
    }

    public String getViewuserattachments() {
        return viewuserattachments;
    }

    public void setViewuserattachments(String viewuserattachments) {
        this.viewuserattachments = viewuserattachments;
    }

    public String getCheckcurrentbooking() {
        return checkcurrentbooking;
    }

    public void setCheckcurrentbooking(String checkcurrentbooking) {
        this.checkcurrentbooking = checkcurrentbooking;
    }

    public String getCheck_password() {
        return check_password;
    }

    public void setCheck_password(String check_password) {
        this.check_password = check_password;
    }

    public String getCompletebooking() {
        return completebooking;
    }

    public void setCompletebooking(String completebooking) {
        this.completebooking = completebooking;
    }

    public String getView_booking_request_history_owner() {
        return view_booking_request_history_owner;
    }

    public void setView_booking_request_history_owner(String view_booking_request_history_owner) {
        this.view_booking_request_history_owner = view_booking_request_history_owner;
    }

    public String getView_booking_request_history() {
        return view_booking_request_history;
    }

    public void setView_booking_request_history(String view_booking_request_history) {
        this.view_booking_request_history = view_booking_request_history;
    }

    public String getGetBrandName() {
        return getBrandName;
    }

    public void setGetBrandName(String getBrandName) {
        this.getBrandName = getBrandName;
    }

    public String getFetch_bdata() {
        return fetch_bdata;
    }

    public void setFetch_bdata(String fetch_bdata) {
        this.fetch_bdata = fetch_bdata;
    }

    public String getFilterExpenses() {
        return filterExpenses;
    }

    public void setFilterExpenses(String filterExpenses) {
        this.filterExpenses = filterExpenses;
    }

    public String getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(String totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    public String getFilterEarnings() {
        return filterEarnings;
    }

    public void setFilterEarnings(String filterEarnings) {
        this.filterEarnings = filterEarnings;
    }

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
