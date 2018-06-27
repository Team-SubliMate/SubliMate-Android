package sublimate.com.sublimate.json;

import com.google.gson.annotations.SerializedName;

public class InventoryItem {
    @SerializedName("name")
    private String itemName;

    public InventoryItem() {
        // no args
    }

    public InventoryItem(String itemName) {
        this.itemName = itemName;
    }

    public String getName() {
        return this.itemName;
    }
}
