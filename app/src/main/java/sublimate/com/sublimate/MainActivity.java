package sublimate.com.sublimate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;

import sublimate.com.sublimate.json.InventoryItem;
import sublimate.com.sublimate.view.InventoryAdapter;

public class MainActivity extends AppCompatActivity {

    private RecyclerView inventoryRecyclerView;
    private InventoryAdapter inventoryAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inventoryRecyclerView = (RecyclerView) findViewById(R.id.rv_inventory);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        inventoryAdapter = new InventoryAdapter(this);

        inventoryRecyclerView.setHasFixedSize(true);
        inventoryRecyclerView.setLayoutManager(layoutManager);
        inventoryRecyclerView.setAdapter(inventoryAdapter);


        mock(); // Testing!!!!!!!

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    // TESTING
    private void mock() {
        for (int i = 1; i <= 20; i++) {
            InventoryItem item = new InventoryItem("Item " + i);
            inventoryAdapter.addInventoryItem(item);
        }
    }
}