package worldGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class World {
	
	private Random random;
	private int sizeX;
	private int size;
	private int nCamps;
	private Camp capital = null;
	private List<Camp> camps = new ArrayList<Camp>();
	private HashMap<String, Integer> world_buff;

	public World(Random random) {
		this.random = random;
		sizeX = random.nextInt(3);
		size = (int) Math.pow(2,(sizeX + 8));
		//size = (int) Math.pow(2,(sizeX + 14));
		nCamps = 1 + random.nextInt(3) + random.nextInt(sizeX+1);
		int capitalTest = nCamps + random.nextInt(4);
		if(capitalTest>6){
			capital = new Capital(this, new float[]{1.0f/3, 1.0f/3}, random);
			nCamps -= 1;
		}
		
		List<float[]> campSlots = createCampSlots();
		
		for(int i=0;i<nCamps; i++){
			int slot = random.nextInt(campSlots.size()-1);
			camps.add(new Camp(this, campSlots.get(slot), random));
			campSlots.remove(slot);
		}
		
		camps.add(capital);
		
		/**
		world_buff = new HashMap<String, Integer>();
		
		for(Camp camp:camps){
			int x = camp.getPosX();
			int y = camp.getPosY();
			int r = camp.getSizeRadius();
			
			for(int i=0;i<r;i++){
				int y_limit = (int) Math.pow((Math.pow(r, 2)-Math.pow(i, 2)),(1.0/2.0));
				for(int j=0;j<y_limit;j++){
					if( i==0){
						world_buff.put(String.valueOf(x) + ":" + String.valueOf(y-j), 1);
						world_buff.put(String.valueOf(x) + ":" + String.valueOf(y+j), 1);
					}else{
						world_buff.put(String.valueOf(x-i) + ":" + String.valueOf(y-j), 1);
						world_buff.put(String.valueOf(x-i) + ":" + String.valueOf(y+j), 1);
						world_buff.put(String.valueOf(x+i) + ":" + String.valueOf(y-j), 1);
						world_buff.put(String.valueOf(x+i) + ":" + String.valueOf(y+j), 1);
					}
				}

			}
			
		}
		*/
	
		
	}
	
	public int getValue(int x, int y){
		for(Camp camp:camps){
			if(camp.isInCamp(x, y)){
				return 1;
			}
		}
		return 0;
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
