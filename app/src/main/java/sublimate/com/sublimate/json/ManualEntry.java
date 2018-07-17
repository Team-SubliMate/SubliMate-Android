package sublimate.com.sublimate.json;

import com.google.gson.annotations.SerializedName;

public class ManualEntry {
    private String EVENT_TYPE = "MANUAL_ENTRY";

    @SerializedName("type")
    private String type;

    @SerializedName("value")
    private InventoryItem item;

    public ManualEntry() {
        // Empty constructor for serialization purposes (DO NOT REMOVE)
    }

    public ManualEntry(InventoryItem item) {
        this.type = EVENT_TYPE;
        this.item = item;
    }
}
