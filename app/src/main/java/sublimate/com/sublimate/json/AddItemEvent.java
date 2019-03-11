package sublimate.com.sublimate.json;

import com.google.gson.annotations.SerializedName;

public class AddItemEvent extends WebSocketEvent {
    public static final String ITEM_ADDED = "ITEM_ADDED";

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
