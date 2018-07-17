package sublimate.com.sublimate.json;

import com.google.gson.annotations.SerializedName;

public class InventoryItem {
    @SerializedName("shelfid")
    private int shelfId;

    @SerializedName("itemid")
    private int itemId;

    @SerializedName("weight")
    private double weight;

    @SerializedName("product")
    private String name;

    @SerializedName("quantity")
    private int quantity;

    public InventoryItem() {
        // Empty constructor for serialization purposes (DO NOT REMOVE)
    }

    public InventoryItem(String name) {
        this.name = name;
    }

    public InventoryItem(String name, double weight, int quantity) {
        this.name = name;
        this.weight = weight;
        this.quantity = quantity;
    }

    public String getName() {
        return this.name;
    }

    public double getWeight() {
        return this.weight;
    }

    public int getQuantity() {
        return this.quantity;
    }
}
