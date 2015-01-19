package worldGenerator;

import java.util.Random;

import helpers.Spline2D;

public class HeightMap {
	
	private int[][] meanHeightGrid;
	private int meanHeightSpace;
	private final int POINT_SPACE = 64;
	private int[][] heightGrid;
	private int size;
	private Random random;
	
	public HeightMap(int world_size, Random random) {
		this.random = random;
		size = world_size/POINT_SPACE;
		heightGrid = new int[size+1][size+1];
		
		generateMeanGrid(40);

		//generate main height grid with local variation
		for(int y = 0; y <= size; y++){
			for (int x = 0; x<= size; x++){
				heightGrid[x][y] = getMeanGridValue(x, y) - 20 + random.nextInt(41);
			}
		}
		
	}
	
	private int getMeanGridValue(int x, int y){
		
		int a = meanHeightGrid[x / (meanHeightSpace)][y / (meanHeightSpace)];
		int b = meanHeightGrid[x / (meanHeightSpace) + 1 ][y / (meanHeightSpace)];
		int c = meanHeightGrid[x / (meanHeightSpace)][y / (meanHeightSpace) + 1];
		int d = meanHeightGrid[x / (meanHeightSpace) + 1][y / (meanHeightSpace) + 1];

		int xMod = (x % meanHeightSpace);
		int yMod = (y % meanHeightSpace);
		
		int x1Weight = a + (b-a)* xMod /meanHeightSpace ;
		int x2Weight = c + (d-c)* xMod /meanHeightSpace ;
		int yWeight = x1Weight + (x2Weight-x1Weight)* yMod /meanHeightSpace ;

		return yWeight;
	}
	
	private void generateMeanGrid(int safe){
		meanHeightGrid = new int[5][5];
		meanHeightSpace = (size) / 3;
		
		// Set value of corners
		for(int i = 0; i < 2; i++){
			for(int j = 0; j < 2; j++){
				int h = 20 + safe + (215 - 2*safe) * random.nextInt(2);
				meanHeightGrid[i*4][j*4] = h;
			}
		}
		
		// Set value of horizontal edges
		for(int i = 0; i < 2; i++){
			for(int j=1; j<4; j++){
				int hWeighted =  meanHeightGrid[i*4][0] + (meanHeightGrid[i*4][4] - meanHeightGrid[i*4][0]) * j / 5;
				meanHeightGrid[i*4][j] = hWeighted - 25 + random.nextInt(51);
			}
		}
		
		// Set value of vertical lines
				for(int i = 0; i < 5; i++){
					for(int j=1; j<4; j++){
						int hWeighted =  meanHeightGrid[0][i] + (meanHeightGrid[4][i] - meanHeightGrid[0][i]) * j / 5;
						meanHeightGrid[j][i] = hWeighted - 25 + random.nextInt(51);
					}
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
		
		Spline2D tmp_spline1 = new Spline2D(0, heightGrid[slotX][slotY], 0.0f);
		tmp_spline1.addVertex(POINT_SPACE, heightGrid[slotX+1][slotY], 0.0f);
		
		Spline2D tmp_spline2 = new Spline2D(0, heightGrid[slotX][slotY+1], 0.0f);
		tmp_spline2.addVertex(POINT_SPACE, heightGrid[slotX+1][slotY+1], 0.0f);
		
		Spline2D tmp_spline3 = new Spline2D(0, tmp_spline1.getValue(weightX), 0.0f);
		tmp_spline3.addVertex(POINT_SPACE, tmp_spline2.getValue(weightX), 0.0f);
		
		return tmp_spline3.getValue(weightY);
	}
	
	public void increaseGridHeight(int x, int y, int value){
		heightGrid[x/POINT_SPACE][y/POINT_SPACE] += value;
	}

}
