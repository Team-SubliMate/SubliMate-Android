package sublimate.com.sublimate.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import sublimate.com.sublimate.R;

import static sublimate.com.sublimate.network.InventoryService.HTTP_URL;
import static sublimate.com.sublimate.network.WebSocketEventListener.WEBSOCKET_URL;

public class PreferencesActivity extends AppCompatActivity {
    public static String WEBSOCKET_ADDRESS = "WEBSOCKET_ADDRESS";
    public static String HTTP_ADDRESS = "HTTP_ADDRESS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferences);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        EditText websocketET = findViewById(R.id.et_websocket_address);
        EditText httpET = findViewById(R.id.et_http_address);

        String defaultWsAddress = WEBSOCKET_URL;
        String defaultHttpAddress = HTTP_URL;

        websocketET.setText(prefs.getString(WEBSOCKET_ADDRESS, defaultWsAddress));
        httpET.setText(prefs.getString(HTTP_ADDRESS, defaultHttpAddress));

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
                editor.commit();
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
                editor.commit();
            }
        });
    }
}
