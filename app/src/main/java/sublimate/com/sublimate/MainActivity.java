package sublimate.com.sublimate;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sublimate.com.sublimate.json.InventoryItem;
import sublimate.com.sublimate.json.InventoryServiceResponse;
import sublimate.com.sublimate.json.ManualEntry;
import sublimate.com.sublimate.network.InventoryService;
import sublimate.com.sublimate.network.WebSocketEventListener;
import sublimate.com.sublimate.view.InventoryAdapter;

public class MainActivity extends AppCompatActivity {
    private RecyclerView inventoryRecyclerView;
    private InventoryAdapter inventoryAdapter;
    private ProgressBar loadingSpinner;
    private TextView errorTextView;
    private FloatingActionButton inventoryActionButton;
    private Toast toast;
    private Dialog manualAddDialog;

    private Callback<InventoryServiceResponse> inventoryCallback;
    private InventoryService inventoryService;
    private OkHttpClient client;
    private WebSocket webSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initHTTP();
        initWS();

        getItems();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        inventoryRecyclerView = (RecyclerView) findViewById(R.id.rv_inventory);
        loadingSpinner = (ProgressBar) findViewById(R.id.pb_main_loader);
        errorTextView = (TextView) findViewById(R.id.tv_error_message);
        inventoryActionButton = (FloatingActionButton) findViewById(R.id.inventory_action_button);

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
        inventoryService = InventoryService.getInventoryService();

        inventoryCallback = new Callback<InventoryServiceResponse>() {
            @Override
            public void onResponse(Call<InventoryServiceResponse> call, Response<InventoryServiceResponse> response) {
                InventoryServiceResponse inventoryResponse = response.body();

                inventoryAdapter.setInventoryItems(inventoryResponse.getItems());
                showContent();
            }

            @Override
            public void onFailure(Call<InventoryServiceResponse> call, Throwable t) {
                Log.d("HTTP Response Failure: ", t.toString());
            }
        };
    }

    private void initWS() {
        // Set up WS
        Request request = new Request.Builder().url(WebSocketEventListener.WEBSOCKET_URL).build();
        WebSocketEventListener listener = new WebSocketEventListener();

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

    private void showManualAddDialog() {
        if (manualAddDialog == null) {
            manualAddDialog = new Dialog(this);
            manualAddDialog.setContentView(R.layout.manual_add_dialog);
            manualAddDialog.setTitle(R.string.add_dialog_title);

            final EditText dialogEditText = (EditText) manualAddDialog.findViewById(R.id.et_inventory_item_name);
            dialogEditText.setText("Item " + (inventoryAdapter.getItemCount() + 1));

            // Set up the save button
            Button dialogButton = (Button) manualAddDialog.findViewById(R.id.button_add_item);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Create item from user entry
                    String itemText = dialogEditText.getText().toString();
                    InventoryItem item = new InventoryItem(itemText);

                    // UI
                    inventoryAdapter.addInventoryItem(item);
                    showToast("The item \"" + item.getName() + "\" has been added.");

                    // Send to backend
                    Gson gson = new Gson();
                    ManualEntry manualEntry = new ManualEntry(item);
                    String itemJson = gson.toJson(manualEntry, ManualEntry.class);
                    webSocket.send(itemJson);
                    Log.d(WebSocketEventListener.TAG, "Manual entry sent.");

                    manualAddDialog.dismiss();
                }
            });
        }

        final EditText dialogEditText = (EditText) manualAddDialog.findViewById(R.id.et_inventory_item_name);
        dialogEditText.setText("Item " + (inventoryAdapter.getItemCount() + 1));
        manualAddDialog.show();
    }

    private void getItems() {
        Call<InventoryServiceResponse> call;
        try {
            call = inventoryService.getInventoryCall();
            call.enqueue(inventoryCallback);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}