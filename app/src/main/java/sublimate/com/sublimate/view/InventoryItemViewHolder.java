package sublimate.com.sublimate.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import sublimate.com.sublimate.R;
import sublimate.com.sublimate.json.InventoryItem;

public class InventoryItemViewHolder extends RecyclerView.ViewHolder {

    private Context context;
    List<InventoryItem> inventoryItems;

    private LinearLayout inventoryItemLayout;
    private TextView itemTextView;

    public InventoryItemViewHolder(@NonNull View itemView, Context context, List<InventoryItem> inventoryItems) {
        super(itemView);

        this.context = context;
        this.inventoryItems = inventoryItems;

        inventoryItemLayout = itemView.findViewById(R.id.ll_inventory_item);
        itemTextView = inventoryItemLayout.findViewById(R.id.tv_inventory_item_text);
    }

    public void setText(String text) {
        itemTextView.setText(text);
    }
}
