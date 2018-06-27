package sublimate.com.sublimate.json;

// TODO: Make serializable for gson
public class InventoryItem {
    private String itemName;

    // no-args constructor
    public InventoryItem() {

    }

    public InventoryItem(String itemName) {
        this.itemName = itemName;
    }

    public String getName() {
        return this.itemName;
    }
}
