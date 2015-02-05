package worldGenerator;

import helpers.PointComparator;
import helpers.Spline2Dextended;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.lwjgl.util.vector.Matrix2f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class River {
	
	private List<int[]> tempPath = new ArrayList<int[]>();
	private Vector2f vector;
	private Vector2f translate;
	private Matrix2f rotateMatrix;
	private Spline2Dextended widthSpline;
	private Spline2Dextended riverSpline;
	private Random random;
	private Vector2f end;
	private int pointSpread;
	private World world;
	private int maxWidth;
	
	public River(int startX, int startY, World world) {
		
		this.random = world.getRandom();
		this.world = world;
		translate = new Vector2f(-startX, -startY);
		tempPath.add(new int[]{startX, startY});
		pointSpread = world.getSLOT_SIZE() * world.getSlots() / 32;
		createTempPath();
		int[] lastPoint = tempPath.get(tempPath.size()-1);
		vector = Vector2f.add(new Vector2f(lastPoint[0],lastPoint[1]), translate, null);
		
		
		/*
		for(int[] point:tempPath){
			System.out.println("x: " + point[0] + " y: " + point[1]);
		}
		*/
		
		createRotateMatrix();
		createRiverSpline();
		setWidth();
		
	}
	
	private void createRiverSpline() {
		boolean started = false;
		for(int[] point:tempPath){
			Vector2f vecP = globalToLocal(point[0], point[1]);
			if(started){
				riverSpline.addPoint(new Vector2f(vecP.x,vecP.y));
				continue;
			}
			riverSpline = new Spline2Dextended(new Vector3f(vecP.x,vecP.y, 0.0f));
			started = true;
		}
		
	}
	
	private Vector2f globalToLocal(int x, int y){
		Vector2f vecT =  Vector2f.add(new Vector2f(x, y), translate, null);
		Vector2f vecR = Matrix2f.transform(rotateMatrix, vecT, null);
		return vecR;
	}

	public Vector2f getEnd() {
		return end;
	}
	
	private void createTempPath(){
		int angle = 0;
		if (tempPath.get(0)[0] == 0){
			// west to east
			angle = 0;
			
		}else if(tempPath.get(0)[0] == world.getSLOT_SIZE() * world.getSlots()){
			// east to west
			angle = 180;
			
		}else if(tempPath.get(0)[1] == 0){
			// north to south
			angle = 90;
			
		}else if(tempPath.get(0)[1] == world.getSLOT_SIZE() * world.getSlots()){
			//south to north
			angle = -90;
		}
		System.out.println("River direction: " + angle);
		for( int i = 0; continueNextPoint(); i++){
			tempPath.add(nextPoint(tempPath.get(i)[0], tempPath.get(i)[1], angle));
			//System.out.println("x: " + tempPath.get(tempPath.size()-1)[0] + " y: " + tempPath.get(tempPath.size()-1)[1]);
		}
		
	}
	
	private boolean continueNextPoint(){
		int li = tempPath.size() -1;
		if(tempPath.get(li)[0] < 0 || tempPath.get(li)[0] > world.getSLOT_SIZE() * world.getSlots()){
			return false;
		}
		if(tempPath.get(li)[1] < 0 || tempPath.get(li)[1] > world.getSLOT_SIZE() * world.getSlots()){
			return false;
		}
		return true;
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
			nx = (int) (x + distance * Math.cos(Math.toRadians(angle + direction)));
			ny = (int) (y + distance * Math.sin(Math.toRadians(angle + direction)));
			score = 0;
			for(Camp camp:world.getCamps()){
				if (camp.distanceToCampArea(nx, ny) < 1 ){
					score += 500;
				}else{
					score += 4 * world.getSLOT_SIZE()/camp.distanceToCampArea(nx, ny);
				}
				
			}
			if (world.getMeanHeight(nx, ny)< refH){
				score += 100;
			}
			score += Math.abs(255 -  refH - world.getMeanHeight(nx, ny));

			totalScore += score;
			options.add(new int[]{score, nx, ny});
			
		}
		Collections.sort(options, new PointComparator());

		for (int[] point: options){
			if(random.nextInt(totalScore) > point[0]){
				return new int[]{point[1], point[2]};
			}
		}

		return new int[]{options.get(options.size()-1)[1], options.get(options.size()-1)[2]};
	}

	private void setWidth() {
		int y = 5 + random.nextInt(16);
		maxWidth = y;
		int x = 0;
		widthSpline = new Spline2Dextended(new Vector3f(x,y, 0.0f));
		while(true){
			y = 5 + random.nextInt(16);
			x += pointSpread + random.nextInt(pointSpread);
			widthSpline.addPoint(new Vector2f(x, y));
			if( y > maxWidth){
				maxWidth = y;
			}
			if (x> (vector.length()-pointSpread*2.5)){
				y = 10 + random.nextInt(11);
				break;
			}
		}
		widthSpline.addPoint(new Vector2f((float) Math.ceil(vector.length()), y));
		
	}


	private void createRotateMatrix(){
		rotateMatrix = new Matrix2f();
		rotateMatrix.m00 = (float) (vector.length()/(vector.x + Math.pow(vector.y, 2)/vector.x));
		rotateMatrix.m01 = -rotateMatrix.m00 * vector.y/vector.x;
		rotateMatrix.m10 = rotateMatrix.m00 * vector.y/vector.x;
		rotateMatrix.m11 = (float) (vector.length()/(vector.x + Math.pow(vector.y, 2)/vector.x));
	
	}
	
	public int getMaxWidth() {
		return maxWidth;
	}

	public int distanceTo(int x, int y){
		Vector2f point =  globalToLocal(x,y);
		Float widthF = widthSpline.getValue((int) point.x); 
		Float riverF = riverSpline.getValue(point.x); 
		if(widthF == null || riverF == null){
			return 10000;
		}
		
		int riverI = (int) Math.round(riverF);
		int widthI = (int) Math.round(widthF);
		return (int) (Math.abs(riverI-point.y) - widthI);
		
	}

}
