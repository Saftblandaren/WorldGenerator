
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
	
	public void createRoutes(){
		int nRoutes = 1 + random.nextInt(2);
		//int nRoutes = 3;
		for(int i = 0; i<nRoutes; i++){
			int endX = 0;
			int endY = 0;
			boolean create = false;
			for(int r = 0; true; r +=25){
				if (posX-r <0 && random.nextInt(11)>9 && !create){
					int dy = (int) Math.sqrt((Math.pow(r, 2) - Math.pow(posX,2)));
					endX = -20;
					endY = posY-dy+random.nextInt(dy);
					//world.addRoute(new Route(posX, posY, 0, posY-dy+random.nextInt(dy), random));
					create = true;
				}
				if (posY-r <0 && random.nextInt(11)>9 && !create){
					int dx = (int) Math.sqrt((Math.pow(r, 2) - Math.pow(posY,2)));
					endX = posX-dx+random.nextInt(dx);
					endY = -20;
					//world.addRoute(new Route(posX, posY, posX-dx+random.nextInt(dx), 0, random));
					create = true;
				}
				if (posX+r > (world.getSLOT_SIZE() * world.getSlots()) && random.nextInt(11)>9 && !create){
					int dy = (int) Math.sqrt((Math.pow(r, 2) - Math.pow(world.getSLOT_SIZE() * world.getSlots()-posX,2)));
					endX = world.getSLOT_SIZE() * world.getSlots()+20;
					endY = posY-dy+random.nextInt(dy);
					//world.addRoute(new Route(posX, posY, world.getSLOT_SIZE() * world.getSlots(), posY-dy+random.nextInt(dy), random));
					create = true;
				}
				if (posY+r > (world.getSLOT_SIZE() * world.getSlots()) && random.nextInt(11)>9 && !create){
					int dx = (int) Math.sqrt((Math.pow(r, 2) - Math.pow(world.getSLOT_SIZE() * world.getSlots()-posY,2)));
					endX = posX-dx+random.nextInt(dx);
					endY = world.getSLOT_SIZE() * world.getSlots()+20;
					
					//world.addRoute(new Route(posX, posY, posX-dx+random.nextInt(dx), world.getSLOT_SIZE() * world.getSlots(), random));
					create = true;
				}
				for(Route route:world.getRoutes()){

					double distance = Math.sqrt(Math.pow((route.getEnd().x-endX), 2) + Math.pow((route.getEnd().y-endY), 2));
					if (distance <1500){
						create = false;
					}
				}
				if(create){
					System.out.println(endX + ", " + endY);
					world.addRoute(new Route(posX, posY, endX, endY, random));
					break;
				}
			}
		}
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
