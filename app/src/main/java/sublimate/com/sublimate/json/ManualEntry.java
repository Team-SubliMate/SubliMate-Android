package sublimate.com.sublimate.json;

import com.google.gson.annotations.SerializedName;

public class ManualEntry {
    private static final String EVENT_TYPE = "MANUAL_ENTRY";
    private static final int FRIDGE_ID = 1;

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
