package helpers;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

public class Spline2D {
	
	private List<int[]> verticies;
	private List<Float> tangets;
	private List<Double> constA;
	private List<Double> constB;
	private List<Double> constC;
	private List<Double> constD;
	
	// y = A * x^3 + B * x^2 + C * x + D
	
	public Spline2D(int x, int y, Float k){
		verticies = new ArrayList<int[]>();
		tangets = new ArrayList<Float>();
		constA = new ArrayList<Double>();
		constB = new ArrayList<Double>();
		constC = new ArrayList<Double>();
		constD = new ArrayList<Double>();
		addV(x, y, k);
		
	}
	
	private void addV(int x, int y, Float k){
		// System.out.println("Adding vertex: " + x + ";" + y);
		verticies.add(new int[]{x, y});
		tangets.add(k);
	}
	
	private void calculateConstants(){
		/** Calculate and add constants (A, B, C and D) for last vertex
		y1 = A * x1^3 + B * x1^2 + C * x1 + D
		y2 = A * x2^3 + B * x2^2 + C * x2 + D
		yp1 = k1 = 3 * A * x1^2 + 2 * B * x1 + C
		yp2 = k2 = 3 * A * x2^2 + 2 * B * x2 + C
		
				x1^3	x1^2	x1		1
		XMat = 	x2^3	x2^2	x2		1
				3*x1^2	2*x1	1		0
				3*x2^2	2*x2	1		0
				
		YVec = [y1	y2	k1	k2]
		CVec = [A	B	C	D]
		
		**/
		
		int lastI = verticies.size()-1;
		
		Matrix4f XMat = new Matrix4f();
		XMat.m00 = (float) Math.pow(((float) verticies.get(lastI-1)[0]), 3);
		XMat.m01 = (float) Math.pow(((float) verticies.get(lastI)[0]), 3);
		
		XMat.m10 = (float) Math.pow(((float) verticies.get(lastI-1)[0]), 2);
		XMat.m11 = (float) Math.pow(((float) verticies.get(lastI)[0]), 2);
		
		XMat.m20 = (float) verticies.get(lastI-1)[0];
		XMat.m21 = (float) verticies.get(lastI)[0];
		
		XMat.m30 = 1.0f;
		XMat.m31 = 1.0f;
		
		XMat.m02 = (float) (3.0f * Math.pow(((float) verticies.get(lastI-1)[0]), 2));
		XMat.m03 = (float) (3.0f * Math.pow(((float) verticies.get(lastI)[0]), 2));
		
		XMat.m12 = (float) (2.0f * verticies.get(lastI-1)[0]);
		XMat.m13 = (float) (2.0f * verticies.get(lastI)[0]);
		
		XMat.m22 = 1.0f;
		XMat.m23 = 1.0f;
		
		XMat.m32 = 0.0f;
		XMat.m33 = 0.0f;
		
		Matrix4f XMatInv = (Matrix4f) XMat.invert();
		if (XMatInv == null){
			System.out.println("ohno");
		}
		Vector4f YVec = new Vector4f(verticies.get(lastI-1)[1], verticies.get(lastI)[1], tangets.get(lastI-1), tangets.get(lastI));
		
		Vector4f CVec = Matrix4f.transform(XMatInv, YVec, null);
		
		constA.add((double) CVec.getX());
		constB.add((double) CVec.getY());
		constC.add((double) CVec.getZ());
		constD.add((double) CVec.getW());
	}
	
	public void addVertex(int x, int y, float tangent){
		addV(x, y, tangent);
		calculateConstants();
		
	}
	
	public void addVertex(int x, int y){
		int xLast = verticies.get(verticies.size() - 1)[0];
		int yLast = verticies.get(verticies.size() - 1)[1];
		
		addVertex(x, y, (float) ((float) (y - yLast) / (x - xLast)));
		
	}
	
	public void close(){
		//use same value as first vertex
		addVertex(360, verticies.get(0)[1], tangets.get(0));
		
	}
	
	public Integer getValue(int x){		
		if (x< verticies.get(0)[0]){
			return null;
		}
		for(int i = 0;i < verticies.size(); i++){
			if(verticies.get(i)[0] >= x){
				if(i>0){
					i--;
				}
				int y = (int) (constA.get(i) * Math.pow(x, 3) + constB.get(i) * Math.pow(x, 2) + constC.get(i) * x + constD.get(i));
				return y;
			}
		}
		return null;
		
	}

}
