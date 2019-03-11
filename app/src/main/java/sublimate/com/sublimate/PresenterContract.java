package sublimate.com.sublimate;

import sublimate.com.sublimate.json.InventoryItem;

public interface PresenterContract {
    void addItem(InventoryItem item);
    void removeItem(int itemId);
    void rearrangeFridge();

    void showToast(String message);
}
