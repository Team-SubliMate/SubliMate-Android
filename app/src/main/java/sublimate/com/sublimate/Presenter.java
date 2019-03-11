package sublimate.com.sublimate;

import android.util.Log;

import com.google.gson.Gson;

import okhttp3.WebSocket;
import sublimate.com.sublimate.json.InventoryItem;
import sublimate.com.sublimate.json.ManualItemEvent;
import sublimate.com.sublimate.json.RemoveItemEvent;
import sublimate.com.sublimate.network.WebSocketEventListener;

public class Presenter implements PresenterContract {
    private ViewContract view;
    private WebSocket webSocket;

    Presenter(ViewContract view) {
        this.view = view;
    }

    public void setWebSocket(WebSocket webSocket) {
        this.webSocket = webSocket;
    }

    @Override
    public void addItem(InventoryItem item) {
        Gson gson = new Gson();
        ManualItemEvent manualItemEvent = new ManualItemEvent(item);
        String itemJson = gson.toJson(manualItemEvent, ManualItemEvent.class);
        webSocket.send(itemJson);
        Log.d(WebSocketEventListener.TAG, "Manual entry sent: " + itemJson);
    }

    @Override
    public void removeItem(int itemId) {
        // Send to backend
        Gson gson = new Gson();
        RemoveItemEvent removeItemEvent = new RemoveItemEvent(itemId);
        String itemJson = gson.toJson(removeItemEvent, RemoveItemEvent.class);

        if (webSocket != null) {
            webSocket.send(itemJson);
            Log.d(WebSocketEventListener.TAG, "Remove item event sent: " + itemJson);
        }
    }

    @Override
    public void rearrangeFridge() {
        // Send rearrange fridge event
    }

    @Override
    public void showToast(String message) {
        view.showToast(message);
    }
}
