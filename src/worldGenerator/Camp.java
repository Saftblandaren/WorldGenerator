package worldGenerator;

import java.util.Random;


public class Camp {
	
	private Random random;
	private World world;
	private float[] slot;
	
	private int sizeRadius;
	private int posX;
	private int posY;

	public Camp(World world, float[] slot, Random random) {
		this.world = world;
		this.slot = slot;
		this.random = random;
		sizeRadius = (int) (world.getSize()*0.02f + random.nextInt((int) (world.getSize()*0.03f)));
		setPosition();
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
	
	

}
