package sublimate.com.sublimate.network;

import android.app.Activity;
import android.util.Log;

import com.google.gson.Gson;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import sublimate.com.sublimate.json.AddItemEvent;
import sublimate.com.sublimate.json.FlowErrorEvent;
import sublimate.com.sublimate.json.RemoveItemEvent;
import sublimate.com.sublimate.json.ResetAckEvent;
import sublimate.com.sublimate.json.TieBreakerEvent;
import sublimate.com.sublimate.json.UpdateItemEvent;
import sublimate.com.sublimate.json.WebSocketEvent;

public class WebSocketEventListener extends WebSocketListener {
    public static final int NORMAL_CLOSURE_STATUS = 1000;

    public static final String TAG = WebSocketEventListener.class.getSimpleName();

    private Activity view;
    private WebSocketEventHandlerContract handler;

    public WebSocketEventListener(Activity view, WebSocketEventHandlerContract handler) {
        this.view = view;
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
    public void onMessage(WebSocket webSocket, final String text) {
        view.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Gson gson = new Gson();

                WebSocketEvent event = gson.fromJson(text, WebSocketEvent.class);
                switch (event.getType()) {
                    case AddItemEvent.EVENT_TYPE:
                        AddItemEvent addItemEvent = gson.fromJson(text, AddItemEvent.class);
                        handler.onAddItemEvent(addItemEvent);
                        break;
                    case RemoveItemEvent.EVENT_TYPE:
                        RemoveItemEvent removeItemEvent = gson.fromJson(text, RemoveItemEvent.class);
                        handler.onRemoveItemEvent(removeItemEvent);
                        break;
                    case ResetAckEvent.EVENT_TYPE:
                        ResetAckEvent resetAckEvent = gson.fromJson(text, ResetAckEvent.class);
                        handler.onResetAckEvent(resetAckEvent);
                        break;
                    case UpdateItemEvent.EVENT_TYPE:
                        UpdateItemEvent updateItemEvent = gson.fromJson(text, UpdateItemEvent.class);
                        handler.onUpdateItemEvent(updateItemEvent);
                        break;
                    case TieBreakerEvent.EVENT_TYPE:
                        TieBreakerEvent tieBreakerEvent = gson.fromJson(text, TieBreakerEvent.class);
                        handler.onTieBreakerEvent(tieBreakerEvent);
                        break;
                    case FlowErrorEvent.EVENT_TYPE:
                        FlowErrorEvent flowErrorEvent = gson.fromJson(text, FlowErrorEvent.class);
                        handler.onFlowErrorEvent(flowErrorEvent);
                    default:
                        break;
                }
            }
        });


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
