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

    @SerializedName("imgurl")
    private String imageUrl;

    @SerializedName("bestBefore")
    private String expiration;

    public InventoryItem(String name) {
        this.name = name;
    }

    public InventoryItem(int itemId, String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public InventoryItem(int itemId, String name, int quantity, int weight) {
        this.name = name;
        this.quantity = quantity;
        this.weight = weight;
    }

    public InventoryItem(int itemId, String name, int quantity, int weight, String imageUrl) {
        this.name = name;
        this.quantity = quantity;
        this.weight = weight;
        this.imageUrl = imageUrl;
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

    public int getItemId() {
        return this.itemId;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getExpiration() {
        return this.expiration;
    }
}
