package ph.roadtrip.roadtrip.classes;

public class EndPoints {

    private static final String ROOT_IP = "http://192.168.8.76/api/roadtrip/";

    private static final String ROOT_URL = ROOT_IP +"fileupload.php?apicall=";
    public static final String UPLOAD_URL = ROOT_URL + "uploadpic";
    public static final String GET_PICS_URL = ROOT_URL + "getpics";

    //File Upload
    private static String ROOT_ATTACHMENT_URL = ROOT_IP +"fileupload_attachment.php?apicall=";
    public static final String UPLOAD_ATTACHMENTS_URL = ROOT_ATTACHMENT_URL + "uploadpic";
    public static final String GET_ATTACHMENT_URL = ROOT_ATTACHMENT_URL + "getpics";

    //Picture Upload for Car Management
    private static final String ROOT_CARPICS_URL = ROOT_IP +"addcaraddpics.php?apicall=";
    public static final String UPLOAD_CARPICS_URL = ROOT_CARPICS_URL + "uploadpic";
    public static final String GET_CAR_PICS_URL = ROOT_URL + "getpics";

    //Upload Car Attachments OR
    private static final String ROOT_CAR_ATTACH_OR = ROOT_IP +"fileupload_OR.php?apicall=";
    public static final String UPLOAD_CAR_ATTACH_URL = ROOT_CAR_ATTACH_OR + "uploadpic";
    //public static final String GET_CAR_PICS_URL = ROOT_CAR_ATTACH_OR + "getpics";

    //Upload Car Attachments CR
    private static final String ROOT_CAR_ATTACH_CR = ROOT_IP +"fileupload_CR.php?apicall=";
    public static final String UPLOAD_CAR_ATTACH_CR_URL = ROOT_CAR_ATTACH_CR + "uploadpic";
    //public static final String GET_CAR_PICS_URL = ROOT_CAR_ATTACH_OR + "getpics";

    //Upload Car Attachments SIR
    private static final String ROOT_CAR_ATTACH_SIR = ROOT_IP +"fileupload_SIR.php?apicall=";
    public static final String UPLOAD_CAR_ATTACH_SIR_URL = ROOT_CAR_ATTACH_SIR + "uploadpic";
    //public static final String GET_CAR_PICS_URL = ROOT_CAR_ATTACH_OR + "getpics";
}
