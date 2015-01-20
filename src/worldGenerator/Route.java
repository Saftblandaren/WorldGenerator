
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
	
	public Route(int startX, int startY, int endX, int endY, Random random) {
		System.out.println(startX + ", " + startY + ", " + endX + ", " + endY);
		this.random = random;
		translate = new Vector2f(-startX, -startY);
		vector = Vector2f.add(new Vector2f(endX, endY), translate, null);
		createRotateMatrix();
		setRoute();
		setWidth();
		
		System.out.println(vector.length());
		System.out.println(vector.x + ", " + vector.y);
		
		
		System.out.println(rotateMatrix.m00 + ", " + rotateMatrix.m01);
		System.out.println(rotateMatrix.m10 + ", " + rotateMatrix.m11);
		
		Vector2f testV = Matrix2f.transform(rotateMatrix, vector, null);
		System.out.println(testV.x + ", " + testV.y);
		System.out.println("");
	}
	
	private void setWidth() {
		int y = -10 + random.nextInt(21);
		int x = 0;
		widthSpline = new Spline2D(x,y, 0.0f);
		while(true){
			y = 5 + random.nextInt(11);
			x += 50 + random.nextInt(51);
			widthSpline.addVertex(x, y);
			if (x> (vector.length()-120)){
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
			y = -30 + random.nextInt(61);
			x += 50 + random.nextInt(51);
			routeSpline.addVertex(x, y);
			if (x> (vector.length()-120)){
				y = -10 + random.nextInt(21);
				break;
			}
		}
		routeSpline.addVertex((int) Math.ceil(vector.length()), y);
		
	}

	private void createRotateMatrix(){
		
		// create perpendicular vector
		Vector2f pVector = new Vector2f(-vector.getY(), vector.getX());
		
		// create result of rotation vectors
		Vector2f resultVector = new Vector2f(vector.length(), 0);

		// rotateMatrix * vector = resultVector
		// rotateMatrix * pVector = resultpVector
		rotateMatrix = new Matrix2f();
		rotateMatrix.m00 = (float) (vector.length()/(vector.x + Math.pow(vector.y, 2)/vector.x));
		rotateMatrix.m01 = -rotateMatrix.m00 * vector.y/vector.x;
		rotateMatrix.m10 = rotateMatrix.m00 * vector.y/vector.x;
		rotateMatrix.m11 = (float) (vector.length()/(vector.x + Math.pow(vector.y, 2)/vector.x));
		/*
		rotateMatrix.m01 = resultVector.x / (vector.y - vector.x * pVector.y / pVector.x);
		rotateMatrix.m00 = -(vector.y / vector.x) * rotateMatrix.m01;
		rotateMatrix.m10 = rotateMatrix.m01;
		rotateMatrix.m11 = -(pVector.y / pVector.x) * rotateMatrix.m01;
		*/		
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
