package sublimate.com.sublimate.view;

import android.app.Dialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sublimate.com.sublimate.PresenterContract;
import sublimate.com.sublimate.R;
import sublimate.com.sublimate.json.InventoryItem;

public class TieBreakerDialog extends Dialog {

    public TieBreakerDialog(@NonNull Context context, final PresenterContract presenter, List<InventoryItem> items) {
        super(context);

        setContentView(R.layout.tiebreaker_dialog);
        setTitle(R.string.tiebreaker_dialog_title);

        RecyclerView recyclerView = findViewById(R.id.rv_tiebreaker_items);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this.getContext(), 2);
        TieBreakerAdapter adapter = new TieBreakerAdapter(this.getContext(), presenter);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setInventoryItems(items);
    }

    @Override
    public void show() {
        super.show();
        MediaPlayer mp = MediaPlayer.create(this.getContext(), R.raw.tiebreaker);
        mp.start();
    }
}
