package sublimate.com.sublimate;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sublimate.com.sublimate.json.InventoryItem;
import sublimate.com.sublimate.json.InventoryServiceResponse;
import sublimate.com.sublimate.json.ManualItemEvent;
import sublimate.com.sublimate.network.InventoryService;
import sublimate.com.sublimate.network.WebSocketEventHandler;
import sublimate.com.sublimate.network.WebSocketEventListener;
import sublimate.com.sublimate.view.InventoryAdapter;
import sublimate.com.sublimate.view.PreferencesActivity;

import static sublimate.com.sublimate.view.PreferencesActivity.USE_MOCK;
import static sublimate.com.sublimate.view.PreferencesActivity.WEBSOCKET_ADDRESS;
import static sublimate.com.sublimate.view.PreferencesActivity.WEBSOCKET_URL;

public class MainActivity extends AppCompatActivity {
    private RecyclerView inventoryRecyclerView;
    private InventoryAdapter inventoryAdapter;
    private ProgressBar loadingSpinner;
    private TextView errorTextView;
    private FloatingActionButton inventoryActionButton;
    private Toast toast;
    private Dialog manualAddDialog;
    private Dialog tieBreakerDialog;

    private Callback<InventoryServiceResponse> inventoryCallback;
    private InventoryService inventoryService;
    private OkHttpClient client;
    private WebSocket webSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initTieBreakerDialog(); // TODO: Clean up
        initHTTP();
        initWS();

        getItems();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_settings:
                Intent intent = new Intent(this, PreferencesActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initView() {
        inventoryRecyclerView = findViewById(R.id.rv_inventory);
        loadingSpinner = findViewById(R.id.pb_main_loader);
        errorTextView = findViewById(R.id.tv_error_message);
        inventoryActionButton = findViewById(R.id.inventory_action_button);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        inventoryAdapter = new InventoryAdapter(this);

        inventoryRecyclerView.setHasFixedSize(true);
        inventoryRecyclerView.setLayoutManager(layoutManager);
        inventoryRecyclerView.setAdapter(inventoryAdapter);

        inventoryActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showContent();
                showManualAddDialog();
            }
        });
    }

    private void initHTTP() {
        // Inventory Service HTTP
        inventoryService = InventoryService.getInventoryService(this);

        inventoryCallback = new Callback<InventoryServiceResponse>() {
            @Override
            public void onResponse(@NonNull Call<InventoryServiceResponse> call, @NonNull Response<InventoryServiceResponse> response) {
                InventoryServiceResponse inventoryResponse = response.body();

                if (inventoryResponse == null) {
                    Log.d("HTTP Response missing", "No inventory response");
                    return;
                }

                inventoryAdapter.setInventoryItems(inventoryResponse.getItems());
                showContent();
            }

            @Override
            public void onFailure(@NonNull Call<InventoryServiceResponse> call, @NonNull Throwable t) {
                Log.d("HTTP Response Failure: ", t.toString());
            }
        };
    }

    private void initWS() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        // Set up WS
        Request request = new Request.Builder().url(prefs.getString(WEBSOCKET_ADDRESS, WEBSOCKET_URL)).build();
        WebSocketEventHandler handler = new WebSocketEventHandler(inventoryAdapter, tieBreakerDialog);
        WebSocketEventListener listener = new WebSocketEventListener(handler);

        client = new OkHttpClient();
        webSocket = client.newWebSocket(request, listener);
        client.dispatcher().executorService().shutdown();
    }

    private void showContent() {
        hideLoading();
        hideError();
        inventoryRecyclerView.setVisibility(View.VISIBLE);
    }

    private void hideContent() {
        inventoryRecyclerView.setVisibility(View.GONE);
    }

    private void showLoading() {
        loadingSpinner.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        loadingSpinner.setVisibility(View.GONE);
    }

    private void showError() {
        hideContent();
        hideLoading();
        errorTextView.setVisibility(View.VISIBLE);
    }

    private void hideError() {
        errorTextView.setVisibility(View.GONE);
    }

    private void showToast(String text) {
        toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
        toast.show();
    }

    // TODO: Clean this up
    @SuppressLint("SetTextI18n")
    private void showManualAddDialog() {
        if (manualAddDialog == null) {
            manualAddDialog = new Dialog(this);
            manualAddDialog.setContentView(R.layout.manual_add_dialog);
            manualAddDialog.setTitle(R.string.add_dialog_title);
        }

        final EditText dialogNameEditText = manualAddDialog.findViewById(R.id.et_inventory_item_name);
        final EditText dialogQuantityEditText = manualAddDialog.findViewById(R.id.et_inventory_item_quantity);

        // Set default values

        String defaultName = "Item " + (inventoryAdapter.getItemCount() + 1);
        String defaultQuantity = "1";
        dialogNameEditText.setText(defaultName);
        dialogQuantityEditText.setText(defaultQuantity);

        // Set up the save button
        final Button dialogButton = manualAddDialog.findViewById(R.id.button_add_item);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create item from user entry
                String itemText = dialogNameEditText.getText().toString();
                int itemQuantity = Integer.parseInt(dialogQuantityEditText.getText().toString());
                InventoryItem item = new InventoryItem(itemText, itemQuantity);

                // Send to backend
                Gson gson = new Gson();
                ManualItemEvent manualItemEvent = new ManualItemEvent(item);
                String itemJson = gson.toJson(manualItemEvent, ManualItemEvent.class);
                webSocket.send(itemJson);
                Log.d(WebSocketEventListener.TAG, "Manual entry sent: " + itemJson);

                // TODO: Don't add right away, wait for backend socket event UI
                inventoryAdapter.addInventoryItem(item);
                showToast("The item \"" + item.getName() + "\" has been added.");

                manualAddDialog.dismiss();
            }
        });

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(editable)) {
                    dialogButton.setEnabled(false);
                } else {
                    dialogButton.setEnabled(true);
                }
            }
        };

        dialogNameEditText.addTextChangedListener(textWatcher);
        dialogQuantityEditText.addTextChangedListener(textWatcher);

        dialogNameEditText.setText("Item " + (inventoryAdapter.getItemCount() + 1));
        manualAddDialog.show();
    }

    private void getItems() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if (prefs.getBoolean(USE_MOCK, false)) {
            List<InventoryItem> items = new ArrayList<>();
            InventoryItem item = new InventoryItem("Item no image", 1, 100);
            InventoryItem itemWithImage = new InventoryItem("Item with image", 1, 100, "https://www.smartpettoysreview.com/wp-content/uploads/2018/04/dog-corgi-husky-mix.jpg");
            items.add(item);
            items.add(itemWithImage);

            inventoryAdapter.setInventoryItems(items);
            showContent();
            return;
        }

        Call<InventoryServiceResponse> call;
        try {
            call = inventoryService.getInventoryCall();
            call.enqueue(inventoryCallback);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initTieBreakerDialog() {
        if (tieBreakerDialog == null) {
            tieBreakerDialog = new Dialog(this);
            tieBreakerDialog.setContentView(R.layout.tiebreaker_dialog);
            tieBreakerDialog.setTitle(R.string.tiebreaker_dialog_title);
        }
    }
}