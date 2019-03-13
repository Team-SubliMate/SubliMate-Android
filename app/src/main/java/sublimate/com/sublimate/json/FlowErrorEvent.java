package sublimate.com.sublimate.json;

import com.google.gson.annotations.SerializedName;

public class FlowErrorEvent extends WebSocketEvent {
    public static final String EVENT_TYPE = "FLOW_ERROR";

    @SerializedName("message")
    private String message;

    public String getMessage() {
        return message;
    }
}
