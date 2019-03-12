package sublimate.com.sublimate.json;

import com.google.gson.annotations.SerializedName;

public class AddItemEvent extends WebSocketEvent {
    public static final String EVENT_TYPE = "ITEM_ADDED";

    @SerializedName("value")
    private InventoryItem item;

    public AddItemEvent(InventoryItem item) {
        this.item = item;
    }

    public InventoryItem getItem() {
        return item;
    }
}
