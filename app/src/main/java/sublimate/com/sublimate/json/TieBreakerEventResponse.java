package sublimate.com.sublimate.json;

import com.google.gson.annotations.SerializedName;

public class TieBreakerEventResponse extends WebSocketEvent {
    private static final String EVENT_TYPE = "THIS_ITEM_REMOVED";

    @SerializedName("value")
    private int itemId;

    public TieBreakerEventResponse() {
        // Empty constructor for serialization purposes (DO NOT REMOVE)
    }

    public TieBreakerEventResponse(int itemId) {
        this.type = EVENT_TYPE;
        this.itemId = itemId;
    }
}
