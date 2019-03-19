package sublimate.com.sublimate;
import android.util.Log;

import com.google.gson.Gson;

import okhttp3.WebSocket;
import sublimate.com.sublimate.json.AddItemEvent;
import sublimate.com.sublimate.json.FlowErrorEvent;
import sublimate.com.sublimate.json.InventoryItem;
import sublimate.com.sublimate.json.ManualItemEvent;
import sublimate.com.sublimate.json.RemoveItemEvent;
import sublimate.com.sublimate.json.ResetAckEvent;
import sublimate.com.sublimate.json.ResetEvent;
import sublimate.com.sublimate.json.StartHandlingEvent;
import sublimate.com.sublimate.json.StopHandlingEvent;
import sublimate.com.sublimate.json.TieBreakerEvent;
import sublimate.com.sublimate.json.TieBreakerEventResponse;
import sublimate.com.sublimate.json.UpdateItemEvent;
import sublimate.com.sublimate.network.WebSocketEventHandlerContract;
import sublimate.com.sublimate.network.WebSocketEventListener;

public class Presenter implements PresenterContract, WebSocketEventHandlerContract {
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
        // It's tie breaker response because backend is lazy
        TieBreakerEventResponse removeItemEvent = new TieBreakerEventResponse(itemId);
        String itemJson = gson.toJson(removeItemEvent, TieBreakerEventResponse.class);
        sendEvent(itemJson);
    }

    @Override
    public void sendResetEvent() {
        ResetEvent resetEvent = new ResetEvent();
        String itemJson = gson.toJson(resetEvent, ResetEvent.class);
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
    public void sendTieBreakerResponse(int itemId) {
        Gson gson = new Gson();
        TieBreakerEventResponse eventResponse = new TieBreakerEventResponse(itemId);
        String response =  gson.toJson(eventResponse, TieBreakerEventResponse.class);
        webSocket.send(response);

        view.hideTieBreakerDialog();
    }

    @Override
    public void showItemDetails(InventoryItem item) {
        view.showItemDetails(item);
    }

    @Override
    public void showManualAddWait() {
        view.showManualAddWait();
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

    @Override
    public void onAddItemEvent(AddItemEvent event) {
        view.addInventoryItem(event.getItem());
    }

    @Override
    public void onRemoveItemEvent(RemoveItemEvent event) {
        view.removeInventoryItem(event.getItemId());
    }

    @Override
    public void onResetAckEvent(ResetAckEvent event) {
        view.resetInventory();
    }

    @Override
    public void onUpdateItemEvent(UpdateItemEvent event) {
        view.updateInventoryItem(event.getItemId(), event.getItemQuantity(), event.getItemWeight());
    }

    @Override
    public void onTieBreakerEvent(final TieBreakerEvent event) {
        int[] itemIds = event.getItemIds();
        view.showTieBreakerDialog(itemIds);
    }

    @Override
    public void onFlowErrorEvent(FlowErrorEvent event) {
        String message = event.getMessage();
        view.showFlowErrorDialog(message);
    }
}
