package entity;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import core.Game;
import util.DebugPrint;

public class Hitbox {

	private Rectangle[] rects;
	private double wid = -1;
	private double hgt = -1;
	private double mside = -1;

	public Hitbox(Rectangle[] rects){
		this.rects=rects;
	}

	public Hitbox(BufferedImage i){
		genFromImage(i);
	}

	public static Hitbox genFromImage(BufferedImage i){//this will generate a rectangle-number efficient hitbox from an image
		Hitbox h = new Hitbox(new Rectangle[]{});
		if (i != null){
			List<Rectangle> temp = new ArrayList<Rectangle>();
			boolean[][] used = new boolean[i.getWidth()][i.getHeight()];//defaults all to false :D (no manual work)
			int x1,y1,x2,y2,count;
			for (int x=0; x<i.getWidth();x++){
				for (int y=0; y<i.getHeight();y++){
					if ((!h.isBox(i.getRGB(x,y))) || used[x][y]==true)
						continue;
					x1=x2=x;
					y1=y2=y;
					count = 1;
					while(x+count<i.getWidth()){
						if (!h.isBox(i.getRGB(x+count,y)))//if the next pixel is also part of it, add it to the rectangle
							break;
						x2=x+count;
						count++;
					}
					DebugPrint.addLvl();
					count = 1;
					outer: while(y+count<i.getHeight()){
						for (int c = x1; c<=x2; c++){//loops though all the values in the line
							used[c][y1+count-1]=true;//adds the previous line to used, because it will always have been rightly checked and part of the rectangle
							if (!h.isBox(i.getRGB(c,y+count))){
								break outer;
							}
						}
						count++;
					}
					for (int c = x1; c<=x2; c++){
						used[c][y1+count-1]=true;//because the last one wont be done in above
					}
					DebugPrint.subLvl();
					y2=y1+count-1;//because it starts at 1
					temp.add(new Rectangle(
							(x1*Game.ENTITY_MOVEMENT_MULTIPLIER*Game.ASPECT_RATIO_X)/Game.getInstance().getJFrame().getWidth(),
							(y1*Game.ENTITY_MOVEMENT_MULTIPLIER*Game.ASPECT_RATIO_Y)/Game.getInstance().getJFrame().getHeight(),
							(x2*Game.ENTITY_MOVEMENT_MULTIPLIER*Game.ASPECT_RATIO_X)/Game.getInstance().getJFrame().getWidth(),
							(y2*Game.ENTITY_MOVEMENT_MULTIPLIER*Game.ASPECT_RATIO_Y)/Game.getInstance().getJFrame().getHeight()));
				}

			}
			h.rects = temp.toArray(new Rectangle[0]);
		}else{
			DebugPrint.errln("attempted to generate hitbox from image of null");
		}
		return h;
	}

	private boolean isBox(int rgb){
		return (rgb == Game.HITBOX_RGB && !Game.USE_HITBOX_RGB_AS_ANTI) || (rgb != Game.HITBOX_RGB && Game.USE_HITBOX_RGB_AS_ANTI);
	}

	public Rectangle[] getRectangles(){
		return rects;
	}

	public void addRectangle(Rectangle r){
		rects = Arrays.copyOf(rects, rects.length+1);
		rects[rects.length-1]=r;
	}

	public double getTotalWidth(){
		if (wid == -1)
			calcTotalSize();
		return wid;
	}

	public double getTotalHeight(){
		if (hgt == -1)
			calcTotalSize();
		return hgt;
	}

	public double getMaxSide(){
		if (mside == -1){
			if (wid == -1 || hgt == -1){
				calcTotalSize();
			}
			mside = Math.max(wid, hgt);
		}
		return mside;
	}

	public void calcTotalSize(){
		double leftx = -1;
		double rightx = -1;
		double topy = -1;
		double boty = -1;
		boolean init = true;
		for (Rectangle r : rects){

			if (init){
				leftx=r.getMinX();
				rightx=r.getMaxX();
				topy=r.getMaxY();
				boty=r.getMinY();
				init=false;
				continue;
			}

			if (r.getMaxX()>rightx){
				rightx=r.getMaxX();
			}

			if (r.getMinX()<leftx){
				leftx=r.getMinX();
			}

			if (r.getMaxX()>topy){
				topy=r.getMaxY();
			}

			if (r.getMinY()<boty){
				boty=r.getMinY();
			}
		}

		if (init){
			wid=0;
			hgt=0;
		}else{
			wid=Math.abs(rightx-leftx);
			hgt=Math.abs(topy-boty);
		}
	}
}

class Rectangle{
	double[] x;
	double[] y;

	public Rectangle(double x1, double y1, double x2, double y2){
		x = new double[]{x1,x2};
		y = new double[]{y1,y2};
	}

	public double[] getPoint1(){
		return new double[]{x[0],y[0]};
	}
	public double[] getPoint2(){
		return new double[]{x[1],y[1]};
	}

	public double getMinX(){
		return Math.min(x[0], x[1]);
	}
	public double getMinY(){
		return Math.min(y[0], y[1]);
	}
	public double getMaxX(){
		return Math.max(x[0], x[1]);
	}
	public double getMaxY(){
		return Math.max(y[0], y[1]);
	}

	public RotRect getRotatedInstance(Entity e){

		double cos = Math.cos(e.getRadRotation());
		double sin = Math.sin(e.getRadRotation());

		return new RotRect(

				(x[0]-e.getCenterX()+e.getX())*cos-(y[0]-e.getCenterY()+e.getY())*sin+e.getCenterX(),//-e.getX(),
				(x[0]-e.getCenterX()+e.getX())*sin+(y[0]-e.getCenterY()+e.getY())*cos+e.getCenterY(),//-e.getY(),

				(x[1]-e.getCenterX()+e.getX())*cos-(y[0]-e.getCenterY()+e.getY())*sin+e.getCenterX(),//-e.getX(),
				(x[1]-e.getCenterX()+e.getX())*sin+(y[0]-e.getCenterY()+e.getY())*cos+e.getCenterY(),//-e.getY(),

				(x[1]-e.getCenterX()+e.getX())*cos-(y[1]-e.getCenterY()+e.getY())*sin+e.getCenterX(),//-e.getX(),
				(x[1]-e.getCenterX()+e.getX())*sin+(y[1]-e.getCenterY()+e.getY())*cos+e.getCenterY(),//-e.getY(),

				(x[0]-e.getCenterX()+e.getX())*cos-(y[1]-e.getCenterY()+e.getY())*sin+e.getCenterX(),//-e.getX(),
				(x[0]-e.getCenterX()+e.getX())*sin+(y[1]-e.getCenterY()+e.getY())*cos+e.getCenterY()//-e.getY()
				);
	}
}

class RotRect{

	private double[] x;
	private double[] y;

	public RotRect(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4){
		x = new double[]{x1,x2,x3,x4};
		y = new double[]{y1,y2,y3,y4};
	}

	public double[][] getPoints(){
		return new double[][]{

			{x[0],y[0]},
			{x[1],y[1]},
			{x[2],y[2]},
			{x[3],y[3]},

		};
	}
}