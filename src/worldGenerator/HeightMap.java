package worldGenerator;

import java.util.Random;

import helpers.Spline2D;

public class HeightMap {
	
	private final int POINT_SPACE = 32;
	private int[][] heightGrid;
	private Spline2D[] heightMap;
	private int size;
	
	public HeightMap(int world_size, Random random) {
		size = world_size/POINT_SPACE;
		heightGrid = new int[size+1][size+1];
		
		//assign random values
		for(int y = 0; y <= size; y++){
			for (int x = 0; x<= size; x++){
				heightGrid[x][y] = 50 + random.nextInt(150);
			}
		}
		
	}
	
	public void grid2Map(){
		heightMap =  new Spline2D[size];
		for(int y = 0; y < size; y++){
			Spline2D tmp_spline = new Spline2D(0, heightGrid[0][y], 0.0f);
			for (int x = 1; x< size; x++){
				tmp_spline.addVertex(x*POINT_SPACE,heightGrid[x][y], 0.0f);
			}	
			heightMap[y] = tmp_spline;
		}
		
	}
	
	public int getHeight(int x, int y){
		int slotX = x / POINT_SPACE;
		int slotY = y / POINT_SPACE;
		int weightX = x % POINT_SPACE;
		int weightY = y % POINT_SPACE;
		
		if (weightX == 0){
			if (weightY == 0){
				return heightGrid[slotX][slotY];
			}
			Spline2D tmp_spline = new Spline2D(0, heightGrid[slotX][slotY], 0.0f);
			tmp_spline.addVertex(POINT_SPACE, heightGrid[slotX][slotY+1], 0.0f);
			
			return tmp_spline.getValue(weightY);
		}
		// if both x and y weight is not zero, two splines in X has to be made and then extract one point from each to create another spline to get final value from
		if (weightY == 0){
			Spline2D tmp_spline = new Spline2D(0, heightGrid[slotX][slotY], 0.0f);
			tmp_spline.addVertex(POINT_SPACE, heightGrid[slotX+1][slotY], 0.0f);
			return tmp_spline.getValue(weightX);
		}
		
		return 150;
	}
	
	public int getGridHeight(int x, int y){
		return heightGrid[x/POINT_SPACE][y/POINT_SPACE];
	}
	
	public void increaseGridHeight(int x, int y, int value){
		heightGrid[x/POINT_SPACE][y/POINT_SPACE] += value;
	}

}
