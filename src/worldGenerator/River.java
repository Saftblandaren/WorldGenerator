package worldGenerator;

import helpers.Spline2D;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import org.lwjgl.util.vector.Matrix2f;
import org.lwjgl.util.vector.Vector2f;

class PointComparator implements Comparator<int[]>{

	public int compare(int[] arg0, int[] arg1) {
		if (arg0[0]>arg1[0]){
			return 1;
		}else if(arg1[0]>arg0[0]){
			return -1;
		}
		return 0;
	}
	
}

public class River {
	
	private List<int[]> tempPath = new ArrayList<int[]>();
	private Vector2f vector;
	private Vector2f translate;
	private Matrix2f rotateMatrix;
	private Spline2D widthSpline;
	private Spline2D riverSpline;
	private Random random;
	private Vector2f end;
	private int pointSpread;
	private World world;
	
	public River(int startX, int startY, World world) {
		
		this.random = world.getRandom();
		this.world = world;
		translate = new Vector2f(-startX, -startY);
		tempPath.add(new int[]{startX, startY});
		pointSpread = world.getSLOT_SIZE() * world.getSlots() / 32;
		
		createTempPath();
		
		createRotateMatrix();
		setRoute();
		setWidth();
	}
	
	public Vector2f getEnd() {
		return end;
	}
	
	private void createTempPath(){
		
		if (tempPath.get(0)[0] == 0){
			// west to east
			nextPoint(tempPath.get(0)[0], tempPath.get(0)[1], 0);
			
		}else if(tempPath.get(0)[0] == world.getSLOT_SIZE() * world.getSlots()){
			// east to west
			nextPoint(tempPath.get(0)[0], tempPath.get(0)[1], 180);
			
		}else if(tempPath.get(0)[1] == 0){
			// north to south
			nextPoint(tempPath.get(0)[0], tempPath.get(0)[1], -90);
			
		}else if(tempPath.get(0)[1] == world.getSLOT_SIZE() * world.getSlots()){
			//south to north
			nextPoint(tempPath.get(0)[0], tempPath.get(0)[1], 90);
		}
		
	}
	
	private int[] nextPoint(int x, int y, int direction){
		List<int[]> options = new ArrayList<int[]>(); // int[]{score, x, y}
		int totalScore = 0;
		int distance  = pointSpread + random.nextInt(pointSpread);
		
		int nx;
		int ny;
		int score; // low score is good
		int refH = world.getHeight(x, y);
		for(int angle = - 60; angle <= 60; angle += 30){
			angle += direction;
			nx = (int) (x + distance * Math.cos(Math.toRadians(angle)));
			ny = (int) (y + distance * Math.sin(Math.toRadians(angle)));
			score = 0;
			for(Camp camp:world.getCamps()){
				score += world.getSLOT_SIZE()/camp.distanceToCampArea(nx, ny);
			}
			if (world.getHeight(nx, ny)< refH){
				score += 100;
			}
			score += Math.abs(255 -  refH - world.getHeight(nx, ny));
			
			
			totalScore += score;
			options.add(new int[]{score, nx, ny});
			
		}
		Collections.sort(options, new PointComparator());
		
		
		for (int[] point: options){
			System.out.println(point[0] + ", " + point[1] + ", " + point[2]);
			if(random.nextInt(totalScore) > point[0]){
				System.out.println("true");
				//return new int[]{point[1], point[2]};
			}
		}
		System.exit(0);
		
		
		return new int[]{options.get(options.size()-1)[1], options.get(options.size()-1)[2]};
		
	}

	private void setWidth() {
		int y = 5 + random.nextInt(16);
		int x = 0;
		widthSpline = new Spline2D(x,y, 0.0f);
		while(true){
			y = 5 + random.nextInt(16);
			x += pointSpread + random.nextInt(pointSpread);
			widthSpline.addVertex(x, y);
			if (x> (vector.length()-pointSpread*2.5)){
				y = 10 + random.nextInt(11);
				break;
			}
		}
		widthSpline.addVertex((int) Math.ceil(vector.length()), y);
		
	}

	private void setRoute() {
		int y = -10 + random.nextInt(21);
		int x = 0;
		riverSpline = new Spline2D(x, y, 0.0f);
		while(true){
			y = -pointSpread/2 + random.nextInt(pointSpread+1);
			x += pointSpread + random.nextInt(pointSpread);
			riverSpline.addVertex(x, y);
			if (x> (vector.length()-pointSpread*2.5)){
				y = -10 + random.nextInt(21);
				break;
			}
		}
		riverSpline.addVertex((int) Math.ceil(vector.length()), y);
		
	}

	private void createRotateMatrix(){
		rotateMatrix = new Matrix2f();
		rotateMatrix.m00 = (float) (vector.length()/(vector.x + Math.pow(vector.y, 2)/vector.x));
		rotateMatrix.m01 = -rotateMatrix.m00 * vector.y/vector.x;
		rotateMatrix.m10 = rotateMatrix.m00 * vector.y/vector.x;
		rotateMatrix.m11 = (float) (vector.length()/(vector.x + Math.pow(vector.y, 2)/vector.x));
	
	}
	
	public boolean isRoute(int x, int y){
		Vector2f vecT =  Vector2f.add(new Vector2f(x, y), translate, null);
		Vector2f vecR = Matrix2f.transform(rotateMatrix, vecT, null);
		Integer width = widthSpline.getValue((int) vecR.x); 
		Integer route = riverSpline.getValue((int) vecR.x); 
		if(width == null || route == null){
			return false;
		}
		if(Math.abs(route-vecR.y) <= width){
			return true;
		}
		
		return false;
		
	}

}
