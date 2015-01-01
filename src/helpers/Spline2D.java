package helpers;

import java.util.ArrayList;
import java.util.List;

public class Spline2D {
	
	private List<int[]> verticies;
	private List<Float> tangets;
	
	public Spline2D(int x, int y, float k){
		verticies = new ArrayList<int[]>();
		tangets = new ArrayList<Float>();
		addVertex(x, y, k);
		
	}
	
	public void addVertex(int x, int y, float tangent){
		verticies.add(new int[]{x, y});
		tangets.add(tangent);
		
	}
	
	public void addVertex(int x, int y){
		int xLast = verticies.get(verticies.size() - 1)[0];
		int yLast = verticies.get(verticies.size() - 1)[1];
		
		addVertex(x, y, (float) ((float) (y - yLast) / (x - xLast)));
		
	}
	
	public void close(){
		//use same value as first vertex
		addVertex(verticies.get(0)[0], verticies.get(0)[1], tangets.get(0));
		
	}
	
	public int getValue(int x){
		int i = 0;
		for(int[] vertex: verticies){
			if(vertex[0]>x){
				
				i++;
			}
		}
		return 0;
		
	}

}
