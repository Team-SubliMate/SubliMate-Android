package sublimate.com.sublimate.json;

import com.google.gson.annotations.SerializedName;

public class UpdateItemEvent extends WebSocketEvent {
    public static final String EVENT_TYPE = "ITEM_UPDATED";

    @SerializedName("itemid")
    private int itemId;

    @SerializedName("quantity")
    private int quantity;

    @SerializedName("weight")
    private double weight;

    public int getItemId() {
        return itemId;
    }

    public int getItemQuantity() {
        return quantity;
    }

    public double getItemWeight() {
        return weight;
    }
}
