package sublimate.com.sublimate.network;

import sublimate.com.sublimate.json.AddItemEvent;
import sublimate.com.sublimate.json.FlowErrorEvent;
import sublimate.com.sublimate.json.RemoveItemEvent;
import sublimate.com.sublimate.json.TieBreakerEvent;
import sublimate.com.sublimate.json.UpdateItemEvent;

public interface WebSocketEventHandlerContract {
    void onAddItemEvent(AddItemEvent event);
    void onRemoveItemEvent(RemoveItemEvent event);
    void onUpdateItemEvent(UpdateItemEvent event);
    void onTieBreakerEvent(TieBreakerEvent event);
    void onFlowErrorEvent(FlowErrorEvent event);
}
