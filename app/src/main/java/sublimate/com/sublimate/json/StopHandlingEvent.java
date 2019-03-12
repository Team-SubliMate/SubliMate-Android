package sublimate.com.sublimate.json;

public class StopHandlingEvent extends WebSocketEvent {
    public static final String STOP_HANDLING = "STOP_HANDLING";

    public StopHandlingEvent() {
        this.type = STOP_HANDLING;
    }
}
