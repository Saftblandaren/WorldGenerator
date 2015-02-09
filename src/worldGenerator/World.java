package worldGenerator;

import helpers.PointComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import objects.Bridge;
import objects.StaticObject;

public class World {
	
	private Random random;
	private int slots;
	//private final int SLOT_SIZE = 1024;
	private final int SLOT_SIZE = 512;
	private int nCamps;
	private Camp capital;
	private List<Camp> camps;
	private HeightMap heightMap;
	private List<Route> routes;
	public List<River> rivers;
	private List<StaticObject> staticObjects;

	public World(Random random) {
		this.random = random;
		camps = new ArrayList<Camp>();
		routes = new ArrayList<Route>();

		// world could be 8x8 slots or 16x16 slots, each slot 8192x8192 pixels
		slots = (int) Math.pow(2, (3 + random.nextInt(2)));
		slots = 8;
		
		heightMap = new HeightMap(this);
		
		setCamps();
		generateRivers();
		for(Camp camp: camps){
			camp.createRoutes();
		}
		
		
		
		heightMap.finalizeHeightGrid();

	}
	private void generateRivers() {
		rivers = new ArrayList<River>();
		List<int[]> startOptions = new ArrayList<int[]>();
		for(int x = 1; x < 7; x++){
			startOptions.add(new int[]{getMeanHeight(x*SLOT_SIZE*slots/7, 0), x*SLOT_SIZE*slots/7, 0} );
			startOptions.add(new int[]{getMeanHeight(x*SLOT_SIZE*slots/7, SLOT_SIZE*slots), x*SLOT_SIZE*slots/7, SLOT_SIZE*slots} );
		}
		for(int y = 1; y < 7; y++){
			startOptions.add(new int[]{getMeanHeight(0, y*SLOT_SIZE*slots/7), 0,  y*SLOT_SIZE*slots/7} );
			startOptions.add(new int[]{getMeanHeight(SLOT_SIZE*slots, y*SLOT_SIZE*slots/7),SLOT_SIZE*slots,  y*SLOT_SIZE*slots/7} );
		}
		
		Collections.sort(startOptions, new PointComparator());
		
		int nRivers = 1 + random.nextInt(3);
		
		System.out.println("Number of rivers: " + nRivers);
		
		
		//for(int i = 0; (rivers.size() < nRivers && i < startOptions.size()); i++){
		for(int i = 0; (rivers.size() < nRivers); i++){
			if(random.nextInt(3) == 0){
				int x = startOptions.get(i)[1];
				int y = startOptions.get(i)[2];
				rivers.add(new River(x, y, this));
			}
		}
		
	}
	
	
	public List<Route> getRoutes(){
		return routes;
	}
	public void addRoute(Route route){
		routes.add(route);
	}
	
	private void setCamps(){
		// Always one capital
		int slotMeanIndex = (slots-1) / 2;
		int capitalXSlot = slotMeanIndex - 1 + random.nextInt(4);
		int capitalYSlot = slotMeanIndex;
		if (slotMeanIndex <= capitalXSlot && capitalXSlot <= slotMeanIndex+1){
			capitalYSlot += -1 + random.nextInt(4);
		} else {
			capitalYSlot += random.nextInt(2);
		}
		capital = new Capital(this, capitalXSlot, capitalYSlot);
		camps.add(capital);
		
		nCamps = random.nextInt(2) + random.nextInt(slots/8) + slots / 16;
		nCamps = 2;
		
		System.out.println("Number of slots: " + slots);
		System.out.println("Number of camps: " + nCamps + "\n");
		
		if(slots > 8){
			System.exit(0);
		}
		
		for(int i = 0; i < nCamps; i++){
			int[] slot = getFreeSlot();
			camps.add(new Camp(this, slot[0], slot[1]));
		}
	}
	
	
	public int getHeight(int x,int y){
		return heightMap.getHeight(x, y);
	}
	
	public int getMeanHeight(int x, int y){
		return heightMap.getMeanGridValue(x/heightMap.POINT_SPACE, y/heightMap.POINT_SPACE);
	}
	
	public int getValue(int x, int y){
		
		int a = 255;
		
		for(Camp camp:camps){
			if(camp.isInCamp(x, y)){
				return ((a<<24) | (255<<16) | (255<<8) | 255);
			}
		}
		for(Route route:routes){
			if(route.distanceTo(x, y)<=0){
				return ((a<<24) | (0<<16) | (255<<8) | 0);
			}
		}
		return ((a<<24) | (10*x/SLOT_SIZE<<16) | (0<<8) | 10*y/SLOT_SIZE);
	}
	
	private int[] getFreeSlot(){
		boolean free = true;
		int x;
		int y;
		do{
			free = true;
			x = 1 + random.nextInt(slots-2);
			random.nextInt(slots-2);
			y = 1 + random.nextInt(slots-2);
			for(Camp camp:camps){
				int dx = x - camp.getSlotX();
				int dy = y - camp.getSlotY();
				double distance = Math.sqrt((Math.pow(dx, 2) + Math.pow(dy, 2)));
				if (distance < 1.5){
					free = false;
				}
			}
			
		}while(!free);
		
		return new int[]{x, y};
		
	}
	

	public Random getRandom() {
		return random;
	}

	public int getSlots() {
		return slots;
	}

	public int getnCamps() {
		return nCamps;
	}

	public Camp getCapital() {
		return capital;
	}

	public List<Camp> getCamps() {
		return camps;
	}

	public int getSLOT_SIZE() {
		return SLOT_SIZE;
	}
	public List<River> getRivers() {
		return rivers;
	}
	public void addStaticObject(StaticObject object) {
		// TODO Auto-generated method stub
		
	}
	
	

}
