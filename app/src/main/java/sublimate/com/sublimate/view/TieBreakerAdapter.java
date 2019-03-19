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

public class TieBreakerAdapter extends RecyclerView.Adapter<TieBreakerViewHolder> {

    private Context context;
    private PresenterContract presenter;

    private List<InventoryItem> inventoryItems;

    public TieBreakerAdapter(Context context, PresenterContract presenter) {
        super();

        this.context = context;
        this.presenter = presenter;
        inventoryItems = new ArrayList<>();
    }

    @NonNull
    @Override
    public TieBreakerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View inventoryView = inflater.inflate(R.layout.inventory_item, parent, false);
        TieBreakerViewHolder viewHolder = new TieBreakerViewHolder(inventoryView, context, inventoryItems);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final TieBreakerViewHolder holder, int position) {
        final InventoryItem item = inventoryItems.get(position);

        holder.setName(item.getName());
        holder.setQuantity(item.getQuantity());
        holder.setImage(item.getImageUrl());

        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int removeId = inventoryItems.get(holder.getAdapterPosition()).getItemId();
                presenter.sendTieBreakerResponse(removeId);
            }
        });
    }

    @Override
    public void onViewRecycled(@NonNull TieBreakerViewHolder holder) {
        super.onViewRecycled(holder);
        holder.clearImage();
    }

    @Override
    public int getItemCount() {
        return inventoryItems.size();
    }

    public void setInventoryItems(List<InventoryItem> items) {
        inventoryItems.clear();
        inventoryItems.addAll(items);
        notifyDataSetChanged();
    }
}
