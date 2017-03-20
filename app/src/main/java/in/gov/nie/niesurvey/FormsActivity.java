package in.gov.nie.niesurvey;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import static in.gov.nie.niesurvey.Connections.formsURL;
import static in.gov.nie.niesurvey.Connections.hostIP;
import static in.gov.nie.niesurvey.Constants.AADHAAR_KEY_NAME;
import static in.gov.nie.niesurvey.Constants.FORM_FILEDS_KEY_NAME;
import static in.gov.nie.niesurvey.Constants.FORM_KEY_NAME;
import static in.gov.nie.niesurvey.Constants.formsAvailable;

public class FormsActivity extends AppCompatActivity {

    private static final String TAG = "FormsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forms);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Select any form", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        LinearLayout ll = (LinearLayout) findViewById(R.id.content_forms);
        for(final String formName: formsAvailable ) {
            Button btn = new Button(this);
            btn.setText(formName);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "Requesting "+formName, Toast.LENGTH_SHORT).show();
                    final String aadhaar = ((EditText) findViewById(R.id.aadhaar)).getText().toString();
                    new RequestForm().execute(formName, aadhaar);
                }
            });
            ll.addView(btn);
        }
    }

    class RequestForm extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... formName) {

            HashMap<String, String> map = new HashMap<>();
            map.put(FORM_KEY_NAME, formName[0]);
            map.put(AADHAAR_KEY_NAME, formName[1]);
            boolean success = true;
            String response = "";
            try {
                response = Connections.doPostRequest(hostIP+formsURL, map).trim();
                Log.d(TAG, "Form Fields of "+formName[0]+":\n"+response);
                JSONObject res = new JSONObject(response); //This line is to ensure we have received a proper JSON, else exception will be thrown

            } catch (IOException e) {
                e.printStackTrace();
                success = false;
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(!success || response.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Error Retrieving Form", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(FormsActivity.this, FormRenderActivity.class);
                intent.putExtra(FORM_FILEDS_KEY_NAME, response);
                intent.putExtra(FORM_KEY_NAME, formName[0]);
                startActivity(intent);
            }

            return null;
        }
    }

}
