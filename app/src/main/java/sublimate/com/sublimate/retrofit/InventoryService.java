package sublimate.com.sublimate.retrofit;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sublimate.com.sublimate.json.InventoryServiceResponse;

public class InventoryService {
    private static int FRIDGE_ID = 1;

    private static InventoryService inventoryService;

    private static String BASE_URL = "https://httpbin.org";

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
        Call<InventoryServiceResponse> call = inventoryService.get();

        return call;
    }
}
