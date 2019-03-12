package sublimate.com.sublimate.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import sublimate.com.sublimate.PresenterContract;
import sublimate.com.sublimate.R;
import sublimate.com.sublimate.json.InventoryItem;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryItemViewHolder> {

    private Context context;
    private PresenterContract presenter;

    private List<InventoryItem> inventoryItems;

    public InventoryAdapter(Context context, PresenterContract presenter) {
        super();

        this.context = context;
        this.presenter = presenter;
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
        final InventoryItem item = inventoryItems.get(position);

        holder.setName(item.getName());
        holder.setQuantity(item.getQuantity());
        holder.setImage(item.getImageUrl());

        holder.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("Remove Item")
                        .setMessage("Do you really want to remove this item?")
                        .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                presenter.removeItem(item.getItemId());
                            }
                        }).show();
                return true;
            }
        });
    }

    @Override
    public void onViewRecycled(@NonNull InventoryItemViewHolder holder) {
        super.onViewRecycled(holder);
        holder.clearImage();
    }

    @Override
    public int getItemCount() {
        return inventoryItems.size();
    }

    public int findItemById(int id) {
        for (int i = 0; i < inventoryItems.size(); i++) {
            InventoryItem item = inventoryItems.get(i);

            if (item.getItemId() == id) {
                return i;
            }
        }

        return -1;
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

    public void removeItemById(int id) {
        int itemPosition = findItemById(id);
        InventoryItem removed = inventoryItems.remove(itemPosition);

        if (removed != null) {
            notifyItemRemoved(itemPosition);
        }
    }

    // TODO: REMOVE
    public InventoryItem getItemById(int id) {
        for (int i = 0; i < inventoryItems.size(); i++) {
            InventoryItem item = inventoryItems.get(i);

            if (item.getItemId() == id) {
                return item;
            }
        }

        return null;
    }
}
