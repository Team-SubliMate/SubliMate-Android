package sublimate.com.sublimate.network;

import sublimate.com.sublimate.json.AddItemEvent;
import sublimate.com.sublimate.json.RemoveItemEvent;
import sublimate.com.sublimate.json.TieBreakerEvent;
import sublimate.com.sublimate.view.InventoryAdapter;

public class WebSocketEventHandler {
    private InventoryAdapter adapter;

    public WebSocketEventHandler(InventoryAdapter adapter) {
        this.adapter = adapter;
    }

    public void onRemoveItemEvent(RemoveItemEvent event) {
        adapter.removeItemById(event.getItemId());
    }

    // TODO: UI STUFF Returns itemId of item to remove
    public int onTieBreakerEvent(TieBreakerEvent event) {
        int mockRemove = event.getItemIds()[0];
        adapter.removeItemById(mockRemove);

        return mockRemove;
    }

    public void onAddItemEvent(AddItemEvent event) {
        adapter.addInventoryItem(event.getItem());
    }
}
