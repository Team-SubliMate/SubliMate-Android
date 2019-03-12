package sublimate.com.sublimate.json;

public class StartHandlingEvent extends WebSocketEvent {
    public static final String START_HANDLING = "START_HANDLING";

    public StartHandlingEvent() {
        this.type = START_HANDLING;
    }
}
