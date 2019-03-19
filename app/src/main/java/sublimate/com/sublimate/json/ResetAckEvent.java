package sublimate.com.sublimate.json;

public class ResetAckEvent extends WebSocketEvent {
    public static final String EVENT_TYPE = "RESET_ACK";

    public ResetAckEvent(int itemId) {
        this.type = EVENT_TYPE;
    }
}
