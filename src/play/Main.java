package play;

import core.Game;
import entity.Crash;
import entity.Player;

public class Main {
	static Player p;
	public static void main(String[] args){

		Game.init();

		p = new Player(Game.ASPECT_RATIO_X*Game.ENTITY_MOVEMENT_MULTIPLIER/2,Game.ASPECT_RATIO_Y*Game.ENTITY_MOVEMENT_MULTIPLIER/2,0);

		Game.getInstance().addEntity(p);
		Game.getInstance().getVisualManager().addEntity(p,0);

		Crash c = new Crash(1000,5000,0);
		Game.getInstance().addEntity(c);
		Game.getInstance().getVisualManager().addEntity(c,0);
	}

	public static Player getPlayer(){
		return p;
	}
}
