package sublimate.com.sublimate;

import sublimate.com.sublimate.json.InventoryItem;

public interface ViewContract {
    void showToast(String message);
    void addInventoryItem(InventoryItem item);
    void removeInventoryItem(int itemId);
    void updateInventoryItem(int itemId, int quantity);
    void showTieBreakerDialog(int[] itemIds);
    void showItemDetails(InventoryItem item);
}
