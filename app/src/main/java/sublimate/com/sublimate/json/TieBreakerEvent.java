package sublimate.com.sublimate.json;

import com.google.gson.annotations.SerializedName;

public class TieBreakerEvent extends WebSocketEvent {
    public static final String EVENT_TYPE = "WHICH_ITEM_REMOVED";

    @SerializedName("value")
    private int[] itemIds;

    public int[] getItemIds() {
        return this.itemIds;
    }
}
