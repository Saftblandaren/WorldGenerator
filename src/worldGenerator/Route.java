
package worldGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import helpers.PointComparator;
import helpers.Spline2Dextended;

import org.lwjgl.util.vector.Matrix2f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Route {
	
	private Vector2f vector;
	private Vector2f translate;
	private Matrix2f rotateMatrix;
	private Spline2Dextended widthSpline;
	private Spline2Dextended routeSpline;
	private Random random;
	private Vector2f end;
	private int pointSpread;
	private World world;
	private int direction;
	private int maxWidth;
	
	public Route(int startX, int startY, int endX, int endY, World world) {
		this.world = world;
		this.random = world.getRandom();
		end = new Vector2f(endX, endY);
		translate = new Vector2f(-startX, -startY);
		vector = Vector2f.add(new Vector2f(endX, endY), translate, null);
		pointSpread = Math.min((int) (vector.length()/8), world.getSLOT_SIZE() * world.getSlots() / 32);
		direction = (int) Math.toDegrees(Math.atan2(endY-startY, endX-startX));
		
		System.out.println("Route direction: " + direction);
		
		createRotateMatrix();
		setRoute();
		setWidth();
		
	}
	
	public Vector2f getEnd() {
		return end;
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

	private int[] nextPoint(int x, int y, int direction){
		List<int[]> options = new ArrayList<int[]>(); // int[]{score, x, y}
		int totalScore = 0;
		int distance  = pointSpread + random.nextInt(pointSpread);
		
		int nx;
		int ny;
		int score; // low score is good
		for(int angle = - 60; angle <= 60; angle += 15){
			nx = (int) (x + distance * Math.cos(Math.toRadians(angle + direction)));
			ny = (int) (y + distance * Math.sin(Math.toRadians(angle + direction)));
			score = 1;
			score += Math.abs(angle);

			for (River r: world.getRivers()){
				if (r.distanceTo(nx, ny)<=0){
					// point in river step-increase score
					score += 15000;
				}else{
					score += 10000/r.distanceTo(nx, ny);
				}
			}
			
			int dist2End = (int) Math.sqrt(Math.pow(end.x - nx, end.y - ny));
			
			score += dist2End/distance;
		
			totalScore += score;
			options.add(new int[]{score, nx, ny});
			
		}
		System.out.println("Total score: " + totalScore);
		Collections.sort(options, new PointComparator());

		for (int[] point: options){
			if(random.nextInt(totalScore) > point[0]){
				return new int[]{point[1], point[2]};
			}
		}
		
		// Take most suitable point
		return new int[]{options.get(0)[1], options.get(0)[2]};
	}
	
	private Vector2f globalToLocal(int x, int y){
		Vector2f vecT =  Vector2f.add(new Vector2f(x, y), translate, null);
		Vector2f vecR = Matrix2f.transform(rotateMatrix, vecT, null);
		return vecR;
	}
	
	private void setRoute() {
		Vector2f nPoint = new Vector2f(0, 0);
		routeSpline = new Spline2Dextended(new Vector3f(nPoint.x, nPoint.y, 0.0f));
		int[] point = nextPoint((int) -translate.x, (int) -translate.y, direction);
		while (nPoint.x < vector.length()- 5*pointSpread/2){
			//System.out.println("point: " + point[0] + ", " + point[1]);
			nPoint = globalToLocal(point[0], point[1]);
			//System.out.println("nPoint: " + nPoint.x + ", " + nPoint.y);
			routeSpline.addPoint(nPoint);
			point = nextPoint(point[0], point[1], direction);
		}
		routeSpline.addPoint(new Vector2f(vector.length(), 0));
	}

	private void createRotateMatrix(){
		rotateMatrix = new Matrix2f();
		rotateMatrix.m00 = (float) (vector.length()/(vector.x + Math.pow(vector.y, 2)/vector.x));
		rotateMatrix.m01 = -rotateMatrix.m00 * vector.y/vector.x;
		rotateMatrix.m10 = rotateMatrix.m00 * vector.y/vector.x;
		rotateMatrix.m11 = (float) (vector.length()/(vector.x + Math.pow(vector.y, 2)/vector.x));
	
	}
	
	public int distanceTo(int x, int y){
		Vector2f point =  globalToLocal(x,y);
		Float widthF = widthSpline.getValue((int) point.x); 
		Float riverF = routeSpline.getValue(point.x); 
		if(widthF == null || riverF == null){
			return 10000;
		}
		
		int riverI = (int) Math.round(riverF);
		int widthI = (int) Math.round(widthF);
		return (int) (Math.abs(riverI-point.y) - widthI);
		
	}
	
}
