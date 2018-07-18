package sublimate.com.sublimate.json;

import com.google.gson.annotations.SerializedName;

public class TieBreakerEvent extends WebSocketEvent {

    @SerializedName("value")
    private int[] itemIds;

    public int[] getItemIds() {
        return this.itemIds;
    }
}
