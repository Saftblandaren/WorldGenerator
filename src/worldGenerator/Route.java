
package worldGenerator;

import java.util.Random;

import helpers.Spline2D;

import org.lwjgl.util.vector.Matrix2f;
import org.lwjgl.util.vector.Vector2f;

public class Route {
	
	private Vector2f vector;
	private Vector2f translate;
	private Matrix2f rotateMatrix;
	private Spline2D widthSpline;
	private Spline2D routeSpline;
	private Random random;
	private Vector2f end;
	private int pointSpread;
	
	public Route(int startX, int startY, int endX, int endY, World world) {
		
		this.random = world.getRandom();
		end = new Vector2f(endX, endY);
		translate = new Vector2f(-startX, -startY);
		vector = Vector2f.add(new Vector2f(endX, endY), translate, null);
		pointSpread = Math.min((int) (vector.length()/8), world.getSLOT_SIZE() * world.getSlots() / 32);
		// System.out.println("point_spread: " + pointSpread);
		
		createRotateMatrix();
		setRoute();
		setWidth();
		
		/*
		System.out.println(startX + ", " + startY + ", " + endX + ", " + endY);
		System.out.println(vector.length());
		System.out.println(vector.x + ", " + vector.y);
		
		
		System.out.println(rotateMatrix.m00 + ", " + rotateMatrix.m01);
		System.out.println(rotateMatrix.m10 + ", " + rotateMatrix.m11);
		
		Vector2f testV = Matrix2f.transform(rotateMatrix, vector, null);
		System.out.println(testV.x + ", " + testV.y);
		System.out.println("");
		*/
	}
	
	public Vector2f getEnd() {
		return end;
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
		routeSpline = new Spline2D(x, y, 0.0f);
		while(true){
			y = -pointSpread/2 + random.nextInt(pointSpread+1);
			x += pointSpread + random.nextInt(pointSpread);
			routeSpline.addVertex(x, y);
			if (x> (vector.length()-pointSpread*2.5)){
				y = -10 + random.nextInt(21);
				break;
			}
		}
		routeSpline.addVertex((int) Math.ceil(vector.length()), y);
		
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
		Integer route = routeSpline.getValue((int) vecR.x); 
		if(width == null || route == null){
			return false;
		}
		if(Math.abs(route-vecR.y) <= width){
			return true;
		}
		
		return false;
		
	}
	

}
