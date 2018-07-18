package sublimate.com.sublimate.network;

import android.app.Dialog;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.WebSocket;
import sublimate.com.sublimate.R;
import sublimate.com.sublimate.json.AddItemEvent;
import sublimate.com.sublimate.json.RemoveItemEvent;
import sublimate.com.sublimate.json.TieBreakerEvent;
import sublimate.com.sublimate.json.TieBreakerEventResponse;
import sublimate.com.sublimate.view.InventoryAdapter;

public class WebSocketEventHandler {
    private InventoryAdapter adapter;
    private Dialog tieBreakerDialog;

    public WebSocketEventHandler(InventoryAdapter adapter, Dialog tieBreakerDialog) {
        this.adapter = adapter;
        this.tieBreakerDialog = tieBreakerDialog;
    }

    public void onRemoveItemEvent(RemoveItemEvent event) {
        adapter.removeItemById(event.getItemId());
    }

    // TODO: UI STUFF Returns itemId of item to remove
    public int onTieBreakerEvent(final TieBreakerEvent event, final WebSocket webSocket) {
        // YOLO
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                int[] itemIds = event.getItemIds();
                int removeId = -1;

                ArrayList<String> choiceNames = new ArrayList<>();

                final Spinner dropdown = tieBreakerDialog.findViewById(R.id.choices);
                Button dialogButton = tieBreakerDialog.findViewById(R.id.button_confirm);

                final HashMap<String, Integer> map = new HashMap<>();

                // Add choices
                for (int i = 0; i < itemIds.length; i++) {
                    String name = adapter.getItemById(itemIds[i]).getName();
                    map.put(name, itemIds[i]);
                    choiceNames.add(name);
                }

                dropdown.setAdapter(new ArrayAdapter<String>(tieBreakerDialog.getContext(), android.R.layout.simple_spinner_dropdown_item, choiceNames));

                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String text = dropdown.getSelectedItem().toString();
                        int removeId = (int) map.get(text);

                        Gson gson = new Gson();
                        TieBreakerEventResponse eventResponse = new TieBreakerEventResponse(removeId);
                        String response =  gson.toJson(eventResponse, TieBreakerEventResponse.class);
                        webSocket.send(response);
                        tieBreakerDialog.dismiss();
                    }
                });

                tieBreakerDialog.show();
            }
        });
        // TODO: END YOLO

        // adapter.removeItemById(removeId);
        return -1;
    }

    public void onAddItemEvent(AddItemEvent event) {
        adapter.addInventoryItem(event.getItem());
    }
}
