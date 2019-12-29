package event;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

import core.Game;
import entity.Entity;

public class MovementEvent implements DestroyableEvent{

	boolean cancel = false;
	boolean destroy = false;

	private Entity e;
	private double[] move;//(x1,y1),(x2,y2)
	private List<CollisionEvent> subs;

	public MovementEvent(Entity mover, double fromX, double fromY, double toX, double toY, boolean checkCollisions){
		e=mover;
		move = new double[]{
				fromX,
				fromY,
				toX,
				toY
		};
		subs = new ArrayList<CollisionEvent>();
		if (checkCollisions){
			for (Entity e : Game.getInstance().getEntities()){
				if (e != mover){
					if (mover.isTouching(e,toX,toY)){
						CollisionEvent c = new CollisionEvent(mover,e,this);
						subs.add(c);
						EventCaller.callEvent(c);
					}
				}
			}
		}
	}
	
	public List<CollisionEvent> getCollisions(){
		return subs;
	}


	public void setPlaceFrom(double[] xy){
		setPlaceFrom(xy[0],xy[1]);
	}

	public void setPlaceFrom(double x, double y){
		move[0]=x;
		move[1]=y;
	}



	public void setPlaceTo(double[] xy){
		setPlaceTo(xy[0],xy[1]);
	}

	public void setPlaceTo(double x, double y){
		move[2]=x;
		move[3]=y;
	}



	public double[] getPlaceFrom(){
		return new double[]{
				move[0],
				move[1]
		};
	}

	public double[] getPlaceTo(){
		return new double[]{
				move[2],
				move[3]
		};
	}

	public Entity getMover(){
		return e;
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface Listen{
	}

	@Override
	public Class<? extends Annotation> getAnnotation(){
		return Listen.class;
	}

	@Override
	public String getName(){
		return "EVENT_MOVEMENT";
	}

	@Override
	public boolean isCancelled(){
		return cancel||destroy;
	}

	@Override
	public void setCancelled(boolean b){
		cancel = b;
	}

	@Override
	public void destroyEvent(){
		destroy = true;
	}

	@Override
	public boolean isDestroyed(){
		return destroy;
	}
}
