package sublimate.com.sublimate;

import android.util.Log;

import com.google.gson.Gson;

import okhttp3.WebSocket;
import sublimate.com.sublimate.json.InventoryItem;
import sublimate.com.sublimate.json.ManualItemEvent;
import sublimate.com.sublimate.json.RemoveItemEvent;
import sublimate.com.sublimate.json.StartHandlingEvent;
import sublimate.com.sublimate.json.StopHandlingEvent;
import sublimate.com.sublimate.network.WebSocketEventListener;

public class Presenter implements PresenterContract {
    private ViewContract view;
    private WebSocket webSocket;

    private Gson gson;

    Presenter(ViewContract view) {
        this.view = view;
        gson = new Gson();
    }

    public void setWebSocket(WebSocket webSocket) {
        this.webSocket = webSocket;
    }

    @Override
    public void addItem(InventoryItem item) {
        ManualItemEvent manualItemEvent = new ManualItemEvent(item);
        String itemJson = gson.toJson(manualItemEvent, ManualItemEvent.class);
        sendEvent(itemJson);
    }

    @Override
    public void removeItem(int itemId) {
        RemoveItemEvent removeItemEvent = new RemoveItemEvent(itemId);
        String itemJson = gson.toJson(removeItemEvent, RemoveItemEvent.class);
        sendEvent(itemJson);
    }

    @Override
    public void stopHandlingEvents() {
        StopHandlingEvent stopHandlingEvent = new StopHandlingEvent();
        String itemJson = gson.toJson(stopHandlingEvent, StopHandlingEvent.class);
        sendEvent(itemJson);
    }

    @Override
    public void startHandlingEvents() {
        StartHandlingEvent startHandlingEvent = new StartHandlingEvent();
        String itemJson = gson.toJson(startHandlingEvent, StartHandlingEvent.class);
        sendEvent(itemJson);
    }

    @Override
    public void showToast(String message) {
        view.showToast(message);
    }

    private void sendEvent(String itemJson) {
        if (webSocket != null) {
            webSocket.send(itemJson);
            Log.d(WebSocketEventListener.TAG, "Event sent: " + itemJson);
        }
    }
}
