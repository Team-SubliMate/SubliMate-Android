package sublimate.com.sublimate.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import sublimate.com.sublimate.json.InventoryServiceResponse;

public interface InventoryServiceInterface {
    @GET("api/{fridgeId}/inventoryItems.json")
    Call<InventoryServiceResponse> getItems(@Path("fridgeId") int fridgeId);

    @GET("get")
    Call<InventoryServiceResponse> get();
}
