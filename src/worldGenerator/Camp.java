
package worldGenerator;

import helpers.Spline2D;

import java.util.Random;

public class Camp {
	
	protected Random random;
	protected World world;
	protected int slotX;
	protected int slotY;
	
	protected int sizeRadius;
	protected int posX;
	protected int posY;
	
	private Spline2D outline;

	public Camp(World world, int slotX, int slotY) {
		this.world = world;
		this.slotX = slotX;
		this.slotY = slotY;
		this.random = world.getRandom();
		sizeRadius = (int) (world.getSLOT_SIZE()*0.2f + random.nextInt((int) (world.getSLOT_SIZE()*0.1f)));
		setPosition();
		setOutline();
	}
	
	public int getSlotX() {
		return slotX;
	}

	public int getSlotY() {
		return slotY;
	}

	protected void setOutline(){
		int angle = 0;
		int radius = (int) (sizeRadius*0.5 +  random.nextInt((int) (sizeRadius*0.5)));
		outline = new Spline2D(angle, radius , 0.0f);
		do{
			angle += 24 + random.nextInt(24);
			radius = (int) (sizeRadius*0.6 +  random.nextInt((int) (sizeRadius*0.4)));
			outline.addVertex(angle, radius, 0f);
		}while(angle<312);
		outline.close();
	}
	
	protected void setPosition(){
		int minX = (int)(slotX * world.getSLOT_SIZE() + sizeRadius);
		int minY = (int)(slotY * world.getSLOT_SIZE() + sizeRadius);
		int delta = (int)(world.getSLOT_SIZE() - sizeRadius*2);
		posX = minX + random.nextInt(delta);
		posY = minY + random.nextInt(delta);
		
	}

	public int getSizeRadius() {
		return sizeRadius;
	}

	public int getPosX() {
		return posX;
	}

	public int getPosY() {
		return posY;
	}
	
	private int distanceTo(int x, int y){
		int deltaX = x-posX;
		int deltaY = y-posY;
		int distance = (int) Math.sqrt((Math.pow(deltaY, 2) + Math.pow(deltaX, 2)));
		return distance;
	}
	
	public int distanceToCampArea(int x, int y){
		return (distanceTo(x, y) - sizeRadius);
	}
	
	public boolean isInCamp(int x, int y){
		int deltaX = x-posX;
		int deltaY = y-posY;
		int angle = (int) Math.toDegrees(Math.atan2(deltaY, deltaX));
		while(angle<0){
			angle += 360;
		}
		int distance = distanceTo(x,y);
		if (distance <= outline.getValue(angle)){
			return true;
		}
		return false;
	}
	
	public Spline2D getOutline() {
		return outline;
	}
	

}
