package listener;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;

import event.Event;
import util.DebugPrint;

public abstract class Listener {

	public static enum PriorityValue{

		HIGHEST			((byte)0),
		HIGH			((byte)1),
		MEDIUM_HIGH		((byte)2),
		MEDIUM			((byte)3),
		MEDIUM_LOW		((byte)4),
		LOW				((byte)5),
		LOWEST			((byte)(-1));

		private final byte val;

		PriorityValue(byte priority){
			val=priority;
		}

		public byte value(){
			return val;
		}
	}

	private int priority = -1;//lower priority integer means a higher priority. 0 is the highest. (-1 is default and treated as lowest priority)

	public void setPriority(int priority){
		this.priority=priority;
	}

	public int getPriority(){
		return priority;
	}

	public void unregister(){
		listeners.remove(this);
	}

	public void register(int priority){
		this.priority=priority;
		Listener.register(this);
	}

	public void register(){
		Listener.register(this);
	}

	private static ArrayList<Listener> listeners = new ArrayList<Listener>();

	public abstract void listen(Event e);//return is whether to cancel or not

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface
	IgnoreCancelled{
	}

	public static void unregister(Listener listener){
		listeners.remove(listener);
	}

	public static void register(Listener listener, int priority){
		listener.priority = priority;
		register(listener);
	}

	public static void register(Listener listener){
		DebugPrint.printlnAdd("registering a listener:");
		try{
			for (Annotation a :listener.getClass().getMethod("listen", Event.class).getAnnotations())
				DebugPrint.println(a.annotationType().getTypeName());
		}catch(Exception e){
			e.printStackTrace();
		}
		DebugPrint.subLvl();

		if (listener.getPriority() == -1){
			listeners.add(listener);
		}else{
			listeners.add(Math.min(Math.max(listener.getPriority(),0),listeners.size()),listener);
		}
	}

	public static ArrayList<Listener> getListeners(){
		return listeners;
	}

	//TODO test
	public static void reorganizePriority(){//just in case, guessing that the -1's wont work without reorganization
		DebugPrint.printlnAdd("Attempting to reorganize listener priority...");
		try{
			int counter = 0;
			while (true){
				counter++;
				if (counter >= listeners.size())
					break;
				if (counter==0){
					continue;
				}
				if ((listeners.get(counter).getPriority()<listeners.get(counter-1).getPriority()) || (listeners.get(counter-1).getPriority() == -1 && listeners.get(counter).getPriority() != -1)){
					Listener temp = listeners.get(counter);
					listeners.remove(counter);
					listeners.add(counter, listeners.get(counter-1));
					listeners.add(counter-1,temp);
					counter-=2;
				}
			}
			DebugPrint.println("done!");
		}finally{
			DebugPrint.subLvl();
		}
	}
}
