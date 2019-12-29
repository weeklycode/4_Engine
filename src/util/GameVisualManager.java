package util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

import javax.swing.JFrame;

import core.Game;
import entity.Entity;

public class GameVisualManager extends VideoManager{

	private PriorityQueue<PaintEntity> paint = new PriorityQueue<PaintEntity>(1, new Comparator<PaintEntity>(){
		@Override
		public int compare(PaintEntity o1, PaintEntity o2) {
			if (o1 == null)
				return -1;
			if (o2 == null)
				return 1;
			return o1.getPriority()>o2.getPriority()? 1:-1;
		}

	});//so it can paint form background to foreground

	private	Graphics2D graphics;
	private double xCent;
	private double yCent;
	private boolean run;

	public GameVisualManager(JFrame gameboard){
		super(gameboard);
		run=true;
	}

	public void addEntity(Entity e, int layerIndex){//layerIndex decides how the foreground background, etc are drawn
		paint.add(new PaintEntity(e,layerIndex));//it draws from lowest index to highest
	}//things added later on the same index level are given priority

	public void removeEntity(Entity e){
		paint.remove(e);
	}

	public PriorityQueue<PaintEntity> getEntities(){
		return paint;
	}

	public void removeNullEntities(){
		ArrayList<PaintEntity> toRemove = new ArrayList<PaintEntity>();
		for (PaintEntity pe : paint){
			if (pe == null){//dont think is possible unless someone is somehow creating an extension of this class which allows them to edit paint
				toRemove.add(pe);
				continue;
			}
			if (pe.getEntity() == null){
				toRemove.add(pe);
				continue;
			}
		}
	}

	public synchronized boolean draw(boolean disposeGraphics2D){//MUST BE SYNCHRONIZED, or else things start breaking down
		if (getFrame() != null && run){
			try{
				if (!getBufferStrategy().contentsLost()){
					getBufferStrategy().show();
				}

				if (graphics == null){
					genGraphics2D();
				}

				//DebugPrint.println("drawing screen");

				graphics.clearRect(0, 0, getDisplayMode().getWidth(), getDisplayMode().getHeight());
				Entity e;
				AffineTransform restore;
				AffineTransform trans;
				for (PaintEntity pe : paint){
					e = pe.getEntity();
					if (e != null){
						restore = graphics.getTransform();
						trans = new AffineTransform();
						//trans.rotate(Math.toRadians(e.getRotation()),(int)Math.round(((e.getCenterX()*getFrame().getWidth())/(Game.ENTITY_MOVEMENT_MULTIPLIER*Game.ASPECT_RATIO_X))+xCent),(int)(((e.getCenterY()*getFrame().getWidth())/(Game.ENTITY_MOVEMENT_MULTIPLIER*Game.ASPECT_RATIO_Y))+yCent));
						trans.rotate(Math.toRadians(e.getRotation()),(int)Math.round((e.getCenterX()/(Game.ENTITY_MOVEMENT_MULTIPLIER*Game.ASPECT_RATIO_X))*getFrame().getWidth()+xCent),
								(int)Math.round((e.getCenterY()/(Game.ENTITY_MOVEMENT_MULTIPLIER*Game.ASPECT_RATIO_Y))*getFrame().getHeight()+yCent));
						graphics.transform(trans);
						graphics.drawImage(e.getSprite(), (int)((e.getX()/(Game.ENTITY_MOVEMENT_MULTIPLIER*Game.ASPECT_RATIO_X))*getFrame().getWidth()+xCent), (int)((e.getY()/(Game.ENTITY_MOVEMENT_MULTIPLIER*Game.ASPECT_RATIO_Y))*getFrame().getHeight()+yCent), null);
						graphics.setTransform(restore);
					}
				}
				restore=null;
				trans=null;
				e=null;
				if (disposeGraphics2D)
					disposeGraphics2D();
				return true;
			}catch(Exception e){
				DebugPrint.errln("error in drawing:");
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}

	public synchronized void genGraphics2D(){
		//DebugPrint.println("generating Graphics2D");
		graphics = (Graphics2D) getBufferStrategy().getDrawGraphics();
		if (graphics == null){
			DebugPrint.addLvl();
			DebugPrint.errln("Graphics2D object (GameVisualManager:\"graphics\") is null: genGraphics2D() not working");
			DebugPrint.print("(incoming error probably)");
			DebugPrint.subLvl();
		}
		graphics.setColor(Color.WHITE);
	}

	public synchronized void disposeGraphics2D(){
		//DebugPrint.println("disposing Graphics2D");
		if (graphics != null)
			graphics.dispose();
		graphics = null;
	}

	@Override
	public synchronized void destroyFrame(){
		DebugPrint.printlnAdd("destroying frame...");
		try{
			DebugPrint.println("setting visible to false");
			getFrame().setVisible(false);

			if (graphics != null){
				DebugPrint.println("disposing graphics");
				graphics.dispose();
			}

			if (getFrame().getBufferStrategy() != null){
				DebugPrint.println("disposing buffer strategy");
				getFrame().getBufferStrategy().dispose();
			}

			super.destroyFrame();
		}finally{
			DebugPrint.subLvl();
		}
	}
	
	public void setRunning(boolean b){
		run = b;
	}

	public double getXCent(){
		return xCent;
	}
	public double getYCent(){
		return yCent;
	}
	
	public int[] getScreenCoordsOf(double[] in){
		return new int[]{
				(int)Math.round((in[0]/(Game.ENTITY_MOVEMENT_MULTIPLIER*Game.ASPECT_RATIO_X))*getFrame().getWidth()+xCent),
				(int)Math.round((in[1]/(Game.ENTITY_MOVEMENT_MULTIPLIER*Game.ASPECT_RATIO_Y))*getFrame().getHeight()+yCent)
		};
	}
}
