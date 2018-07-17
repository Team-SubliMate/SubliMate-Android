package sublimate.com.sublimate.network;

import android.util.Log;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class WebSocketEventListener extends WebSocketListener {
    public static final String WEBSOCKET_URL = "ws://10.0.2.2:8090";
    public static final int NORMAL_CLOSURE_STATUS = 1000;

    public static final String TAG = WebSocketEventListener.class.getSimpleName();

    public WebSocketEventListener() {
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
