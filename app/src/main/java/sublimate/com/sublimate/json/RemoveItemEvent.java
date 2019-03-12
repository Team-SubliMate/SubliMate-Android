package sublimate.com.sublimate.json;

import com.google.gson.annotations.SerializedName;

public class RemoveItemEvent extends WebSocketEvent {
    public static final String EVENT_TYPE = "ITEM_REMOVED";

    @SerializedName("value")
    private int itemId;

    public RemoveItemEvent(int itemId) {
        this.type = EVENT_TYPE;
        this.itemId = itemId;
    }

    public int getItemId() {
        return this.itemId;
    }
}
