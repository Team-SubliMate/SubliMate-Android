package sublimate.com.sublimate.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import sublimate.com.sublimate.PresenterContract;
import sublimate.com.sublimate.R;
import sublimate.com.sublimate.json.InventoryItem;

public class ManualAddDialog extends Dialog {
    private PresenterContract presenter;

    public ManualAddDialog(@NonNull Context context, final PresenterContract presenter) {
        super(context);

        this.presenter = presenter;

        setContentView(R.layout.manual_add_dialog);
        setTitle(R.string.add_dialog_title);

        final EditText dialogNameEditText = findViewById(R.id.et_inventory_item_name);
        final EditText dialogQuantityEditText = findViewById(R.id.et_inventory_item_quantity);
        final Button dialogButton = findViewById(R.id.button_add_item);

        // Set default values
        String defaultQuantity = "1";
        dialogQuantityEditText.setText(defaultQuantity);

        // Set up the save button
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create item from user entry
                String itemText = dialogNameEditText.getText().toString();
                int itemQuantity = Integer.parseInt(dialogQuantityEditText.getText().toString());
                InventoryItem item = new InventoryItem(42, itemText, itemQuantity);

                presenter.addItem(item);
                presenter.showManualAddWait();

                dialogNameEditText.setText("");
                dismiss();
            }
        });

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(editable)) {
                    dialogButton.setEnabled(false);
                } else {
                    dialogButton.setEnabled(true);
                }
            }
        };

        dialogNameEditText.addTextChangedListener(textWatcher);
        dialogQuantityEditText.addTextChangedListener(textWatcher);
    }
}
