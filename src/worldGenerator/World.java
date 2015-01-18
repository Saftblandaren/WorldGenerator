package worldGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World {
	
	private Random random;
	private int slots;
	//private final int SLOT_SIZE = 2048;
	private final int SLOT_SIZE = 256;
	private int nCamps;
	private Camp capital;
	private List<Camp> camps;
	private HeightMap heightMap;

	public World(Random random) {
		this.random = random;
		camps = new ArrayList<Camp>();
		
		// world could be 8x8 slots or 16x16 slots, each slot 8192x8192 pixels
		slots = (int) Math.pow(2, (3 + random.nextInt(2)));
		slots = 8;
		
		// Alway one capital
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
		
		System.out.print("\nNumber of slots: ");
		System.out.print(slots);
		System.out.print("\nNumber of camps: ");
		System.out.print(nCamps);
		
		if(slots > 8){
			System.exit(0);
		}
		
		for(int i = 0; i < nCamps; i++){
			int[] slot = getFreeSlot();
			camps.add(new Camp(this, slot[0], slot[1]));
		}
		
		heightMap = new HeightMap(slots*SLOT_SIZE, random);
		
		
	}
	
	public int getHeight(int x,int y){
		return heightMap.getHeight(x, y);
	}
	
	public int getValue(int x, int y){
		for(Camp camp:camps){
			if(camp.isInCamp(x, y)){
				//System.out.println("pixel");
				return 1;
			}
		}
		return 0;
	}
	
	private int[] getFreeSlot(){
		boolean free = false;
		int x;
		int y;
		do{
			x = 1 + random.nextInt(slots-2);
			y = 1 + random.nextInt(slots-2);
			for(Camp camp:camps){
				int dx = x - camp.getSlotX();
				int dy = y - camp.getSlotY();
				double distance = Math.sqrt((Math.pow(dx, 2) + Math.pow(dy, 2)));
				if (distance > 1.5){
					free = true;
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
	
	

}
