package in.gov.nie.niesurvey;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import static in.gov.nie.niesurvey.Connections.hostIP;
import static in.gov.nie.niesurvey.Connections.submitFormURL;
import static in.gov.nie.niesurvey.Constants.*;

public class FormRenderActivity extends AppCompatActivity {

    private static final String TAG = "FormSubmission";
    //HashMap<Integer, String> idMap = new HashMap<>();
    SparseArray<String> textIdMap = new SparseArray<>();
    SparseArray<String> radioIdMap = new SparseArray<>();
    String formName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_render);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Submitting form", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                submitForm();
            }
        });

        renderForm(getIntent().getStringExtra(FORM_FILEDS_KEY_NAME));
        formName = getIntent().getStringExtra(FORM_KEY_NAME);

    }

    void renderForm(String json) {

        LinearLayout ll = (LinearLayout) findViewById(R.id.content_form_render);
        boolean rendered = true;
        try {
            JSONObject form = new JSONObject(json);
            JSONArray fields = form.getJSONArray(FIELDS_KEY_NAME);

            for(int i=0; i<fields.length(); ++i) {
                ll.addView(getFieldView(fields.getJSONObject(i)));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            rendered = false;
        }
    }

    View getFieldView(JSONObject json) throws JSONException {
        View field = null;
        String inputType = json.getString(INPUT_TYPE_KEY_NAME);

        if(inputType.equals(TEXT_INPUT_TYPE_KEY_NAME)) {
            EditText et = new EditText(this);
            et.setId(View.generateViewId());
            et.setHint(json.getString(FIELD_HUMAN_NAME_KEY_NAME));
            Log.d(TAG, json.getString(FIELD_HUMAN_NAME_KEY_NAME)+": "+et.getId());

            if(json.has(VALUE_KEY_NAME)) {
                et.setText(json.getString(VALUE_KEY_NAME));
            }
            field = et;
            textIdMap.put(et.getId(), json.getString(FIELD_ID_KEY_NAME));

        } else if(inputType.equals(RADIO_INPUT_TYPE_KEY_NAME)) {

            //LinearLayout container = new LinearLayout(this);
            RadioGroup radioGroup = new RadioGroup(this);
            radioGroup.setId(View.generateViewId());
            TextView caption = new TextView(this);
            caption.setText(json.getString(FIELD_HUMAN_NAME_KEY_NAME));
            radioGroup.addView(caption);
            String value = "";
            boolean hasValue = false;
            if(json.has(VALUE_KEY_NAME)) {
                value = json.getString(VALUE_KEY_NAME);
                hasValue = true;
            }

            if(json.has(FIELD_OPTIONS_KEY_NAME)) {
                JSONArray options = json.getJSONArray(FIELD_OPTIONS_KEY_NAME);

                for (int i = 0; i < options.length(); ++i) {
                    RadioButton rb = new RadioButton(this);
                    rb.setId(View.generateViewId());
                    rb.setText(options.getString(i));
                    if(hasValue && options.getString(i).equals(value)) rb.setChecked(true);
                    radioGroup.addView(rb);
                }
            }

            field = radioGroup;
            Log.d(TAG, json.getString(FIELD_HUMAN_NAME_KEY_NAME)+": "+radioGroup.getId());
            radioIdMap.put(radioGroup.getId(), json.getString(FIELD_ID_KEY_NAME));

        }

        return field;
    }

    void submitForm() {
        final HashMap<String, String> map = new HashMap<>();

        map.put(FORM_KEY_NAME, formName);

        for(int i=0; i<textIdMap.size(); ++i) {
            int key = textIdMap.keyAt(i);
            String value = ((EditText) findViewById(key)).getText().toString();
            map.put(textIdMap.get(key), value);
        }
        for (int i=0; i<radioIdMap.size(); ++i) {
            int key = radioIdMap.keyAt(i);
            String value = (String) ((RadioButton) findViewById((
                    (RadioGroup) findViewById(key)
                    ).getCheckedRadioButtonId())
                    ).getText();
            map.put(radioIdMap.get(key), value);
        }

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.alert_confirm_send)
                .setMessage(R.string.alert_confirm_submit)
                .setPositiveButton(R.string.action_submit_form, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "Form to be submitted:\n"+map.toString());

                    }
                })
                .setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        alertDialog.show();

        new SubmitForm(map).execute(hostIP+submitFormURL);
    }

    class SubmitForm extends AsyncTask<String, Void, Boolean> {

        HashMap<String, String> data;
        SubmitForm(HashMap<String, String> fields) {
            data = fields;
        }

        @Override
        protected Boolean doInBackground(String... url) {

            boolean success = true;
            String response = "";
            try {
                response = Connections.doPostRequest(url[0], data).trim();
                //JSONObject res = new JSONObject(response); //This line is to ensure we have received a proper JSON, else exception will be thrown

                Log.d(TAG, "Submit Response: "+response);

            } catch (IOException e) {
                e.printStackTrace();
                success = false;
            } /*catch (JSONException e) {
                e.printStackTrace();
            }*/

            if(!success || response.isEmpty()) {
                //TODO: Toast.makeText(getApplicationContext(), "Error Submitting Form", Toast.LENGTH_SHORT).show();
            } else {
                //TODO: Find what the error is from response JSON and report it
            }

            return null;
        }
    }

}
