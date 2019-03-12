package sublimate.com.sublimate.json;

public class StopHandlingEvent extends WebSocketEvent {
    public static final String EVENT_TYPE = "STOP_HANDLING";

    public StopHandlingEvent() {
        this.type = EVENT_TYPE;
    }
}
