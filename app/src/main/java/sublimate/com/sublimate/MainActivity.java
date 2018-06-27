package sublimate.com.sublimate;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import sublimate.com.sublimate.json.InventoryItem;
import sublimate.com.sublimate.view.InventoryAdapter;

public class MainActivity extends AppCompatActivity {

    private RecyclerView inventoryRecyclerView;
    private InventoryAdapter inventoryAdapter;

    private ProgressBar loadingSpinner;
    private TextView errorTextView;

    private FloatingActionButton inventoryActionButton;
    private Toast toast;

    private Dialog manualAddDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inventoryRecyclerView = (RecyclerView) findViewById(R.id.rv_inventory);
        loadingSpinner = (ProgressBar) findViewById(R.id.pb_main_loader);
        errorTextView = (TextView) findViewById(R.id.tv_error_message);
        inventoryActionButton = (FloatingActionButton) findViewById(R.id.inventory_action_button);

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

    private void showContent() {
        hideLoading();
        hideError();
        inventoryRecyclerView.setVisibility(View.VISIBLE);
    }

    private void hideContent() {
        inventoryRecyclerView.setVisibility(View.GONE);
    }

    private void showLoading() {
        loadingSpinner.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        loadingSpinner.setVisibility(View.GONE);
    }

    private void showError() {
        hideContent();
        hideLoading();
        errorTextView.setVisibility(View.VISIBLE);
    }

    private void hideError() {
        errorTextView.setVisibility(View.GONE);
    }

    private void showToast(String text) {
        toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
        toast.show();
    }

    private void showManualAddDialog() {
        if (manualAddDialog == null) {
            manualAddDialog = new Dialog(this);
            manualAddDialog.setContentView(R.layout.manual_add_dialog);
            manualAddDialog.setTitle(R.string.add_dialog_title);

            final EditText dialogEditText = (EditText) manualAddDialog.findViewById(R.id.et_inventory_item_name);
            dialogEditText.setText("Item " + (inventoryAdapter.getItemCount() + 1));

            // Set up the save button
            Button dialogButton = (Button) manualAddDialog.findViewById(R.id.button_add_item);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String itemText = dialogEditText.getText().toString();
                    InventoryItem item = new InventoryItem(itemText);

                    inventoryAdapter.addInventoryItem(item);
                    showToast("The item \"" + item.getName() + "\" has been added.");
                    manualAddDialog.dismiss();
                }
            });
        }

        final EditText dialogEditText = (EditText) manualAddDialog.findViewById(R.id.et_inventory_item_name);
        dialogEditText.setText("Item " + (inventoryAdapter.getItemCount() + 1));
        manualAddDialog.show();
    }

    // TESTING
    private void mock() {
        // showContent();

        /*for (int i = 1; i <= 9; i++) {
            InventoryItem item = new InventoryItem("Item " + i);
            inventoryAdapter.addInventoryItem(item);
        }*/

        inventoryActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showContent();
                showManualAddDialog();
            }
        });
    }
}