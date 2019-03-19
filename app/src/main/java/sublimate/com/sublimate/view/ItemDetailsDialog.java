package sublimate.com.sublimate.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import sublimate.com.sublimate.R;
import sublimate.com.sublimate.json.InventoryItem;

public class ItemDetailsDialog extends Dialog {
    public ItemDetailsDialog(@NonNull Context context, InventoryItem item) {
        super(context);

        setContentView(R.layout.item_details_dialog);

        // Find view items
        ImageView itemImageIV = findViewById(R.id.iv_inventory_item_image);
        TextView itemTitleTV = findViewById(R.id.tv_inventory_item_name);
        TextView itemWeightTV = findViewById(R.id.tv_inventory_item_weight);
        TextView itemQuantityTV = findViewById(R.id.tv_inventory_item_quantity);
        TextView itemExpirationTV = findViewById(R.id.tv_inventory_item_expiration);

        // Set values
        if (item.getImageUrl() != null) {
            Glide.with(context)
                    .load(item.getImageUrl())
                    .centerInside()
                    .into(itemImageIV);
        }
        String titleText = item.getName() + "  ";
        String weightText = context.getString(R.string.item_details_weight, (int) item.getWeight());
        String quantityText = context.getString(R.string.item_details_quantity, item.getQuantity());
        String expirationText = context.getString(R.string.item_details_expiration, item.getExpiration());

        itemTitleTV.setText(titleText);
        itemWeightTV.setText(weightText);
        itemQuantityTV.setText(quantityText);

        if (item.getExpiration() != null) {
            itemExpirationTV.setText(expirationText);
        }
    }
}
