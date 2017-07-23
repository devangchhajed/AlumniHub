package zuts.bit.connect.helper;

import android.net.ConnectivityManager;

/**
 * Created by Devang on 12/30/2016.
 */

public class AppConfig {

    public static final String WEATHER_AT_BIT="http://api.openweathermap.org/data/2.5/weather?zip=302020,india&units=metric&appid=85134df4b7cfc8e6e639ddba2dd6e797";
    public static final String WEATHER_ICON="http://openweathermap.org/img/w/";

    public static final String BASE_DOMAIN="http://codejuris.com";
    public static final String BASE_URL=BASE_DOMAIN+"/BITConnect/TEst/index.php";
    public static final String API_VERSION="/v1";
    public static final String UPDATE_URL=BASE_URL+"/update";

    public static final String REGISER_NEW_USER_URL=BASE_URL+API_VERSION+"/user/register";
    public static final String UPDATE_USER_IMG_URL=BASE_URL+API_VERSION+"/user/updateuserimage";
    public static final String USER_FEEDBACK_URL=BASE_URL+API_VERSION+"/user/feedback";
    public static final String USER_DETAIL_URL=BASE_URL+API_VERSION+"/user/fetchuserdetail";
    public static final String UPDATE_FCM_ID=BASE_URL+API_VERSION+"/user/updatefcmid";
    public static final String GET_NOTIFICATION_LIST=BASE_URL+API_VERSION+"/user/notification";
    public static final String REGISTER_IMAGE_REPORT=BASE_URL+API_VERSION+"/user/reportimage";


    public static final String NEWS_FEED_URL=BASE_URL+API_VERSION+"/newsfeed/timeline";
    public static final String POST_FEED_URL=BASE_URL+API_VERSION+"/newsfeed/post";
    public static final String USER_FEED_URL=BASE_URL+API_VERSION+"/newsfeed/user";
    public static final String DELETE_POST_FEED_URL=BASE_URL+API_VERSION+"/newsfeed/deletepost";
    public static final String LIKE_POST_FEED_URL=BASE_URL+API_VERSION+"/newsfeed/status/like";
    public static final String UNLIKE_FEED_URL=BASE_URL+API_VERSION+"/newsfeed/status/unlike";
    public static final String COMMENT_POST_FEED_URL=BASE_URL+API_VERSION+"/newsfeed/status/comment";
    public static final String UNCOMMENT_POST_FEED_URL=BASE_URL+API_VERSION+"/newsfeed/status/uncomment";
    public static final String FULL_POST_FEED_URL=BASE_URL+API_VERSION+"/newsfeed/status/getfullpost";

    public static final String FETCH_SUBJECT_LIST=BASE_URL+API_VERSION+"/datacorner/list";
    public static final String FETCH_PAPER_LIST=BASE_URL+API_VERSION+"/datacorner/papers";
    public static final String UPLOAD_PAPER=BASE_URL+API_VERSION+"/datacorner/uploadpapers";


    public static final String DEFAULT_USER_PIC="http://codejuris.com/BITConnect/TEst/images/app/default_user_pic.jpg";


    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    public static final String PREF_NAME = "BITConnect";

}
