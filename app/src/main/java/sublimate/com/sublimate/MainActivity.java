package sublimate.com.sublimate;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.LruCache;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import sublimate.com.sublimate.network.InventoryService;
import sublimate.com.sublimate.network.WebSocketEventHandler;
import sublimate.com.sublimate.network.WebSocketEventListener;
import sublimate.com.sublimate.view.InventoryAdapter;
import sublimate.com.sublimate.view.ManualAddDialog;
import sublimate.com.sublimate.view.PreferencesActivity;

import static sublimate.com.sublimate.view.PreferencesActivity.USE_MOCK;
import static sublimate.com.sublimate.view.PreferencesActivity.WEBSOCKET_ADDRESS;
import static sublimate.com.sublimate.view.PreferencesActivity.WEBSOCKET_URL;

public class MainActivity extends AppCompatActivity implements ViewContract {
    private static int MAX_CACHE_SIZE = 5;

    private Presenter presenter;

    private ViewGroup content;
    private RecyclerView inventoryRecyclerView;
    private RecyclerView frequentRecyclerView;
    private InventoryAdapter inventoryAdapter;
    private InventoryAdapter frequentAdapter;
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

    private LruCache<Integer, InventoryItem> frequentItemsCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter = new Presenter(this);
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
    public boolean onOptionsItemSelected(final MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_settings:
                Intent intent = new Intent(this, PreferencesActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_rearrange:
                if (item.getTitle().equals(getString(R.string.menu_rearrange))) {
                    new AlertDialog.Builder(this)
                            .setTitle("Rearrange Contents")
                            .setMessage("You're about to stop the fridge from responding to item changes in order to rearrange the fridge contents. Are you sure you want to proceed?")
                            .setPositiveButton(R.string.menu_rearrange, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    presenter.stopHandlingEvents();
                                    item.setTitle(R.string.menu_done_rearrange);
                                }
                            }).show();
                    return true;
                }

                if (item.getTitle().equals(getString(R.string.menu_done_rearrange))) {
                    presenter.startHandlingEvents();
                    item.setTitle(R.string.menu_rearrange);
                    return true;
                }

                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initView() {
        content = findViewById(R.id.content);
        frequentItemsCache = new LruCache<>(MAX_CACHE_SIZE);
        loadingSpinner = findViewById(R.id.pb_main_loader);
        errorTextView = findViewById(R.id.tv_error_message);
        inventoryActionButton = findViewById(R.id.inventory_action_button);

        inventoryActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showContent();
                showManualAddDialog();
            }
        });

        initInventory();
        initFrequent();
    }

    private void initFrequent() {
        frequentRecyclerView = findViewById(R.id.rv_frequent);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 5);
        frequentAdapter = new InventoryAdapter(this, presenter);

        frequentRecyclerView.setHasFixedSize(true);
        frequentRecyclerView.setLayoutManager(layoutManager);
        frequentRecyclerView.setAdapter(frequentAdapter);
    }

    private void initInventory() {
        inventoryRecyclerView = findViewById(R.id.rv_inventory);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 3);
        inventoryAdapter = new InventoryAdapter(this, presenter);

        inventoryRecyclerView.setHasFixedSize(true);
        inventoryRecyclerView.setLayoutManager(layoutManager);
        inventoryRecyclerView.setAdapter(inventoryAdapter);
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

        presenter.setWebSocket(webSocket);
    }

    private void showContent() {
        hideLoading();
        hideError();
        content.setVisibility(View.VISIBLE);
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

    private void showManualAddDialog() {
        if (manualAddDialog == null) {
            manualAddDialog = new ManualAddDialog(this, presenter);
        }

        manualAddDialog.show();
    }

    private void getItems() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if (prefs.getBoolean(USE_MOCK, false)) {
            setUpMock();
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

    private void setUpMock() {
        List<InventoryItem> items = new ArrayList<>();
        InventoryItem item = new InventoryItem(1, "Item no image", 1, 100);
        InventoryItem itemWithImage = new InventoryItem(2, "Item with image", 1, 100, "http://bulktraveler.com/wp-content/uploads/2015/06/Costco-London-Biritish-Rotisserie-Chicken-1.jpg");
        items.add(item);
        items.add(itemWithImage);

        frequentItemsCache.put(1, item);
        frequentItemsCache.put(2, itemWithImage);

        // filler items
        for (int i = 3; i < 12; i++) {
            InventoryItem fill = new InventoryItem(i, "Item " + i, 1, 100);
            items.add(fill);
            frequentItemsCache.put(i, fill);
        }

        inventoryAdapter.setInventoryItems(items);
        frequentAdapter.setInventoryItems(new ArrayList<>(frequentItemsCache.snapshot().values()));
        showContent();
    }

    private void initTieBreakerDialog() {
        if (tieBreakerDialog == null) {
            tieBreakerDialog = new Dialog(this);
            tieBreakerDialog.setContentView(R.layout.tiebreaker_dialog);
            tieBreakerDialog.setTitle(R.string.tiebreaker_dialog_title);
        }
    }

    @Override
    public void showToast(String message) {
        toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();
    }
}