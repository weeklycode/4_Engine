package event;

import java.lang.reflect.Method;

import listener.Listener;
import listener.Listener.IgnoreCancelled;
import util.DebugPrint;

public class EventCaller {
	public static void callEvent(Event e){
		try{
			Method listen;
			for (Listener l : Listener.getListeners()){
				listen = l.getClass().getMethod("listen", Event.class);
				if (e instanceof CancellableEvent){
					if (e instanceof DestroyableEvent){
						if (((DestroyableEvent)e).isDestroyed())
							break;
					}
					if (((CancellableEvent)e).isCancelled() && !listen.isAnnotationPresent(IgnoreCancelled.class))
						continue;
				}
				if (listen.isAnnotationPresent(e.getAnnotation())){
					l.listen(e);
				}
			}
		}catch (NoSuchMethodException e1){
			DebugPrint.errln("error trying to call event + " + e.getName() + ":");
			e1.printStackTrace();
		}catch (SecurityException e2){
			DebugPrint.errln("error trying to call event + " + e.getName() + ":");
			e2.printStackTrace();
		}
	}
}
