package sublimate.com.sublimate.json;

import com.google.gson.annotations.SerializedName;

public class TieBreakerEvent extends WebSocketEvent {
    public static final String WHICH_ITEM_REMOVED = "WHICH_ITEM_REMOVED";

    @SerializedName("value")
    private int[] itemIds;

    public int[] getItemIds() {
        return this.itemIds;
    }
}
