package event;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import entity.Entity;

public class CollisionEvent implements DestroyableEvent{
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface Listen{
	}

	private Entity a;
	private Entity d;
	private boolean cancel = false;
	private boolean destroy = false;
	private MovementEvent cause;

	public CollisionEvent(Entity agress, Entity defend, MovementEvent cause){
		a=agress;
		d=defend;
		this.cause=cause;
	}

	public Class<? extends Annotation> getAnnotation(){
		return Listen.class;
	}

	public Entity getAgressor(){
		return a;
	}

	public Entity getDefender(){
		return d;
	}

	public void setDefender(Entity e){
		d=e;
	}

	public MovementEvent getCause(){
		return cause;
	}
	
	public boolean isNew(){
		return !(a.getTouching().contains(d)||a.getTouching().contains(a));
	}

	@Override
	public boolean isCancelled(){
		return cancel || destroy;
	}

	@Override
	public void setCancelled(boolean b){
		cancel = b;
	}

	@Override
	public String getName(){
		return "EVENT_COLLISION";
	}

	@Override
	public void destroyEvent(){
		destroy = true;
	}

	@Override
	public boolean isDestroyed() {
		return destroy;
	}
}
