package sublimate.com.sublimate;

import android.util.Log;

import com.google.gson.Gson;

import okhttp3.WebSocket;
import sublimate.com.sublimate.json.RemoveItemEvent;
import sublimate.com.sublimate.network.WebSocketEventListener;

public class Presenter implements PresenterContract {
    private WebSocket webSocket;

    public void setWebSocket(WebSocket webSocket) {
        this.webSocket = webSocket;
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
}
