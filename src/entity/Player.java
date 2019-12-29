package entity;

import core.Game;
import event.Event;
import event.KeyPressEvent;
import event.KeyReleaseEvent;
import listener.Listener;
import util.DebugPrint;
import util.KeyHandler;

public class Player extends PlayerBase{//user wrapper

	private boolean add;

	public Player(double x, double y, double rot){
		super(x,y,rot);

		new Listener(){

			@Override
			@KeyHandler.Listen
			public void listen(Event e){
				if (e instanceof KeyPressEvent){
					add = true;
				}else if (e instanceof KeyReleaseEvent){
					add = false;
				}
			}

		}.register();
	}

	@Override
	public void update(){
		super.update();
		if (add){
			setRotation(getRotation()+5);
			for (Entity e : Game.getInstance().getEntities()){
				if (e == this)
					continue;

				if (e.isTouching(this)){
					DebugPrint.println("collision");
				}
			}
		}
	}

}
