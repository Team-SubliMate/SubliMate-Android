package sublimate.com.sublimate.json;

import com.google.gson.annotations.SerializedName;

public class WebSocketEvent {
    @SerializedName("type")
    String type;

    public WebSocketEvent() {
        // Empty constructor for serialization purposes (DO NOT REMOVE)
    }

    public String getType() {
        return this.type;
    }
}
