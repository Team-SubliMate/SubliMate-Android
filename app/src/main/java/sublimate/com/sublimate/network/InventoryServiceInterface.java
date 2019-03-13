package sublimate.com.sublimate.network;

import retrofit2.Call;
import retrofit2.http.GET;
import sublimate.com.sublimate.json.InventoryServiceResponse;

public interface InventoryServiceInterface {
    @GET("api/inventory")
    Call<InventoryServiceResponse> getInventory();
}
