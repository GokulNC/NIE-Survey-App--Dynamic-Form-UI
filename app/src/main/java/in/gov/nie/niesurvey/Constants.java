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
    public static final String FORM_KEY_NAME = "form";
    public static final String FIELDS_KEY_NAME = "fields";
    public static final String FIELD_HUMAN_NAME_KEY_NAME = "human_name";
    public static final String INPUT_TYPE_KEY_NAME = "input_type";
    public static final String FIELD_ID_KEY_NAME = "id";
    public static final String FIELD_OPTIONS_KEY_NAME = "options";

    public static final String TEXT_INPUT_TYPE_KEY_NAME = "text";
    public static final String RADIO_INPUT_TYPE_KEY_NAME = "radio";

    static ArrayList<String> formsAvailable;
}
