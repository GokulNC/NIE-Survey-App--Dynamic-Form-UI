package in.gov.nie.niesurvey;

import android.util.Log;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static in.gov.nie.niesurvey.Constants.*;

/**
 * Created by Gokul on 18-Mar-17.
 */

class Connections {

    private static final String TAG = "OkHttp";
    static String hostIP = "http://122.174.163.129";
    static String loginURL = "/NIE/login.jsp";
    static String formsURL = "/NIE/forms.jsp";
    static String submitFormURL = "/NIE/formhandler.jsp";
    static String testSource = "http://github.com/GokulNC/Programming_Practice/blob/master/To%20Solve.txt";

    /*//For Volley:
    static RequestQueue requestQueue;

    static void initConnection(Context context) {
        //Important Note: Call this first before GET or POST
        requestQueue = Volley.newRequestQueue(context);
    }*/

    static OkHttpClient client = new OkHttpClient().newBuilder()
            .followRedirects(true)
            .followSslRedirects(true)
            .build();

    static void initOkHttpClient() {
        client = new OkHttpClient().newBuilder()
                .followRedirects(true)
                .followSslRedirects(true)
                .build();
    }

    static String doGetRequest(String url) throws IOException {
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .build();

        Log.d(TAG, "Sending GET Request to "+url);

        if(client==null) initOkHttpClient();
        okhttp3.Response response = client.newCall(request).execute();

        Log.d(TAG, "Response Code: "+response.code());
        Log.d(TAG, "Response Message: "+response.message());
        Log.d(TAG, "Response Body: "+response.body().string());
        return response.body().toString();
    }

    static String doPostRequest(String url, HashMap<String, String> map) throws IOException {

        //For OkHttp 2.0:
        /*RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("username", "admin")
                .addFormDataPart("password", "admin")
                .build();*/

        FormBody.Builder bodyBuilder = new FormBody.Builder();
        for(String key: map.keySet()) {
            bodyBuilder.add(key, map.get(key));
        }
        RequestBody body = bodyBuilder.build();

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                //.method("POST", RequestBody.create(null, new byte[0]))
                .post(body)
                .build();

        if(client==null) initOkHttpClient();
        okhttp3.Response response = client.newCall(request).execute();

        return response.body().string();

    }

    static String doPostRequestViaBody(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                //.method("POST", RequestBody.create(null, new byte[0]))
                .post(body)
                .build();

        if(client==null) initOkHttpClient();
        okhttp3.Response response = client.newCall(request).execute();

        return response.body().string();

    }

}
