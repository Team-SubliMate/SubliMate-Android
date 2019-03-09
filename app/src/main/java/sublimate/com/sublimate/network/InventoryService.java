package sublimate.com.sublimate.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sublimate.com.sublimate.json.InventoryServiceResponse;
import sublimate.com.sublimate.view.PreferencesActivity;

import static sublimate.com.sublimate.view.PreferencesActivity.HTTP_URL;

public class InventoryService {
    // This doesn't work on the Android Emulator because it'll refer to the loopback address on the EMULATOR. Use http://10.0.2.2:3000 on the emulator instead.

    private static InventoryService inventoryService;

    private Retrofit retrofit;

    private InventoryService(Context context) {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        retrofit = new Retrofit.Builder()
                .baseUrl(prefs.getString(PreferencesActivity.HTTP_ADDRESS, HTTP_URL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static InventoryService getInventoryService(Context context) {
        if (inventoryService == null) {
            inventoryService = new InventoryService(context);
        }

        return inventoryService;
    }

    public Call<InventoryServiceResponse> getInventoryCall() throws IOException {
        InventoryServiceInterface inventoryService = retrofit.create(InventoryServiceInterface.class);
        Call<InventoryServiceResponse> call = inventoryService.getInventory();

        return call;
    }
}
