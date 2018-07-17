package sublimate.com.sublimate.json;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InventoryServiceResponse {
    @SerializedName("items")
    private List<InventoryItem> items;

    public InventoryServiceResponse() {
        // Empty constructor for serialization purposes (DO NOT REMOVE)
    }

    public List<InventoryItem> getItems() {
        return this.items;
    }
}
