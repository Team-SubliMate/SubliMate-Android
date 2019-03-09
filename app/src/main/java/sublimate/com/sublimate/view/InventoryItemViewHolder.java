package sublimate.com.sublimate.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import sublimate.com.sublimate.R;
import sublimate.com.sublimate.json.InventoryItem;

public class InventoryItemViewHolder extends RecyclerView.ViewHolder {

    private Context context;
    List<InventoryItem> inventoryItems;

    private LinearLayout inventoryItemLayout;
    private ImageView itemImageIV;
    private TextView itemNameTV;
    private TextView itemQuantityTV;

    public InventoryItemViewHolder(@NonNull View itemView, Context context, List<InventoryItem> inventoryItems) {
        super(itemView);

        this.context = context;
        this.inventoryItems = inventoryItems;

        inventoryItemLayout = itemView.findViewById(R.id.ll_inventory_item);
        itemImageIV = inventoryItemLayout.findViewById(R.id.iv_inventory_item_image);
        itemNameTV = inventoryItemLayout.findViewById(R.id.tv_inventory_item_text);
        itemQuantityTV = inventoryItemLayout.findViewById(R.id.tv_inventory_item_quantity);
    }

    public void setName(String text) {
        itemNameTV.setText(text);
    }

    public void setQuantity(int quantity) {
        itemQuantityTV.setText(String.valueOf(quantity));
    }

    public void setImage(String url) {
        if (url == null) {
            itemImageIV.setImageResource(R.drawable.default_image);
            return;
        }

        Glide.with(context)
                .load(url)
                .fitCenter()
                .into(itemImageIV);
    }

    public void clearImage() {
        Glide.with(context).clear(itemImageIV);
    }
}
