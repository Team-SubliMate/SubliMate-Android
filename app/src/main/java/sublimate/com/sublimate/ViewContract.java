package sublimate.com.sublimate;

import sublimate.com.sublimate.json.InventoryItem;

public interface ViewContract {
    void addInventoryItem(InventoryItem item);
    void removeInventoryItem(int itemId);
    void resetInventory();
    void updateInventoryItem(int itemId, int quantity);

    void showTieBreakerDialog(int[] itemIds);
    void showItemDetails(InventoryItem item);
    void showFlowErrorDialog(String message);
    void showManualAddWait();
    void hideManualAddWait();
    void showToast(String message);
}
