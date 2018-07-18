package sublimate.com.sublimate.json;

import com.google.gson.annotations.SerializedName;

public class RemoveItemEvent extends WebSocketEvent {
    @SerializedName("value")
    private int itemId;

    public int getItemId() {
        return this.itemId;
    }
}
