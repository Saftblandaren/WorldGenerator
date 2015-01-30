package helpers;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class Spline2DSegment {
	
	private Vector3f start;
	private Vector3f end;
	private Vector3f delta;
	
	private Vector4f constants;

	public Spline2DSegment(Vector3f start, Vector3f end) {
		this.start = start;
		this.end = end;
		delta = Vector3f.sub(end, start, null);
		calculateConstants();

	}
	
	private void calculateConstants(){
				
		Matrix4f XMat = new Matrix4f();
		XMat.m00 = 0.0f;
		XMat.m01 = (float) Math.pow(delta.x, 3);
		
		XMat.m10 = 0.0f;
		XMat.m11 = (float) Math.pow(delta.x, 2);
		
		XMat.m20 = 0.0f;
		XMat.m21 = delta.x;
		
		XMat.m30 = 1.0f;
		XMat.m31 = 1.0f;
		
		XMat.m02 = 0.0f;
		XMat.m03 = (float) (3.0f * Math.pow(delta.x, 2));
		
		XMat.m12 = 0.0f;
		XMat.m13 = (float) (2.0f * delta.x);
		
		XMat.m22 = 1.0f;
		XMat.m23 = 1.0f;
		
		XMat.m32 = 0.0f;
		XMat.m33 = 0.0f;
		
		Matrix4f XMatInv = (Matrix4f) XMat.invert();
		if (XMatInv == null){
			System.out.println("ohno");
		}
		Vector4f YVec = new Vector4f(0.0f, delta.y, start.z, end.z);
		
		constants = Matrix4f.transform(XMatInv, YVec, null);
		
	}
	
	public Vector3f getEnd() {
		return end;
	}

	protected Float getValue(float x){
		float dx = x-start.x;
		
		if(dx < 0 || dx >= delta.x){
			return null;
		}
		double dy = constants.x * Math.pow(dx, 3) + constants.y * Math.pow(dx, 2) + constants.z * dx + constants.w;
		
		float result = (float) dy + start.y;
		
		return result;
		
	}
	

}
