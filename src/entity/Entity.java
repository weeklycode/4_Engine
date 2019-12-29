package entity;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import util.DebugPrint;
import util.PaintEntity;
import util.VecUtils;

public abstract class Entity {

	private ArrayList<BufferedImage> sprites;
	private Hitbox hitbox;
	private double x;//from 0 to aspect ratio x times entity movement multiplier for the leftmost to the rightmost parts of the screen
	private double y;//from 0 to aspect ratio y times entity movement multiplier for the topmost to the bottommost parts of the screen
	private double cenX;//for rotation
	private double cenY;//for rotation
	private double rot;//rotation from 0 to 360 degrees
	private double radrot;//rotation in radians
	private List<Entity> touching;

	int spriteIndex = 0;

	protected Entity(ArrayList<BufferedImage> s, Hitbox h, double x, double y, double rot, double cenXOffset, double cenYOffset){
		DebugPrint.println("adding an entity");
		this.sprites=s;
		this.hitbox=h;
		this.x=x;
		this.y=y;
		this.rot=rot;
		this.radrot=Math.toRadians(rot);
		this.cenX=cenXOffset;
		this.cenX=cenYOffset;
		touching = new ArrayList<Entity>();
	}

	public Entity(ArrayList<BufferedImage> sprites, Hitbox hitbox, double x, double y, double rot){
		DebugPrint.println("adding an entity");
		this.sprites=sprites;
		this.hitbox=hitbox;
		this.x=x;
		this.y=y;
		this.rot=rot;
		this.radrot=Math.toRadians(rot);
		touching = new ArrayList<Entity>();
	}

	public List<Entity> getTouching(){
		return touching;
	}

	public void setTouching(List<Entity> t){
		touching = t;
	}

	public void addTouching(Entity e){
		touching.add(e);
	}

	public void removeTouching(Entity e){
		touching.remove(e);
	}

	public double getX(){
		return x;
	}
	public double getY(){
		return y;
	}

	public void setX(double x){
		this.x=x;
	}
	public void setY(double y){
		this.y=y;
	}

	public void setRotation(double rot){
		this.rot=rot;
		this.radrot=Math.toRadians(rot);
	}
	public double getRotation(){
		return rot;
	}

	public double getRadRotation(){
		return radrot;
	}

	public void setSprites(ArrayList<BufferedImage> sprites){
		this.sprites=sprites;
	}

	public void addSprite(BufferedImage sprite){
		sprites.add(sprite);
	}

	public ArrayList<BufferedImage> getSprites(){
		return sprites;
	}

	public BufferedImage getSprite(){
		return sprites.get(spriteIndex);
	}

	public void setSpriteIndex(int index){
		spriteIndex = index;
	}

	public int getSpriteIndex(){
		return spriteIndex;
	}

	public Hitbox getHitbox(){
		return hitbox;
	}

	public void setHitbox(Hitbox hitbox){
		this.hitbox=hitbox;
	}

	public void setCenterXOffset(double cenX){
		this.cenX=cenX;
	}
	public void setCenterYOffset(double cenY){
		this.cenY=cenY;
	}

	public double getCenterX(){
		return cenX+x;
	}
	public double getCenterY(){
		return cenY+y;
	}

	public boolean isTouching(Entity other){
		return isTouching(other,x,y);
	}

	public boolean isTouching(Entity other, double newX, double newY){

		if (hitbox == null)
			return false;
		if (other.hitbox == null)
			return false;
		
		if (hitbox.getMaxSide()+other.hitbox.getMaxSide()<Math.abs(x-other.x))
			return false;
		if (hitbox.getMaxSide()+other.hitbox.getMaxSide()<Math.abs(y-other.y))
			return false;
			
		if (other == this){
			DebugPrint.errln("isTouching(Entity o, double nx, double ny) called on same entity as from");
			return false;
		}

		double[][] points1;
		double[][] points2;
		double[] vec;
		double[] s;//subtract vector
		double proj;
		double min;
		double max;
		double min2;
		double max2;

		for (int c1 = 0; c1<hitbox.getRectangles().length; c1++){

			points1 = hitbox.getRectangles()[c1].getRotatedInstance(this).getPoints();

			no_overlap : for (int c2 = 0; c2<other.hitbox.getRectangles().length; c2++){

				points2 = other.hitbox.getRectangles()[c2].getRotatedInstance(other).getPoints();

				for (int i = 0, j = points1.length-1; i<points1.length; j=i++){

					min2 = min = Double.POSITIVE_INFINITY;
					max2 = max = Double.NEGATIVE_INFINITY;

					s = points1[j];

					vec = new double[]{
							s[1]-points1[i][1],
							points1[i][0]-s[0]
					};

					for (int c3 = 0; c3<points2.length; c3++){
						proj = VecUtils.scalarProject(vec, VecUtils.subtract(points2[c3],s));
						//System.out.printf("projection2 %f\n",proj);
						if (proj<min2){
							min2=proj;
						}
						if (proj>max2){
							max2=proj;
						}
					}

					for (int c3 = 0; c3<points1.length; c3++){
						proj = VecUtils.scalarProject(vec, VecUtils.subtract(points1[c3],s));
						//System.out.printf("projection1 %f\n",proj);
						if (proj<min){
							min=proj;
						}
						if (proj>max){
							max=proj;
						}
					}

					//System.out.printf("min: %f. max: %f\n",min,max);
					//System.out.printf("min2: %f. max2: %f\n\n ",min2,max2);

					if (max2<min || min2>max){//no overlap
						continue no_overlap;
					}
				}
				
				
				
				for (int i = 0, j = points2.length-1; i<points2.length; j=i++){

					min2 = min = Double.POSITIVE_INFINITY;
					max2 = max = Double.NEGATIVE_INFINITY;

					s = points2[j];

					vec = new double[]{
							s[1]-points2[i][1],
							points2[i][0]-s[0]
					};

					for (int c3 = 0; c3<points1.length; c3++){
						proj = VecUtils.scalarProject(vec, VecUtils.subtract(points1[c3],s));
						//System.out.printf("projection2 %f\n",proj);
						if (proj<min2){
							min2=proj;
						}
						if (proj>max2){
							max2=proj;
						}
					}

					for (int c3 = 0; c3<points2.length; c3++){
						proj = VecUtils.scalarProject(vec, VecUtils.subtract(points2[c3],s));
						//System.out.printf("projection1 %f\n",proj);
						if (proj<min){
							min=proj;
						}
						if (proj>max){
							max=proj;
						}
					}

					//System.out.printf("min: %f. max: %f\n",min,max);
					//System.out.printf("min2: %f. max2: %f\n\n ",min2,max2);

					if (max2<min || min2>max){//no overlap
						continue no_overlap;
					}
				}

				//System.out.println("returning true");
				return true;

			}

		}

		//System.out.println("returning false");
		return false;
	}

	@Override
	public boolean equals(Object other){//need this so if we check if this equals the paint entity version of it, it returns true
		if (other instanceof PaintEntity){
			return other.equals(this);
		}
		return super.equals(other);
	}

	public abstract void update();
}
