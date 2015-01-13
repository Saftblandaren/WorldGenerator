
package worldGenerator;

import helpers.Spline2D;

import java.util.Random;


public class Camp {
	
	private Random random;
	private World world;
	private int slotX;
	private int slotY;
	
	private int sizeRadius;
	private int posX;
	private int posY;
	
	private Spline2D outline;

	public Camp(World world, int slotX, int slotY) {
		this.world = world;
		this.slotX = slotX;
		this.slotY = slotY;
		this.random = world.getRandom();
		sizeRadius = (int) (world.getSize()*0.02f + random.nextInt((int) (world.getSize()*0.03f)));
		setPosition();
		setOutline();
	}
	
	public int getSlotX() {
		return slotX;
	}

	public int getSlotY() {
		return slotY;
	}

	private void setOutline(){
		outline = new Spline2D(posX, (int) (posY-sizeRadius*0.9), 0.0f);
		int angle = 0;
		do{
			angle += 24 + random.nextInt(24);
			int radius = (int) (sizeRadius*0.8 +  random.nextInt((int) (sizeRadius*0.2)));
			outline.addVertex(angle, radius, 0f);
		}while(angle<324);
		outline.close();
	}
	
	private void setPosition(){
		int minX = (int)((slot[0] + 0.05f) * (float) world.getSize());
		int minY = (int)((slot[1] + 0.05f) * (float) world.getSize());
		int deltaX = (int)(0.25f * (float) world.getSize());
		int deltaY = (int)(0.25f * (float) world.getSize());
		posX = minX + random.nextInt(deltaX);
		posY = minY + random.nextInt(deltaY);
		
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
	
	public boolean isInCamp(int x, int y){
		int deltaX = x-posX;
		int deltaY = y-posY;
		int angle = (int) Math.toDegrees(Math.atan(deltaY/deltaX));
		int distance = (int) Math.pow(Math.pow(deltaY, 2) + Math.pow(deltaX, 2), 2);
		if (distance <= outline.getValue(angle)){
			System.out.println("True");
			return true;
		}
		
		System.out.println("False");
		return false;
	}
	
	//just a minor change

}
