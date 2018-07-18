package sublimate.com.sublimate.json;

import com.google.gson.annotations.SerializedName;

public class AddItemEvent extends WebSocketEvent {
    @SerializedName("value")
    private InventoryItem item;

    public AddItemEvent() {
        // Empty constructor for serialization purposes (DO NOT REMOVE)
    }

    public AddItemEvent(InventoryItem item) {
        this.item = item;
    }

    public InventoryItem getItem() {
        return item;
    }
}
