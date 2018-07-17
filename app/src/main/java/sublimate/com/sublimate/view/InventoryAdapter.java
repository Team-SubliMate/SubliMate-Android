package sublimate.com.sublimate.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import sublimate.com.sublimate.R;
import sublimate.com.sublimate.json.InventoryItem;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryItemViewHolder> {

    private Context context;
    private List<InventoryItem> inventoryItems;

    public InventoryAdapter(Context context) {
        super();

        this.context = context;
        inventoryItems = new ArrayList<>();
    }

    @NonNull
    @Override
    public InventoryItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View inventoryView = inflater.inflate(R.layout.inventory_item, parent, false);
        InventoryItemViewHolder inventoryViewHolder = new InventoryItemViewHolder(inventoryView, context, inventoryItems);

        return inventoryViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull InventoryItemViewHolder holder, int position) {
        InventoryItem item = inventoryItems.get(position);
        String itemName = item.getName();

        holder.setText(itemName);
    }

    @Override
    public int getItemCount() {
        return inventoryItems.size();
    }

    public void setInventoryItems(List<InventoryItem> items) {
        inventoryItems.addAll(items);
        notifyDataSetChanged();
    }

    public void addInventoryItem(InventoryItem item) {
        inventoryItems.add(item);
        notifyItemInserted(inventoryItems.size() - 1);
    }

    public void resetInventoryItems() {
        inventoryItems.clear();
        notifyDataSetChanged();
    }
}
