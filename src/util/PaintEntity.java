package util;

import entity.Entity;

public class PaintEntity {

	private Entity e;
	private int priority;
	
	public PaintEntity(Entity e, int priority) {
		this.e = e;
		this.priority=priority;
	}
	
	public Entity getEntity(){
		return e;
	}
	
	public int getPriority(){
		return priority;
	}
	
	@Override
	public boolean equals(Object other){
		return e.equals(other);
	}
}
