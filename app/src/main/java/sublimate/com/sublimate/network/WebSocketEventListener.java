package sublimate.com.sublimate.network;

import android.util.Log;

import com.google.gson.Gson;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import sublimate.com.sublimate.json.AddItemEvent;
import sublimate.com.sublimate.json.RemoveItemEvent;
import sublimate.com.sublimate.json.TieBreakerEvent;
import sublimate.com.sublimate.json.TieBreakerEventResponse;
import sublimate.com.sublimate.json.WebSocketEvent;

public class WebSocketEventListener extends WebSocketListener {
    public static final int NORMAL_CLOSURE_STATUS = 1000;

    public static final String TAG = WebSocketEventListener.class.getSimpleName();

    private WebSocketEventHandler handler;

    public WebSocketEventListener(WebSocketEventHandler handler) {
        this.handler = handler;
    }

    /**
     * Invoked when a web socket has been accepted by the remote peer and may begin transmitting
     * messages.
     */
    public void onOpen(WebSocket webSocket, Response response) {
        Log.d(TAG, "Socket opened.");
        webSocket.send("{\"type\": \"NEW_CLIENT\", \"value\": \"android\"}");
        //webSocket.close(NORMAL_CLOSURE_STATUS, "nothing to do");
    }

    /** Invoked when a text (type {@code 0x1}) message has been received. */
    public void onMessage(WebSocket webSocket, String text) {
        Gson gson = new Gson();

        WebSocketEvent event = gson.fromJson(text, WebSocketEvent.class);

        switch (event.getType()) {
            case RemoveItemEvent.EVENT_TYPE:
                RemoveItemEvent removeItemEvent = gson.fromJson(text, RemoveItemEvent.class);
                handler.onRemoveItemEvent(removeItemEvent);
                break;
            case TieBreakerEvent.WHICH_ITEM_REMOVED:
                TieBreakerEvent tieBreakerEvent = gson.fromJson(text, TieBreakerEvent.class);
                int itemId = handler.onTieBreakerEvent(tieBreakerEvent, webSocket);
                TieBreakerEventResponse eventResponse = new TieBreakerEventResponse(itemId);

                // TODO: FIX
//                String response = gson.toJson(eventResponse, TieBreakerEventResponse.class);
//                webSocket.send(response);
                break;
            case AddItemEvent.EVENT_TYPE:
                AddItemEvent addItemEvent = gson.fromJson(text, AddItemEvent.class);
                handler.onAddItemEvent(addItemEvent);
                break;
            default:
                break;
        }

        Log.d(TAG, "Received message: " + text);
    }

    /** Invoked when a binary (type {@code 0x2}) message has been received. */
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        Log.d(TAG, "Received byte message: " + bytes.hex());
    }

    /**
     * Invoked when the remote peer has indicated that no more incoming messages will be
     * transmitted.
     */
    public void onClosing(WebSocket webSocket, int code, String reason) {
        webSocket.close(NORMAL_CLOSURE_STATUS, reason);
        Log.d(TAG, "Closing socket with code " + code + " and reason: " + reason);
    }

    /**
     * Invoked when both peers have indicated that no more messages will be transmitted and the
     * connection has been successfully released. No further calls to this listener will be made.
     */
    public void onClosed(WebSocket webSocket, int code, String reason) {
        Log.d(TAG, "Closed socket with code " + code + " and reason: " + reason);
    }

    /**
     * Invoked when a web socket has been closed due to an error reading from or writing to the
     * network. Both outgoing and incoming messages may have been lost. No further calls to this
     * listener will be made.
     */
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        Log.d(TAG, "Error: " + t.getMessage());
    }
}
