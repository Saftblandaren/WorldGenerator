package worldGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class World {
	
	private Random random;
	private int slots;
	private final int SLOT_SIZE = 8192;
	private int sizeX;
	private int size;
	private int nCamps;
	private Camp capital = null;
	private List<Camp> camps = new ArrayList<Camp>();
	private HashMap<String, Integer> world_buff;

	public World(Random random) {
		this.random = random;
		// world could be 8x8 slots or 16x16 slots, each slot 8192x8192 pixels
		slots = (int) Math.pow(2, (3 + random.nextInt(2)));
		
		// Alway one capital
		int slotMeanIndex = (slots-1) / 2;
		int capitalXSlot = slotMeanIndex - 1 + random.nextInt(4);
		int capitalYSlot = slotMeanIndex;
		if (slotMeanIndex <= capitalXSlot && capitalXSlot <= slotMeanIndex+1){
			capitalYSlot += -1 + random.nextInt(4);
		} else {
			capitalYSlot += random.nextInt(2);
		}
		capital = new Capital(this, capitalXSlot, capitalYSlot)
		nCamps = random.nextInt(2) + random.nextInt(slots/8) + slots / 16;
		camps.add(capital);
		
		for(int i = 0; i < nCamps; i++){
			int[] slot = getFreeSlot();
		}
		
		/*
		sizeX = random.nextInt(3);
		size = (int) Math.pow(2,(sizeX + 8));
		//size = (int) Math.pow(2,(sizeX + 14));
		nCamps = 1 + random.nextInt(3) + random.nextInt(sizeX+1);
		int capitalTest = nCamps + random.nextInt(4);
		if(capitalTest>6){
			capital = new Capital(this, new float[]{1.0f/3, 1.0f/3}, random);
			nCamps -= 1;
		}
		*/
		
		List<float[]> campSlots = createCampSlots();
		
		for(int i=0;i<nCamps; i++){
			int slot = random.nextInt(campSlots.size()-1);
			camps.add(new Camp(this, campSlots.get(slot), random));
			campSlots.remove(slot);
		}
			
		
	}
	
	public int getValue(int x, int y){
		for(Camp camp:camps){
			if(camp.isInCamp(x, y)){
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
			x = 1 + random.nextInt(slots-1);
			y = 1 + random.nextInt(slots-1);
			for(Camp camp:camps){
				int dx = x - camp.getSlotX();
				int dy = y - camp.getSlotY();
				double distance = Math.pow(Math.pow(dx, 2) + Math.pow(dy, 2), (1/2));
				if (distance > 1.5){
					free = true;
				}
			}
			
		}while(!free);
		
		return new int[]{x, y};
		
	}
	
	public HashMap<String, Integer> getWorld_buff() {
		return world_buff;
	}

	private List<float[]> createCampSlots(){
		List<float[]> campSlots = new ArrayList<float[]>();
		//List<Tuple> campSlots = new ArrayList<Tuple>();
		campSlots.add(new float[]{0.0f/3, 0.0f/3});
		campSlots.add(new float[]{1.0f/3, 0.0f/3});
		campSlots.add(new float[]{2.0f/3, 0.0f/3});
		campSlots.add(new float[]{0.0f/3, 1.0f/3});
		campSlots.add(new float[]{2.0f/3, 1.0f/3});
		campSlots.add(new float[]{0.0f/3, 2.0f/3});
		campSlots.add(new float[]{1.0f/3, 2.0f/3});
		campSlots.add(new float[]{2.0f/3, 2.0f/3});
		return campSlots;
		
		
	}

	public Random getRandom() {
		return random;
	}

	public int getSizeX() {
		return sizeX;
	}

	public int getSize() {
		return size;
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
	
	

}
