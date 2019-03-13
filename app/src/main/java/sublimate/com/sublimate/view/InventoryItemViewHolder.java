package sublimate.com.sublimate.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import sublimate.com.sublimate.R;
import sublimate.com.sublimate.json.InventoryItem;

public class InventoryItemViewHolder extends RecyclerView.ViewHolder {

    private Context context;
    List<InventoryItem> inventoryItems;

    private ViewGroup inventoryItemLayout;
    private ImageView itemImageIV;
    private TextView itemNameTV;
    private TextView itemQuantityTV;

    public InventoryItemViewHolder(@NonNull View itemView, Context context, List<InventoryItem> inventoryItems) {
        super(itemView);

        this.context = context;
        this.inventoryItems = inventoryItems;

        inventoryItemLayout = itemView.findViewById(R.id.layout_inventory_item);
        itemImageIV = inventoryItemLayout.findViewById(R.id.iv_inventory_item_image);
        itemNameTV = inventoryItemLayout.findViewById(R.id.tv_inventory_item_text);
        itemQuantityTV = inventoryItemLayout.findViewById(R.id.tv_inventory_item_quantity);
    }

    public void setName(String text) {
        if (text.length() > 20) {
            text = text.substring(0, 17) + "...";
        }
        itemNameTV.setText(text);
    }

    public void setQuantity(int quantity) {
        itemQuantityTV.setText(String.valueOf(quantity));
    }

    public void setImage(String url) {
        if (url == null) {
            Glide.with(context)
                    .load(R.drawable.default_image)
                    .fitCenter()
                    .into(itemImageIV);
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

    public void setOnLongClickListener(View.OnLongClickListener listener) {
        this.inventoryItemLayout.setOnLongClickListener(listener);
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.inventoryItemLayout.setOnClickListener(listener);
    }
}
