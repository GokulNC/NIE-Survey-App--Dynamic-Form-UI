package in.gov.nie.niesurvey;

import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MediaType;

/**
 * Created by Gokul on 18-Mar-17.
 */

public class Constants {

    public static final String login_json = "{ 'username': 'admin', 'password': 'admin' }";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    static ArrayList<String> formsAvailable;
}
