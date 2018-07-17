package sublimate.com.sublimate.network;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sublimate.com.sublimate.json.InventoryServiceResponse;

public class InventoryService {
    // This doesn't work on the Android Emulator because it'll refer to the loopback address on the EMULATOR. Use http://10.0.2.2:3000 on the emulator instead.
    private static final String BASE_URL = "http://10.0.2.2:3000";

    private static InventoryService inventoryService;

    private Retrofit retrofit;

    private InventoryService() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static InventoryService getInventoryService() {
        if (inventoryService == null) {
            inventoryService = new InventoryService();
        }

        return inventoryService;
    }

    public Call<InventoryServiceResponse> getInventoryCall() throws IOException {
        InventoryServiceInterface inventoryService = retrofit.create(InventoryServiceInterface.class);
        Call<InventoryServiceResponse> call = inventoryService.getInventory();

        return call;
    }
}
