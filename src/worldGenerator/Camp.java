
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
		outline = new Spline2D(posX, (int) (posY-sizeRadius*0.9), 0.0f);
		int angle = 0;
		do{
			angle += 24 + random.nextInt(24);
			int radius = (int) (sizeRadius*0.8 +  random.nextInt((int) (sizeRadius*0.2)));
			outline.addVertex(angle, radius, 0f);
		}while(angle<324);
		outline.close();
	}
	
	protected void setPosition(){
		System.out.print("\nSizeRadius: ");
		System.out.print(sizeRadius);
		int minX = (int)(slotX * world.getSLOT_SIZE() + sizeRadius);
		int minY = (int)(slotY * world.getSLOT_SIZE() + sizeRadius);
		int delta = (int)(world.getSLOT_SIZE() - sizeRadius);
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
	
	public boolean isInCamp(int x, int y){
		int deltaX = x-posX;
		int deltaY = y-posY;
		int angle;
		if (deltaX == 0){
			angle = 90 + 90 * Integer.signum(deltaY);
		}else{
			angle = (int) Math.toDegrees(Math.atan(deltaY/deltaX));
		}
		int distance = (int) Math.pow(Math.pow(deltaY, 2) + Math.pow(deltaX, 2), 2);
		if (distance <= outline.getValue(angle)){
			//System.out.println("True");
			return true;
		}
		
		//System.out.println("False");
		return false;
	}

}
