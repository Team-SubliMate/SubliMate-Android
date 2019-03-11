package sublimate.com.sublimate.json;

import com.google.gson.annotations.SerializedName;

public class RemoveItemEvent extends WebSocketEvent {
    public static final String ITEM_REMOVED = "ITEM_REMOVED";

    @SerializedName("value")
    private int itemId;

    public RemoveItemEvent(int itemId) {
        this.type = ITEM_REMOVED;
        this.itemId = itemId;
    }

    public int getItemId() {
        return this.itemId;
    }
}
