package sublimate.com.sublimate.json;

public class ResetEvent extends WebSocketEvent {
    public static final String EVENT_TYPE = "RESET";

    public ResetEvent() {
        this.type = EVENT_TYPE;
    }
}
