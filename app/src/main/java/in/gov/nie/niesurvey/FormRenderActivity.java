package in.gov.nie.niesurvey;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static in.gov.nie.niesurvey.Constants.*;

public class FormRenderActivity extends AppCompatActivity {

    HashMap<String, Integer> radioMap = new HashMap<>();
    HashMap<String, Integer> textMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_render);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        renderForm(getIntent().getStringExtra(FORM_KEY_NAME));

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
            et.setHint(json.getString(FIELD_HUMAN_NAME_KEY_NAME));
            field = et;

        } else if(inputType.equals(RADIO_INPUT_TYPE_KEY_NAME)) {

            //LinearLayout container = new LinearLayout(this);
            RadioGroup radioGroup = new RadioGroup(this);
            TextView caption = new TextView(this);
            caption.setText(json.getString(FIELD_HUMAN_NAME_KEY_NAME));
            radioGroup.addView(caption);

            JSONArray options = json.getJSONArray(FIELD_OPTIONS_KEY_NAME);

            for(int i=0; i<options.length(); ++i) {
                RadioButton rb = new RadioButton(this);
                rb.setText(options.getString(i));
                radioGroup.addView(rb);
            }

            field = radioGroup;

        }

        return field;
    }

}
