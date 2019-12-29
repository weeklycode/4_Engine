package entity;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import core.Game;

public class PlayerBase extends Entity{//for players which are not the user

	public PlayerBase(double x, double y, double rot){
		super(null, null, x, y, rot);
		
		ArrayList<BufferedImage> mag = new ArrayList<BufferedImage>();
		
		try{
			mag.add(ImageIO.read(new File(Game.ASSET_PATH + "/player.png")));
		}catch (IOException e){
			e.printStackTrace();
		}
		
		this.setSprites(mag);
		
		Hitbox h = Hitbox.genFromImage(getSprite());
		this.setHitbox(h);
		this.setCenterXOffset((Game.ENTITY_MOVEMENT_MULTIPLIER*Game.ASPECT_RATIO_X*getSprite().getWidth())/(Game.getInstance().getJFrame().getWidth()<<1));
		this.setCenterYOffset((Game.ENTITY_MOVEMENT_MULTIPLIER*Game.ASPECT_RATIO_Y*getSprite().getHeight())/(Game.getInstance().getJFrame().getHeight()<<1));
	}

	@Override
	public void update(){
		
	}

}
