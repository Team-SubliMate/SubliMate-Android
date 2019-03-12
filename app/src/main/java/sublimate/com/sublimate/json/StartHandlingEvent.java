package sublimate.com.sublimate.json;

public class StartHandlingEvent extends WebSocketEvent {
    public static final String EVENT_TYPE = "START_HANDLING";

    public StartHandlingEvent() {
        this.type = EVENT_TYPE;
    }
}
