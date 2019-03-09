package sublimate.com.sublimate.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import sublimate.com.sublimate.R;

public class PreferencesActivity extends AppCompatActivity {
    public static String USE_MOCK = "USE_MOCK";
    public static String WEBSOCKET_ADDRESS = "WEBSOCKET_ADDRESS";
    public static String HTTP_ADDRESS = "HTTP_ADDRESS";

    public static final String WEBSOCKET_URL = "ws://10.0.2.2:8090"; // ws://192.168.0.134:8090 ws://10.0.2.2:8090
    public static final String HTTP_URL = "http://10.0.2.2:3000"; // http://192.168.0.134:3000 http://10.0.2.2:3000

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferences);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        Switch mockSwitch = findViewById(R.id.switch_use_mock);
        EditText websocketET = findViewById(R.id.et_websocket_address);
        EditText httpET = findViewById(R.id.et_http_address);

        mockSwitch.setChecked(prefs.getBoolean(USE_MOCK, false));
        websocketET.setText(prefs.getString(WEBSOCKET_ADDRESS, WEBSOCKET_URL));
        httpET.setText(prefs.getString(HTTP_ADDRESS, HTTP_URL));

        mockSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean(USE_MOCK, b);
                editor.apply();
            }
        });

        websocketET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(WEBSOCKET_ADDRESS, editable.toString());
                editor.apply();
            }
        });

        httpET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(HTTP_ADDRESS, editable.toString());
                editor.apply();
            }
        });
    }
}
