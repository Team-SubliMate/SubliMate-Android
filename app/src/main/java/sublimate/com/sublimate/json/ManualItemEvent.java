package sublimate.com.sublimate.json;

import com.google.gson.annotations.SerializedName;

public class ManualItemEvent extends WebSocketEvent{
    private static final String EVENT_TYPE = "MANUAL_ENTRY";

    @SerializedName("value")
    private InventoryItem item;

    public ManualItemEvent() {
        // Empty constructor for serialization purposes (DO NOT REMOVE)
    }

    public ManualItemEvent(InventoryItem item) {
        this.type = EVENT_TYPE;
        this.item = item;
    }
}
