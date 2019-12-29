package entity;

import core.Game;
import event.Event;
import event.KeyPressEvent;
import event.KeyReleaseEvent;
import listener.Listener;
import play.Main;
import util.KeyHandler;

public class Crash extends PlayerBase{

	private boolean move = false;
	
	public Crash(double x, double y, double rot) {
		super(x, y, rot);
		new Listener(){
			@Override
			@KeyHandler.Listen
			public void listen(Event e){
				if (e instanceof KeyPressEvent){
					if (((KeyPressEvent)e).getKey() == ' '){
						move = true;
					}
				}else if (e instanceof KeyReleaseEvent){
					if (((KeyReleaseEvent)e).getKey() == ' '){
						move = false;
					}
				}
			}
		}.register();
	}

	@Override
	public void update(){
		super.update();
		if (move){
			double x = Main.getPlayer().getX();
			if (Math.abs(this.getX()-x)<=20){
				double y = Main.getPlayer().getY();
				if (Math.abs(this.getY()-y)>=20){
					this.setY((this.getY()+20)%10000);
				}
			}else{
				this.setX((this.getX()+20)%10000);
			}
			if (this.isTouching(Main.getPlayer())){
				Game.getInstance().destroy();
			}
		}
	}
	
}
